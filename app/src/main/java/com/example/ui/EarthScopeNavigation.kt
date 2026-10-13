package com.example.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.BatteryManager
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.LocalFreeFeaturesStore
import com.example.data.model.WorldGeographicData
import kotlinx.coroutines.delay

enum class EarthScopeDestination {
    SPLASH,
    HOME_DASHBOARD,
    WORLD_GLOBE,
    SPEEDOMETER,
    MAP_PAGE,
    COMPASS_ONLY,
    NEARBY_PLACES,
    SHARE_LOCATION,
    ALTITUDE_COORDINATES,
    COUNTRY_ATLAS,
    SAVED_PLACES,
    TRIP_HISTORY
}

data class DashboardFeatureCard(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val accentColor: Color,
    val destination: EarthScopeDestination? = null,
    val actionType: String = "NAV"
)

@Composable
fun EarthScopeAppHost(viewModel: GlobeViewModel) {
    val context = LocalContext.current
    val localStore = remember { LocalFreeFeaturesStore(context) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyItems by viewModel.historyItems.collectAsStateWithLifecycle()
    val systemDark = isSystemInDarkTheme()
    val isLightTheme = when (uiState.themeMode) {
        AppThemeMode.LIGHT -> true
        AppThemeMode.DARK -> false
        AppThemeMode.SYSTEM -> !systemDark
    }

    // Keep Android Status Bar & Navigation Bar icons (clock, battery, wifi, nav buttons)
    // clearly visible in both Light and Dark mode regardless of phone system settings!
    val view = androidx.compose.ui.platform.LocalView.current
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
            val insetsController = androidx.core.view.WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = isLightTheme
            insetsController.isAppearanceLightNavigationBars = isLightTheme
            @Suppress("DEPRECATION")
            window.statusBarColor = if (isLightTheme) {
                android.graphics.Color.parseColor("#F8FAFC")
            } else {
                android.graphics.Color.parseColor("#040812")
            }
            @Suppress("DEPRECATION")
            window.navigationBarColor = if (isLightTheme) {
                android.graphics.Color.parseColor("#FFFFFF")
            } else {
                android.graphics.Color.parseColor("#040812")
            }
        }
    }

    var currentDestination by remember { mutableStateOf(EarthScopeDestination.SPLASH) }
    var initialMapLat by remember { mutableStateOf<Double?>(null) }
    var initialMapLon by remember { mutableStateOf<Double?>(null) }
    var initialMapLabel by remember { mutableStateOf<String?>(null) }
    var startInMeasureMode by remember { mutableStateOf(false) }

    // First-Launch Privacy Policy & Terms Consent Dialog State
    var hasAcceptedPrivacyTerms by remember { mutableStateOf(localStore.hasAcceptedPrivacyAndTerms()) }

    // Frequency-Capped Interstitial Ad Manager (Shows on feature navigation)
    var featureNavigationClickCount by remember { mutableIntStateOf(0) }
    var pendingInterstitialAction by remember { mutableStateOf<(() -> Unit)?>(null) }
    var showInterstitialDialog by remember { mutableStateOf(false) }

    fun navigateWithOptionalInterstitial(triggerLabel: String, action: () -> Unit) {
        featureNavigationClickCount += 1
        // Show interstitial ad every 2nd navigation so user can clearly test it!
        if (featureNavigationClickCount % 2 == 0) {
            pendingInterstitialAction = action
            showInterstitialDialog = true
        } else {
            action()
        }
    }

    val dashboardGridState = androidx.compose.foundation.lazy.grid.rememberLazyGridState()

    // Fast, smooth startup transition (550ms) without any heavy blocking loading screen or App Open Ad wait
    LaunchedEffect(Unit) {
        delay(550)
        if (currentDestination == EarthScopeDestination.SPLASH) {
            currentDestination = EarthScopeDestination.HOME_DASHBOARD
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF040812))
    ) {
        when (currentDestination) {
            EarthScopeDestination.SPLASH -> {
                EarthScopeSplashScreen(
                    isLightTheme = isLightTheme,
                    onContinue = {
                        currentDestination = EarthScopeDestination.HOME_DASHBOARD
                    }
                )
            }

            EarthScopeDestination.HOME_DASHBOARD -> {
                EarthScopeDashboardScreen(
                    gridState = dashboardGridState,
                    isLightTheme = isLightTheme,
                    onNavigate = { dest ->
                        navigateWithOptionalInterstitial("Open ${dest.name}") {
                            startInMeasureMode = false
                            currentDestination = dest
                        }
                    },
                    onOpenMeasureMap = {
                        navigateWithOptionalInterstitial("Open Map Distance Measure") {
                            startInMeasureMode = true
                            currentDestination = EarthScopeDestination.MAP_PAGE
                        }
                    },
                    onOpenSettings = { viewModel.openSettingsDialog() },
                    onOpenFlightDirections = {
                        navigateWithOptionalInterstitial("Open Air Distance Route") {
                            currentDestination = EarthScopeDestination.WORLD_GLOBE
                            viewModel.openDirectionDialog()
                        }
                    }
                )
            }

            EarthScopeDestination.WORLD_GLOBE -> {
                GlobeScreen(
                    viewModel = viewModel,
                    onBackToDashboard = { currentDestination = EarthScopeDestination.HOME_DASHBOARD }
                )
            }

            EarthScopeDestination.SPEEDOMETER -> {
                SpeedometerScreen(
                    isLightTheme = isLightTheme,
                    onBack = { currentDestination = EarthScopeDestination.HOME_DASHBOARD },
                    onOpenTripHistory = { currentDestination = EarthScopeDestination.TRIP_HISTORY }
                )
            }

            EarthScopeDestination.MAP_PAGE -> {
                InteractiveMapPageScreen(
                    viewModel = viewModel,
                    isLightTheme = isLightTheme,
                    initialTargetLat = initialMapLat,
                    initialTargetLon = initialMapLon,
                    initialTargetLabel = initialMapLabel,
                    initialMeasureMode = startInMeasureMode,
                    onBack = {
                        initialMapLat = null
                        initialMapLon = null
                        initialMapLabel = null
                        startInMeasureMode = false
                        currentDestination = EarthScopeDestination.HOME_DASHBOARD
                    }
                )
            }

            EarthScopeDestination.COMPASS_ONLY -> {
                StandaloneCompassScreen(
                    isLightTheme = isLightTheme,
                    onBack = { currentDestination = EarthScopeDestination.HOME_DASHBOARD }
                )
            }

            EarthScopeDestination.NEARBY_PLACES -> {
                NearbyPlacesScreen(
                    isLightTheme = isLightTheme,
                    onBack = { currentDestination = EarthScopeDestination.HOME_DASHBOARD },
                    onOpenInAppMap = {
                        currentDestination = EarthScopeDestination.MAP_PAGE
                    }
                )
            }

            EarthScopeDestination.SHARE_LOCATION -> {
                ShareLocationScreen(
                    isLightTheme = isLightTheme,
                    onBack = { currentDestination = EarthScopeDestination.HOME_DASHBOARD }
                )
            }

            EarthScopeDestination.ALTITUDE_COORDINATES -> {
                AltitudeAndGpsInfoScreen(
                    isLightTheme = isLightTheme,
                    onBack = { currentDestination = EarthScopeDestination.HOME_DASHBOARD }
                )
            }

            EarthScopeDestination.COUNTRY_ATLAS -> {
                WorldCountryAtlasScreen(
                    viewModel = viewModel,
                    isLightTheme = isLightTheme,
                    onBack = { currentDestination = EarthScopeDestination.HOME_DASHBOARD },
                    onOpenOn3DGlobe = { country ->
                        viewModel.selectCountry(country)
                        currentDestination = EarthScopeDestination.WORLD_GLOBE
                    }
                )
            }

            EarthScopeDestination.SAVED_PLACES -> {
                SavedPlacesScreen(
                    isLightTheme = isLightTheme,
                    onBack = { currentDestination = EarthScopeDestination.HOME_DASHBOARD },
                    onOpenLocationOnMap = { lat, lon, label ->
                        initialMapLat = lat
                        initialMapLon = lon
                        initialMapLabel = label
                        startInMeasureMode = false
                        currentDestination = EarthScopeDestination.MAP_PAGE
                    }
                )
            }

            EarthScopeDestination.TRIP_HISTORY -> {
                TripHistoryScreen(
                    isLightTheme = isLightTheme,
                    onBack = { currentDestination = EarthScopeDestination.HOME_DASHBOARD }
                )
            }
        }

        if (uiState.showSettingsDialog) {
            ThemeSettingsDialog(
                currentThemeMode = uiState.themeMode,
                isLightTheme = isLightTheme,
                onSelectTheme = { mode -> viewModel.setThemeMode(mode) },
                onDismiss = { viewModel.closeSettingsDialog() }
            )
        }

        if (uiState.showHistorySheet && currentDestination == EarthScopeDestination.HOME_DASHBOARD) {
            LocationHistorySheet(
                historyList = historyItems,
                isLightTheme = isLightTheme,
                onDismiss = { viewModel.closeHistorySheet() },
                onSelectLocation = { item ->
                    viewModel.closeHistorySheet()
                    currentDestination = EarthScopeDestination.WORLD_GLOBE
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

        // First-Launch Privacy Policy & Terms Consent Dialog (Shown on startup until accepted)
        if (!hasAcceptedPrivacyTerms && currentDestination != EarthScopeDestination.SPLASH) {
            FirstLaunchPrivacyTermsDialog(
                isLightTheme = isLightTheme,
                onAcceptAndContinue = {
                    localStore.setAcceptedPrivacyAndTerms(true)
                    hasAcceptedPrivacyTerms = true
                }
            )
        }

        // Frequency-Capped Interstitial Ad Dialog
        if (showInterstitialDialog) {
            AdMobDummyInterstitialDialog(
                placementTrigger = "Feature Navigation",
                adUnitId = AdMobConfig.INTERSTITIAL_FEATURE_NAV_AD_UNIT_ID,
                onAdClosed = {
                    showInterstitialDialog = false
                    val action = pendingInterstitialAction
                    pendingInterstitialAction = null
                    action?.invoke()
                }
            )
        }
    }
}

/**
 * Clean, Minimal Splash Screen (Zero background box behind the logo)
 */
@Composable
fun EarthScopeSplashScreen(
    isLightTheme: Boolean,
    onContinue: () -> Unit
) {
    val bgBrush = if (isLightTheme) {
        Brush.verticalGradient(listOf(Color(0xFFF8FAFC), Color(0xFFE0F2FE)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFF040914), Color(0xFF0B192C)))
    }
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val infiniteTransition = rememberInfiniteTransition(label = "splash_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgBrush)
            .clickable { onContinue() }
            .testTag("earth_scope_splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            EarthScopeTransparentLogo(
                modifier = Modifier
                    .size(132.dp)
                    .scale(pulseScale)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Earth Scope",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = primaryText
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "3D Globe • Google Maps • Compass • Speed",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = secondaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Live Battery & GPS Status Pill for Top Bars
 */
@Composable
fun GpsAndBatteryStatusPill(isLightTheme: Boolean) {
    val context = LocalContext.current
    var batteryPct by remember { mutableIntStateOf(100) }
    var isCharging by remember { mutableStateOf(false) }
    var isGpsActive by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            try {
                val bm = context.getSystemService(Context.BATTERY_SERVICE) as? BatteryManager
                val cap = bm?.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) ?: -1
                if (cap in 0..100) {
                    batteryPct = cap
                } else {
                    val intent = context.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                    val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                    val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                    if (level >= 0 && scale > 0) {
                        batteryPct = (level * 100) / scale
                    }
                    val status = intent?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
                    isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
                }

                val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                val hasPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                isGpsActive = hasPerm && (lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true || lm?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true)
            } catch (_: Exception) {}
            delay(5000)
        }
    }

    val pillBg = if (isLightTheme) Color(0xFFF1F5F9) else Color(0xFF1E293B)
    val textCol = if (isLightTheme) Color(0xFF0F172A) else Color.White

    Surface(
        color = pillBg,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF334155))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.GpsFixed,
                contentDescription = "GPS Status",
                tint = if (isGpsActive) Color(0xFF22C55E) else Color(0xFFF59E0B),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = if (isGpsActive) "GPS" else "Wait",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isGpsActive) Color(0xFF22C55E) else Color(0xFFF59E0B)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = if (isCharging) Icons.Default.BatteryChargingFull else Icons.Default.BatteryFull,
                contentDescription = "Battery",
                tint = if (batteryPct > 20) Color(0xFF0284C7) else Color(0xFFEF4444),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = "$batteryPct%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = textCol
            )
        }
    }
}

