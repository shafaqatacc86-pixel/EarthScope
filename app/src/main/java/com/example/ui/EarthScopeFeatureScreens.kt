package com.example.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Atm
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalLibrary
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.LocalPostOffice
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.BuildConfig
import com.example.data.local.LocalFreeFeaturesStore
import com.example.data.local.MapFloatingPinEntity
import com.example.data.local.SavedPlaceItem
import com.example.data.local.SavedTripItem
import com.example.data.model.CountryInfo
import com.example.data.model.WorldGeographicData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import android.graphics.Paint as AndroidPaint
import android.graphics.Typeface

/**
 * ============================================================================
 * FEATURE 2: FULL GPS SPEEDOMETER + TRIP TIMER + AVG/MAX SPEED + SPEED GRAPH
 * + SPEED LIMIT LOCAL SOUND & VIBRATION ALERTS + DRIVING DIRECTION + SAVE TRIP
 * ============================================================================
 */
@SuppressLint("MissingPermission")
@Composable
fun SpeedometerScreen(
    isLightTheme: Boolean,
    onBack: () -> Unit,
    onOpenTripHistory: () -> Unit = {}
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val localStore = remember { LocalFreeFeaturesStore(context) }

    var currentSpeedKmh by remember { mutableFloatStateOf(0f) }
    var maxSpeedKmh by remember { mutableFloatStateOf(0f) }
    var avgSpeedKmh by remember { mutableFloatStateOf(0f) }
    var totalDistanceKm by remember { mutableDoubleStateOf(0.0) }
    var altitudeMeters by remember { mutableDoubleStateOf(0.0) }
    var gpsAccuracyMeters by remember { mutableFloatStateOf(0f) }
    var headingDeg by remember { mutableFloatStateOf(0f) }
    var currentRoadName by remember { mutableStateOf("GPS Ready") }
    var tripSeconds by remember { mutableLongStateOf(0L) }
    var isTripRunning by remember { mutableStateOf(true) }
    var useMph by remember { mutableStateOf(false) }

    // Speed Limit & Alert Settings
    var speedLimitKmh by remember { mutableIntStateOf(localStore.getSpeedLimitKmh()) }
    var soundAlertEnabled by remember { mutableStateOf(localStore.isSpeedSoundAlertEnabled()) }
    var vibrationAlertEnabled by remember { mutableStateOf(localStore.isSpeedVibrationAlertEnabled()) }
    var lastAlertTimestampMs by remember { mutableLongStateOf(0L) }

    // Live Speed Graph History (last 30 samples)
    val speedGraphPoints = remember { mutableStateListOf<Float>() }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        hasLocationPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    // Live Trip Timer + Live Speed Graph Recorder
    LaunchedEffect(isTripRunning) {
        while (isTripRunning) {
            delay(1000L)
            tripSeconds += 1L
            if (tripSeconds > 0 && totalDistanceKm > 0.001) {
                val hours = tripSeconds / 3600.0
                avgSpeedKmh = (totalDistanceKm / hours).toFloat().coerceAtMost(maxSpeedKmh.coerceAtLeast(1f))
            }
            speedGraphPoints.add(currentSpeedKmh)
            if (speedGraphPoints.size > 30) {
                speedGraphPoints.removeAt(0)
            }
        }
    }

    // Sensor Heading fallback so Driving Direction (N/NE/E/SE...) always works even when stationary
    DisposableEffect(Unit) {
        val sm = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val rotSensor = sm?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        val rotMatrix = FloatArray(9)
        val orient = FloatArray(3)
        val sensorListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event?.sensor?.type == Sensor.TYPE_ROTATION_VECTOR && currentSpeedKmh < 3f) {
                    SensorManager.getRotationMatrixFromVector(rotMatrix, event.values)
                    SensorManager.getOrientation(rotMatrix, orient)
                    headingDeg = ((Math.toDegrees(orient[0].toDouble()).toFloat() % 360f) + 360f) % 360f
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        if (sm != null && rotSensor != null) {
            sm.registerListener(sensorListener, rotSensor, SensorManager.SENSOR_DELAY_UI)
        }
        onDispose { sm?.unregisterListener(sensorListener) }
    }

    DisposableEffect(hasLocationPermission) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (!hasLocationPermission || lm == null) {
            onDispose { }
        } else {
            var previousLoc: Location? = null
            var smoothedKmh = 0f
            var lastGeocodeMs = 0L
            val listener = object : LocationListener {
                override fun onLocationChanged(loc: Location) {
                    // Ignore coarse cell-tower / Wi-Fi jumps that cause fake 79-170 km/h spikes while walking
                    val isGpsProvider = loc.provider == LocationManager.GPS_PROVIDER
                    val acc = if (loc.hasAccuracy()) loc.accuracy else 99f
                    if (loc.hasAccuracy()) {
                        gpsAccuracyMeters = acc
                    }
                    if (loc.hasAltitude()) {
                        altitudeMeters = loc.altitude
                    }

                    // Only trust speed when GPS accuracy is reasonable (<= 25m)
                    val rawKmh: Float = when {
                        !isGpsProvider && !loc.hasSpeed() -> {
                            // Never compute distance/time speed from NETWORK_PROVIDER jumps
                            0f
                        }
                        loc.hasSpeed() && acc <= 25f -> {
                            val s = loc.speed * 3.6f
                            // Ignore stationary GPS drift (< 1.2 km/h)
                            if (s < 1.2f) 0f else s
                        }
                        isGpsProvider && acc <= 15f && previousLoc != null -> {
                            val prev = previousLoc!!
                            val dtSec = ((loc.time - prev.time) / 1000.0)
                            val distM = prev.distanceTo(loc)
                            // Require at least 2 seconds and distance larger than GPS accuracy radius
                            if (dtSec >= 2.0 && distM > (acc * 1.2f)) {
                                val calcKmh = ((distM / dtSec) * 3.6).toFloat()
                                // Reject impossible instant acceleration spike (> 35 km/h jump in 1 update)
                                if (calcKmh - smoothedKmh > 35f) smoothedKmh else calcKmh
                            } else {
                                0f
                            }
                        }
                        else -> 0f
                    }

                    // Smooth out sudden spikes with a responsive filter
                    val clampedRaw = if (rawKmh - smoothedKmh > 40f && smoothedKmh < 12f) {
                        // Prevent a single bad GPS packet while walking from jumping to 79 or 170 km/h
                        smoothedKmh
                    } else {
                        rawKmh.coerceIn(0f, 300f)
                    }

                    smoothedKmh = if (clampedRaw == 0f && smoothedKmh < 2.0f) {
                        0f
                    } else {
                        (smoothedKmh * 0.45f) + (clampedRaw * 0.55f)
                    }

                    currentSpeedKmh = smoothedKmh
                    if (smoothedKmh > maxSpeedKmh) maxSpeedKmh = smoothedKmh

                    if (loc.hasBearing() && smoothedKmh >= 2f) {
                        headingDeg = ((loc.bearing % 360f) + 360f) % 360f
                    }

                    if (isGpsProvider && acc <= 20f) {
                        previousLoc?.let { prev ->
                            val stepMeters = prev.distanceTo(loc)
                            // Only accumulate real movement steps larger than GPS jitter
                            if (stepMeters in 2.5f..250f && smoothedKmh >= 1.2f) {
                                totalDistanceKm += (stepMeters / 1000.0)
                            }
                        }
                        previousLoc = loc
                    }

                    // Speed Limit Local Alert + Vibration Alert
                    if (smoothedKmh > speedLimitKmh) {
                        val now = System.currentTimeMillis()
                        if (now - lastAlertTimestampMs > 3500L) {
                            lastAlertTimestampMs = now
                            if (vibrationAlertEnabled) {
                                triggerSpeedVibration(context)
                            }
                            if (soundAlertEnabled) {
                                triggerSpeedAlertBeep()
                            }
                        }
                    }

                    // Reverse Geocode Current Road Position every 10 seconds
                    val nowMs = System.currentTimeMillis()
                    if (nowMs - lastGeocodeMs > 10000L) {
                        lastGeocodeMs = nowMs
                        coroutineScope.launch {
                            val road = resolveRoadOrPlaceName(context, loc.latitude, loc.longitude)
                            currentRoadName = road
                        }
                    }
                }
                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }

            try {
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600L, 0f, listener, Looper.getMainLooper())
                }
            } catch (_: Exception) {}

            onDispose {
                try { lm.removeUpdates(listener) } catch (_: Exception) {}
            }
        }
    }

    val isOverSpeedLimit = currentSpeedKmh > speedLimitKmh
    val animatedSpeed by animateFloatAsState(
        targetValue = if (useMph) currentSpeedKmh * 0.621371f else currentSpeedKmh,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "speed_gauge"
    )

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val unitLabel = if (useMph) "MPH" else "KM/H"
    val maxGaugeVal = if (useMph) 160f else 240f
    val displayMaxSpeed = if (useMph) maxSpeedKmh * 0.621371f else maxSpeedKmh
    val displayAvgSpeed = if (useMph) avgSpeedKmh * 0.621371f else avgSpeedKmh
    val dirLabel = getCardinalDirectionShort(headingDeg)

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("speedometer_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            FeatureSubScreenTopBar(
                title = "Speedometer",
                accentColor = if (isOverSpeedLimit) Color(0xFFEF4444) else Color(0xFF22C55E),
                isLightTheme = isLightTheme,
                onBack = onBack,
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        GpsAndBatteryStatusPill(isLightTheme = isLightTheme)
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFF22C55E).copy(alpha = 0.16f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, Color(0xFF22C55E)),
                            modifier = Modifier
                                .clickable { useMph = !useMph }
                                .testTag("toggle_speed_unit_button")
                        ) {
                            Text(
                                text = unitLabel,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF22C55E),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!hasLocationPermission) {
                    Button(
                        onClick = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
                    ) {
                        Text("Tap to Enable GPS Speed", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                // Speed Limit Alert Banner when exceeded
                if (isOverSpeedLimit) {
                    Surface(
                        color = Color(0xFFEF4444),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .testTag("speed_limit_alert_banner")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "OVER SPEED LIMIT ($speedLimitKmh km/h)!",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                // Current Road Position & Driving Direction Capsule
                Surface(
                    color = cardBg,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentRoadName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Surface(
                            color = Color(0xFF22C55E).copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = "🧭 ${headingDeg.roundToInt()}° $dirLabel",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF22C55E),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Big Clean Circular Speedometer Gauge
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeW = 20.dp.toPx()
                        val arcSize = size.minDimension - strokeW * 1.4f
                        val topLeft = Offset((size.width - arcSize) / 2f, (size.height - arcSize) / 2f)

                        drawArc(
                            color = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E293B),
                            startAngle = 135f,
                            sweepAngle = 270f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(arcSize, arcSize),
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )

                        val progress = (animatedSpeed / maxGaugeVal).coerceIn(0f, 1f)
                        val activeColor = when {
                            isOverSpeedLimit -> Color(0xFFEF4444)
                            progress > 0.65f -> Color(0xFFF59E0B)
                            else -> Color(0xFF22C55E)
                        }
                        drawArc(
                            color = activeColor,
                            startAngle = 135f,
                            sweepAngle = 270f * progress,
                            useCenter = false,
                            topLeft = topLeft,
                            size = Size(arcSize, arcSize),
                            style = Stroke(width = strokeW, cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = animatedSpeed.roundToInt().toString(),
                            fontSize = 62.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isOverSpeedLimit) Color(0xFFEF4444) else primaryText
                        )
                        Text(
                            text = "$unitLabel • $dirLabel",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF22C55E)
                        )
                    }
                }

                // 6-Tile Trip Statistics Grid (Distance, Trip Timer, Avg Speed, Max Speed, Altitude, Accuracy)
                Row(
                    modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SimpleStatBox(
                        title = "Distance",
                        value = "${String.format(Locale.US, "%.2f", totalDistanceKm)} km",
                        accent = Color(0xFF0284C7),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Trip Timer",
                        value = formatDurationHms(tripSeconds),
                        accent = Color(0xFF8B5CF6),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Avg Speed",
                        value = "${displayAvgSpeed.roundToInt()} $unitLabel",
                        accent = Color(0xFF10B981),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SimpleStatBox(
                        title = "Max Speed",
                        value = "${displayMaxSpeed.roundToInt()} $unitLabel",
                        accent = Color(0xFFEF4444),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Altitude",
                        value = "${altitudeMeters.roundToInt()} m",
                        accent = Color(0xFFF59E0B),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Accuracy",
                        value = "±${gpsAccuracyMeters.roundToInt()} m",
                        accent = Color(0xFF06B6D4),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 📈 LIVE SPEED GRAPH CARD
                Surface(
                    color = cardBg,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "📈 Live Speed Graph",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Text(
                                text = "Limit: $speedLimitKmh km/h",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF59E0B)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp)
                                .testTag("live_speed_graph_canvas")
                        ) {
                            val maxGraphY = (maxOf(speedLimitKmh.toFloat() * 1.25f, maxSpeedKmh, 60f)).coerceAtLeast(40f)
                            val w = size.width
                            val h = size.height

                            // Speed Limit Threshold Dashed Line
                            val limitY = h - ((speedLimitKmh / maxGraphY) * h).coerceIn(0f, h)
                            drawLine(
                                color = Color(0xFFEF4444).copy(alpha = 0.55f),
                                start = Offset(0f, limitY),
                                end = Offset(w, limitY),
                                strokeWidth = 2f
                            )

                            if (speedGraphPoints.size >= 2) {
                                val path = Path()
                                val stepX = w / (speedGraphPoints.size - 1).coerceAtLeast(1)
                                speedGraphPoints.forEachIndexed { idx, spd ->
                                    val x = idx * stepX
                                    val y = h - ((spd / maxGraphY) * h).coerceIn(2f, h - 2f)
                                    if (idx == 0) path.moveTo(x, y) else path.lineTo(x, y)
                                }
                                drawPath(
                                    path = path,
                                    color = Color(0xFF22C55E),
                                    style = Stroke(width = 5f, cap = StrokeCap.Round)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 🔔 SPEED LIMIT & VIBRATION ALERT CONTROLS CARD
                Surface(
                    color = cardBg,
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🔔 Speed Alert Limit: $speedLimitKmh km/h",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = "Sound Alert",
                                    tint = if (soundAlertEnabled) Color(0xFF22C55E) else secondaryText,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clickable {
                                            soundAlertEnabled = !soundAlertEnabled
                                            localStore.setSpeedSoundAlertEnabled(soundAlertEnabled)
                                        }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Icon(
                                    imageVector = Icons.Default.Vibration,
                                    contentDescription = "Vibration Alert",
                                    tint = if (vibrationAlertEnabled) Color(0xFFF59E0B) else secondaryText,
                                    modifier = Modifier
                                        .size(22.dp)
                                        .clickable {
                                            vibrationAlertEnabled = !vibrationAlertEnabled
                                            localStore.setSpeedVibrationAlertEnabled(vibrationAlertEnabled)
                                        }
                                )
                            }
                        }

                        Slider(
                            value = speedLimitKmh.toFloat(),
                            onValueChange = {
                                speedLimitKmh = it.roundToInt()
                                localStore.setSpeedLimitKmh(speedLimitKmh)
                            },
                            valueRange = 30f..180f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF22C55E),
                                activeTrackColor = Color(0xFF22C55E)
                            ),
                            modifier = Modifier.testTag("speed_limit_slider")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Save Trip Locally + Reset Buttons
                Row(
                    modifier = Modifier.widthIn(max = 480.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val trip = SavedTripItem(
                                id = "trip_${System.currentTimeMillis()}",
                                startTimeMs = System.currentTimeMillis() - (tripSeconds * 1000L),
                                durationSec = tripSeconds,
                                distanceKm = totalDistanceKm,
                                avgSpeedKmh = avgSpeedKmh,
                                maxSpeedKmh = maxSpeedKmh,
                                startRoadOrCoords = currentRoadName,
                                endRoadOrCoords = "$dirLabel • ${altitudeMeters.roundToInt()}m"
                            )
                            localStore.addSavedTrip(trip)
                            Toast.makeText(context, "Trip Saved Locally!", Toast.LENGTH_SHORT).show()
                            onOpenTripHistory()
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E), contentColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_trip_locally_button")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Trip", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            currentSpeedKmh = 0f
                            maxSpeedKmh = 0f
                            avgSpeedKmh = 0f
                            totalDistanceKm = 0.0
                            tripSeconds = 0L
                            speedGraphPoints.clear()
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7), contentColor = Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("reset_trip_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Sticky Bottom Adaptive Banner Ad on Speedometer Screen
            AdMobAdaptiveBanner(
                placementName = "Speedometer_Bottom_Banner",
                adUnitId = AdMobConfig.BANNER_SPEEDOMETER_AD_UNIT_ID,
                isLightTheme = isLightTheme
            )
        }
    }
}

@Composable
private fun SimpleStatBox(
    title: String,
    value: String,
    accent: Color,
    cardBg: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = cardBg,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = secondaryText,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = accent,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * ============================================================================
 * FEATURE 3: OFFICIAL GOOGLE MAPS SDK FOR ANDROID SCREEN
 * Includes:
 * - Normal / Satellite / Terrain + 3D Buildings
 * - Custom Markers & Pin Drop on Tap
 * - Zoom In / Zoom Out / Map Rotate 90°
 * - Follow-My-Location Live Mode
 * - 2-Point Straight-Line Distance & Multi-Point Map Distance Measurement Tool
 * - One-Tap Save Pin as Home, Work, or Favorite Location
 * - Current Road Position Banner
 * ============================================================================
 */
@SuppressLint("SetJavaScriptEnabled", "MissingPermission")
@Composable
fun InteractiveMapPageScreen(
    viewModel: GlobeViewModel,
    isLightTheme: Boolean,
    initialTargetLat: Double? = null,
    initialTargetLon: Double? = null,
    initialTargetLabel: String? = null,
    initialMeasureMode: Boolean = false,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    val localStore = remember { LocalFreeFeaturesStore(context) }
    val savedFloatingPins by viewModel.floatingPins.collectAsStateWithLifecycle()

    // Dedicated Details Page state when user taps the Floating Window on Map or an item in All Locations List
    var selectedPinForDetailsPage by remember { mutableStateOf<MapFloatingPinEntity?>(null) }
    var showAllLocationsListPage by remember { mutableStateOf(false) }

    // Keep selectedPinForDetailsPage synced with the latest database state
    val liveDetailsPin = remember(selectedPinForDetailsPage, savedFloatingPins) {
        val current = selectedPinForDetailsPage ?: return@remember null
        savedFloatingPins.find { it.id == current.id } ?: current
    }

    // Map Theme / Layer Mode (Top-right corner Layer Switch):
    // 0 = Normal / White Map
    // 1 = Satellite
    // 2 = Terrain
    // 3 = Black / Dark Map
    // 4 = Green Map
    var mapLayerMode by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var targetLat by remember { mutableDoubleStateOf(initialTargetLat ?: 30.3753) }
    var targetLon by remember { mutableDoubleStateOf(initialTargetLon ?: 69.3451) }
    var targetZoom by remember { mutableIntStateOf(if (initialTargetLat != null) 14 else 6) }
    var targetLabel by remember { mutableStateOf(initialTargetLabel ?: "Pakistan") }
    var mapBearingDeg by remember { mutableFloatStateOf(0f) }
    var followMyLocation by remember { mutableStateOf(false) }
    var isMeasureMode by remember { mutableStateOf(initialMeasureMode) }
    val measurePoints = remember { mutableStateListOf<LatLng>() }

    // Category filter state ("ALL" or PinLocationCategory.key)
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }
    val filteredSavedPins = remember(savedFloatingPins, selectedCategoryFilter) {
        if (selectedCategoryFilter == "ALL") {
            savedFloatingPins
        } else {
            savedFloatingPins.filter { it.categoryKey.equals(selectedCategoryFilter, ignoreCase = true) }
        }
    }

    // Floating Window & Bottom Info Card visibility states
    // When user taps normally on the map, both hide!
    var showAddLocationSourceDialog by remember { mutableStateOf(false) }
    var isPickingCustomMapPointForFloatingPin by remember { mutableStateOf(false) }
    var showCreateFloatingPinFormDialog by remember { mutableStateOf(false) }
    var formDialogSourceType by remember { mutableStateOf("CURRENT") }
    var formDialogLat by remember { mutableStateOf<Double?>(targetLat) }
    var formDialogLon by remember { mutableStateOf<Double?>(targetLon) }
    var formDialogSuggestedName by remember { mutableStateOf("") }
    var activeFloatingWindowPin by remember { mutableStateOf<MapFloatingPinEntity?>(null) }
    var showBottomQuickSaveSheet by remember { mutableStateOf(initialTargetLat != null) }

    // Screen pixel anchor of the currently active marker so the Floating Window stays connected to its marker!
    var activeMarkerScreenX by remember { mutableStateOf<Float?>(null) }
    var activeMarkerScreenY by remember { mutableStateOf<Float?>(null) }

    // Keep activeFloatingWindowPin synced whenever the shared Room database updates (e.g. edited name/category/photo) or deletes a pin
    LaunchedEffect(savedFloatingPins, filteredSavedPins) {
        if (activeFloatingWindowPin != null) {
            val updated = filteredSavedPins.find { it.id == activeFloatingWindowPin?.id }
                ?: savedFloatingPins.find { it.id == activeFloatingWindowPin?.id }
            activeFloatingWindowPin = updated
        }
    }

    BackHandler {
        when {
            liveDetailsPin != null -> selectedPinForDetailsPage = null
            showAllLocationsListPage -> showAllLocationsListPage = false
            else -> onBack()
        }
    }

    val configuredMapsKey = BuildConfig.MAPS_API_KEY
    val hasValidGoogleKey = configuredMapsKey.isNotBlank() &&
            configuredMapsKey != "YOUR_GOOGLE_MAPS_API_KEY" &&
            configuredMapsKey != "DEFAULT_VALUE"

    val webViewRef = remember { mutableStateOf<WebView?>(null) }
    var isWebViewLoaded by remember { mutableStateOf(false) }

    // Sync saved pins to Leaflet WebView whenever filteredSavedPins or activeFloatingWindowPin changes
    fun syncPinsToWebView() {
        val wv = webViewRef.value ?: return
        if (!isWebViewLoaded) return
        val jsonArrayStr = filteredSavedPins.joinToString(prefix = "[", postfix = "]") { pin ->
            val cat = PinLocationCategory.fromKey(pin.categoryKey)
            val markerOpt = CustomMapMarkerOption.fromKey(pin.markerStyleKey)
            val safeName = pin.locationName
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", " ")
            val safeCatLabel = cat.label
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
            val coordsStr = String.format(Locale.US, "%.4f°, %.4f°", pin.latitude, pin.longitude)
            val thumbDataUrl = rememberOrEncodeThumbDataUrl(pin.primaryImagePath)
            """{"id":${pin.id},"lat":${pin.latitude},"lon":${pin.longitude},"name":"$safeName","catLabel":"$safeCatLabel","catColor":"${cat.hexColor}","coords":"$coordsStr","color":"${markerOpt.hexColor}","emoji":"${cat.emoji}","symbol":"${markerOpt.badgeSymbol}","thumb":"$thumbDataUrl"}"""
        }
        val activeId = activeFloatingWindowPin?.id ?: -1L
        wv.evaluateJavascript(
            "if(window.renderSavedPins){ window.renderSavedPins($jsonArrayStr, $activeId); }",
            null
        )
    }

    LaunchedEffect(filteredSavedPins, activeFloatingWindowPin, isWebViewLoaded) {
        if (!hasValidGoogleKey && isWebViewLoaded) {
            syncPinsToWebView()
        }
    }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.Builder()
            .target(LatLng(targetLat, targetLon))
            .zoom(targetZoom.toFloat())
            .bearing(mapBearingDeg)
            .tilt(45f)
            .build()
    }

    // Continuously project the active marker's LatLng to screen pixel coordinates on every frame (60fps)
    // when GoogleMap is active so the Speech-Bubble Info Window stays 100% locked to its marker
    // and never drifts or lags behind when the user pans, flings, or zooms the map!
    LaunchedEffect(
        activeFloatingWindowPin,
        hasValidGoogleKey
    ) {
        if (hasValidGoogleKey) {
            val pin = activeFloatingWindowPin
            if (pin != null) {
                val targetLatLng = LatLng(pin.latitude, pin.longitude)
                while (true) {
                    val pt = cameraPositionState.projection?.toScreenLocation(targetLatLng)
                    if (pt != null) {
                        activeMarkerScreenX = pt.x.toFloat()
                        activeMarkerScreenY = pt.y.toFloat()
                    }
                    androidx.compose.runtime.withFrameNanos { }
                }
            } else {
                activeMarkerScreenX = null
                activeMarkerScreenY = null
            }
        }
    }

    fun flyMapTo(lat: Double, lon: Double, zoom: Int, label: String, bearing: Float = mapBearingDeg) {
        targetLat = lat
        targetLon = lon
        targetZoom = zoom
        targetLabel = label
        if (hasValidGoogleKey) {
            coroutineScope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                            .target(LatLng(lat, lon))
                            .zoom(zoom.toFloat())
                            .bearing(bearing)
                            .tilt(if (zoom >= 15) 50f else 30f)
                            .build()
                    ),
                    durationMs = 650
                )
            }
        } else {
            val safe = label.replace("'", "\\'")
            webViewRef.value?.evaluateJavascript(
                "if(window.flyToLocation){ window.flyToLocation($lat, $lon, $zoom, '$safe'); }",
                null
            )
        }
    }

    fun openFormDialogForCoordinates(lat: Double?, lon: Double?, sourceType: String) {
        formDialogLat = lat
        formDialogLon = lon
        formDialogSourceType = sourceType
        formDialogSuggestedName = ""
        showCreateFloatingPinFormDialog = true
        if (lat != null && lon != null) {
            coroutineScope.launch {
                val resolved = resolveRoadOrPlaceName(context, lat, lon)
                if (formDialogSuggestedName.isBlank()) {
                    formDialogSuggestedName = resolved
                }
            }
        }
    }

    // Follow-My-Location Continuous GPS Listener
    DisposableEffect(followMyLocation, hasLocationPermission) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        if (!followMyLocation || !hasLocationPermission || lm == null) {
            onDispose { }
        } else {
            val listener = object : LocationListener {
                override fun onLocationChanged(loc: Location) {
                    flyMapTo(loc.latitude, loc.longitude, 16, "Following GPS")
                }
                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
            try {
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500L, 2f, listener, Looper.getMainLooper())
                }
            } catch (_: Exception) {}
            onDispose {
                try { lm.removeUpdates(listener) } catch (_: Exception) {}
            }
        }
    }

    var pendingCurrentLocationForFloatingDialog by remember { mutableStateOf(false) }

    // Helper that fetches Current Location once permission & GPS are ready
    fun executeCurrentLocationAction(openFloatingPinDialog: Boolean) {
        val loc = getQuickLastKnownLocation(context)
        if (loc != null) {
            flyMapTo(loc.latitude, loc.longitude, 16, "My Current Location")
            if (openFloatingPinDialog) {
                openFormDialogForCoordinates(loc.latitude, loc.longitude, "CURRENT")
            } else {
                Toast.makeText(context, "📍 Showing Current GPS Location", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Request a fresh single GPS update from LocationManager
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            var delivered = false
            val singleListener = object : LocationListener {
                override fun onLocationChanged(freshLoc: Location) {
                    if (!delivered) {
                        delivered = true
                        flyMapTo(freshLoc.latitude, freshLoc.longitude, 16, "My Current Location")
                        if (openFloatingPinDialog) {
                            openFormDialogForCoordinates(freshLoc.latitude, freshLoc.longitude, "CURRENT")
                        }
                    }
                    try { lm?.removeUpdates(this) } catch (_: Exception) {}
                }
                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
            try {
                if (lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500L, 0f, singleListener, Looper.getMainLooper())
                } else if (lm?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500L, 0f, singleListener, Looper.getMainLooper())
                }
            } catch (_: Exception) {}

            flyMapTo(targetLat, targetLon, 16, "Current Location")
            if (openFloatingPinDialog) {
                openFormDialogForCoordinates(targetLat, targetLon, "CURRENT")
            } else {
                Toast.makeText(context, "📍 Locating via GPS...", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Built-in Google Play Services GPS Enable Dialog Launcher (StartIntentSenderForResult)
    val gpsEnableDialogLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        val openDialog = pendingCurrentLocationForFloatingDialog
        pendingCurrentLocationForFloatingDialog = false
        if (result.resultCode == Activity.RESULT_OK) {
            executeCurrentLocationAction(openFloatingPinDialog = openDialog)
        } else {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val isNowOn = lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
            if (isNowOn) {
                executeCurrentLocationAction(openFloatingPinDialog = openDialog)
            } else {
                Toast.makeText(context, "Please enable GPS to view your current location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun requestCurrentGpsLocationWithBuiltInCheck(openFloatingPinDialog: Boolean) {
        pendingCurrentLocationForFloatingDialog = openFloatingPinDialog
        checkAndPromptEnableGps(
            context = context,
            resolutionLauncher = { intentSenderRequest ->
                gpsEnableDialogLauncher.launch(intentSenderRequest)
            },
            onGpsAlreadyEnabled = {
                pendingCurrentLocationForFloatingDialog = false
                executeCurrentLocationAction(openFloatingPinDialog = openFloatingPinDialog)
            }
        )
    }

    val locPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = granted
        if (granted) {
            requestCurrentGpsLocationWithBuiltInCheck(
                openFloatingPinDialog = pendingCurrentLocationForFloatingDialog
            )
        } else {
            pendingCurrentLocationForFloatingDialog = false
            Toast.makeText(context, "Location permission is needed to fetch Current GPS coordinates", Toast.LENGTH_SHORT).show()
        }
    }

    // Perform Search helper (used by both Keyboard Search action and the Top Search Button Icon)
    fun performMapSearch() {
        val q = searchQuery.trim()
        if (q.isEmpty()) {
            Toast.makeText(context, "Enter a city, country or saved place name", Toast.LENGTH_SHORT).show()
            return
        }
        val savedMatch = savedFloatingPins.find {
            it.locationName.contains(q, ignoreCase = true) ||
                    it.locationNotes.contains(q, ignoreCase = true)
        }
        if (savedMatch != null) {
            activeFloatingWindowPin = savedMatch
            flyMapTo(savedMatch.latitude, savedMatch.longitude, 15, savedMatch.locationName)
        } else {
            val city = WorldGeographicData.findCity(q)
            if (city != null) {
                flyMapTo(city.lat, city.lon, 14, city.name)
                showBottomQuickSaveSheet = true
            } else {
                val country = WorldGeographicData.findCountry(q)
                if (country != null) {
                    flyMapTo(country.centerLat, country.centerLon, 6, country.name)
                    showBottomQuickSaveSheet = true
                } else {
                    Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show()
                }
            }
        }
        focusManager.clearFocus()
    }

    // Calculate total distance of measured points
    val measuredDistanceKm = remember(measurePoints.size) {
        if (measurePoints.size < 2) 0.0 else {
            var sumMeters = 0.0
            for (i in 0 until measurePoints.size - 1) {
                val res = FloatArray(1)
                Location.distanceBetween(
                    measurePoints[i].latitude, measurePoints[i].longitude,
                    measurePoints[i + 1].latitude, measurePoints[i + 1].longitude,
                    res
                )
                sumMeters += res[0]
            }
            sumMeters / 1000.0
        }
    }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("dedicated_map_page_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            FeatureSubScreenTopBar(
                title = if (isMeasureMode) "Distance Tool" else "Google Map",
                accentColor = Color(0xFF10B981),
                isLightTheme = isLightTheme,
                onBack = onBack,
                trailingContent = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Measure Mode Toggle Button
                        Surface(
                            color = if (isMeasureMode) Color(0xFFF59E0B) else Color(0xFFF59E0B).copy(alpha = 0.16f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                            modifier = Modifier
                                .clickable {
                                    isMeasureMode = !isMeasureMode
                                    if (!hasValidGoogleKey) {
                                        webViewRef.value?.evaluateJavascript(
                                            "if(window.setMeasureMode){ window.setMeasureMode($isMeasureMode); }",
                                            null
                                        )
                                    }
                                }
                                .testTag("map_measure_mode_toggle")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Straighten,
                                    contentDescription = "Measure",
                                    tint = if (isMeasureMode) Color.White else Color(0xFFF59E0B),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Ruler",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMeasureMode) Color.White else Color(0xFFF59E0B)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Top Layer Switch (Normal / Satellite / Terrain / Black Dark / Green Map)
                        val layerName = when (mapLayerMode) {
                            0 -> "Normal"
                            1 -> "Satellite"
                            2 -> "Terrain"
                            3 -> "Black Map"
                            else -> "Green Map"
                        }
                        val layerChipColor = when (mapLayerMode) {
                            0 -> Color(0xFF0284C7)
                            1 -> Color(0xFF0EA5E9)
                            2 -> Color(0xFFF59E0B)
                            3 -> Color(0xFF818CF8)
                            else -> Color(0xFF10B981)
                        }
                        Surface(
                            color = layerChipColor.copy(alpha = 0.16f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, layerChipColor),
                            modifier = Modifier
                                .clickable {
                                    mapLayerMode = (mapLayerMode + 1) % 5
                                    if (!hasValidGoogleKey) {
                                        webViewRef.value?.evaluateJavascript(
                                            "if(window.setMapLayer){ window.setMapLayer($mapLayerMode); }",
                                            null
                                        )
                                    }
                                }
                                .testTag("map_page_layer_switch")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Layers, contentDescription = null, tint = layerChipColor, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(layerName, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = layerChipColor)
                            }
                        }
                    }
                }
            )

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (hasValidGoogleKey) {
                    val googleMapType = when (mapLayerMode) {
                        1 -> MapType.HYBRID
                        2 -> MapType.TERRAIN
                        else -> MapType.NORMAL
                    }
                    val googleMapStyleOptions = remember(mapLayerMode) {
                        when (mapLayerMode) {
                            3 -> MapStyleOptions(GOOGLE_MAP_BLACK_STYLE_JSON)
                            4 -> MapStyleOptions(GOOGLE_MAP_GREEN_STYLE_JSON)
                            else -> null
                        }
                    }
                    var lastMapTouchDownX by remember { mutableFloatStateOf(-1f) }
                    var lastMapTouchDownY by remember { mutableFloatStateOf(-1f) }
                    var lastMarkerSelectTimeMs by remember { mutableStateOf(0L) }

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event = awaitPointerEvent(PointerEventPass.Initial)
                                        val firstChange = event.changes.firstOrNull()
                                        if (firstChange != null && firstChange.pressed && !firstChange.previousPressed) {
                                            lastMapTouchDownX = firstChange.position.x
                                            lastMapTouchDownY = firstChange.position.y
                                        }
                                    }
                                }
                            }
                            .testTag("official_google_map_view"),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            mapType = googleMapType,
                            mapStyleOptions = googleMapStyleOptions,
                            isMyLocationEnabled = hasLocationPermission,
                            isBuildingEnabled = true
                        ),
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                            compassEnabled = true,
                            rotationGesturesEnabled = true,
                            tiltGesturesEnabled = true,
                            myLocationButtonEnabled = false
                        ),
                        onMapClick = { latLng ->
                            focusManager.clearFocus()
                            if (isPickingCustomMapPointForFloatingPin) {
                                isPickingCustomMapPointForFloatingPin = false
                                flyMapTo(latLng.latitude, latLng.longitude, 15, "Custom Location")
                                openFormDialogForCoordinates(latLng.latitude, latLng.longitude, "CUSTOM")
                            } else if (isMeasureMode) {
                                measurePoints.add(latLng)
                            } else {
                                // Normal map tap: do NOT add marker; hide floating window & overlays
                                activeFloatingWindowPin = null
                                showBottomQuickSaveSheet = false
                            }
                        }
                    ) {
                        // PERMANENTLY PRE-MOUNTED BASE MARKER + SPEECH-BUBBLE MARKER FOR EACH PIN
                        // Both Markers stay permanently mounted in the GoogleMap composition tree (keyed only by savedPin.id).
                        // Tapping a marker simply flips `visible = isSelected` on the already-loaded Speech-Bubble Marker,
                        // guaranteeing 100% instant single-click opening without ever unmounting or requiring 2 clicks!
                        filteredSavedPins.forEach { savedPin ->
                            val isSelected = activeFloatingWindowPin?.id == savedPin.id
                            androidx.compose.runtime.key("pin_pair_${savedPin.id}") {
                                val cat = PinLocationCategory.fromKey(savedPin.categoryKey)
                                val markerOpt = CustomMapMarkerOption.fromKey(savedPin.markerStyleKey)

                                // 1. Base Circular Marker (Always visible)
                                val baseIcon = remember(
                                    savedPin.id,
                                    savedPin.markerStyleKey,
                                    savedPin.categoryKey
                                ) {
                                    createCustomMarkerBitmapDescriptor(markerOpt, cat, false)
                                }
                                val baseMarkerState = com.google.maps.android.compose.rememberMarkerState(
                                    key = "base_state_${savedPin.id}",
                                    position = LatLng(savedPin.latitude, savedPin.longitude)
                                )
                                LaunchedEffect(savedPin.latitude, savedPin.longitude) {
                                    baseMarkerState.position = LatLng(savedPin.latitude, savedPin.longitude)
                                }
                                Marker(
                                    state = baseMarkerState,
                                    anchor = androidx.compose.ui.geometry.Offset(0.5f, 0.5f),
                                    zIndex = if (isSelected) 20f else 10f,
                                    icon = baseIcon,
                                    onClick = {
                                        // Single click on circular marker immediately shows its pre-mounted Speech-Bubble!
                                        activeFloatingWindowPin = savedPin
                                        targetLat = savedPin.latitude
                                        targetLon = savedPin.longitude
                                        targetLabel = savedPin.locationName
                                        true
                                    }
                                )

                                // 2. Pre-Mounted Speech-Bubble 💬 Info Window Marker (Toggled via `visible = isSelected`)
                                val bubbleIcon = remember(
                                    savedPin.id,
                                    savedPin.locationName,
                                    savedPin.latitude,
                                    savedPin.longitude,
                                    savedPin.markerStyleKey,
                                    savedPin.categoryKey,
                                    savedPin.primaryImagePath,
                                    isLightTheme
                                ) {
                                    com.example.ui.createSpeechBubbleOnlyBitmapDescriptor(
                                        pin = savedPin,
                                        markerOption = markerOpt,
                                        category = cat,
                                        isLightTheme = isLightTheme
                                    )
                                }
                                val bubbleMarkerState = com.google.maps.android.compose.rememberMarkerState(
                                    key = "bubble_state_${savedPin.id}",
                                    position = LatLng(savedPin.latitude, savedPin.longitude)
                                )
                                LaunchedEffect(savedPin.latitude, savedPin.longitude) {
                                    bubbleMarkerState.position = LatLng(savedPin.latitude, savedPin.longitude)
                                }
                                Marker(
                                    state = bubbleMarkerState,
                                    visible = isSelected,
                                    anchor = androidx.compose.ui.geometry.Offset(0.5f, 332f / 340f),
                                    zIndex = 100f,
                                    icon = bubbleIcon,
                                    onClick = {
                                        if (!isSelected) {
                                            activeFloatingWindowPin = savedPin
                                            return@Marker true
                                        }
                                        val screenPt = cameraPositionState.projection?.toScreenLocation(
                                            LatLng(savedPin.latitude, savedPin.longitude)
                                        )
                                        if (screenPt != null && lastMapTouchDownX >= 0f && lastMapTouchDownY >= 0f) {
                                            val tapZone = com.example.ui.detectActiveBubbleTapZone(
                                                markerScreenX = screenPt.x.toFloat(),
                                                markerScreenY = screenPt.y.toFloat(),
                                                tapX = lastMapTouchDownX,
                                                tapY = lastMapTouchDownY
                                            )
                                            when (tapZone) {
                                                com.example.ui.ActiveBubbleTapZone.CLOSE_BUTTON -> {
                                                    // Tapped the top-right 'X' close cross icon on the Info Window
                                                    activeFloatingWindowPin = null
                                                }
                                                com.example.ui.ActiveBubbleTapZone.INFO_WINDOW_CARD -> {
                                                    // Tapped inside the Speech-Bubble Info Window card -> open Details Page!
                                                    selectedPinForDetailsPage = savedPin
                                                }
                                                com.example.ui.ActiveBubbleTapZone.MARKER_BADGE,
                                                com.example.ui.ActiveBubbleTapZone.OUTSIDE -> {
                                                    // Tapped on the circular marker badge -> keep Info Window open
                                                    activeFloatingWindowPin = savedPin
                                                }
                                            }
                                        } else {
                                            selectedPinForDetailsPage = savedPin
                                        }
                                        true
                                    }
                                )
                            }
                        }

                        if (measurePoints.isNotEmpty()) {
                            measurePoints.forEachIndexed { idx, pt ->
                                Marker(
                                    state = MarkerState(position = pt),
                                    title = "Point ${idx + 1}"
                                )
                            }
                            if (measurePoints.size >= 2) {
                                Polyline(
                                    points = measurePoints.toList(),
                                    color = Color(0xFFF59E0B),
                                    width = 10f
                                )
                            }
                        }
                    }
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            WebView(ctx).apply {
                                if (AdMobSdkState.isRunningOnEmulator()) {
                                    setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                                }
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    databaseEnabled = true
                                    cacheMode = WebSettings.LOAD_DEFAULT
                                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                }
                                addJavascriptInterface(
                                    object {
                                        @JavascriptInterface
                                        fun onNormalMapTap(lat: Double, lon: Double) {
                                            coroutineScope.launch {
                                                focusManager.clearFocus()
                                                if (isPickingCustomMapPointForFloatingPin) {
                                                    isPickingCustomMapPointForFloatingPin = false
                                                    targetLat = lat
                                                    targetLon = lon
                                                    openFormDialogForCoordinates(lat, lon, "CUSTOM")
                                                } else {
                                                    activeFloatingWindowPin = null
                                                    showBottomQuickSaveSheet = false
                                                }
                                            }
                                        }

                                        @JavascriptInterface
                                        fun onSavedMarkerClicked(pinId: Long, screenX: Float, screenY: Float) {
                                            coroutineScope.launch {
                                                val found = savedFloatingPins.find { it.id == pinId }
                                                if (found != null) {
                                                    val d = context.resources.displayMetrics.density
                                                    activeMarkerScreenX = screenX * d
                                                    activeMarkerScreenY = screenY * d
                                                    activeFloatingWindowPin = found
                                                    targetLat = found.latitude
                                                    targetLon = found.longitude
                                                    targetLabel = found.locationName
                                                }
                                            }
                                        }

                                        @JavascriptInterface
                                        fun onOpenPinDetailsFromBubble(pinId: Long) {
                                            coroutineScope.launch {
                                                val found = savedFloatingPins.find { it.id == pinId }
                                                if (found != null) {
                                                    selectedPinForDetailsPage = found
                                                }
                                            }
                                        }

                                        @JavascriptInterface
                                        fun onCloseActiveBubble() {
                                            coroutineScope.launch {
                                                activeFloatingWindowPin = null
                                            }
                                        }

                                        @JavascriptInterface
                                        fun onActiveMarkerPositionUpdated(screenX: Float, screenY: Float) {
                                            coroutineScope.launch {
                                                val d = context.resources.displayMetrics.density
                                                activeMarkerScreenX = screenX * d
                                                activeMarkerScreenY = screenY * d
                                            }
                                        }

                                        @JavascriptInterface
                                        fun onMeasureUpdated(lat1: Double, lon1: Double, lat2: Double, lon2: Double) {
                                            coroutineScope.launch {
                                                measurePoints.clear()
                                                measurePoints.add(LatLng(lat1, lon1))
                                                measurePoints.add(LatLng(lat2, lon2))
                                            }
                                        }
                                    },
                                    "AndroidMapBridge"
                                )
                                webChromeClient = WebChromeClient()
                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        isWebViewLoaded = true
                                        view?.evaluateJavascript(
                                            "if(window.setMapLayer){ window.setMapLayer($mapLayerMode); }",
                                            null
                                        )
                                        syncPinsToWebView()
                                    }
                                }
                                loadDataWithBaseURL(
                                    "https://earthscope.local/",
                                    buildDedicatedMapHtml(targetLat, targetLon, targetZoom, isMeasureMode),
                                    "text/html",
                                    "UTF-8",
                                    null
                                )
                                webViewRef.value = this
                            }
                        }
                    )
                }

                // ===============================================================
                // SLEEK, COMPACT, RESPONSIVE TOP SEARCH BAR + SEARCH ICON BUTTON
                // + HORIZONTAL CATEGORY FILTER BAR
                // ===============================================================
                Column(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .widthIn(max = 460.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Compact 40.dp Pill Search Input Bar
                        Surface(
                            color = cardBg.copy(alpha = 0.95f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, borderColor),
                            shadowElevation = 5.dp,
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("map_page_search_input")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(modifier = Modifier.weight(1f)) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Search city, country or saved place...",
                                            fontSize = 12.sp,
                                            color = secondaryText,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    BasicTextField(
                                        value = searchQuery,
                                        onValueChange = { searchQuery = it },
                                        singleLine = true,
                                        textStyle = TextStyle(
                                            color = primaryText,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        cursorBrush = SolidColor(Color(0xFF10B981)),
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                        keyboardActions = KeyboardActions(
                                            onSearch = { performMapSearch() }
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { searchQuery = "" },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear",
                                            tint = secondaryText,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Top Search Button Icon (Right next to Search Input Bar as requested!)
                        Surface(
                            color = Color(0xFF10B981),
                            shape = CircleShape,
                            shadowElevation = 5.dp,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { performMapSearch() }
                                .testTag("map_page_search_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search Location",
                                    tint = Color.White,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }
                    }

                    // Horizontal Category Filter Bar (All + Travelling, Office, Work, Home, etc.)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .widthIn(max = 460.dp)
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isAllSelected = selectedCategoryFilter == "ALL"
                        Surface(
                            color = if (isAllSelected) Color(0xFF10B981) else cardBg.copy(alpha = 0.92f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, if (isAllSelected) Color(0xFF10B981) else borderColor),
                            shadowElevation = 3.dp,
                            modifier = Modifier
                                .clickable { selectedCategoryFilter = "ALL" }
                                .testTag("filter_category_all")
                        ) {
                            Text(
                                text = "All (${savedFloatingPins.size})",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isAllSelected) Color.White else primaryText,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                            )
                        }

                        PinLocationCategory.entries.forEach { cat ->
                            val count = savedFloatingPins.count { it.categoryKey.equals(cat.key, ignoreCase = true) }
                            val isSelected = selectedCategoryFilter == cat.key
                            Surface(
                                color = if (isSelected) cat.color else cardBg.copy(alpha = 0.92f),
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, if (isSelected) cat.color else borderColor),
                                shadowElevation = 3.dp,
                                modifier = Modifier
                                    .clickable {
                                        selectedCategoryFilter = if (isSelected) "ALL" else cat.key
                                    }
                                    .testTag("filter_category_${cat.key.lowercase()}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = cat.icon,
                                        contentDescription = cat.label,
                                        tint = if (isSelected) Color.White else cat.color,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (count > 0) "${cat.label} ($count)" else cat.label,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else primaryText
                                    )
                                }
                            }
                        }
                    }

                    // Banner when user selected "Custom Location (Pick on Map)"
                    if (isPickingCustomMapPointForFloatingPin) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = Color(0xFF0284C7),
                            shape = RoundedCornerShape(14.dp),
                            shadowElevation = 6.dp,
                            modifier = Modifier.widthIn(max = 460.dp).fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "📌 Tap on the map to place your Custom Location marker",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.5.sp,
                                    modifier = Modifier.weight(1f)
                                )
                                TextButton(onClick = { isPickingCustomMapPointForFloatingPin = false }) {
                                    Text("Cancel", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    // Measurement Distance Readout Banner when Ruler Mode is active
                    if (isMeasureMode) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            color = Color(0xFF0F172A).copy(alpha = 0.92f),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                            modifier = Modifier.widthIn(max = 460.dp).fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = if (measurePoints.size < 2) "Tap 2+ points on map to measure"
                                        else "Distance: ${String.format(Locale.US, "%.2f", measuredDistanceKm)} km (${(measuredDistanceKm * 1000).roundToInt()} m)",
                                        color = Color(0xFFFDE047),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "${measurePoints.size} points pinned",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 10.sp
                                    )
                                }
                                TextButton(
                                    onClick = {
                                        measurePoints.clear()
                                        webViewRef.value?.evaluateJavascript("if(window.clearMeasurement){ window.clearMeasurement(); }", null)
                                    }
                                ) {
                                    Text("Clear", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                // Speech-Bubble 💬 Info Window is rendered directly inside the Google Maps OpenGL Marker
                // (and Leaflet marker layer) so it moves 100% locked with the marker during map gestures!

                // ===============================================================
                // RIGHT-SIDE FLOATING CONTROLS:
                // 0. All Locations Button (Opens dedicated All Locations List Page with Search & Category Filter)
                // 1. Circular '+' Button to Add Location
                // 2. Current Location Button (Moved here on bottom/right with the other buttons!)
                // 3. Zoom In / Zoom Out / Rotate Map Buttons
                // ===============================================================
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 10.dp, bottom = if (showBottomQuickSaveSheet) 136.dp else 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 0. All Locations Circular Icon Button on Right Side of Map Page
                    Surface(
                        color = Color(0xFF8B5CF6),
                        shape = CircleShape,
                        border = BorderStroke(1.5.dp, Color.White),
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .size(44.dp)
                            .clickable {
                                showAllLocationsListPage = true
                            }
                            .testTag("map_all_locations_right_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.FormatListBulleted,
                                contentDescription = "All Locations",
                                tint = Color.White,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                    }

                    // 1. Circular '+' Floating Action Button for Floating Windows on Map
                    Surface(
                        color = Color(0xFF10B981),
                        shape = CircleShape,
                        border = BorderStroke(2.dp, Color.White),
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {
                                showAddLocationSourceDialog = true
                            }
                            .testTag("map_add_floating_window_fab")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Location Floating Window",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    // 2. Current Location Button (Checks GPS -> shows built-in enable dialog if off -> shows current location)
                    Surface(
                        color = if (followMyLocation) Color(0xFF10B981) else Color(0xFF0284C7),
                        shape = CircleShape,
                        border = BorderStroke(1.5.dp, Color.White),
                        shadowElevation = 6.dp,
                        modifier = Modifier
                            .size(44.dp)
                            .clickable {
                                if (!hasLocationPermission) {
                                    pendingCurrentLocationForFloatingDialog = false
                                    locPermLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                } else {
                                    requestCurrentGpsLocationWithBuiltInCheck(openFloatingPinDialog = false)
                                }
                            }
                            .testTag("map_page_my_location_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Current Location",
                                tint = Color.White,
                                modifier = Modifier.size(21.dp)
                            )
                        }
                    }

                    MapCircleControlButton(
                        icon = Icons.Default.Add,
                        contentDescription = "Map Zoom In",
                        cardBg = cardBg,
                        primaryText = primaryText,
                        tag = "map_zoom_in_button",
                        onClick = {
                            targetZoom = (targetZoom + 1).coerceIn(2, 20)
                            flyMapTo(targetLat, targetLon, targetZoom, targetLabel)
                        }
                    )
                    MapCircleControlButton(
                        icon = Icons.Default.Remove,
                        contentDescription = "Map Zoom Out",
                        cardBg = cardBg,
                        primaryText = primaryText,
                        tag = "map_zoom_out_button",
                        onClick = {
                            targetZoom = (targetZoom - 1).coerceIn(2, 20)
                            flyMapTo(targetLat, targetLon, targetZoom, targetLabel)
                        }
                    )
                    MapCircleControlButton(
                        icon = Icons.Default.RotateRight,
                        contentDescription = "Rotate Map",
                        cardBg = cardBg,
                        primaryText = primaryText,
                        tag = "map_rotate_button",
                        onClick = {
                            mapBearingDeg = (mapBearingDeg + 45f) % 360f
                            if (hasValidGoogleKey) {
                                flyMapTo(targetLat, targetLon, targetZoom, targetLabel, mapBearingDeg)
                            } else {
                                Toast.makeText(context, "Map Heading: ${mapBearingDeg.roundToInt()}°", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }

                // Bottom Pinned Location Card (Hidden on normal map tap so everything stays clean!)
                if (showBottomQuickSaveSheet) {
                    Surface(
                        color = cardBg.copy(alpha = 0.96f),
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, borderColor),
                        shadowElevation = 8.dp,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(10.dp)
                            .widthIn(max = 460.dp)
                            .fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = targetLabel,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = primaryText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "${String.format(Locale.US, "%.4f", targetLat)}°, ${String.format(Locale.US, "%.4f", targetLon)}°",
                                        fontSize = 11.sp,
                                        color = secondaryText
                                    )
                                }
                                IconButton(
                                    onClick = { showBottomQuickSaveSheet = false },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Close",
                                        tint = secondaryText,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                QuickSavePinChip(
                                    label = "Set Home",
                                    icon = Icons.Default.Home,
                                    color = Color(0xFF0284C7),
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        localStore.setHomePlace(targetLat, targetLon, targetLabel)
                                        Toast.makeText(context, "🏠 Home Location Saved!", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                QuickSavePinChip(
                                    label = "Set Work",
                                    icon = Icons.Default.Work,
                                    color = Color(0xFFF59E0B),
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        localStore.setWorkPlace(targetLat, targetLon, targetLabel)
                                        Toast.makeText(context, "💼 Work Location Saved!", Toast.LENGTH_SHORT).show()
                                    }
                                )
                                QuickSavePinChip(
                                    label = "Favorite",
                                    icon = Icons.Default.Star,
                                    color = Color(0xFFEC4899),
                                    modifier = Modifier.weight(1f),
                                    onClick = {
                                        localStore.addFavoritePlace(targetLabel, targetLat, targetLon, "FAVORITE")
                                        Toast.makeText(context, "⭐ Added to Favorites!", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // =========================================================================
    // DIALOG 1: Ask User -> Current Location (GPS) OR Custom Location
    // =========================================================================
    if (showAddLocationSourceDialog) {
        AddFloatingLocationSourceDialog(
            isLightTheme = isLightTheme,
            onSelectCurrentLocation = {
                showAddLocationSourceDialog = false
                if (!hasLocationPermission) {
                    pendingCurrentLocationForFloatingDialog = true
                    locPermLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                } else {
                    // Checks GPS -> shows built-in system dialog to turn on GPS if off -> opens Current Location dialog
                    requestCurrentGpsLocationWithBuiltInCheck(openFloatingPinDialog = true)
                }
            },
            onSelectCustomLocationOnMap = {
                showAddLocationSourceDialog = false
                isPickingCustomMapPointForFloatingPin = true
                Toast.makeText(context, "Tap on the map to pick your Custom Location", Toast.LENGTH_SHORT).show()
            },
            onSelectCustomCoordinatesNow = {
                showAddLocationSourceDialog = false
                // Pass null for initialLat and initialLon so the Latitude & Longitude input fields start EMPTY!
                openFormDialogForCoordinates(null, null, "CUSTOM")
            },
            onDismiss = {
                showAddLocationSourceDialog = false
            }
        )
    }

    // =========================================================================
    // DIALOG 2: Form Dialog (Required Lat/Long + Date/Time + Category + Custom Marker + Name/Notes + up to 3 Photos)
    // =========================================================================
    var showPinSavedInterstitialAd by remember { mutableStateOf(false) }

    if (showCreateFloatingPinFormDialog) {
        CreateFloatingPinFormDialog(
            isLightTheme = isLightTheme,
            initialLat = formDialogLat,
            initialLon = formDialogLon,
            initialSuggestedName = formDialogSuggestedName,
            sourceType = formDialogSourceType,
            onCancel = {
                showCreateFloatingPinFormDialog = false
            },
            onSave = { locName, locNotes, lat, lon, categoryKey, markerStyleKey, formattedDateTime, img1, img2, img3 ->
                showCreateFloatingPinFormDialog = false
                viewModel.saveMapFloatingPin(
                    locationName = locName,
                    locationNotes = locNotes,
                    latitude = lat,
                    longitude = lon,
                    sourceType = formDialogSourceType,
                    categoryKey = categoryKey,
                    markerStyleKey = markerStyleKey,
                    formattedDateTime = formattedDateTime,
                    primaryImagePath = img1,
                    secondImagePath = img2,
                    thirdImagePath = img3,
                    onSaved = { savedEntity ->
                        if (selectedCategoryFilter != "ALL" && !selectedCategoryFilter.equals(categoryKey, ignoreCase = true)) {
                            selectedCategoryFilter = "ALL"
                        }
                        activeMarkerScreenX = null
                        activeMarkerScreenY = null
                        activeFloatingWindowPin = savedEntity
                        flyMapTo(lat, lon, 16, locName)
                        showPinSavedInterstitialAd = true
                        Toast.makeText(context, "Location saved with custom marker!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }

    // Interstitial Dummy Ad shown after successfully saving a new Floating Location Pin on Map
    if (showPinSavedInterstitialAd) {
        AdMobDummyInterstitialDialog(
            placementTrigger = "Saved New Map Location Pin",
            adUnitId = AdMobConfig.INTERSTITIAL_PIN_SAVED_AD_UNIT_ID,
            onAdClosed = { showPinSavedInterstitialAd = false }
        )
    }

    // 2. If user clicked the "All Locations" button on the right side of the Map Page, render the All Locations List Page overlay (keeping Map Page alive underneath so state is preserved!)
    if (showAllLocationsListPage) {
        AllLocationsListPageScreen(
            allSavedPins = savedFloatingPins,
            isLightTheme = isLightTheme,
            onBackToMap = { showAllLocationsListPage = false },
            onSelectLocationForDetails = { clickedPin ->
                selectedPinForDetailsPage = clickedPin
            },
            onShowLocationOnMap = { pinToFocus ->
                showAllLocationsListPage = false
                if (selectedCategoryFilter != "ALL" && !selectedCategoryFilter.equals(pinToFocus.categoryKey, ignoreCase = true)) {
                    selectedCategoryFilter = "ALL"
                }
                activeMarkerScreenX = null
                activeMarkerScreenY = null
                activeFloatingWindowPin = pinToFocus
                flyMapTo(pinToFocus.latitude, pinToFocus.longitude, 16, pinToFocus.locationName)
            },
            onAddNewLocation = {
                showAllLocationsListPage = false
                showAddLocationSourceDialog = true
            }
        )
    }

    // 1. If user is viewing a single location's Details Page (from Floating Window OR from All Locations List), render it full-screen overlay (keeping Map & All Locations List state alive underneath!)
    if (liveDetailsPin != null) {
        FloatingLocationDetailsPageScreen(
            pin = liveDetailsPin,
            isLightTheme = isLightTheme,
            onBack = {
                selectedPinForDetailsPage = null
            },
            onShowOnMap = { pinToFocus ->
                selectedPinForDetailsPage = null
                showAllLocationsListPage = false
                if (selectedCategoryFilter != "ALL" && !selectedCategoryFilter.equals(pinToFocus.categoryKey, ignoreCase = true)) {
                    selectedCategoryFilter = "ALL"
                }
                activeMarkerScreenX = null
                activeMarkerScreenY = null
                activeFloatingWindowPin = pinToFocus
                flyMapTo(pinToFocus.latitude, pinToFocus.longitude, 16, pinToFocus.locationName)
            },
            onUpdatePin = { updatedEntity ->
                viewModel.updateMapFloatingPin(updatedEntity) { saved ->
                    selectedPinForDetailsPage = saved
                    if (activeFloatingWindowPin?.id == saved.id) {
                        activeFloatingWindowPin = saved
                    }
                    flyMapTo(saved.latitude, saved.longitude, targetZoom, saved.locationName)
                }
            },
            onDeletePin = { pinToDelete ->
                viewModel.deleteMapFloatingPin(pinToDelete.id)
                if (activeFloatingWindowPin?.id == pinToDelete.id) {
                    activeFloatingWindowPin = null
                }
                selectedPinForDetailsPage = null
                Toast.makeText(context, "Location deleted", Toast.LENGTH_SHORT).show()
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose { webViewRef.value?.destroy() }
    }
}

@Composable
private fun MapCircleControlButton(
    icon: ImageVector,
    contentDescription: String,
    cardBg: Color,
    primaryText: Color,
    tag: String,
    onClick: () -> Unit
) {
    Surface(
        color = cardBg.copy(alpha = 0.95f),
        shape = CircleShape,
        shadowElevation = 5.dp,
        modifier = Modifier
            .size(42.dp)
            .clickable { onClick() }
            .testTag(tag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = contentDescription, tint = primaryText, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
private fun QuickSavePinChip(
    label: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        color = color.copy(alpha = 0.14f),
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(vertical = 7.dp, horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(label, fontSize = 10.5.sp, fontWeight = FontWeight.Bold, color = color, maxLines = 1)
        }
    }
}

// Custom Google Maps JSON Style for BLACK (Dark Night) Map
internal const val GOOGLE_MAP_BLACK_STYLE_JSON = """
[
  {"elementType": "geometry", "stylers": [{"color": "#0b1320"}]},
  {"elementType": "labels.text.fill", "stylers": [{"color": "#94a3b8"}]},
  {"elementType": "labels.text.stroke", "stylers": [{"color": "#090f1a"}]},
  {"featureType": "administrative", "elementType": "geometry.stroke", "stylers": [{"color": "#334155"}]},
  {"featureType": "administrative.land_parcel", "elementType": "labels.text.fill", "stylers": [{"color": "#64748b"}]},
  {"featureType": "landscape.natural", "elementType": "geometry", "stylers": [{"color": "#0f172a"}]},
  {"featureType": "poi", "elementType": "geometry", "stylers": [{"color": "#162238"}]},
  {"featureType": "poi", "elementType": "labels.text.fill", "stylers": [{"color": "#64748b"}]},
  {"featureType": "poi.park", "elementType": "geometry.fill", "stylers": [{"color": "#112a26"}]},
  {"featureType": "road", "elementType": "geometry", "stylers": [{"color": "#1e293b"}]},
  {"featureType": "road.arterial", "elementType": "geometry", "stylers": [{"color": "#26354d"}]},
  {"featureType": "road.highway", "elementType": "geometry", "stylers": [{"color": "#334155"}]},
  {"featureType": "road.highway", "elementType": "geometry.stroke", "stylers": [{"color": "#1e293b"}]},
  {"featureType": "water", "elementType": "geometry", "stylers": [{"color": "#040811"}]},
  {"featureType": "water", "elementType": "labels.text.fill", "stylers": [{"color": "#38bdf8"}]}
]
"""

// Custom Google Maps JSON Style for GREEN (Vibrant Green Land + Royal Blue Water + Red Highway Shields) Map
internal const val GOOGLE_MAP_GREEN_STYLE_JSON = """
[
  {"elementType": "geometry", "stylers": [{"color": "#26c44b"}]},
  {"elementType": "labels.text.fill", "stylers": [{"color": "#052e16"}]},
  {"elementType": "labels.text.stroke", "stylers": [{"color": "#ffffff"}, {"weight": 2.5}]},
  {"featureType": "administrative", "elementType": "geometry.stroke", "stylers": [{"color": "#15803d"}, {"weight": 1.4}]},
  {"featureType": "administrative.country", "elementType": "labels.text.fill", "stylers": [{"color": "#064e3b"}]},
  {"featureType": "landscape.natural", "elementType": "geometry", "stylers": [{"color": "#2bd452"}]},
  {"featureType": "landscape.man_made", "elementType": "geometry", "stylers": [{"color": "#22b845"}]},
  {"featureType": "poi", "elementType": "geometry", "stylers": [{"color": "#1fa93f"}]},
  {"featureType": "poi.park", "elementType": "geometry.fill", "stylers": [{"color": "#16a34a"}]},
  {"featureType": "road", "elementType": "geometry.fill", "stylers": [{"color": "#ffffff"}]},
  {"featureType": "road", "elementType": "geometry.stroke", "stylers": [{"color": "#bbf7d0"}]},
  {"featureType": "road.arterial", "elementType": "geometry.fill", "stylers": [{"color": "#ffffff"}]},
  {"featureType": "road.highway", "elementType": "geometry.fill", "stylers": [{"color": "#ef4444"}]},
  {"featureType": "road.highway", "elementType": "geometry.stroke", "stylers": [{"color": "#ffffff"}, {"weight": 1.5}]},
  {"featureType": "road.highway", "elementType": "labels.text.fill", "stylers": [{"color": "#ffffff"}]},
  {"featureType": "road.highway", "elementType": "labels.text.stroke", "stylers": [{"color": "#b91c1c"}]},
  {"featureType": "water", "elementType": "geometry.fill", "stylers": [{"color": "#1d63dc"}]},
  {"featureType": "water", "elementType": "labels.text.fill", "stylers": [{"color": "#ffffff"}]},
  {"featureType": "water", "elementType": "labels.text.stroke", "stylers": [{"color": "#1e40af"}]}
]
"""

private fun rememberOrEncodeThumbDataUrl(filePath: String?): String {
    if (filePath.isNullOrBlank()) return ""
    return try {
        val file = java.io.File(filePath)
        if (!file.exists()) return ""
        val boundsOpts = android.graphics.BitmapFactory.Options().apply { inJustDecodeBounds = true }
        android.graphics.BitmapFactory.decodeFile(file.absolutePath, boundsOpts)
        var sampleSize = 1
        while (boundsOpts.outWidth / sampleSize > 140 || boundsOpts.outHeight / sampleSize > 140) {
            sampleSize *= 2
        }
        val decodeOpts = android.graphics.BitmapFactory.Options().apply { inSampleSize = sampleSize }
        val bmp = android.graphics.BitmapFactory.decodeFile(file.absolutePath, decodeOpts) ?: return ""
        val baos = java.io.ByteArrayOutputStream()
        bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 72, baos)
        val b64 = android.util.Base64.encodeToString(baos.toByteArray(), android.util.Base64.NO_WRAP)
        "data:image/jpeg;base64,$b64"
    } catch (_: Exception) {
        ""
    }
}

private fun buildDedicatedMapHtml(
    initLat: Double,
    initLon: Double,
    initZoom: Int,
    initMeasureMode: Boolean
): String {
    return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
    <style>
        * { box-sizing: border-box; -webkit-tap-highlight-color: transparent; }
        html, body, #map { margin: 0; padding: 0; width: 100%; height: 100%; background: #f8fafc; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; transition: background 0.2s ease; }
        /* Mode 0: White Map (Clean Bright Look) */
        body.map-mode-white #map { background: #f8fafc; }
        body.map-mode-white .leaflet-tile-pane { filter: brightness(1.03) contrast(0.98); }

        /* Mode 1: Black Map (Deep Dark Mode) */
        body.map-mode-black #map { background: #070d19; }
        body.map-mode-black .leaflet-tile-pane { filter: brightness(0.95) contrast(1.12); }

        /* Mode 2: Green Map (Vibrant Green Land + Deep Royal Blue Water + White Roads & Red Shields) */
        body.map-mode-green #map { background: #1d63dc; }
        body.map-mode-green .leaflet-tile-pane {
            filter: url(#green-map-color-filter);
        }

        /* =====================================================================
           CUSTOM USER MARKER (ROUND BADGE ON THE EXACT LOCATION)
           + SINGLE SPEECH-BUBBLE 💬 INFO WINDOW LOCKED DIRECTLY TO THE MARKER
           Moves 100% locked with the map & marker at 60fps — NEVER lags or drifts!
           ===================================================================== */
        .custom-pin-container {
            position: relative;
            width: 52px;
            height: 52px;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .custom-pin-ground-pulse {
            position: absolute;
            width: 48px;
            height: 48px;
            border-radius: 50%;
            opacity: 0.28;
            pointer-events: none;
        }
        .custom-pin-badge {
            position: relative;
            width: 42px;
            height: 42px;
            border-radius: 50%;
            border: 3.5px solid #ffffff;
            box-shadow: 0 4px 12px rgba(15, 23, 42, 0.45);
            display: flex;
            align-items: center;
            justify-content: center;
            z-index: 3;
            cursor: pointer;
            transition: transform 0.18s ease;
        }
        .custom-pin-badge.selected {
            transform: scale(1.12);
            box-shadow: 0 0 0 3px rgba(255,255,255,0.92), 0 6px 16px rgba(15, 23, 42, 0.55);
        }
        .custom-pin-inner {
            width: 28px;
            height: 28px;
            background: #ffffff;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 16px;
            line-height: 1;
        }

        /* Speech-Bubble 💬 Info Window anchored directly above the marker inside the map layer */
        .pin-speech-bubble-wrap {
            position: absolute;
            bottom: 54px;
            left: 50%;
            transform: translateX(-50%);
            width: 218px;
            z-index: 20;
            filter: drop-shadow(0 8px 18px rgba(15, 23, 42, 0.32));
            pointer-events: auto;
        }
        .pin-speech-bubble-card {
            background: #ffffff;
            border-radius: 16px;
            border: 2px solid #10b981;
            padding: 9px 10px;
            cursor: pointer;
        }
        .bubble-top-row {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 6px;
        }
        .bubble-cat-group {
            display: flex;
            align-items: center;
            gap: 5px;
            min-width: 0;
        }
        .bubble-dot-icon {
            width: 19px;
            height: 19px;
            border-radius: 50%;
            color: #ffffff;
            font-size: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
        }
        .bubble-cat-pill {
            font-size: 9.5px;
            font-weight: 800;
            padding: 2px 6px;
            border-radius: 6px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .bubble-close-btn {
            width: 20px;
            height: 20px;
            border-radius: 50%;
            border: none;
            background: #f1f5f9;
            color: #64748b;
            font-size: 13px;
            font-weight: bold;
            line-height: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            padding: 0;
            flex-shrink: 0;
        }
        .bubble-main-row {
            display: flex;
            align-items: center;
            gap: 8px;
        }
        .bubble-thumb {
            width: 44px;
            height: 44px;
            border-radius: 10px;
            object-fit: cover;
            flex-shrink: 0;
            background: #f1f5f9;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
        }
        .bubble-info-col {
            flex: 1;
            min-width: 0;
        }
        .bubble-title {
            font-size: 12.5px;
            font-weight: 800;
            color: #0f172a;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
            margin-bottom: 2px;
        }
        .bubble-coords {
            font-size: 9.5px;
            font-weight: 700;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .bubble-eye-badge {
            width: 25px;
            height: 25px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 12px;
            flex-shrink: 0;
        }
        /* Single Speech-Bubble 💬 Pointer Triangle between Info Window and Marker */
        .bubble-tail-outer {
            width: 0;
            height: 0;
            border-left: 11px solid transparent;
            border-right: 11px solid transparent;
            border-top: 13px solid #10b981;
            margin: 0 auto;
            position: relative;
        }
        .bubble-tail-inner {
            position: absolute;
            top: -15px;
            left: -9px;
            width: 0;
            height: 0;
            border-left: 9px solid transparent;
            border-right: 9px solid transparent;
            border-top: 11px solid #ffffff;
        }
    </style>
</head>
<body class="map-mode-white">
    <!-- Precision SVG Color Filter for Green Map:
         Turns OSM cream/grey land into vivid lime-emerald green (#24C848),
         turns light blue water into rich royal blue (#1D63DC),
         keeps roads crisp white and highway shields bold red -->
    <svg width="0" height="0" style="position:absolute;z-index:-1;">
        <defs>
            <filter id="green-map-color-filter" color-interpolation-filters="sRGB">
                <feColorMatrix type="matrix" values="
                    0.95  0.10 -0.90  0.00  0.00
                    0.10  1.45 -0.45  0.00  0.02
                   -0.75 -0.25  1.95  0.00  0.00
                    0.00  0.00  0.00  1.00  0.00" />
                <feComponentTransfer>
                    <feFuncR type="gamma" amplitude="1.05" exponent="1.15" offset="-0.02" />
                    <feFuncG type="gamma" amplitude="1.12" exponent="0.88" offset="0.02" />
                    <feFuncB type="gamma" amplitude="1.08" exponent="1.02" offset="0.00" />
                </feComponentTransfer>
            </filter>
        </defs>
    </svg>
    <div id="map"></div>
    <script>
        var whiteTiles = L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', { maxZoom: 19 });
        var blackTiles = L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', { maxZoom: 19 });
        var greenBaseTiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19 });
        var sat = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', { maxZoom: 19 });
        var labels = L.tileLayer('https://services.arcgisonline.com/ArcGIS/rest/services/Reference/World_Boundaries_and_Places/MapServer/tile/{z}/{y}/{x}', { maxZoom: 19 });
        var topo = L.tileLayer('https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png', { maxZoom: 17 });

        var map = L.map('map', { center: [$initLat, $initLon], zoom: $initZoom, zoomControl: false, layers: [whiteTiles] });
        var savedPinsLayer = L.layerGroup().addTo(map);
        var activePinLatLng = null;

        var measureMode = $initMeasureMode;
        var measurePts = [];
        var measureLine = null;

        window.onOpenBubbleDetails = function(ev, pinId) {
            if (ev) {
                ev.stopPropagation();
                ev.preventDefault();
            }
            if (window.AndroidMapBridge && window.AndroidMapBridge.onOpenPinDetailsFromBubble) {
                window.AndroidMapBridge.onOpenPinDetailsFromBubble(pinId);
            }
        };

        window.onCloseBubbleCard = function(ev) {
            if (ev) {
                ev.stopPropagation();
                ev.preventDefault();
            }
            activePinLatLng = null;
            if (window.AndroidMapBridge && window.AndroidMapBridge.onCloseActiveBubble) {
                window.AndroidMapBridge.onCloseActiveBubble();
            }
        };

        map.on('click', function(e) {
            if (measureMode) {
                measurePts.push([e.latlng.lat, e.latlng.lng]);
                if (measureLine) map.removeLayer(measureLine);
                if (measurePts.length >= 2) {
                    measureLine = L.polyline(measurePts, { color: '#f59e0b', weight: 4 }).addTo(map);
                    var p1 = measurePts[measurePts.length - 2];
                    var p2 = measurePts[measurePts.length - 1];
                    if (window.AndroidMapBridge && window.AndroidMapBridge.onMeasureUpdated) {
                        window.AndroidMapBridge.onMeasureUpdated(p1[0], p1[1], p2[0], p2[1]);
                    }
                }
            } else {
                // Normal map tap: do NOT add marker; notify Android to hide floating window or pick custom point if active
                activePinLatLng = null;
                if (window.AndroidMapBridge && window.AndroidMapBridge.onNormalMapTap) {
                    window.AndroidMapBridge.onNormalMapTap(e.latlng.lat, e.latlng.lng);
                }
            }
        });

        window.renderSavedPins = function(pinsArray, activeId) {
            savedPinsLayer.clearLayers();
            activePinLatLng = null;
            if (!pinsArray) return;
            pinsArray.forEach(function(p) {
                var isSelected = (p.id === activeId);
                var bubbleHtml = '';
                if (isSelected) {
                    var thumbEl = (p.thumb && p.thumb.length > 10)
                        ? '<img class="bubble-thumb" src="' + p.thumb + '" />'
                        : '<div class="bubble-thumb" style="background:' + p.color + '22; color:' + p.color + ';">' + (p.emoji || '📍') + '</div>';
                    var pinSvg = '<svg width="11" height="11" viewBox="0 0 24 24" fill="white" xmlns="http://www.w3.org/2000/svg"><path d="M12 2C8.13 2 5 5.13 5 9c0 5.25 7 13 7 13s7-7.75 7-13c0-3.87-3.13-7-7-7zm0 9.5c-1.38 0-2.5-1.12-2.5-2.5s1.12-2.5 2.5-2.5 2.5 1.12 2.5 2.5-1.12 2.5-2.5 2.5z"/></svg>';
                    bubbleHtml =
                        '<div class="pin-speech-bubble-wrap" onclick="window.onOpenBubbleDetails(event, ' + p.id + ')">' +
                            '<div class="pin-speech-bubble-card" style="border-color:' + p.color + ';">' +
                                '<div class="bubble-top-row">' +
                                    '<div class="bubble-cat-group">' +
                                        '<div class="bubble-dot-icon" style="background:' + p.color + ';">' + pinSvg + '</div>' +
                                        '<div class="bubble-cat-pill" style="background:' + (p.catColor || p.color) + '24; color:' + (p.catColor || p.color) + ';">' + (p.emoji || '') + ' ' + (p.catLabel || 'Saved') + '</div>' +
                                    '</div>' +
                                    '<button class="bubble-close-btn" onclick="window.onCloseBubbleCard(event)">✕</button>' +
                                '</div>' +
                                '<div class="bubble-main-row">' +
                                    thumbEl +
                                    '<div class="bubble-info-col">' +
                                        '<div class="bubble-title">' + (p.name || 'Saved Pin') + '</div>' +
                                        '<div class="bubble-coords" style="color:' + p.color + ';">' + (p.coords || '') + '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +
                            '<div class="bubble-tail-outer" style="border-top-color:' + p.color + ';"><div class="bubble-tail-inner"></div></div>' +
                        '</div>';
                }

                var html =
                    '<div class="custom-pin-container">' +
                        bubbleHtml +
                        '<div class="custom-pin-ground-pulse" style="background:' + p.color + ';"></div>' +
                        '<div class="custom-pin-badge ' + (isSelected ? 'selected' : '') + '" style="background:' + p.color + ';">' +
                            '<div class="custom-pin-inner">' + (p.emoji || p.symbol || '📍') + '</div>' +
                        '</div>' +
                    '</div>';

                var customIcon = L.divIcon({
                    className: '',
                    html: html,
                    iconSize: [52, 52],
                    iconAnchor: [26, 26]
                });
                var m = L.marker([p.lat, p.lon], {
                    icon: customIcon,
                    zIndexOffset: isSelected ? 1000 : 0
                }).addTo(savedPinsLayer);
                if (isSelected) {
                    activePinLatLng = L.latLng(p.lat, p.lon);
                }
                m.on('click', function(ev) {
                    L.DomEvent.stopPropagation(ev);
                    activePinLatLng = L.latLng(p.lat, p.lon);
                    var pt = map.latLngToContainerPoint(activePinLatLng);
                    if (window.AndroidMapBridge && window.AndroidMapBridge.onSavedMarkerClicked) {
                        window.AndroidMapBridge.onSavedMarkerClicked(p.id, pt.x, pt.y);
                    }
                });
            });
        };

        window.setMeasureMode = function(enabled) {
            measureMode = enabled;
        };

        window.clearMeasurement = function() {
            measurePts = [];
            if (measureLine) { map.removeLayer(measureLine); measureLine = null; }
        };

        window.setMapLayer = function(mode) {
            document.body.className = '';
            map.eachLayer(function(l) {
                if (l !== savedPinsLayer && l !== measureLine) map.removeLayer(l);
            });
            if (mode === 1) {
                sat.addTo(map);
                labels.addTo(map);
            } else if (mode === 2) {
                topo.addTo(map);
            } else if (mode === 3) {
                document.body.className = 'map-mode-black';
                blackTiles.addTo(map);
            } else if (mode === 4) {
                document.body.className = 'map-mode-green';
                greenBaseTiles.addTo(map);
            } else {
                document.body.className = 'map-mode-white';
                whiteTiles.addTo(map);
            }
            savedPinsLayer.addTo(map);
        };

        window.flyToLocation = function(lat, lon, zoom, label) {
            map.flyTo([lat, lon], zoom, { duration: 0.65 });
        };
    </script>
</body>
</html>
    """.trimIndent()
}

/**
 * ============================================================================
 * FEATURE 4: STANDALONE DIGITAL COMPASS SCREEN
 * ============================================================================
 */
@Composable
fun StandaloneCompassScreen(
    isLightTheme: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current

    var azimuthDeg by remember { mutableFloatStateOf(0f) }
    var pitchDeg by remember { mutableFloatStateOf(0f) }
    var rollDeg by remember { mutableFloatStateOf(0f) }
    var magneticStrengthUt by remember { mutableFloatStateOf(45f) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        if (sensorManager == null) {
            onDispose { }
        } else {
            val rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
            val accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            val magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

            val gravity = FloatArray(3)
            val geomagnetic = FloatArray(3)
            var hasGravity = false
            val rotationMatrix = FloatArray(9)
            val remappedMatrix = FloatArray(9)
            val orientationAngles = FloatArray(3)

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event == null) return
                    when (event.sensor.type) {
                        Sensor.TYPE_ROTATION_VECTOR -> {
                            SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)
                            val matrixToUse = if (abs(rotationMatrix[8]) < 0.75f) {
                                SensorManager.remapCoordinateSystem(
                                    rotationMatrix,
                                    SensorManager.AXIS_X,
                                    SensorManager.AXIS_Z,
                                    remappedMatrix
                                )
                                remappedMatrix
                            } else rotationMatrix
                            SensorManager.getOrientation(matrixToUse, orientationAngles)
                            val deg = ((Math.toDegrees(orientationAngles[0].toDouble()).toFloat() % 360f) + 360f) % 360f
                            azimuthDeg = deg
                            pitchDeg = Math.toDegrees(orientationAngles[1].toDouble()).toFloat()
                            rollDeg = Math.toDegrees(orientationAngles[2].toDouble()).toFloat()
                        }
                        Sensor.TYPE_ACCELEROMETER -> {
                            System.arraycopy(event.values, 0, gravity, 0, 3)
                            hasGravity = true
                        }
                        Sensor.TYPE_MAGNETIC_FIELD -> {
                            System.arraycopy(event.values, 0, geomagnetic, 0, 3)
                            val mag = sqrt(
                                event.values[0] * event.values[0] +
                                        event.values[1] * event.values[1] +
                                        event.values[2] * event.values[2]
                            )
                            magneticStrengthUt = mag
                            if (rotationSensor == null && hasGravity) {
                                if (SensorManager.getRotationMatrix(rotationMatrix, null, gravity, geomagnetic)) {
                                    SensorManager.getOrientation(rotationMatrix, orientationAngles)
                                    val deg = ((Math.toDegrees(orientationAngles[0].toDouble()).toFloat() % 360f) + 360f) % 360f
                                    azimuthDeg = deg
                                }
                            }
                        }
                    }
                }
                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }

            if (rotationSensor != null) {
                sensorManager.registerListener(listener, rotationSensor, SensorManager.SENSOR_DELAY_GAME)
            }
            if (accelSensor != null) {
                sensorManager.registerListener(listener, accelSensor, SensorManager.SENSOR_DELAY_GAME)
            }
            if (magSensor != null) {
                sensorManager.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_GAME)
            }

            onDispose { sensorManager.unregisterListener(listener) }
        }
    }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val headingInt = ((azimuthDeg.roundToInt() % 360) + 360) % 360
    val cardinalShort = getCardinalDirectionShort(azimuthDeg)
    val cardinalFull = when (cardinalShort) {
        "N" -> "North"
        "NE" -> "North-East"
        "E" -> "East"
        "SE" -> "South-East"
        "S" -> "South"
        "SW" -> "South-West"
        "W" -> "West"
        else -> "North-West"
    }

    val textPaint = remember(isLightTheme) {
        AndroidPaint().apply {
            isAntiAlias = true
            textAlign = AndroidPaint.Align.CENTER
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        }
    }

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("standalone_compass_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            FeatureSubScreenTopBar(
                title = "Compass",
                accentColor = Color(0xFFEF4444),
                isLightTheme = isLightTheme,
                onBack = onBack,
                trailingContent = {
                    GpsAndBatteryStatusPill(isLightTheme = isLightTheme)
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$headingInt° $cardinalShort",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryText
                    )
                    Text(
                        text = cardinalFull,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFEF4444)
                    )
                }

                Surface(
                    color = cardBg,
                    shape = CircleShape,
                    border = BorderStroke(2.dp, borderColor),
                    modifier = Modifier.size(280.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                            val cx = size.width / 2f
                            val cy = size.height / 2f
                            val r = size.minDimension / 2f

                            drawLine(
                                color = Color(0xFFEF4444),
                                start = Offset(cx, cy - r - 6f),
                                end = Offset(cx, cy - r + 18f),
                                strokeWidth = 6f,
                                cap = StrokeCap.Round
                            )

                            for (deg in 0 until 360 step 5) {
                                val angleRad = Math.toRadians((deg - azimuthDeg - 90f).toDouble())
                                val isCardinal = deg % 90 == 0
                                val isMajor = deg % 30 == 0
                                val innerR = when {
                                    isCardinal -> r * 0.80f
                                    isMajor -> r * 0.85f
                                    else -> r * 0.91f
                                }
                                val sx = cx + (r * cos(angleRad)).toFloat()
                                val sy = cy + (r * sin(angleRad)).toFloat()
                                val ex = cx + (innerR * cos(angleRad)).toFloat()
                                val ey = cy + (innerR * sin(angleRad)).toFloat()

                                drawLine(
                                    color = when {
                                        deg == 0 -> Color(0xFFEF4444)
                                        isCardinal -> if (isLightTheme) Color(0xFF0F172A) else Color.White
                                        isMajor -> Color(0xFF38BDF8)
                                        else -> Color(0xFF94A3B8).copy(alpha = 0.45f)
                                    },
                                    start = Offset(sx, sy),
                                    end = Offset(ex, ey),
                                    strokeWidth = if (isCardinal) 4.5f else if (isMajor) 2.5f else 1.2f,
                                    cap = StrokeCap.Round
                                )

                                if (isCardinal) {
                                    val labelR = r * 0.65f
                                    val lx = cx + (labelR * cos(angleRad)).toFloat()
                                    val ly = cy + (labelR * sin(angleRad)).toFloat()
                                    val letter = when (deg) {
                                        0 -> "N"
                                        90 -> "E"
                                        180 -> "S"
                                        else -> "W"
                                    }
                                    textPaint.textSize = 42f
                                    textPaint.color = if (deg == 0) {
                                        android.graphics.Color.parseColor("#EF4444")
                                    } else if (isLightTheme) {
                                        android.graphics.Color.parseColor("#0F172A")
                                    } else {
                                        android.graphics.Color.WHITE
                                    }
                                    drawContext.canvas.nativeCanvas.drawText(letter, lx, ly + 14f, textPaint)
                                }
                            }

                            drawCircle(
                                color = Color(0xFF0284C7).copy(alpha = 0.15f),
                                radius = r * 0.28f,
                                center = Offset(cx, cy)
                            )
                            drawCircle(
                                color = Color(0xFF0284C7).copy(alpha = 0.5f),
                                radius = r * 0.28f,
                                center = Offset(cx, cy),
                                style = Stroke(width = 2f)
                            )
                            val bubbleX = (cx + (rollDeg.coerceIn(-25f, 25f) / 25f) * (r * 0.2f))
                            val bubbleY = (cy + (pitchDeg.coerceIn(-25f, 25f) / 25f) * (r * 0.2f))
                            drawCircle(
                                color = Color(0xFF22C55E),
                                radius = 14f,
                                center = Offset(bubbleX, bubbleY)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .widthIn(max = 460.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SimpleStatBox(
                        title = "Direction",
                        value = "$headingInt° $cardinalShort",
                        accent = Color(0xFFEF4444),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Magnetic",
                        value = "${magneticStrengthUt.roundToInt()} µT",
                        accent = Color(0xFF0284C7),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Level",
                        value = "${pitchDeg.roundToInt()}° / ${rollDeg.roundToInt()}°",
                        accent = Color(0xFF22C55E),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Sticky Bottom Adaptive Banner Ad on Compass Screen
            AdMobAdaptiveBanner(
                placementName = "Compass_Bottom_Banner",
                adUnitId = AdMobConfig.BANNER_COMPASS_AD_UNIT_ID,
                isLightTheme = isLightTheme
            )
        }
    }
}

/**
 * ============================================================================
 * FEATURE 5: SAVED PLACES (HOME 🏠, WORK 💼, FAVORITE LOCATIONS ⭐)
 * Locally persisted with one-tap GPS save or open on Google Map.
 * ============================================================================
 */
@Composable
fun SavedPlacesScreen(
    isLightTheme: Boolean,
    onBack: () -> Unit,
    onOpenLocationOnMap: (Double, Double, String) -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val localStore = remember { LocalFreeFeaturesStore(context) }

    var homePlace by remember { mutableStateOf(localStore.getHomePlace()) }
    var workPlace by remember { mutableStateOf(localStore.getWorkPlace()) }
    var favoriteList by remember { mutableStateOf(localStore.getFavoritePlaces()) }
    var customFavName by remember { mutableStateOf("") }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    fun getCurrentLatLon(): Pair<Double, Double> {
        val loc = getQuickLastKnownLocation(context)
        return if (loc != null) Pair(loc.latitude, loc.longitude) else Pair(31.5204, 74.3587)
    }

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("saved_places_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            FeatureSubScreenTopBar(
                title = "Saved Places",
                accentColor = Color(0xFFEC4899),
                isLightTheme = isLightTheme,
                onBack = onBack
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                // 🏠 HOME LOCATION CARD
                item {
                    HomeOrWorkLocationCard(
                        title = "Home Location",
                        icon = Icons.Default.Home,
                        accent = Color(0xFF0284C7),
                        savedItem = homePlace,
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        onSaveCurrentGps = {
                            val (lat, lon) = getCurrentLatLon()
                            localStore.setHomePlace(lat, lon, "Saved via GPS")
                            homePlace = localStore.getHomePlace()
                            Toast.makeText(context, "🏠 Home Location Saved!", Toast.LENGTH_SHORT).show()
                        },
                        onOpenMap = {
                            homePlace?.let { onOpenLocationOnMap(it.latitude, it.longitude, "Home") }
                        }
                    )
                }

                // 💼 WORK LOCATION CARD
                item {
                    HomeOrWorkLocationCard(
                        title = "Work Location",
                        icon = Icons.Default.Work,
                        accent = Color(0xFFF59E0B),
                        savedItem = workPlace,
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        onSaveCurrentGps = {
                            val (lat, lon) = getCurrentLatLon()
                            localStore.setWorkPlace(lat, lon, "Saved via GPS")
                            workPlace = localStore.getWorkPlace()
                            Toast.makeText(context, "💼 Work Location Saved!", Toast.LENGTH_SHORT).show()
                        },
                        onOpenMap = {
                            workPlace?.let { onOpenLocationOnMap(it.latitude, it.longitude, "Work") }
                        }
                    )
                }

                // ⭐ ADD CURRENT LOCATION TO FAVORITES
                item {
                    Surface(
                        color = cardBg,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "⭐ Save Favorite Place",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = primaryText
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = customFavName,
                                    onValueChange = { customFavName = it },
                                    placeholder = { Text("Place name (e.g. Gym, Market)", fontSize = 13.sp, color = secondaryText) },
                                    singleLine = true,
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = {
                                        val (lat, lon) = getCurrentLatLon()
                                        val label = customFavName.trim().ifEmpty { "Favorite Spot" }
                                        localStore.addFavoritePlace(label, lat, lon, "FAVORITE")
                                        favoriteList = localStore.getFavoritePlaces()
                                        customFavName = ""
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEC4899), contentColor = Color.White),
                                    modifier = Modifier.height(52.dp)
                                ) {
                                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Save", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                items(favoriteList, key = { it.id }) { fav ->
                    Surface(
                        color = cardBg,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenLocationOnMap(fav.latitude, fav.longitude, fav.title) }
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color(0xFFEC4899).copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFEC4899))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(fav.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = primaryText)
                                Text(
                                    "${String.format(Locale.US, "%.4f", fav.latitude)}°, ${String.format(Locale.US, "%.4f", fav.longitude)}°",
                                    fontSize = 12.sp,
                                    color = secondaryText
                                )
                            }
                            IconButton(
                                onClick = {
                                    localStore.removeFavoritePlace(fav.id)
                                    favoriteList = localStore.getFavoritePlaces()
                                }
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color(0xFFEF4444))
                            }
                        }
                    }
                }
            }

            // Sticky Bottom Adaptive Banner Ad on Saved Places Screen
            AdMobAdaptiveBanner(
                placementName = "Saved_Places_Bottom_Banner",
                adUnitId = AdMobConfig.BANNER_SAVED_PLACES_AD_UNIT_ID,
                isLightTheme = isLightTheme
            )
        }
    }
}

@Composable
private fun HomeOrWorkLocationCard(
    title: String,
    icon: ImageVector,
    accent: Color,
    savedItem: SavedPlaceItem?,
    cardBg: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color,
    onSaveCurrentGps: () -> Unit,
    onOpenMap: () -> Unit
) {
    Surface(
        color = cardBg,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(accent.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = accent, modifier = Modifier.size(26.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = primaryText)
                if (savedItem != null) {
                    Text(
                        text = "${String.format(Locale.US, "%.4f", savedItem.latitude)}°, ${String.format(Locale.US, "%.4f", savedItem.longitude)}°",
                        fontSize = 12.sp,
                        color = accent,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text("Not set yet", fontSize = 12.sp, color = secondaryText)
                }
            }
            if (savedItem != null) {
                TextButton(onClick = onOpenMap) {
                    Text("Map", color = accent, fontWeight = FontWeight.Bold)
                }
            }
            Button(
                onClick = onSaveCurrentGps,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accent, contentColor = Color.White)
            ) {
                Text(if (savedItem == null) "Set GPS" else "Update", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

/**
 * ============================================================================
 * FEATURE 6: SAVED TRIP HISTORY & TRIP STATISTICS SCREEN
 * ============================================================================
 */
@Composable
fun TripHistoryScreen(
    isLightTheme: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val localStore = remember { LocalFreeFeaturesStore(context) }
    var trips by remember { mutableStateOf(localStore.getSavedTrips()) }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val totalSavedDistance = trips.sumOf { it.distanceKm }
    val allTimeMaxSpeed = trips.maxOfOrNull { it.maxSpeedKmh } ?: 0f

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("trip_history_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            FeatureSubScreenTopBar(
                title = "Trip History",
                accentColor = Color(0xFF0EA5E9),
                isLightTheme = isLightTheme,
                onBack = onBack,
                trailingContent = {
                    if (trips.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                localStore.clearAllTrips()
                                trips = emptyList()
                            }
                        ) {
                            Text("Clear", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )

            Column(modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)) {
                // Summary Trip Statistics Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SimpleStatBox(
                        title = "Total Trips",
                        value = "${trips.size}",
                        accent = Color(0xFF0EA5E9),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Total Distance",
                        value = "${String.format(Locale.US, "%.1f", totalSavedDistance)} km",
                        accent = Color(0xFF22C55E),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Top Speed",
                        value = "${allTimeMaxSpeed.roundToInt()} km/h",
                        accent = Color(0xFFEF4444),
                        cardBg = cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (trips.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No saved trips yet.\nTap 'Save Trip' in Speedometer to save!",
                            color = secondaryText,
                            textAlign = TextAlign.Center,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    val dateFormat = remember { SimpleDateFormat("MMM d, hh:mm a", Locale.getDefault()) }
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(trips, key = { it.id }) { trip ->
                            Surface(
                                color = cardBg,
                                shape = RoundedCornerShape(18.dp),
                                border = BorderStroke(1.dp, borderColor),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${String.format(Locale.US, "%.2f", trip.distanceKm)} km • ${formatDurationHms(trip.durationSec)}",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = primaryText
                                        )
                                        Text(
                                            text = "Avg: ${trip.avgSpeedKmh.roundToInt()} km/h • Max: ${trip.maxSpeedKmh.roundToInt()} km/h",
                                            fontSize = 12.sp,
                                            color = Color(0xFF22C55E),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = "${trip.startRoadOrCoords} • ${dateFormat.format(Date(trip.startTimeMs))}",
                                            fontSize = 11.sp,
                                            color = secondaryText
                                        )
                                    }
                                    IconButton(
                                        onClick = {
                                            localStore.deleteSavedTrip(trip.id)
                                            trips = localStore.getSavedTrips()
                                        }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFEF4444))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Sticky Bottom Adaptive Banner Ad on Trip History Screen
            AdMobAdaptiveBanner(
                placementName = "Trip_History_Bottom_Banner",
                adUnitId = AdMobConfig.BANNER_TRIP_HISTORY_AD_UNIT_ID,
                isLightTheme = isLightTheme
            )
        }
    }
}

/**
 * ============================================================================
 * FEATURE 7: CLEAN VISUAL NEARBY PLACES GRID
 * ============================================================================
 */
data class NearbyPlaceCategory(
    val id: String,
    val title: String,
    val queryKeyword: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun NearbyPlacesScreen(
    isLightTheme: Boolean,
    onBack: () -> Unit,
    onOpenInAppMap: (String) -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    var customNearbyQuery by remember { mutableStateOf("") }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val categories = remember {
        listOf(
            NearbyPlaceCategory("mosque", "Mosques", "Mosque Masjid near me", Icons.Default.Mosque, Color(0xFF10B981)),
            NearbyPlaceCategory("hospital", "Hospitals", "Hospital Clinic near me", Icons.Default.LocalHospital, Color(0xFFEF4444)),
            NearbyPlaceCategory("pharmacy", "Pharmacy", "Pharmacy Medical Store near me", Icons.Default.LocalPharmacy, Color(0xFF06B6D4)),
            NearbyPlaceCategory("atm", "ATM & Bank", "ATM Bank near me", Icons.Default.Atm, Color(0xFF0284C7)),
            NearbyPlaceCategory("fuel", "Fuel Pump", "Petrol pump gas station near me", Icons.Default.LocalGasStation, Color(0xFFF59E0B)),
            NearbyPlaceCategory("ev_charge", "EV Charging", "EV charging station near me", Icons.Default.EvStation, Color(0xFF22C55E)),
            NearbyPlaceCategory("restaurant", "Restaurants", "Restaurant Halal food near me", Icons.Default.Restaurant, Color(0xFFEC4899)),
            NearbyPlaceCategory("fast_food", "Fast Food", "Burger Pizza Fast Food near me", Icons.Default.Restaurant, Color(0xFFF97316)),
            NearbyPlaceCategory("cafe", "Coffee & Tea", "Cafe Tea Chai near me", Icons.Default.LocalCafe, Color(0xFF8B5CF6)),
            NearbyPlaceCategory("bakery", "Bakery", "Bakery Sweet shop near me", Icons.Default.Storefront, Color(0xFFD97706)),
            NearbyPlaceCategory("hotel", "Hotels", "Hotel Guest House near me", Icons.Default.Hotel, Color(0xFF6366F1)),
            NearbyPlaceCategory("grocery", "Supermarket", "Supermarket Grocery store near me", Icons.Default.ShoppingCart, Color(0xFF16A34A)),
            NearbyPlaceCategory("mall", "Shopping Mall", "Shopping Mall Market near me", Icons.Default.Storefront, Color(0xFFE11D48)),
            NearbyPlaceCategory("electronics", "Mobile & Tech", "Mobile repair Electronics shop near me", Icons.Default.Build, Color(0xFF0EA5E9)),
            NearbyPlaceCategory("car_repair", "Mechanic", "Car mechanic workshop tire shop near me", Icons.Default.Build, Color(0xFF64748B)),
            NearbyPlaceCategory("car_wash", "Car Wash", "Car wash service station near me", Icons.Default.DirectionsCar, Color(0xFF0284C7)),
            NearbyPlaceCategory("parking", "Parking", "Car Parking near me", Icons.Default.LocalParking, Color(0xFF475569)),
            NearbyPlaceCategory("school", "Schools", "School College University near me", Icons.Default.School, Color(0xFF14B8A6)),
            NearbyPlaceCategory("library", "Library", "Public Library Bookstore near me", Icons.Default.LocalLibrary, Color(0xFF7C3AED)),
            NearbyPlaceCategory("gym", "Gym & Fitness", "Gym Fitness Center near me", Icons.Default.FitnessCenter, Color(0xFFEF4444)),
            NearbyPlaceCategory("stadium", "Sports Ground", "Cricket Football Stadium Sports complex near me", Icons.Default.SportsSoccer, Color(0xFF22C55E)),
            NearbyPlaceCategory("park", "Parks", "Family Park Garden near me", Icons.Default.Park, Color(0xFF15803D)),
            NearbyPlaceCategory("police", "Police", "Police station near me", Icons.Default.LocalPolice, Color(0xFF2563EB)),
            NearbyPlaceCategory("post_office", "Post & Courier", "Post office TCS Courier near me", Icons.Default.LocalPostOffice, Color(0xFFF59E0B)),
            NearbyPlaceCategory("dentist", "Dentist", "Dental clinic Dentist near me", Icons.Default.LocalHospital, Color(0xFF06B6D4)),
            NearbyPlaceCategory("vet", "Pet & Vet", "Veterinary clinic Pet shop near me", Icons.Default.Pets, Color(0xFFEC4899)),
            NearbyPlaceCategory("salon", "Salon & Barber", "Barber shop Beauty salon Spa near me", Icons.Default.Spa, Color(0xFF9333EA)),
            NearbyPlaceCategory("cinema", "Cinema", "Movie theater Cinema near me", Icons.Default.Movie, Color(0xFFE11D48)),
            NearbyPlaceCategory("museum", "Tourist Spots", "Museum Historical Landmark Tourist attraction near me", Icons.Default.Museum, Color(0xFFD97706)),
            NearbyPlaceCategory("transit", "Bus & Train", "Bus stand Railway Train station near me", Icons.Default.Train, Color(0xFF0891B2)),
            NearbyPlaceCategory("airport", "Airports", "Airport near me", Icons.Default.AirplanemodeActive, Color(0xFF0284C7))
        )
    }

    fun launchNearbySearch(query: String) {
        try {
            val loc = getQuickLastKnownLocation(context)
            val geoUri = if (loc != null) {
                Uri.parse("geo:${loc.latitude},${loc.longitude}?q=${Uri.encode(query)}")
            } else {
                Uri.parse("geo:0,0?q=${Uri.encode(query)}")
            }
            val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
            context.startActivity(mapIntent)
        } catch (_: Exception) {
            try {
                val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(query)}")
                context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
            } catch (_: Exception) {
                onOpenInAppMap(query)
            }
        }
    }

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("nearby_places_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            FeatureSubScreenTopBar(
                title = "Nearby Places",
                accentColor = Color(0xFF8B5CF6),
                isLightTheme = isLightTheme,
                onBack = onBack,
                trailingContent = {
                    GpsAndBatteryStatusPill(isLightTheme = isLightTheme)
                }
            )

            // Custom Search Bar for Any Place Near Me
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customNearbyQuery,
                    onValueChange = { customNearbyQuery = it },
                    placeholder = { Text("Search anything near me...", fontSize = 13.sp, color = secondaryText) },
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF8B5CF6)) },
                    trailingIcon = {
                        if (customNearbyQuery.isNotEmpty()) {
                            IconButton(onClick = { customNearbyQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = secondaryText)
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (customNearbyQuery.isNotBlank()) {
                                launchNearbySearch("${customNearbyQuery.trim()} near me")
                                focusManager.clearFocus()
                            }
                        }
                    ),
                    shape = RoundedCornerShape(50),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = primaryText,
                        unfocusedTextColor = primaryText,
                        focusedContainerColor = cardBg,
                        unfocusedContainerColor = cardBg,
                        focusedBorderColor = Color(0xFF8B5CF6),
                        unfocusedBorderColor = borderColor
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 104.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(categories.take(6), key = { it.id }) { cat ->
                    Surface(
                        color = cardBg,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { launchNearbySearch(cat.queryKeyword) }
                            .testTag("nearby_cat_${cat.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(cat.color.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(cat.icon, contentDescription = cat.title, tint = cat.color, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = cat.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Native Ad Card inside Nearby Places List
                item(span = { GridItemSpan(maxLineSpan) }) {
                    AdMobNativeAdCard(
                        placementName = "Nearby_Places_Feed_1",
                        adUnitId = AdMobConfig.NATIVE_NEARBY_PLACES_AD_UNIT_ID,
                        isLightTheme = isLightTheme
                    )
                }

                items(categories.drop(6), key = { it.id }) { cat ->
                    Surface(
                        color = cardBg,
                        shape = RoundedCornerShape(18.dp),
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { launchNearbySearch(cat.queryKeyword) }
                            .testTag("nearby_cat_${cat.id}")
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 16.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(cat.color.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(cat.icon, contentDescription = cat.title, tint = cat.color, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = cat.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * ============================================================================
 * FEATURE 8: FULL LIVE SHARE LOCATION SCREEN
 * Automatically fetches & displays:
 * - Current Street / Area Location
 * - City
 * - Province / State
 * - Country
 * - Latitude & Longitude + Altitude + Accuracy
 * With 100% responsive full-width buttons that never cut or wrap text awkwardly.
 * ============================================================================
 */
private data class DetailedAddressInfo(
    val streetOrArea: String,
    val city: String,
    val provinceOrState: String,
    val country: String
)

@SuppressLint("MissingPermission")
@Composable
fun ShareLocationScreen(
    isLightTheme: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentLat by remember { mutableDoubleStateOf(31.5204) }
    var currentLon by remember { mutableDoubleStateOf(74.3587) }
    var currentAltMeters by remember { mutableDoubleStateOf(217.0) }
    var currentAccMeters by remember { mutableFloatStateOf(5.0f) }

    var streetOrArea by remember { mutableStateOf("Fetching current area...") }
    var cityName by remember { mutableStateOf("Lahore") }
    var provinceName by remember { mutableStateOf("Punjab") }
    var countryName by remember { mutableStateOf("Pakistan") }
    var isFetchingGps by remember { mutableStateOf(false) }

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    fun updateAddressFromCoords(lat: Double, lon: Double) {
        coroutineScope.launch {
            val info = fetchDetailedAddress(context, lat, lon)
            streetOrArea = info.streetOrArea
            cityName = info.city
            provinceName = info.provinceOrState
            countryName = info.country
            isFetchingGps = false
        }
    }

    val locPermLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        hasLocationPermission = granted
        if (granted) {
            isFetchingGps = true
            val loc = getQuickLastKnownLocation(context)
            if (loc != null) {
                currentLat = loc.latitude
                currentLon = loc.longitude
                if (loc.hasAltitude()) currentAltMeters = loc.altitude
                if (loc.hasAccuracy()) currentAccMeters = loc.accuracy
                updateAddressFromCoords(loc.latitude, loc.longitude)
            } else {
                updateAddressFromCoords(currentLat, currentLon)
            }
        }
    }

    // Live GPS Listener so coordinates & address stay fresh automatically
    DisposableEffect(hasLocationPermission) {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val initialLoc = getQuickLastKnownLocation(context)
        if (initialLoc != null) {
            currentLat = initialLoc.latitude
            currentLon = initialLoc.longitude
            if (initialLoc.hasAltitude()) currentAltMeters = initialLoc.altitude
            if (initialLoc.hasAccuracy()) currentAccMeters = initialLoc.accuracy
        }
        updateAddressFromCoords(currentLat, currentLon)

        if (!hasLocationPermission || lm == null) {
            onDispose { }
        } else {
            val listener = object : LocationListener {
                override fun onLocationChanged(loc: Location) {
                    currentLat = loc.latitude
                    currentLon = loc.longitude
                    if (loc.hasAltitude()) currentAltMeters = loc.altitude
                    if (loc.hasAccuracy()) currentAccMeters = loc.accuracy
                    updateAddressFromCoords(loc.latitude, loc.longitude)
                }
                @Deprecated("Deprecated in Java")
                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }
            try {
                if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000L, 5f, listener, Looper.getMainLooper())
                }
                if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000L, 5f, listener, Looper.getMainLooper())
                }
            } catch (_: Exception) {}
            onDispose {
                try { lm.removeUpdates(listener) } catch (_: Exception) {}
            }
        }
    }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val latFormatted = String.format(Locale.US, "%.5f", currentLat)
    val lonFormatted = String.format(Locale.US, "%.5f", currentLon)
    val mapsUrl = "https://maps.google.com/?q=$latFormatted,$lonFormatted"
    val fullShareMessage = buildString {
        appendLine("📍 My Current Location:")
        appendLine("Area: $streetOrArea")
        appendLine("City: $cityName")
        appendLine("Province/State: $provinceName")
        appendLine("Country: $countryName")
        appendLine("Coordinates: $latFormatted°, $lonFormatted°")
        append("Google Maps: $mapsUrl")
    }

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("share_location_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            FeatureSubScreenTopBar(
                title = "Share Location",
                accentColor = Color(0xFF06B6D4),
                isLightTheme = isLightTheme,
                onBack = onBack,
                trailingContent = {
                    GpsAndBatteryStatusPill(isLightTheme = isLightTheme)
                }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!hasLocationPermission) {
                    Button(
                        onClick = {
                            locPermLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B), contentColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .widthIn(max = 480.dp)
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text("Tap to Allow GPS Location", fontWeight = FontWeight.Bold, maxLines = 1)
                    }
                }

                // Main Location Card
                Surface(
                    color = cardBg,
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier
                        .widthIn(max = 480.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Top Pin Icon & Current Location Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .background(Color(0xFF06B6D4).copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF06B6D4),
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "CURRENT LOCATION",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF06B6D4)
                                )
                                Text(
                                    text = streetOrArea,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = primaryText,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "$cityName, $provinceName, $countryName",
                                    fontSize = 13.sp,
                                    color = secondaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 2x2 Responsive Grid: City, Province/State, Country, Altitude
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ShareDetailInfoTile(
                                label = "City",
                                value = cityName,
                                accentColor = Color(0xFF10B981),
                                isLightTheme = isLightTheme,
                                primaryText = primaryText,
                                secondaryText = secondaryText,
                                modifier = Modifier.weight(1f)
                            )
                            ShareDetailInfoTile(
                                label = "Province / State",
                                value = provinceName,
                                accentColor = Color(0xFF8B5CF6),
                                isLightTheme = isLightTheme,
                                primaryText = primaryText,
                                secondaryText = secondaryText,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ShareDetailInfoTile(
                                label = "Country",
                                value = countryName,
                                accentColor = Color(0xFF0284C7),
                                isLightTheme = isLightTheme,
                                primaryText = primaryText,
                                secondaryText = secondaryText,
                                modifier = Modifier.weight(1f)
                            )
                            ShareDetailInfoTile(
                                label = "Altitude & GPS",
                                value = "${currentAltMeters.roundToInt()} m (±${currentAccMeters.roundToInt()}m)",
                                accentColor = Color(0xFFF59E0B),
                                isLightTheme = isLightTheme,
                                primaryText = primaryText,
                                secondaryText = secondaryText,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Latitude & Longitude 2-Column Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ShareDetailInfoTile(
                                label = "Latitude",
                                value = "$latFormatted°",
                                accentColor = Color(0xFF22C55E),
                                isLightTheme = isLightTheme,
                                primaryText = primaryText,
                                secondaryText = secondaryText,
                                modifier = Modifier.weight(1f)
                            )
                            ShareDetailInfoTile(
                                label = "Longitude",
                                value = "$lonFormatted°",
                                accentColor = Color(0xFF06B6D4),
                                isLightTheme = isLightTheme,
                                primaryText = primaryText,
                                secondaryText = secondaryText,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Primary Full-Width Share Button (Clean, non-wrapping text)
                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, fullShareMessage)
                                }
                                context.startActivity(Intent.createChooser(intent, "Share Location"))
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E), contentColor = Color.White),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("share_location_now_button")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Share Full Location",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Secondary Action Row: Copy Details + Refresh GPS (Responsive single-line text)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = {
                                    val cb = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                    cb?.setPrimaryClip(ClipData.newPlainText("GPS Location", fullShareMessage))
                                    Toast.makeText(context, "Full Location & Link Copied!", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7), contentColor = Color.White),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("copy_location_button")
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Copy Info",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Button(
                                onClick = {
                                    isFetchingGps = true
                                    val loc = getQuickLastKnownLocation(context)
                                    if (loc != null) {
                                        currentLat = loc.latitude
                                        currentLon = loc.longitude
                                        if (loc.hasAltitude()) currentAltMeters = loc.altitude
                                        if (loc.hasAccuracy()) currentAccMeters = loc.accuracy
                                        updateAddressFromCoords(loc.latitude, loc.longitude)
                                        Toast.makeText(context, "Location Updated!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        locPermLauncher.launch(
                                            arrayOf(
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION
                                            )
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF06B6D4), contentColor = Color.White),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("refresh_gps_location_button")
                            ) {
                                Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isFetchingGps) "Updating..." else "Update GPS",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Sticky Bottom Adaptive Banner Ad on Share Location Screen
            AdMobAdaptiveBanner(
                placementName = "Share_Location_Bottom_Banner",
                adUnitId = AdMobConfig.BANNER_SHARE_LOC_AD_UNIT_ID,
                isLightTheme = isLightTheme
            )
        }
    }
}

@Composable
private fun ShareDetailInfoTile(
    label: String,
    value: String,
    accentColor: Color,
    isLightTheme: Boolean,
    primaryText: Color,
    secondaryText: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF172642),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = primaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Suppress("DEPRECATION")
private suspend fun fetchDetailedAddress(context: Context, lat: Double, lon: Double): DetailedAddressInfo {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val list = geocoder.getFromLocation(lat, lon, 1)
            val addr = list?.firstOrNull()
            if (addr != null) {
                val street = addr.thoroughfare ?: addr.subLocality ?: addr.featureName ?: addr.locality ?: "Live GPS Pin"
                val city = addr.locality ?: addr.subAdminArea ?: addr.subLocality ?: "Unknown City"
                val province = addr.adminArea ?: addr.subAdminArea ?: "Unknown Province"
                val country = addr.countryName ?: "Unknown Country"
                DetailedAddressInfo(
                    streetOrArea = street,
                    city = city,
                    provinceOrState = province,
                    country = country
                )
            } else {
                fallbackAddressFromWorldData(lat, lon)
            }
        } catch (_: Exception) {
            fallbackAddressFromWorldData(lat, lon)
        }
    }
}

private fun fallbackAddressFromWorldData(lat: Double, lon: Double): DetailedAddressInfo {
    val nearestCountry = WorldGeographicData.countries.minByOrNull { c ->
        val dLat = c.centerLat - lat
        val dLon = c.centerLon - lon
        dLat * dLat + dLon * dLon
    }
    return DetailedAddressInfo(
        streetOrArea = "${String.format(Locale.US, "%.4f", lat)}°, ${String.format(Locale.US, "%.4f", lon)}°",
        city = nearestCountry?.capital ?: "Local City",
        provinceOrState = nearestCountry?.region ?: "Region",
        country = nearestCountry?.name ?: "Earth"
    )
}

/**
 * ============================================================================
 * FEATURE 9: ALTIMETER, GPS ACCURACY, ROAD POSITION & LOCK-SCREEN GPS HUD MODE
 * Includes "🔒 Screen par GPS information" full-screen lockable HUD!
 * ============================================================================
 */
@Composable
fun AltitudeAndGpsInfoScreen(
    isLightTheme: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val context = LocalContext.current
    val loc = remember { getQuickLastKnownLocation(context) }

    val lat = loc?.latitude ?: 33.6844
    val lon = loc?.longitude ?: 73.0479
    val altMeters = loc?.altitude ?: 540.0
    val altFeet = altMeters * 3.28084
    val accuracyM = loc?.accuracy ?: 4.5f
    var isHudLocked by remember { mutableStateOf(false) }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    Surface(
        color = if (isHudLocked) Color(0xFF020617) else pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("altitude_gps_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            if (!isHudLocked) {
                FeatureSubScreenTopBar(
                    title = "GPS & Altimeter",
                    accentColor = Color(0xFF14B8A6),
                    isLightTheme = isLightTheme,
                    onBack = onBack,
                    trailingContent = {
                        GpsAndBatteryStatusPill(isLightTheme = isLightTheme)
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (isHudLocked) {
                    Text(
                        text = "🔒 GPS SCREEN LOCKED",
                        color = Color(0xFFF59E0B),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Surface(
                    color = if (isHudLocked) Color(0xFF0F172A) else cardBg,
                    shape = RoundedCornerShape(26.dp),
                    border = BorderStroke(1.dp, if (isHudLocked) Color(0xFF14B8A6) else borderColor),
                    modifier = Modifier
                        .widthIn(max = 440.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${altMeters.roundToInt()} m",
                            fontSize = 56.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF14B8A6)
                        )
                        Text(
                            text = "${altFeet.roundToInt()} Feet Above Sea Level",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isHudLocked) Color.White else secondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.widthIn(max = 440.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SimpleStatBox(
                        title = "Latitude",
                        value = "${String.format(Locale.US, "%.4f", lat)}°",
                        accent = Color(0xFF22C55E),
                        cardBg = if (isHudLocked) Color(0xFF0F172A) else cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Longitude",
                        value = "${String.format(Locale.US, "%.4f", lon)}°",
                        accent = Color(0xFF0284C7),
                        cardBg = if (isHudLocked) Color(0xFF0F172A) else cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    SimpleStatBox(
                        title = "Accuracy",
                        value = "±${accuracyM.roundToInt()} m",
                        accent = Color(0xFFF59E0B),
                        cardBg = if (isHudLocked) Color(0xFF0F172A) else cardBg,
                        borderColor = borderColor,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Lock / Unlock GPS Info on Screen Button
                Button(
                    onClick = { isHudLocked = !isHudLocked },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isHudLocked) Color(0xFFF59E0B) else Color(0xFF14B8A6),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .widthIn(max = 440.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("lock_gps_hud_button")
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isHudLocked) "Tap to Unlock Screen" else "Lock GPS Info on Screen",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * ============================================================================
 * FEATURE 10: CLEAN VISUAL WORLD COUNTRIES LIST
 * ============================================================================
 */
@Composable
fun WorldCountryAtlasScreen(
    viewModel: GlobeViewModel,
    isLightTheme: Boolean,
    onBack: () -> Unit,
    onOpenOn3DGlobe: (CountryInfo) -> Unit
) {
    var filterQuery by remember { mutableStateOf("") }
    var selectedCountryForDetails by remember { mutableStateOf<CountryInfo?>(null) }

    BackHandler {
        if (selectedCountryForDetails != null) {
            selectedCountryForDetails = null
        } else {
            onBack()
        }
    }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val filteredCountries = remember(filterQuery) {
        val all = WorldGeographicData.countries.sortedBy { it.name }
        if (filterQuery.isBlank()) all
        else all.filter {
            it.name.contains(filterQuery, ignoreCase = true) ||
                    it.capital.contains(filterQuery, ignoreCase = true)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            color = pageBg,
            modifier = Modifier
                .fillMaxSize()
                .testTag("country_atlas_screen")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .windowInsetsPadding(WindowInsets.navigationBars)
            ) {
                FeatureSubScreenTopBar(
                    title = "World Countries",
                    accentColor = Color(0xFF6366F1),
                    isLightTheme = isLightTheme,
                    onBack = onBack
                )

                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    OutlinedTextField(
                        value = filterQuery,
                        onValueChange = { filterQuery = it },
                        placeholder = { Text("Search country...", fontSize = 14.sp, color = secondaryText) },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = secondaryText) },
                        shape = RoundedCornerShape(50),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = primaryText,
                            unfocusedTextColor = primaryText,
                            focusedBorderColor = Color(0xFF6366F1),
                            unfocusedBorderColor = borderColor
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    item {
                        AdMobNativeAdCard(
                            placementName = "Country_Atlas_Feed",
                            adUnitId = AdMobConfig.NATIVE_COUNTRY_ATLAS_AD_UNIT_ID,
                            isLightTheme = isLightTheme
                        )
                    }

                    items(filteredCountries, key = { it.id }) { country ->
                        Surface(
                            color = cardBg,
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, borderColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedCountryForDetails = country }
                                .testTag("atlas_country_${country.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = country.flagEmoji, fontSize = 32.sp)
                                Spacer(modifier = Modifier.width(14.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = country.name,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryText
                                    )
                                    Text(
                                        text = country.capital,
                                        fontSize = 13.sp,
                                        color = secondaryText
                                    )
                                }
                                IconButton(
                                    onClick = { onOpenOn3DGlobe(country) }
                                ) {
                                    Icon(Icons.Default.Public, contentDescription = "Open in 3D", tint = Color(0xFF0284C7))
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }

        val activeDetailCountry = selectedCountryForDetails
        if (activeDetailCountry != null) {
            LocationDetailSheet(
                country = activeDetailCountry,
                customLabel = activeDetailCountry.name,
                isLightTheme = isLightTheme,
                onDismiss = { selectedCountryForDetails = null },
                onOpenDirections = {
                    selectedCountryForDetails = null
                    onOpenOn3DGlobe(activeDetailCountry)
                    viewModel.openDirectionDialog()
                },
                onTrackAsVisited = { note ->
                    viewModel.recordInHistory(country = activeDetailCountry, type = "VISITED", notes = note)
                }
            )
        }
    }
}

private fun getCardinalDirectionShort(deg: Float): String {
    val dirs = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val idx = (((deg + 22.5f) % 360f + 360f) % 360f / 45f).toInt() % 8
    return dirs[idx]
}

private fun formatDurationHms(totalSec: Long): String {
    val hrs = totalSec / 3600
    val mins = (totalSec % 3600) / 60
    val secs = totalSec % 60
    return if (hrs > 0) {
        String.format(Locale.US, "%d:%02d:%02d", hrs, mins, secs)
    } else {
        String.format(Locale.US, "%02d:%02d", mins, secs)
    }
}

@SuppressLint("MissingPermission")
private fun triggerSpeedVibration(context: Context) {
    try {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vm?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(350L, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(350L)
            }
        }
    } catch (_: Exception) {}
}

private fun triggerSpeedAlertBeep() {
    try {
        val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 85)
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2, 280)
    } catch (_: Exception) {}
}

@Suppress("DEPRECATION")
private suspend fun resolveRoadOrPlaceName(context: Context, lat: Double, lon: Double): String {
    return withContext(Dispatchers.IO) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val list = geocoder.getFromLocation(lat, lon, 1)
            val addr = list?.firstOrNull()
            if (addr != null) {
                val road = addr.thoroughfare ?: addr.subLocality ?: addr.locality ?: addr.countryName
                val city = addr.locality ?: addr.adminArea ?: ""
                if (!road.isNullOrBlank() && !city.isNullOrBlank() && road != city) {
                    "$road, $city"
                } else {
                    road ?: "${String.format(Locale.US, "%.4f", lat)}°, ${String.format(Locale.US, "%.4f", lon)}°"
                }
            } else {
                "${String.format(Locale.US, "%.4f", lat)}°, ${String.format(Locale.US, "%.4f", lon)}°"
            }
        } catch (_: Exception) {
            "${String.format(Locale.US, "%.4f", lat)}°, ${String.format(Locale.US, "%.4f", lon)}°"
        }
    }
}

@SuppressLint("MissingPermission")
private fun getQuickLastKnownLocation(context: Context): Location? {
    return try {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        if (!fine && !coarse) return null
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
        var best: Location? = null
        for (p in lm.getProviders(true)) {
            val l = lm.getLastKnownLocation(p) ?: continue
            if (best == null || l.accuracy < best.accuracy) best = l
        }
        best
    } catch (_: Exception) {
        null
    }
}
