package com.example.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.GlobeDatabase
import com.example.data.local.LocationHistoryEntity
import com.example.data.model.CountryInfo
import com.example.data.model.GeoPoint
import com.example.data.model.WorldGeographicData
import com.example.data.repository.LocationHistoryRepository
import com.example.globe.GlobeMath
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

enum class AppThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class GlobeUiState(
    val centerLat: Double = 30.3753,
    val centerLon: Double = 69.3451,
    val zoomScale: Float = 1.0f,
    val selectedCountry: CountryInfo? = null,
    val highlightedCustomPoint: GeoPoint? = null,
    val highlightedLabel: String = "Pakistan",
    val searchQuery: String = "",
    val searchSuggestions: List<CountryInfo> = emptyList(),
    val isAutoRotating: Boolean = false,
    val isSoundEnabled: Boolean = true,
    val isCompassModeActive: Boolean = false,
    val compassBearingDeg: Float = 0f,
    val showDetailSheet: Boolean = false,
    val showHistorySheet: Boolean = false,
    val showDirectionDialog: Boolean = false,
    val showSettingsDialog: Boolean = false,
    val themeMode: AppThemeMode = AppThemeMode.DARK,
    val routeStartCountry: CountryInfo? = null,
    val routeEndCountry: CountryInfo? = null,
    val userLocation: GeoPoint? = null,
    val historyFilterFavoriteOnly: Boolean = false
)

class GlobeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LocationHistoryRepository
    private val prefs = application.getSharedPreferences("globe_settings_prefs", Context.MODE_PRIVATE)
    private var flyJob: Job? = null
    private var zoomAnimJob: Job? = null
    private var autoRotateJob: Job? = null
    private var flingJob: Job? = null
    private var compassResetJob: Job? = null

    private val _uiState = MutableStateFlow(GlobeUiState())
    val uiState: StateFlow<GlobeUiState> = _uiState.asStateFlow()

    val historyItems: StateFlow<List<LocationHistoryEntity>>

    init {
        val database = GlobeDatabase.getInstance(application)
        repository = LocationHistoryRepository(database.locationHistoryDao())

        historyItems = repository.allHistory.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        val savedThemeName = prefs.getString("theme_mode", AppThemeMode.DARK.name)
        val initialTheme = try {
            AppThemeMode.valueOf(savedThemeName ?: AppThemeMode.DARK.name)
        } catch (_: Exception) {
            AppThemeMode.DARK
        }

        // Select Pakistan by default centered at exact dead center of the 3D globe
        val defaultCountry = WorldGeographicData.findCountry("Pakistan")
        _uiState.update {
            it.copy(
                themeMode = initialTheme,
                selectedCountry = defaultCountry ?: it.selectedCountry,
                highlightedCustomPoint = defaultCountry?.let { c -> GeoPoint(c.centerLat, c.centerLon) } ?: it.highlightedCustomPoint,
                highlightedLabel = defaultCountry?.name ?: it.highlightedLabel,
                centerLat = defaultCountry?.centerLat ?: it.centerLat,
                centerLon = defaultCountry?.centerLon ?: it.centerLon
            )
        }
    }

    fun stopFling() {
        flingJob?.cancel()
        flingJob = null
    }

    fun onFling(vx: Float, vy: Float) {
        if (_uiState.value.isAutoRotating) return
        stopFling()
        flyJob?.cancel()

        // Natural momentum velocity scaled by zoom level
        val sensitivity = 0.00032f / _uiState.value.zoomScale
        var velLon = vx * sensitivity
        var velLat = vy * sensitivity

        val initialSpeed = kotlin.math.hypot(velLon, velLat)
        if (initialSpeed < 0.04f) return // Ignore micro-touches

        flingJob = viewModelScope.launch {
            val friction = 0.945f
            while (kotlin.math.hypot(velLon, velLat) > 0.008f) {
                delay(16)
                var curLon = _uiState.value.centerLon - velLon
                while (curLon > 180.0) curLon -= 360.0
                while (curLon < -180.0) curLon += 360.0

                // Dragging down (vy > 0) brings northern latitudes down to center (centerLat increases)
                val curLat = (_uiState.value.centerLat + velLat).coerceIn(-85.0, 85.0)

                _uiState.update {
                    it.copy(
                        centerLon = curLon,
                        centerLat = curLat
                    )
                }

                velLon *= friction
                velLat *= friction
            }
        }
    }

    fun onRotateDrag(deltaX: Float, deltaY: Float) {
        stopFling()
        flyJob?.cancel()

        val sensitivity = 0.35f / _uiState.value.zoomScale
        var newLon = _uiState.value.centerLon - (deltaX * sensitivity)
        while (newLon > 180.0) newLon -= 360.0
        while (newLon < -180.0) newLon += 360.0

        // Swiping down (deltaY > 0) pulls northern hemisphere down into center (centerLat increases)
        // Swiping up (deltaY < 0) pushes southern hemisphere up into center (centerLat decreases)
        val newLat = (_uiState.value.centerLat + (deltaY * sensitivity)).coerceIn(-85.0, 85.0)

        _uiState.update {
            it.copy(
                centerLon = newLon,
                centerLat = newLat
            )
        }
    }

    fun onZoomChange(deltaScale: Float, focalLat: Double? = null, focalLon: Double? = null) {
        zoomAnimJob?.cancel()
        flyJob?.cancel()
        val oldScale = _uiState.value.zoomScale
        val newScale = (oldScale * deltaScale).coerceIn(0.6f, 5.0f)

        var newLat = _uiState.value.centerLat
        var newLon = _uiState.value.centerLon

        // Focal zoom: gently track towards touch focus point when zooming in
        if (focalLat != null && focalLon != null && deltaScale > 1.0f) {
            val factor = ((deltaScale - 1f) * 0.25f).coerceIn(0f, 0.2f)
            newLat = (newLat + (focalLat - newLat) * factor).coerceIn(-85.0, 85.0)

            var diffLon = focalLon - newLon
            while (diffLon > 180.0) diffLon -= 360.0
            while (diffLon < -180.0) diffLon += 360.0
            newLon += diffLon * factor
            while (newLon > 180.0) newLon -= 360.0
            while (newLon < -180.0) newLon += 360.0
        }

        _uiState.update {
            it.copy(
                zoomScale = newScale,
                centerLat = newLat,
                centerLon = newLon
            )
        }
    }

    fun onZoomDirect(scale: Float) {
        zoomAnimJob?.cancel()
        _uiState.update { it.copy(zoomScale = scale.coerceIn(0.6f, 5.0f)) }
    }

    fun zoomIn() {
        val targetScale = (_uiState.value.zoomScale * 1.38f).coerceIn(0.6f, 5.0f)
        animateZoomSmooth(targetScale)
    }

    fun zoomOut() {
        val targetScale = (_uiState.value.zoomScale / 1.38f).coerceIn(0.6f, 5.0f)
        animateZoomSmooth(targetScale)
    }

    private fun animateZoomSmooth(targetScale: Float) {
        zoomAnimJob?.cancel()
        val startScale = _uiState.value.zoomScale
        if (abs(targetScale - startScale) < 0.001f) return
        zoomAnimJob = viewModelScope.launch {
            val startTimeNanos = System.nanoTime()
            val durationNanos = 260_000_000L // 260ms smooth cubic ease-out
            while (true) {
                val elapsed = System.nanoTime() - startTimeNanos
                val t = (elapsed.toFloat() / durationNanos).coerceIn(0f, 1f)
                val ease = 1f - (1f - t) * (1f - t) * (1f - t)
                val curScale = startScale + (targetScale - startScale) * ease
                _uiState.update { it.copy(zoomScale = curScale) }
                if (t >= 1f) break
                delay(8)
            }
            _uiState.update { it.copy(zoomScale = targetScale) }
        }
    }

    /**
     * Reset View:
     * - Stops auto-rotation, music, and live compass mode immediately right where the globe is
     * - Smoothly resets compass bearing back to North-Up (0°)
     * - Cancels any active fling or flight animation
     * - Clears active direction route (line and moving airplane)
     * - Smoothly resets zoom back to normal 1.0f without jumping away
     */
    fun resetView() {
        stopFling()
        flyJob?.cancel()
        zoomAnimJob?.cancel()
        stopAutoRotate()

        _uiState.update {
            it.copy(
                isAutoRotating = false,
                isCompassModeActive = false,
                routeStartCountry = null,
                routeEndCountry = null,
                searchSuggestions = emptyList()
            )
        }

        animateCompassBearingToNorth()
        animateZoomSmooth(1.0f)
    }

    /**
     * Activates or toggles Live Compass Mode:
     * - If auto-rotation is running, stops auto-rotation (and rotation sound) immediately.
     * - Centers the globe on the user's location / main selected point and stops there.
     * - Enables live phone compass sensor tracking so rotating the mobile rotates North/South/East/West on the map in real time!
     * - Tapping again when already active smoothly locks the map back to North-Up (0°) and turns off live sensor tracking.
     */
    fun toggleCompassMode(userLat: Double? = null, userLon: Double? = null) {
        val enabling = !_uiState.value.isCompassModeActive
        stopFling()
        flyJob?.cancel()
        compassResetJob?.cancel()

        if (enabling) {
            // Stop previous auto-rotation immediately so globe stops at the main point
            stopAutoRotate()

            val targetLat = userLat
                ?: _uiState.value.userLocation?.lat
                ?: _uiState.value.selectedCountry?.centerLat
                ?: _uiState.value.centerLat
            val targetLon = userLon
                ?: _uiState.value.userLocation?.lon
                ?: _uiState.value.selectedCountry?.centerLon
                ?: _uiState.value.centerLon

            val userGeo = if (userLat != null && userLon != null) {
                GeoPoint(userLat, userLon)
            } else {
                _uiState.value.userLocation
            }
            val matchedCountry = findCountryAtCoordinate(targetLat, targetLon) ?: _uiState.value.selectedCountry

            _uiState.update {
                it.copy(
                    isAutoRotating = false,
                    isCompassModeActive = true,
                    userLocation = userGeo ?: it.userLocation,
                    selectedCountry = matchedCountry,
                    highlightedCustomPoint = GeoPoint(targetLat, targetLon),
                    highlightedLabel = matchedCountry?.name ?: it.highlightedLabel
                )
            }

            // Smoothly center the globe on the user's main point and stop there
            flyTo(
                lat = targetLat,
                lon = targetLon,
                zoom = _uiState.value.zoomScale.coerceIn(1.0f, 2.5f),
                country = matchedCountry,
                preserveAutoRotate = false
            )
        } else {
            _uiState.update { it.copy(isCompassModeActive = false) }
            animateCompassBearingToNorth()
        }
    }

    /**
     * Updates the live compass azimuth bearing (0° = North, 90° = East, 180° = South, 270° = West)
     * from the phone's hardware rotation/magnetic sensors using shortest-angle low-pass smoothing.
     */
    fun onSensorAzimuthChanged(azimuthDeg: Float) {
        if (!_uiState.value.isCompassModeActive) return
        val normalizedTarget = ((azimuthDeg % 360f) + 360f) % 360f
        val current = _uiState.value.compassBearingDeg
        var diff = normalizedTarget - current
        while (diff > 180f) diff -= 360f
        while (diff < -180f) diff += 360f

        // Smooth out micro-jitter while keeping immediate response when user rotates phone
        if (abs(diff) < 0.15f) return
        val smoothed = (((current + diff * 0.22f) % 360f) + 360f) % 360f
        _uiState.update { it.copy(compassBearingDeg = smoothed) }
    }

    private fun animateCompassBearingToNorth() {
        compassResetJob?.cancel()
        val startBearing = _uiState.value.compassBearingDeg
        var diff = 0f - startBearing
        while (diff > 180f) diff -= 360f
        while (diff < -180f) diff += 360f
        if (abs(diff) < 0.2f) {
            _uiState.update { it.copy(compassBearingDeg = 0f) }
            return
        }
        compassResetJob = viewModelScope.launch {
            val startTime = System.nanoTime()
            val duration = 300_000_000L
            while (true) {
                val elapsed = System.nanoTime() - startTime
                val t = (elapsed.toFloat() / duration).coerceIn(0f, 1f)
                val ease = 1f - (1f - t) * (1f - t) * (1f - t)
                val cur = (((startBearing + diff * ease) % 360f) + 360f) % 360f
                _uiState.update { it.copy(compassBearingDeg = cur) }
                if (t >= 1f) break
                delay(8)
            }
            _uiState.update { it.copy(compassBearingDeg = 0f) }
        }
    }

    fun toggleAutoRotate() {
        val next = !_uiState.value.isAutoRotating
        _uiState.update {
            it.copy(
                isAutoRotating = next,
                isCompassModeActive = if (next) false else it.isCompassModeActive
            )
        }
        if (next) {
            animateCompassBearingToNorth()
            startAutoRotate()
        } else {
            stopAutoRotate()
        }
    }

    fun toggleSound() {
        if (!_uiState.value.isAutoRotating) return
        _uiState.update { it.copy(isSoundEnabled = !it.isSoundEnabled) }
    }

    private fun startAutoRotate() {
        autoRotateJob?.cancel()
        autoRotateJob = viewModelScope.launch {
            while (_uiState.value.isAutoRotating) {
                delay(16)
                var newLon = _uiState.value.centerLon + 0.35
                if (newLon > 180.0) newLon -= 360.0
                _uiState.update { it.copy(centerLon = newLon) }
            }
        }
    }

    private fun stopAutoRotate() {
        autoRotateJob?.cancel()
        autoRotateJob = null
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { state ->
            val suggestions = if (query.isBlank()) {
                emptyList()
            } else {
                WorldGeographicData.countries.filter {
                    it.name.contains(query, ignoreCase = true) ||
                            it.capital.contains(query, ignoreCase = true) ||
                            it.nativeName.contains(query, ignoreCase = true)
                }.take(6)
            }
            state.copy(searchQuery = query, searchSuggestions = suggestions)
        }
    }

    fun onSearchSubmit(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return

        // 1. Check if user searched a specific city (e.g., Lahore, Karachi, Dubai, Makkah)
        val city = WorldGeographicData.findCity(trimmed)
        if (city != null) {
            val country = WorldGeographicData.countries.firstOrNull { it.id == city.countryId }
            val label = if (country != null) "${city.name}, ${country.name}" else city.name
            val cityPoint = GeoPoint(city.lat, city.lon)
            _uiState.update {
                it.copy(
                    selectedCountry = country,
                    highlightedCustomPoint = cityPoint,
                    highlightedLabel = label,
                    searchQuery = label,
                    searchSuggestions = emptyList()
                )
            }
            flyTo(lat = city.lat, lon = city.lon, zoom = 1.6f, country = country, preserveAutoRotate = true)
            viewModelScope.launch {
                repository.recordLocation(
                    name = label,
                    countryCode = city.countryId,
                    capital = country?.capital ?: city.name,
                    continent = country?.continent ?: "World",
                    latitude = city.lat,
                    longitude = city.lon,
                    visitType = "SEARCHED"
                )
            }
            return
        }

        // 2. Check exact or fuzzy country match
        val found = WorldGeographicData.findCountry(trimmed)
            ?: _uiState.value.searchSuggestions.firstOrNull()
        if (found != null) {
            selectCountry(found)
        }
    }

    fun selectCountry(country: CountryInfo, targetZoom: Float = 1.35f) {
        val centerPoint = GeoPoint(country.centerLat, country.centerLon)
        _uiState.update {
            it.copy(
                selectedCountry = country,
                highlightedLabel = country.name,
                highlightedCustomPoint = centerPoint,
                searchQuery = country.name,
                searchSuggestions = emptyList()
            )
        }

        // Smoothly rotate globe so country is at the EXACT center of the sphere (keeps auto-rotation if already active)
        flyTo(
            lat = country.centerLat,
            lon = country.centerLon,
            zoom = targetZoom,
            country = country,
            preserveAutoRotate = true
        )

        // Track in Room history
        viewModelScope.launch {
            repository.recordLocation(
                name = country.name,
                countryCode = country.id,
                capital = country.capital,
                continent = country.continent,
                latitude = country.centerLat,
                longitude = country.centerLon,
                visitType = "SEARCHED"
            )
        }
    }

    /**
     * Handles direct tap on the 3D globe at any zoom level.
     * Places the dot at the EXACT tapped coordinate (tappedLat, tappedLon) so the dot
     * always appears right under the user's finger regardless of zoom in or zoom out,
     * and highlights the country if tapped inside its territory.
     * If tapped on a country, updates both the search bar and bottom pill.
     * If tapped on empty ocean/unmapped area, clears selectedCountry so no raw coordinate pill overlaps the bottom.
     */
    fun onGlobeTap(screenX: Float, screenY: Float, radius: Float, cx: Float, cy: Float) {
        stopFling()
        flyJob?.cancel()
        val geo = GlobeMath.unproject(
            screenX = screenX,
            screenY = screenY,
            centerLatDeg = _uiState.value.centerLat,
            centerLonDeg = _uiState.value.centerLon,
            radius = radius,
            cx = cx,
            cy = cy,
            bearingDeg = _uiState.value.compassBearingDeg
        ) ?: return

        val tappedLat = geo.first
        val tappedLon = geo.second
        val exactClickedPoint = GeoPoint(tappedLat, tappedLon)

        val matchedCountry = findCountryAtCoordinate(tappedLat, tappedLon)

        if (matchedCountry != null) {
            _uiState.update {
                it.copy(
                    selectedCountry = matchedCountry,
                    // Keep exact clicked point so the dot shows right where the user clicked!
                    highlightedCustomPoint = exactClickedPoint,
                    highlightedLabel = matchedCountry.name,
                    // Also populate search bar with the clicked country name!
                    searchQuery = matchedCountry.name,
                    searchSuggestions = emptyList()
                )
            }
            viewModelScope.launch {
                repository.recordLocation(
                    name = matchedCountry.name,
                    countryCode = matchedCountry.id,
                    capital = matchedCountry.capital,
                    continent = matchedCountry.continent,
                    latitude = tappedLat,
                    longitude = tappedLon,
                    visitType = "TAPPED"
                )
            }
        } else {
            // Tapped custom ocean/unmapped point: show dot on globe, but do NOT show raw coordinate pill at bottom
            _uiState.update {
                it.copy(
                    selectedCountry = null,
                    highlightedCustomPoint = exactClickedPoint,
                    highlightedLabel = "",
                    searchSuggestions = emptyList()
                )
            }
        }
    }

    private fun findCountryAtCoordinate(lat: Double, lon: Double): CountryInfo? {
        // 1. Direct point-in-polygon matching on country boundaries
        val polygonMatch = WorldGeographicData.countries.firstOrNull { country ->
            country.boundary.isNotEmpty() && isPointInPolygon(lat, lon, country.boundary)
        }
        if (polygonMatch != null) return polygonMatch

        // 2.Tight proximity check (within 320 km) for smaller countries
        var closest: CountryInfo? = null
        var minDistance = Double.MAX_VALUE
        for (country in WorldGeographicData.countries) {
            val dist = GlobeMath.greatCircleDistanceKm(lat, lon, country.centerLat, country.centerLon)
            if (dist < minDistance) {
                minDistance = dist
                closest = country
            }
        }
        return if (closest != null && minDistance < 320.0) closest else null
    }

    private fun isPointInPolygon(lat: Double, lon: Double, polygon: List<GeoPoint>): Boolean {
        if (polygon.size < 3) return false
        var inside = false
        var j = polygon.size - 1
        for (i in polygon.indices) {
            val xi = polygon[i].lon
            val yi = polygon[i].lat
            val xj = polygon[j].lon
            val yj = polygon[j].lat
            val intersect = ((yi > lat) != (yj > lat)) &&
                    (lon < (xj - xi) * (lat - yi) / (yj - yi) + xi)
            if (intersect) inside = !inside
            j = i
        }
        return inside
    }

    fun flyTo(
        lat: Double,
        lon: Double,
        zoom: Float = 1.35f,
        country: CountryInfo? = null,
        preserveAutoRotate: Boolean = true
    ) {
        stopFling()
        zoomAnimJob?.cancel()
        flyJob?.cancel()
        val wasRotating = _uiState.value.isAutoRotating
        stopAutoRotate()
        if (!preserveAutoRotate && wasRotating) {
            _uiState.update { it.copy(isAutoRotating = false) }
        }

        flyJob = viewModelScope.launch {
            val startLat = _uiState.value.centerLat
            val startLon = _uiState.value.centerLon
            val startZoom = _uiState.value.zoomScale

            // Shortest angular path around the globe
            var diffLon = lon - startLon
            while (diffLon > 180.0) diffLon -= 360.0
            while (diffLon < -180.0) diffLon += 360.0
            val targetLon = startLon + diffLon

            val startTimeNanos = System.nanoTime()
            val durationNanos = 520_000_000L // 520ms smooth Google Earth camera flight

            while (true) {
                val elapsed = System.nanoTime() - startTimeNanos
                val t = (elapsed.toFloat() / durationNanos).coerceIn(0f, 1f)
                // Smooth cubic ease-out curve
                val ease = 1f - (1f - t) * (1f - t) * (1f - t)

                val curLat = startLat + (lat - startLat) * ease
                var curLon = startLon + (targetLon - startLon) * ease
                while (curLon > 180.0) curLon -= 360.0
                while (curLon < -180.0) curLon += 360.0
                val curZoom = startZoom + (zoom - startZoom) * ease

                _uiState.update {
                    it.copy(
                        centerLat = curLat,
                        centerLon = curLon,
                        zoomScale = curZoom
                    )
                }
                if (t >= 1f) break
                delay(8)
            }

            _uiState.update {
                it.copy(
                    centerLat = lat,
                    centerLon = lon,
                    zoomScale = zoom,
                    selectedCountry = country ?: it.selectedCountry
                )
            }

            // If auto-rotation was enabled and should be preserved, seamlessly continue rotating!
            if (preserveAutoRotate && _uiState.value.isAutoRotating) {
                startAutoRotate()
            }
        }
    }

    /**
     * Current Location (GPS) button behavior:
     * 1. Stops auto-rotation and stops sound immediately
     * 2. Highlights user's location and country
     * 3. Smoothly flies & zooms IN (1.75x) to focus and highlight the user's location
     * 4. Holds briefly so user sees the highlight, then smoothly zooms back OUT to normal (1.0f)
     *    while keeping focus centered on that exact location.
     */
    fun onUserGpsLocated(lat: Double, lon: Double) {
        stopFling()
        zoomAnimJob?.cancel()
        flyJob?.cancel()
        stopAutoRotate()

        val point = GeoPoint(lat, lon)
        val matchedCountry = findCountryAtCoordinate(lat, lon)
        val label = if (matchedCountry != null) "My Location (${matchedCountry.name})" else "My Location"

        _uiState.update {
            it.copy(
                isAutoRotating = false,
                userLocation = point,
                selectedCountry = matchedCountry ?: it.selectedCountry,
                highlightedCustomPoint = point,
                highlightedLabel = label,
                searchQuery = matchedCountry?.name ?: label,
                searchSuggestions = emptyList()
            )
        }

        flyJob = viewModelScope.launch {
            // Phase 1: Fly to user's location and zoom IN (1.75f)
            val startLat = _uiState.value.centerLat
            val startLon = _uiState.value.centerLon
            val startZoom = _uiState.value.zoomScale
            val peakZoom = 1.75f

            var diffLon = lon - startLon
            while (diffLon > 180.0) diffLon -= 360.0
            while (diffLon < -180.0) diffLon += 360.0
            val targetLon = startLon + diffLon

            val zoomInDuration = 540_000_000L
            val zoomInStart = System.nanoTime()
            while (true) {
                val elapsed = System.nanoTime() - zoomInStart
                val t = (elapsed.toFloat() / zoomInDuration).coerceIn(0f, 1f)
                val ease = 1f - (1f - t) * (1f - t) * (1f - t)

                val curLat = startLat + (lat - startLat) * ease
                var curLon = startLon + (targetLon - startLon) * ease
                while (curLon > 180.0) curLon -= 360.0
                while (curLon < -180.0) curLon += 360.0
                val curZoom = startZoom + (peakZoom - startZoom) * ease

                _uiState.update {
                    it.copy(
                        centerLat = curLat,
                        centerLon = curLon,
                        zoomScale = curZoom
                    )
                }
                if (t >= 1f) break
                delay(8)
            }

            _uiState.update {
                it.copy(
                    centerLat = lat,
                    centerLon = lon,
                    zoomScale = peakZoom,
                    selectedCountry = matchedCountry ?: it.selectedCountry
                )
            }

            // Brief pause at zoomed-in view so the highlighted location is clearly seen
            delay(550)

            // Phase 2: Smoothly zoom back OUT to normal conditions (1.0f) while keeping focus on (lat, lon)
            val zoomOutDuration = 520_000_000L
            val zoomOutStart = System.nanoTime()
            val normalZoom = 1.0f
            while (true) {
                val elapsed = System.nanoTime() - zoomOutStart
                val t = (elapsed.toFloat() / zoomOutDuration).coerceIn(0f, 1f)
                val ease = 1f - (1f - t) * (1f - t) * (1f - t)
                val curZoom = peakZoom + (normalZoom - peakZoom) * ease

                _uiState.update {
                    it.copy(
                        centerLat = lat,
                        centerLon = lon,
                        zoomScale = curZoom
                    )
                }
                if (t >= 1f) break
                delay(8)
            }

            _uiState.update {
                it.copy(
                    centerLat = lat,
                    centerLon = lon,
                    zoomScale = normalZoom
                )
            }
        }

        viewModelScope.launch {
            repository.recordLocation(
                name = label,
                countryCode = matchedCountry?.id ?: "GPS",
                capital = matchedCountry?.capital ?: "Current Position",
                continent = matchedCountry?.continent ?: "Live",
                latitude = lat,
                longitude = lon,
                visitType = "GPS"
            )
        }
    }

    fun openDetailSheet() {
        _uiState.update { it.copy(showDetailSheet = true) }
    }

    fun closeDetailSheet() {
        _uiState.update { it.copy(showDetailSheet = false) }
    }

    fun openHistorySheet() {
        _uiState.update { it.copy(showHistorySheet = true) }
    }

    fun closeHistorySheet() {
        _uiState.update { it.copy(showHistorySheet = false) }
    }

    fun toggleFavorite(item: LocationHistoryEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(item.id, !item.isFavorite)
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }

    fun saveHistoryNotes(id: Long, notes: String) {
        viewModelScope.launch {
            repository.updateNotes(id, notes)
        }
    }

    fun openDirectionDialog() {
        _uiState.update { it.copy(showDirectionDialog = true) }
    }

    fun closeDirectionDialog() {
        _uiState.update { it.copy(showDirectionDialog = false) }
    }

    fun startDirectionRoute(startCountry: CountryInfo, endCountry: CountryInfo) {
        val midPoint = GlobeMath.interpolateGreatCircle(
            lat1 = startCountry.capitalLat,
            lon1 = startCountry.capitalLon,
            lat2 = endCountry.capitalLat,
            lon2 = endCountry.capitalLon,
            t = 0.5f
        )
        val distKm = GlobeMath.exactCapitalDistanceKm(startCountry, endCountry)
        val idealZoom = when {
            distKm > 9000 -> 0.88f
            distKm > 5000 -> 1.02f
            distKm > 2500 -> 1.18f
            else -> 1.35f
        }
        val routeLabel = "${startCountry.name} ✈ ${endCountry.name}"

        _uiState.update {
            it.copy(
                showDirectionDialog = false,
                routeStartCountry = startCountry,
                routeEndCountry = endCountry,
                selectedCountry = endCountry,
                highlightedCustomPoint = GeoPoint(endCountry.capitalLat, endCountry.capitalLon),
                highlightedLabel = routeLabel,
                searchQuery = routeLabel,
                searchSuggestions = emptyList()
            )
        }

        // Smoothly rotate globe to the Great-Circle midpoint and keep auto-rotating if already active!
        flyTo(
            lat = midPoint.lat,
            lon = midPoint.lon,
            zoom = idealZoom,
            country = endCountry,
            preserveAutoRotate = true
        )

        viewModelScope.launch {
            repository.recordLocation(
                name = routeLabel,
                countryCode = endCountry.id,
                capital = "${startCountry.capital} → ${endCountry.capital}",
                continent = endCountry.continent,
                latitude = endCountry.centerLat,
                longitude = endCountry.centerLon,
                visitType = "ROUTE"
            )
        }
    }

    fun clearDirectionRoute() {
        _uiState.update {
            it.copy(
                routeStartCountry = null,
                routeEndCountry = null
            )
        }
    }

    fun openSettingsDialog() {
        _uiState.update { it.copy(showSettingsDialog = true) }
    }

    fun closeSettingsDialog() {
        _uiState.update { it.copy(showSettingsDialog = false) }
    }

    fun setThemeMode(mode: AppThemeMode) {
        prefs.edit().putString("theme_mode", mode.name).apply()
        _uiState.update { it.copy(themeMode = mode) }
    }

    fun recordInHistory(country: CountryInfo, type: String = "SEARCHED", notes: String? = null) {
        viewModelScope.launch {
            repository.recordLocation(
                name = country.name,
                countryCode = country.id,
                capital = country.capital,
                continent = country.continent,
                latitude = country.centerLat,
                longitude = country.centerLon,
                visitType = type,
                userNotes = notes
            )
        }
    }
}