/**
 * Modern, Simple, Visual & User-Friendly Mobile App Home Screen
 */
@Composable
fun EarthScopeDashboardScreen(
    gridState: androidx.compose.foundation.lazy.grid.LazyGridState = androidx.compose.foundation.lazy.grid.rememberLazyGridState(),
    isLightTheme: Boolean,
    onNavigate: (EarthScopeDestination) -> Unit,
    onOpenMeasureMap: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenFlightDirections: () -> Unit
) {
    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val features = remember {
        listOf(
            DashboardFeatureCard(
                id = "world_globe",
                title = "3D World",
                subtitle = "Ball Globe",
                icon = Icons.Default.Public,
                accentColor = Color(0xFF0284C7),
                destination = EarthScopeDestination.WORLD_GLOBE
            ),
            DashboardFeatureCard(
                id = "map_page",
                title = "Google Map",
                subtitle = "Pins & 3D Map",
                icon = Icons.Default.Map,
                accentColor = Color(0xFF10B981),
                destination = EarthScopeDestination.MAP_PAGE
            ),
            DashboardFeatureCard(
                id = "speedometer",
                title = "Speedometer",
                subtitle = "Alerts & Graph",
                icon = Icons.Default.Speed,
                accentColor = Color(0xFF22C55E),
                destination = EarthScopeDestination.SPEEDOMETER
            ),
            DashboardFeatureCard(
                id = "compass_only",
                title = "Compass",
                subtitle = "Heading & N/NE",
                icon = Icons.Default.Explore,
                accentColor = Color(0xFFEF4444),
                destination = EarthScopeDestination.COMPASS_ONLY
            ),
            DashboardFeatureCard(
                id = "map_measure",
                title = "Map Measure",
                subtitle = "2-Point Distance",
                icon = Icons.Default.Straighten,
                accentColor = Color(0xFFF59E0B),
                actionType = "MEASURE_MAP"
            ),
            DashboardFeatureCard(
                id = "saved_places",
                title = "Saved Places",
                subtitle = "Home • Work • ★",
                icon = Icons.Default.Bookmark,
                accentColor = Color(0xFFEC4899),
                destination = EarthScopeDestination.SAVED_PLACES
            ),
            DashboardFeatureCard(
                id = "trip_history",
                title = "Trip History",
                subtitle = "Saved Drives",
                icon = Icons.Default.History,
                accentColor = Color(0xFF0EA5E9),
                destination = EarthScopeDestination.TRIP_HISTORY
            ),
            DashboardFeatureCard(
                id = "altitude_gps",
                title = "GPS & Lock",
                subtitle = "Altitude & HUD",
                icon = Icons.Default.Landscape,
                accentColor = Color(0xFF14B8A6),
                destination = EarthScopeDestination.ALTITUDE_COORDINATES
            ),
            DashboardFeatureCard(
                id = "nearby_places",
                title = "Nearby",
                subtitle = "Find Places",
                icon = Icons.Default.NearMe,
                accentColor = Color(0xFF8B5CF6),
                destination = EarthScopeDestination.NEARBY_PLACES
            ),
            DashboardFeatureCard(
                id = "share_location",
                title = "Share Pin",
                subtitle = "Send Location",
                icon = Icons.Default.Share,
                accentColor = Color(0xFF06B6D4),
                destination = EarthScopeDestination.SHARE_LOCATION
            ),
            DashboardFeatureCard(
                id = "flight_calculator",
                title = "Air Distance",
                subtitle = "Flight Route",
                icon = Icons.Default.FlightTakeoff,
                accentColor = Color(0xFFD97706),
                actionType = "DIRECTIONS"
            ),
            DashboardFeatureCard(
                id = "country_atlas",
                title = "Countries",
                subtitle = "World Flags",
                icon = Icons.Default.MenuBook,
                accentColor = Color(0xFF6366F1),
                destination = EarthScopeDestination.COUNTRY_ATLAS
            ),
            DashboardFeatureCard(
                id = "settings_card",
                title = "Settings",
                subtitle = "Dark / Light",
                icon = Icons.Default.Settings,
                accentColor = Color(0xFF64748B),
                actionType = "SETTINGS"
            )
        )
    }

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_dashboard_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            // Simple, Clean App Header with Logo + Live GPS/Battery Pill + Settings Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    EarthScopeTransparentLogo(modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Earth Scope",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryText
                        )
                        Text(
                            text = "GPS & Maps Suite",
                            fontSize = 11.sp,
                            color = secondaryText
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    GpsAndBatteryStatusPill(isLightTheme = isLightTheme)
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .size(42.dp)
                            .background(cardBg, CircleShape)
                            .border(1.dp, borderColor, CircleShape)
                            .testTag("dashboard_top_settings_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Modern Visual Feature Grid (Optimized for butter-smooth scrolling)
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Adaptive(minSize = 148.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                item(span = { GridItemSpan(maxLineSpan) }, contentType = "hero_banner") {
                    HeroGlobeBannerCard(
                        onOpenGlobe = { onNavigate(EarthScopeDestination.WORLD_GLOBE) }
                    )
                }

                items(
                    items = features.take(4),
                    key = { it.id },
                    contentType = { "feature_card" }
                ) { item ->
                    FeatureCategoryCard(
                        item = item,
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        onClick = {
                            when (item.actionType) {
                                "SETTINGS" -> onOpenSettings()
                                "DIRECTIONS" -> onOpenFlightDirections()
                                "MEASURE_MAP" -> onOpenMeasureMap()
                                else -> item.destination?.let { onNavigate(it) }
                            }
                        }
                    )
                }

                items(
                    items = features.drop(4),
                    key = { it.id },
                    contentType = { "feature_card" }
                ) { item ->
                    FeatureCategoryCard(
                        item = item,
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        onClick = {
                            when (item.actionType) {
                                "SETTINGS" -> onOpenSettings()
                                "DIRECTIONS" -> onOpenFlightDirections()
                                "MEASURE_MAP" -> onOpenMeasureMap()
                                else -> item.destination?.let { onNavigate(it) }
                            }
                        }
                    )
                }
            }

            // Sticky Bottom Adaptive Banner Ad on Home Dashboard
            AdMobAdaptiveBanner(
                placementName = "Home_Bottom_Banner",
                adUnitId = AdMobConfig.BANNER_HOME_AD_UNIT_ID,
                isLightTheme = isLightTheme
            )
        }
    }
}

