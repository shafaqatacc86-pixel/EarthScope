package com.example.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.WorldGeographicData
import com.example.globe.CosmicSoundEngine
import com.example.globe.GlobeCanvas
import com.example.globe.GlobeMath
import kotlin.math.roundToInt

@SuppressLint("MissingPermission")
@Composable
fun GlobeScreen(viewModel: GlobeViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyItems by viewModel.historyItems.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val animatedZoom = remember { Animatable(uiState.zoomScale) }
    val soundEngine = remember { CosmicSoundEngine() }

    val systemDark = isSystemInDarkTheme()
    val isLightTheme = when (uiState.themeMode) {
        AppThemeMode.LIGHT -> true
        AppThemeMode.DARK -> false
        AppThemeMode.SYSTEM -> !systemDark
    }

    val topSurfaceColor = if (isLightTheme) Color(0xF2FFFFFF) else Color(0xDD111C30)
    val topBorderColor = if (isLightTheme) Color(0xFFCBD5E1) else Color(0xFF263955)
    val primaryTextColor = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryTextColor = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)
    val defaultIconTint = if (isLightTheme) Color(0xFF1E293B) else Color.White

    // Play cosmic starlight sound ONLY when auto-rotation is active AND sound is enabled
    val shouldPlaySound = uiState.isAutoRotating && uiState.isSoundEnabled
    LaunchedEffect(shouldPlaySound) {
        soundEngine.setPlaying(shouldPlaySound)
    }

    DisposableEffect(Unit) {
        onDispose {
            soundEngine.stopSound()
        }
    }

    // Real-time Android Hardware Compass Sensor Listener (Rotation Vector / Magnetometer + Accelerometer)
    DisposableEffect(uiState.isCompassModeActive) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        if (!uiState.isCompassModeActive || sensorManager == null) {
            onDispose { }
        } else {
            val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            val magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

            val gravity = FloatArray(3)
            val geomagnetic = FloatArray(3)
            var hasGravity = false
            var hasMag = false
            val rotationMatrix = FloatArray(9)
            val orientationAngles = FloatArray(3)

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null) return
                    when (event.sensor.type) {
                        Sensor.TYPE_ROTATION_VECTOR -> {
                            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                            SensorManager.getOrientation(rotationMatrix, orientationAngles)
                            val azimuthDeg = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                            val normalized = ((azimuthDeg % 360f) + 360f) % 360f
                            viewModel.onSensorAzimuthChanged(normalized)
                        }
                        Sensor.TYPE_ACCELEROMETER -> {
                            System.arraycopy(event.values, 0, gravity, 0, 3)
                            hasGravity = true
                            if (rotationSensor == null && hasMag) {
                                if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                                    val azimuthDeg = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                                    val normalized = ((azimuthDeg % 360f) + 360f) % 360f
                                    viewModel.onSensorAzimuthChanged(normalized)
                                }
                            }
                        }
                        Sensor.TYPE_MAGNETIC_FIELD -> {
                            System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                            hasMag = true
                            if (rotationSensor == null && hasGravity) {
                                if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                                    val azimuthDeg = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()
                                    val normalized = ((azimuthDeg % 360f) + 360f) % 360f
                                    viewModel.onSensorAzimuthChanged(normalized)
                                }
                            }
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            if (rotationSensor != null) {
                sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_GAME)
            } else {
                if (accelSensor != null) {
                    sensorManager.registerListener(listener, accelSensor, SensorManager.SENSOR_DELAY_GAME)
                }
                if (magSensor != null) {
                    sensorManager.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_GAME)
                }
            }

            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }

    LaunchedEffect(uiState.zoomScale) {
        val diff = abs(animatedZoom.value - uiState.zoomScale)
        if (diff > 0.08f) {
            animatedZoom.animateTo(
                targetValue = uiState.zoomScale,
                animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
            )
        } else if (diff > 0.0001f) {
            animatedZoom.snapTo(uiState.zoomScale)
        }
    }

    // GPS Location Permission Launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            locateUser(context, viewModel)
        } else {
            Toast.makeText(context, "Location permission needed to locate on globe", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isLightTheme) Color(0xFFF0F9FF) else Color(0xFF030712))
    ) {
        // 1. The 3D Rotating & Zooming Earth Globe
        GlobeCanvas(
            centerLat = uiState.centerLat,
            centerLon = uiState.centerLon,
            zoomScale = animatedZoom.value,
            selectedCountry = uiState.selectedCountry,
            highlightedCustomPoint = uiState.highlightedCustomPoint,
            userLocation = uiState.userLocation,
            routeStartCountry = uiState.routeStartCountry,
            routeEndCountry = uiState.routeEndCountry,
            compassBearingDeg = uiState.compassBearingDeg,
            isCompassModeActive = uiState.isCompassModeActive,
            isLightTheme = isLightTheme,
            modifier = Modifier.fillMaxSize(),
            onRotateDrag = { dx, dy -> viewModel.onRotateDrag(dx, dy) },
            onZoomChange = { delta, fLat, fLon ->
                val current = animatedZoom.value
                val next = (current * delta).coerceIn(0.6f, 5.0f)
                coroutineScope.launch {
                    animatedZoom.snapTo(next)
                }
                viewModel.onZoomChange(delta, fLat, fLon)
            },
            onGlobeTap = { sx, sy, radius, cx, cy ->
                viewModel.onGlobeTap(sx, sy, radius, cx, cy)
            },
            onFling = { vx, vy -> viewModel.onFling(vx, vy) },
            onStopFling = { viewModel.stopFling() }
        )

        // 2. Top Bar: Search Input, History Button & Settings Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Bar Capsule
                Surface(
                    color = topSurfaceColor,
                    shape = RoundedCornerShape(28.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, topBorderColor),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(12.dp, RoundedCornerShape(28.dp), spotColor = Color(0x550284C7))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = secondaryTextColor,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        TextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            placeholder = {
                                Text(
                                    text = "Search country or city...",
                                    color = secondaryTextColor,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedTextColor = primaryTextColor,
                                unfocusedTextColor = primaryTextColor
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    viewModel.onSearchSubmit(uiState.searchQuery)
                                    focusManager.clearFocus()
                                }
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("search_text_input")
                        )

                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    viewModel.onSearchQueryChanged("")
                                    focusManager.clearFocus()
                                },
                                modifier = Modifier
                                    .size(30.dp)
                                    .testTag("clear_search_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = secondaryTextColor,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Location History Action Button with Badge
                IconButton(
                    onClick = { viewModel.openHistorySheet() },
                    modifier = Modifier
                        .size(48.dp)
                        .background(topSurfaceColor, CircleShape)
                        .border(1.dp, topBorderColor, CircleShape)
                        .testTag("open_history_button")
                ) {
                    BadgedBox(
                        badge = {
                            if (historyItems.isNotEmpty()) {
                                Badge(
                                    containerColor = Color(0xFF22C55E),
                                    contentColor = Color.White
                                ) {
                                    Text(
                                        text = historyItems.size.coerceAtMost(99).toString(),
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Location History",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(23.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Settings Action Button (White Theme / Black Theme / System Default)
                IconButton(
                    onClick = { viewModel.openSettingsDialog() },
                    modifier = Modifier
                        .size(48.dp)
                        .background(topSurfaceColor, CircleShape)
                        .border(1.dp, topBorderColor, CircleShape)
                        .testTag("open_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Theme Settings",
                        tint = if (isLightTheme) Color(0xFF0F172A) else Color(0xFF38BDF8),
                        modifier = Modifier.size(23.dp)
                    )
                }
            }

            // Live Compass Mode Status Pill (shows N / NE / E / SE / S / SW / W / NW + degrees when active)
            AnimatedVisibility(
                visible = uiState.isCompassModeActive,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val headingDeg = uiState.compassBearingDeg.roundToInt() % 360
                val dirs = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
                val cardinalDir = dirs[((uiState.compassBearingDeg + 22.5f) / 45f).toInt() % 8]
                val fullCardinal = when (cardinalDir) {
                    "N" -> "North"
                    "NE" -> "North-East"
                    "E" -> "East"
                    "SE" -> "South-East"
                    "S" -> "South"
                    "SW" -> "South-West"
                    "W" -> "West"
                    else -> "North-West"
                }

                Surface(
                    color = topSurfaceColor,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, Color(0xFFEF4444)),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .shadow(8.dp, RoundedCornerShape(20.dp))
                        .clickable { viewModel.toggleCompassMode() }
                        .testTag("live_compass_status_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = "Compass Needle",
                            tint = Color(0xFFEF4444),
                            modifier = Modifier
                                .size(19.dp)
                                .rotate(-uiState.compassBearingDeg)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "LIVE COMPASS: $headingDeg° $cardinalDir ($fullCardinal)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryTextColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• Rotate phone to orient map",
                            fontSize = 11.sp,
                            color = secondaryTextColor
                        )
                    }
                }
            }

            // Search suggestions dropdown list
            AnimatedVisibility(
                visible = uiState.searchSuggestions.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Surface(
                    color = if (isLightTheme) Color(0xFAFFFFFF) else Color(0xF0111C30),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, topBorderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .shadow(16.dp, RoundedCornerShape(16.dp))
                ) {
                    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                        items(uiState.searchSuggestions) { country ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.selectCountry(country)
                                        focusManager.clearFocus()
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .testTag("suggestion_${country.id}"),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = country.flagEmoji,
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = country.name,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryTextColor
                                    )
                                    Text(
                                        text = "${country.capital} • ${country.continent}",
                                        fontSize = 12.sp,
                                        color = secondaryTextColor
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = secondaryTextColor
                                )
                            }
                        }
                    }
                }
            }
        }

        // 3. Right-side Floating Controls (Zoom +, Zoom -, Compass, Auto-Rotate, Music, GPS, Reset, Directions)
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(end = 14.dp)
                .padding(top = 64.dp, bottom = 84.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Zoom In (+) Button
            FloatingCircleButton(
                icon = Icons.Default.Add,
                contentDescription = "Zoom In",
                tag = "zoom_in_button",
                tint = defaultIconTint,
                isLightTheme = isLightTheme,
                onClick = {
                    val target = (animatedZoom.value * 1.38f).coerceIn(0.6f, 5.0f)
                    viewModel.onZoomDirect(target)
                    coroutineScope.launch {
                        animatedZoom.animateTo(
                            targetValue = target,
                            animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
                        )
                    }
                }
            )

            // Zoom Out (-) Button
            FloatingCircleButton(
                icon = Icons.Default.Remove,
                contentDescription = "Zoom Out",
                tag = "zoom_out_button",
                tint = defaultIconTint,
                isLightTheme = isLightTheme,
                onClick = {
                    val target = (animatedZoom.value / 1.38f).coerceIn(0.6f, 5.0f)
                    viewModel.onZoomDirect(target)
                    coroutineScope.launch {
                        animatedZoom.animateTo(
                            targetValue = target,
                            animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing)
                        )
                    }
                }
            )

            // Live Compass Button (Stops auto-rotation, centers on user/main location, and rotates N/S/E/W with phone)
            FloatingCircleButton(
                icon = Icons.Default.Explore,
                contentDescription = "Live Compass",
                tag = "compass_button",
                tint = if (uiState.isCompassModeActive) Color(0xFFEF4444) else defaultIconTint,
                iconRotationDeg = if (uiState.isCompassModeActive) -uiState.compassBearingDeg else 0f,
                isLightTheme = isLightTheme,
                onClick = {
                    val bestLoc = getBestLastKnownLocation(context)
                    viewModel.toggleCompassMode(
                        userLat = bestLoc?.latitude,
                        userLon = bestLoc?.longitude
                    )
                }
            )

            // Auto-Rotate Toggle Button
            FloatingCircleButton(
                icon = Icons.Default.Sync,
                contentDescription = "Auto Rotate",
                tag = "auto_rotate_button",
                tint = if (uiState.isAutoRotating) Color(0xFF16A34A) else defaultIconTint,
                isLightTheme = isLightTheme,
                onClick = { viewModel.toggleAutoRotate() }
            )

            // Music / Star Sound Toggle Button (Active only when Auto-Rotate is enabled)
            FloatingCircleButton(
                icon = if (uiState.isSoundEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                contentDescription = "Rotation Sound",
                tag = "sound_toggle_button",
                enabled = uiState.isAutoRotating,
                isLightTheme = isLightTheme,
                tint = when {
                    !uiState.isAutoRotating -> Color(0xFF94A3B8)
                    uiState.isSoundEnabled -> Color(0xFF16A34A)
                    else -> Color(0xFFEF4444)
                },
                onClick = {
                    if (uiState.isAutoRotating) {
                        viewModel.toggleSound()
                    } else {
                        Toast.makeText(
                            context,
                            "Enable Auto-Rotate first to control rotation sound",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )

            // GPS Location Button
            FloatingCircleButton(
                icon = Icons.Default.MyLocation,
                contentDescription = "My Location",
                tag = "my_location_button",
                isLightTheme = isLightTheme,
                tint = if (uiState.userLocation != null) Color(0xFF0284C7) else defaultIconTint,
                onClick = {
                    val fineGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                    val coarseGranted = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

                    if (fineGranted || coarseGranted) {
                        locateUser(context, viewModel)
                    } else {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                }
            )

            // Reset View Button
            FloatingCircleButton(
                icon = Icons.Default.RestartAlt,
                contentDescription = "Reset View",
                tag = "reset_view_button",
                tint = defaultIconTint,
                isLightTheme = isLightTheme,
                onClick = { viewModel.resetView() }
            )

            // Flight Directions Button (Start Country -> Destination Country with moving Plane)
            FloatingCircleButton(
                icon = Icons.Default.Directions,
                contentDescription = "Flight Directions",
                tag = "directions_button",
                isLightTheme = isLightTheme,
                tint = if (uiState.routeStartCountry != null && uiState.routeEndCountry != null) {
                    if (isLightTheme) Color(0xFFD97706) else Color(0xFFFACC15)
                } else {
                    Color(0xFF0284C7)
                },
                onClick = { viewModel.openDirectionDialog() }
            )
        }

        // 4. Bottom Location Badge Pill (Responsive, non-cutting text, shows exact KM distance when route is active)
        val label = uiState.highlightedLabel.ifBlank { uiState.selectedCountry?.name.orEmpty() }
        val routeDistFormatted = remember(uiState.routeStartCountry, uiState.routeEndCountry) {
            val s = uiState.routeStartCountry
            val e = uiState.routeEndCountry
            if (s != null && e != null) {
                val exactKm = GlobeMath.exactCapitalDistanceKm(s, e)
                String.format(java.util.Locale.US, "%,.1f", exactKm)
            } else null
        }

        if (label.isNotBlank() && (uiState.selectedCountry != null || uiState.userLocation != null)) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    color = if (isLightTheme) Color(0xF5FFFFFF) else Color(0xEE0B192C),
                    shape = RoundedCornerShape(32.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5f.dp, Color(0xFF22C55E)),
                    modifier = Modifier
                        .shadow(16.dp, RoundedCornerShape(32.dp), spotColor = Color(0xFF22C55E))
                        .clickable { viewModel.openDetailSheet() }
                        .testTag("location_badge_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Green Circle with White Pin
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(Color(0xFF22C55E), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // Location Name + Route Distance Subtitle (Responsive, never cuts off)
                        Column(
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            Text(
                                text = label,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryTextColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (routeDistFormatted != null) {
                                val startCap = uiState.routeStartCountry?.capital.orEmpty()
                                val endCap = uiState.routeEndCountry?.capital.orEmpty()
                                Text(
                                    text = "$routeDistFormatted km ($startCap → $endCap) • Tap for details",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isLightTheme) Color(0xFFD97706) else Color(0xFFFACC15),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Chevron >
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Details",
                            tint = if (isLightTheme) Color(0xFF16A34A) else Color(0xFF86EFAC),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Reserved clean bottom space (68.dp) above system navigation menu for future Banner Ad placement
                Spacer(modifier = Modifier.height(68.dp))
            }
        }

        // 5. Location & Flight Route Details Bottom Sheet
        if (uiState.showDetailSheet) {
            LocationDetailSheet(
                country = uiState.selectedCountry,
                routeStartCountry = uiState.routeStartCountry,
                routeEndCountry = uiState.routeEndCountry,
                customLabel = uiState.highlightedLabel,
                isLightTheme = isLightTheme,
                onDismiss = { viewModel.closeDetailSheet() },
                onTrackAsVisited = { note ->
                    val country = uiState.selectedCountry
                    if (country != null) {
                        viewModel.recordInHistory(country = country, type = "VISITED", notes = note)
                    }
                }
            )
        }

        // 6. Location History Tracking Bottom Sheet
        if (uiState.showHistorySheet) {
            LocationHistorySheet(
                historyList = historyItems,
                isLightTheme = isLightTheme,
                onDismiss = { viewModel.closeHistorySheet() },
                onSelectLocation = { item ->
                    val c = WorldGeographicData.findCountry(item.name)
                    if (c != null) {
                        viewModel.selectCountry(c)
                    } else {
                        viewModel.flyTo(item.latitude, item.longitude, zoom = 1.35f)
                    }
                },
                onToggleFavorite = { item -> viewModel.toggleFavorite(item) },
                onDeleteLocation = { id -> viewModel.deleteHistoryItem(id) },
                onClearAll = { viewModel.clearAllHistory() }
            )
        }

        // 7. Country Flight Directions Dialog (Start Country -> Destination Country)
        if (uiState.showDirectionDialog) {
            DirectionRouteDialog(
                initialStartCountry = uiState.routeStartCountry ?: uiState.selectedCountry,
                initialEndCountry = uiState.routeEndCountry,
                hasActiveRoute = uiState.routeStartCountry != null && uiState.routeEndCountry != null,
                isLightTheme = isLightTheme,
                onDismiss = { viewModel.closeDirectionDialog() },
                onShowDirection = { start, end ->
                    viewModel.startDirectionRoute(start, end)
                },
                onClearRoute = {
                    viewModel.clearDirectionRoute()
                }
            )
        }

        // 8. Theme Settings Dialog (System Default / White Theme / Black Theme)
        if (uiState.showSettingsDialog) {
            ThemeSettingsDialog(
                currentThemeMode = uiState.themeMode,
                isLightTheme = isLightTheme,
                onSelectTheme = { mode ->
                    viewModel.setThemeMode(mode)
                },
                onDismiss = { viewModel.closeSettingsDialog() }
            )
        }
    }
}

@Composable
private fun FloatingCircleButton(
    icon: ImageVector,
    contentDescription: String,
    tag: String,
    tint: Color = Color.White,
    iconRotationDeg: Float = 0f,
    enabled: Boolean = true,
    isLightTheme: Boolean = false,
    onClick: () -> Unit
) {
    val bgColor = when {
        isLightTheme && enabled -> Color(0xF0FFFFFF)
        isLightTheme && !enabled -> Color(0x99F1F5F9)
        !isLightTheme && enabled -> Color(0xCC0E1A2E)
        else -> Color(0x660E1A2E)
    }
    val borderColor = when {
        isLightTheme && enabled -> Color(0xFFCBD5E1)
        isLightTheme && !enabled -> Color(0xFFE2E8F0)
        !isLightTheme && enabled -> Color(0xFF263955)
        else -> Color(0xFF1E293B)
    }

    Surface(
        color = bgColor,
        shape = CircleShape,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        modifier = Modifier
            .size(44.dp)
            .alpha(if (enabled) 1f else 0.48f)
            .shadow(if (enabled) 8.dp else 2.dp, CircleShape)
            .clip(CircleShape)
            .clickable { onClick() }
            .testTag(tag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier
                    .size(21.dp)
                    .rotate(iconRotationDeg)
            )
        }
    }
}

@SuppressLint("MissingPermission")
private fun getBestLastKnownLocation(context: Context): Location? {
    return try {
        val fineGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarseGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!fineGranted && !coarseGranted) return null

        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
        val providers = lm.getProviders(true)
        var bestLoc: Location? = null
        for (p in providers) {
            val l = lm.getLastKnownLocation(p) ?: continue
            if (bestLoc == null || l.accuracy < bestLoc.accuracy) {
                bestLoc = l
            }
        }
        bestLoc
    } catch (_: Exception) {
        null
    }
}

@SuppressLint("MissingPermission")
private fun locateUser(context: Context, viewModel: GlobeViewModel) {
    try {
        val bestLoc = getBestLastKnownLocation(context)
        if (bestLoc != null) {
            viewModel.onUserGpsLocated(bestLoc.latitude, bestLoc.longitude)
            Toast.makeText(
                context,
                "Located: ${String.format("%.2f", bestLoc.latitude)}°, ${String.format("%.2f", bestLoc.longitude)}°",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(context, "Acquiring GPS position...", Toast.LENGTH_SHORT).show()
            // Default fallback to center of user's timezone or approximate
            viewModel.onUserGpsLocated(31.5204, 74.3587) // Lahore / South Asia fallback
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Could not acquire location", Toast.LENGTH_SHORT).show()
    }
}