@Composable
private fun HeroGlobeBannerCard(
    onOpenGlobe: () -> Unit
) {
    val gradient = Brush.horizontalGradient(
        listOf(Color(0xFF0284C7), Color(0xFF0369A1), Color(0xFF0F172A))
    )

    Surface(
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.45f)),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable { onOpenGlobe() }
            .testTag("hero_open_3d_globe_card")
    ) {
        Row(
            modifier = Modifier
                .background(gradient)
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EarthScopeTransparentLogo(modifier = Modifier.size(60.dp))

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "3D Earth Globe",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "Tap to explore 3D Ball World",
                    fontSize = 13.sp,
                    color = Color(0xFFE0F2FE)
                )
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Open",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
private fun FeatureCategoryCard(
    item: DashboardFeatureCard,
    cardBg: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color,
    onClick: () -> Unit
) {
    Surface(
        color = cardBg,
        shape = RoundedCornerShape(22.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(22.dp))
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() }
            .testTag("feature_card_${item.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(item.accentColor.copy(alpha = 0.14f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.accentColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = item.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = primaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = item.subtitle,
                fontSize = 12.sp,
                color = secondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Clean, Minimal Top Bar for Sub-Screens
 */
@Composable
fun FeatureSubScreenTopBar(
    title: String,
    subtitle: String = "",
    accentColor: Color,
    isLightTheme: Boolean,
    onBack: () -> Unit,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val cardBg = if (isLightTheme) Color.White else Color(0xFF101D33)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White

    Surface(
        color = cardBg,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(42.dp)
                        .background(accentColor.copy(alpha = 0.14f), CircleShape)
                        .testTag("subscreen_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = accentColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (trailingContent != null) {
                trailingContent()
            }
        }
    }
}
