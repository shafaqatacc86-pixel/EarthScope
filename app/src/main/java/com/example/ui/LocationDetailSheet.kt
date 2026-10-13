package com.example.ui

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.BuildConfig
import com.example.data.model.CountryInfo
import com.example.data.model.GeoPoint
import com.example.data.model.WorldGeographicData
import com.example.globe.GlobeMath
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
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LocationDetailSheet(
    country: CountryInfo?,
    routeStartCountry: CountryInfo? = null,
    routeEndCountry: CountryInfo? = null,
    customLabel: String,
    isLightTheme: Boolean = false,
    onDismiss: () -> Unit,
    onOpenDirections: () -> Unit = {},
    onTrackAsVisited: (note: String) -> Unit
) {
    BackHandler { onDismiss() }

    val activeCountry = country

    var isAddingNote by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }
    var markedVisited by remember { mutableStateOf(false) }
    val numberFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    val isRouteMode = routeStartCountry != null && routeEndCountry != null
    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val sheetBg = if (isLightTheme) Color.White else Color(0xFF0F172A)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF475569) else Color(0xFF94A3B8)
    val sectionTitleColor = if (isLightTheme) Color(0xFF1E293B) else Color(0xFFE2E8F0)
    val cardBg = if (isLightTheme) Color(0xFFF1F5F9) else Color(0xFF1E293B)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)

    Surface(
        color = pageBg,
        contentColor = primaryText,
        modifier = Modifier
            .fillMaxSize()
            .testTag("country_detail_next_page")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            // Top Bar for Dedicated Next Page (Country Details & Real Google Map)
            Surface(
                color = sheetBg,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF22C55E).copy(alpha = 0.15f), CircleShape)
                                .testTag("close_sheet_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Globe",
                                tint = Color(0xFF22C55E)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        val headerCountry = activeCountry
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when {
                                    isRouteMode && routeStartCountry != null && routeEndCountry != null ->
                                        "${routeStartCountry.flagEmoji} ${routeStartCountry.name} ✈ ${routeEndCountry.flagEmoji} ${routeEndCountry.name}"
                                    headerCountry != null -> "${headerCountry.flagEmoji} ${headerCountry.name}"
                                    else -> customLabel
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (headerCountry != null && !isRouteMode) {
                                Text(
                                    text = "${headerCountry.capital} • ${headerCountry.continent}",
                                    fontSize = 12.sp,
                                    color = secondaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            var isTouchingCountryMap by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState(), enabled = !isTouchingCountryMap)
            ) {
                Spacer(modifier = Modifier.height(12.dp))
            if (isRouteMode && routeStartCountry != null && routeEndCountry != null) {
                val capitalDistKm = remember(routeStartCountry, routeEndCountry) {
                    GlobeMath.exactCapitalDistanceKm(routeStartCountry, routeEndCountry)
                }
                val centerDistKm = remember(routeStartCountry, routeEndCountry) {
                    GlobeMath.exactCentroidDistanceKm(routeStartCountry, routeEndCountry)
                }
                val distMiles = capitalDistKm * 0.621371192
                val distNauticalMiles = capitalDistKm * 0.539956803
                val flightHoursTotal = capitalDistKm / 850.0
                val flightHrs = flightHoursTotal.toInt()
                val flightMins = ((flightHoursTotal - flightHrs) * 60).roundToInt()

                val bearingInfo = remember(routeStartCountry, routeEndCountry) {
                    calculateBearing(
                        routeStartCountry.capitalLat,
                        routeStartCountry.capitalLon,
                        routeEndCountry.capitalLat,
                        routeEndCountry.capitalLon
                    )
                }

                val tzDiff = routeEndCountry.timeZoneOffsetHours - routeStartCountry.timeZoneOffsetHours
                val tzDiffStr = when {
                    abs(tzDiff) < 0.01 -> "Same Timezone"
                    tzDiff > 0 -> "+${formatHours(tzDiff)}h"
                    else -> "${formatHours(tzDiff)}h"
                }

                // SIDE-BY-SIDE MINI COUNTRY MAP CARDS FOR BOTH ROUTE COUNTRIES
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CountryMiniMapBoxCard(
                        country = routeStartCountry,
                        roleLabel = "FROM • ${routeStartCountry.name.uppercase()}",
                        accentColor = Color(0xFF0284C7),
                        isLightTheme = isLightTheme,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("route_start_country_map_card")
                    )
                    CountryMiniMapBoxCard(
                        country = routeEndCountry,
                        roleLabel = "TO • ${routeEndCountry.name.uppercase()}",
                        accentColor = Color(0xFF22C55E),
                        isLightTheme = isLightTheme,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("route_end_country_map_card")
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Highlighted Exact Distance Banner Card
                Surface(
                    color = if (isLightTheme) Color(0xFFECFDF5) else Color(0xFF16243D),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.2.dp, Color(0xFF22C55E)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "FLIGHT DISTANCE",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isLightTheme) Color(0xFF15803D) else Color(0xFF86EFAC)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${String.format(Locale.US, "%,.1f", capitalDistKm)} km",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = primaryText
                                )
                                Text(
                                    text = "${String.format(Locale.US, "%,.1f", distMiles)} mi • ${String.format(Locale.US, "%,.1f", distNauticalMiles)} NM",
                                    fontSize = 12.sp,
                                    color = secondaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Surface(
                                color = if (isLightTheme) Color.White else Color(0xFF0F172A),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFF0284C7))
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "FLIGHT TIME",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0284C7)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${flightHrs}h ${flightMins}m",
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLightTheme) Color(0xFFD97706) else Color(0xFFFACC15)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Geographic Centroid Distance Row
                        Surface(
                            color = if (isLightTheme) Color.White.copy(alpha = 0.75f) else Color(0xFF0F172A).copy(alpha = 0.6f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Center-to-Center:",
                                    fontSize = 12.sp,
                                    color = secondaryText
                                )
                                Text(
                                    text = "${String.format(Locale.US, "%,.1f", centerDistKm)} km",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Route Telemetry Grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Heading",
                        value = bearingInfo,
                        isLightTheme = isLightTheme,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Time Diff",
                        value = tzDiffStr,
                        isLightTheme = isLightTheme,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "${routeStartCountry.capital} Coordinates",
                        value = String.format(Locale.US, "%.4f°, %.4f°", routeStartCountry.capitalLat, routeStartCountry.capitalLon),
                        isLightTheme = isLightTheme,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "${routeEndCountry.capital} Coordinates",
                        value = String.format(Locale.US, "%.4f°, %.4f°", routeEndCountry.capitalLat, routeEndCountry.capitalLon),
                        isLightTheme = isLightTheme,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Side-by-Side Country Comparison
                Text(
                    text = "Country Comparison",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = sectionTitleColor
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CountryComparisonColumn(
                        label = "STARTING COUNTRY",
                        country = routeStartCountry,
                        accentColor = Color(0xFF0284C7),
                        numberFormat = numberFormat,
                        isLightTheme = isLightTheme,
                        modifier = Modifier.weight(1f)
                    )
                    CountryComparisonColumn(
                        label = "DESTINATION",
                        country = routeEndCountry,
                        accentColor = Color(0xFF16A34A),
                        numberFormat = numberFormat,
                        isLightTheme = isLightTheme,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                // Single Country / Location Mode
                val currentCountry = activeCountry

                if (currentCountry != null) {
                    val nearbyCountries = remember(currentCountry.id) {
                        findNearbyCountriesForStructure(currentCountry, maxCount = 6)
                    }

                    // 1. REAL INTERACTIVE GOOGLE MAP FOR THE SELECTED COUNTRY ONLY
                    CountryInteractiveRealGoogleMapCard(
                        country = currentCountry,
                        isLightTheme = isLightTheme,
                        onOpenDirections = onOpenDirections,
                        onMapTouchActiveChanged = { isTouchingCountryMap = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("country_real_google_map_card")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. SELECTED COUNTRY & NEIGHBORING COUNTRIES STRUCTURE DIAGRAM
                    CountryStructureAndNeighborsCard(
                        country = currentCountry,
                        nearbyCountries = nearbyCountries,
                        isLightTheme = isLightTheme,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("single_country_map_card")
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        color = cardBg,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = currentCountry.description,
                            fontSize = 13.5.sp,
                            lineHeight = 19.sp,
                            color = if (isLightTheme) Color(0xFF334155) else Color(0xFFCBD5E1),
                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Country Details",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = sectionTitleColor
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val localTimeStr = remember(currentCountry.timeZoneOffsetHours) {
                        formatLocalTime(currentCountry.timeZoneOffsetHours)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Capital",
                            value = currentCountry.capital,
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Continent",
                            value = currentCountry.continent,
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Population",
                            value = numberFormat.format(currentCountry.population),
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Area",
                            value = "${numberFormat.format(currentCountry.areaKm2.toLong())} km²",
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Local Time",
                            value = localTimeStr,
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Currency",
                            value = currentCountry.currency,
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Coordinates",
                            value = String.format("%.2f°, %.2f°", currentCountry.centerLat, currentCountry.centerLon),
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "ISO Code",
                            value = currentCountry.id,
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Languages",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = secondaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        currentCountry.languages.forEach { lang ->
                            Surface(
                                color = cardBg,
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, if (isLightTheme) Color(0xFFCBD5E1) else Color(0xFF334155))
                            ) {
                                Text(
                                    text = lang,
                                    fontSize = 12.sp,
                                    color = Color(0xFF0284C7),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Action / Note Section
            if (isAddingNote) {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = { Text("Add note...", color = secondaryText) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("note_input_field"),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF22C55E),
                        unfocusedBorderColor = if (isLightTheme) Color(0xFFCBD5E1) else Color(0xFF334155),
                        focusedTextColor = primaryText,
                        unfocusedTextColor = primaryText
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        markedVisited = true
                        onTrackAsVisited(noteText)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("track_location_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (markedVisited) Color(0xFF16A34A) else Color(0xFF22C55E)
                    )
                ) {
                    Icon(
                        imageVector = if (markedVisited) Icons.Default.CheckCircle else Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (markedVisited) "Saved" else "Save to History",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Button(
                    onClick = { isAddingNote = !isAddingNote },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("add_note_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cardBg)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Note",
                        tint = Color(0xFF0284C7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Native Ad Card inside Country / Route Details Sheet
            AdMobNativeAdCard(
                placementName = "Country_Details_Native_Ad",
                adUnitId = AdMobConfig.NATIVE_COUNTRY_DETAILS_AD_UNIT_ID,
                isLightTheme = isLightTheme
            )

            Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

/**
 * Helper to return effective polygon boundary points for a country (uses real boundary if present,
 * or generates a realistic geographic territory polygon scaled by country areaKm2).
 */
private fun getCountryStructurePolygon(country: CountryInfo): List<GeoPoint> {
    if (country.boundary.size >= 3) {
        return country.boundary
    }
    val spanDeg = (sqrt(country.areaKm2.coerceAtLeast(2500.0)) / 220.0).coerceIn(0.8, 6.5)
    val cLat = country.centerLat
    val cLon = country.centerLon
    return listOf(
        GeoPoint(cLat + spanDeg * 0.85, cLon - spanDeg * 0.95),
        GeoPoint(cLat + spanDeg * 1.05, cLon - spanDeg * 0.15),
        GeoPoint(cLat + spanDeg * 0.78, cLon + spanDeg * 0.88),
        GeoPoint(cLat + spanDeg * 0.12, cLon + spanDeg * 1.12),
        GeoPoint(cLat - spanDeg * 0.75, cLon + spanDeg * 0.82),
        GeoPoint(cLat - spanDeg * 1.02, cLon + spanDeg * 0.08),
        GeoPoint(cLat - spanDeg * 0.80, cLon - spanDeg * 0.88),
        GeoPoint(cLat - spanDeg * 0.08, cLon - spanDeg * 1.10)
    )
}

/**
 * Finds the closest neighboring/surrounding countries to display their structure & borders
 * alongside the selected country.
 */
private fun findNearbyCountriesForStructure(country: CountryInfo, maxCount: Int = 6): List<CountryInfo> {
    return WorldGeographicData.countries
        .asSequence()
        .filter { it.id != country.id }
        .sortedBy { other ->
            GlobeMath.exactCentroidDistanceKm(country, other)
        }
        .take(maxCount)
        .toList()
}

private fun idealCountryZoomLevel(areaKm2: Double): Int {
    return when {
        areaKm2 > 7_000_000 -> 3
        areaKm2 > 1_500_000 -> 4
        areaKm2 > 300_000 -> 5
        areaKm2 > 60_000 -> 6
        else -> 7
    }
}

/**
 * ============================================================================
 * REAL INTERACTIVE GOOGLE MAP CARD FOR SELECTED COUNTRY + OTHER COUNTRIES STRUCTURE
 * ============================================================================
 */
@SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
@Composable
private fun CountryInteractiveRealGoogleMapCard(
    country: CountryInfo,
    isLightTheme: Boolean,
    onOpenDirections: () -> Unit,
    onMapTouchActiveChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val configuredMapsKey = BuildConfig.MAPS_API_KEY
    val hasValidGoogleKey = configuredMapsKey.isNotBlank() &&
            configuredMapsKey != "YOUR_GOOGLE_MAPS_API_KEY" &&
            configuredMapsKey != "DEFAULT_VALUE"

    val defaultZoom = remember(country.id) { idealCountryZoomLevel(country.areaKm2) }
    var targetLat by remember(country.id) { mutableDoubleStateOf(country.centerLat) }
    var targetLon by remember(country.id) { mutableDoubleStateOf(country.centerLon) }
    var currentZoom by remember(country.id) { mutableIntStateOf(defaultZoom) }
    var mapBearingDeg by remember(country.id) { mutableFloatStateOf(0f) }
    var mapLayerMode by remember { mutableIntStateOf(1) } // 0=Normal, 1=Satellite, 2=Terrain, 3=Black, 4=Green
    var showCountryStructures by remember { mutableStateOf(true) }

    val webViewRef = remember { mutableStateOf<WebView?>(null) }
    var isWebViewLoaded by remember { mutableStateOf(false) }

    val cameraPositionState = rememberCameraPositionState(country.id) {
        position = CameraPosition.Builder()
            .target(LatLng(country.centerLat, country.centerLon))
            .zoom(defaultZoom.toFloat())
            .bearing(0f)
            .tilt(0f)
            .build()
    }

    fun updateMapCamera(lat: Double, lon: Double, zoom: Int, bearing: Float) {
        targetLat = lat
        targetLon = lon
        currentZoom = zoom
        mapBearingDeg = ((bearing % 360f) + 360f) % 360f
        if (hasValidGoogleKey) {
            coroutineScope.launch {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                            .target(LatLng(lat, lon))
                            .zoom(zoom.toFloat())
                            .bearing(mapBearingDeg)
                            .tilt(0f)
                            .build()
                    ),
                    durationMs = 450
                )
            }
        } else {
            webViewRef.value?.evaluateJavascript(
                "if(window.flyCountryMap){ window.flyCountryMap($lat, $lon, $zoom, $mapBearingDeg); }",
                null
            )
        }
    }

    // Build JSON payload of ONLY the selected country highlight structure for WebView
    val structuresJson = remember(country.id, showCountryStructures) {
        buildCountryAndNeighborsJson(country, emptyList(), showCountryStructures)
    }

    LaunchedEffect(country.id, structuresJson, isWebViewLoaded) {
        if (!hasValidGoogleKey && isWebViewLoaded) {
            webViewRef.value?.evaluateJavascript(
                "if(window.renderCountryStructures){ window.renderCountryStructures($structuresJson); }",
                null
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onMapTouchActiveChanged(false)
            webViewRef.value?.destroy()
        }
    }

    val cardBg = if (isLightTheme) Color.White else Color(0xFF142238)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White

    val layerName = when (mapLayerMode) {
        0 -> "Normal"
        1 -> "Satellite"
        2 -> "Terrain"
        3 -> "Black"
        else -> "Green"
    }
    val layerChipColor = when (mapLayerMode) {
        0 -> Color(0xFF0284C7)
        1 -> Color(0xFF0EA5E9)
        2 -> Color(0xFFF59E0B)
        3 -> Color(0xFF818CF8)
        else -> Color(0xFF10B981)
    }

    Surface(
        color = cardBg,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, Color(0xFF22C55E).copy(alpha = 0.7f)),
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Card Header: Title + Map Layer Switch
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF22C55E).copy(alpha = 0.16f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = null,
                            tint = Color(0xFF22C55E),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${country.flagEmoji} ${country.name} Map",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Layer Switch Chip (Satellite / Terrain / Black / Green / Normal)
                Surface(
                    color = layerChipColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, layerChipColor),
                    modifier = Modifier
                        .clickable {
                            mapLayerMode = (mapLayerMode + 1) % 5
                            if (!hasValidGoogleKey) {
                                webViewRef.value?.evaluateJavascript(
                                    "if(window.setCountryMapLayer){ window.setCountryMapLayer($mapLayerMode); }",
                                    null
                                )
                            }
                        }
                        .testTag("country_detail_map_layer_switch")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Layers,
                            contentDescription = "Switch Map Layer",
                            tint = layerChipColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = layerName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = layerChipColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Focus & Structure Filter Bar above Map
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFF22C55E).copy(alpha = 0.16f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFF22C55E)),
                    modifier = Modifier
                        .clickable {
                            updateMapCamera(country.centerLat, country.centerLon, defaultZoom, 0f)
                        }
                        .testTag("country_map_focus_territory")
                ) {
                    Text(
                        text = "${country.flagEmoji} ${country.name}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF22C55E),
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Surface(
                    color = Color(0xFFF59E0B).copy(alpha = 0.16f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                    modifier = Modifier
                        .clickable {
                            updateMapCamera(country.capitalLat, country.capitalLon, 11, mapBearingDeg)
                        }
                        .testTag("country_map_focus_capital")
                ) {
                    Text(
                        text = "★ ${country.capital}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF59E0B),
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                Surface(
                    color = if (showCountryStructures) Color(0xFF0284C7) else Color(0xFF0284C7).copy(alpha = 0.14f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFF0284C7)),
                    modifier = Modifier
                        .clickable {
                            showCountryStructures = !showCountryStructures
                        }
                        .testTag("country_map_toggle_structures")
                ) {
                    Text(
                        text = if (showCountryStructures) "Border On" else "Border Off",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (showCountryStructures) Color.White else Color(0xFF0284C7),
                        maxLines = 1,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Clean, Unobstructed Interactive Map Box (360.dp height)
            // Hides all floating button icons on top of the map and locks parent verticalScroll
            // while touching the map so Google Map moves 100% freely in all directions without hanging!
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
                    .pointerInput(country.id) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                val anyPressed = event.changes.any { it.pressed }
                                onMapTouchActiveChanged(anyPressed)
                            }
                        }
                    }
            ) {
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

                    val mainPolygonPoints = remember(country.id) {
                        getCountryStructurePolygon(country).map { LatLng(it.lat, it.lon) }
                    }
                    val capitalMarkerState = com.google.maps.android.compose.rememberMarkerState(
                        key = "country_capital_${country.id}",
                        position = LatLng(country.capitalLat, country.capitalLon)
                    )
                    LaunchedEffect(country.id, country.capitalLat, country.capitalLon) {
                        capitalMarkerState.position = LatLng(country.capitalLat, country.capitalLon)
                        cameraPositionState.move(
                            CameraUpdateFactory.newCameraPosition(
                                CameraPosition.Builder()
                                    .target(LatLng(country.centerLat, country.centerLon))
                                    .zoom(defaultZoom.toFloat())
                                    .bearing(0f)
                                    .tilt(0f)
                                    .build()
                            )
                        )
                    }

                    GoogleMap(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("country_detail_google_map_view"),
                        cameraPositionState = cameraPositionState,
                        properties = MapProperties(
                            mapType = googleMapType,
                            mapStyleOptions = googleMapStyleOptions,
                            isBuildingEnabled = true
                        ),
                        uiSettings = MapUiSettings(
                            zoomControlsEnabled = false,
                            compassEnabled = false,
                            rotationGesturesEnabled = true,
                            tiltGesturesEnabled = true,
                            scrollGesturesEnabled = true,
                            zoomGesturesEnabled = true,
                            scrollGesturesEnabledDuringRotateOrZoom = true,
                            myLocationButtonEnabled = false,
                            mapToolbarEnabled = false
                        )
                    ) {
                        if (showCountryStructures && mainPolygonPoints.size >= 3) {
                            Polygon(
                                points = mainPolygonPoints,
                                fillColor = Color(0xFF22C55E).copy(alpha = 0.24f),
                                strokeColor = Color(0xFF22C55E),
                                strokeWidth = 7f,
                                clickable = false
                            )
                        }
                        Marker(
                            state = capitalMarkerState,
                            title = "${country.flagEmoji} ${country.name}",
                            snippet = "Capital: ${country.capital}"
                        )
                    }
                } else {
                    AndroidView(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("country_detail_webview_map"),
                        factory = { ctx ->
                            WebView(ctx).apply {
                                if (AdMobSdkState.isRunningOnEmulator()) {
                                    setLayerType(android.view.View.LAYER_TYPE_SOFTWARE, null)
                                }
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                isFocusable = true
                                isFocusableInTouchMode = true
                                setOnTouchListener { v, event ->
                                    when (event.actionMasked) {
                                        MotionEvent.ACTION_DOWN,
                                        MotionEvent.ACTION_MOVE,
                                        MotionEvent.ACTION_POINTER_DOWN -> {
                                            onMapTouchActiveChanged(true)
                                            v.parent?.requestDisallowInterceptTouchEvent(true)
                                        }
                                        MotionEvent.ACTION_UP,
                                        MotionEvent.ACTION_CANCEL -> {
                                            onMapTouchActiveChanged(false)
                                            v.parent?.requestDisallowInterceptTouchEvent(false)
                                        }
                                    }
                                    false
                                }
                                settings.apply {
                                    javaScriptEnabled = true
                                    domStorageEnabled = true
                                    databaseEnabled = true
                                    cacheMode = WebSettings.LOAD_DEFAULT
                                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                                }
                                webChromeClient = WebChromeClient()
                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        isWebViewLoaded = true
                                        view?.evaluateJavascript(
                                            "if(window.setCountryMapLayer){ window.setCountryMapLayer($mapLayerMode); }",
                                            null
                                        )
                                        view?.evaluateJavascript(
                                            "if(window.renderCountryStructures){ window.renderCountryStructures($structuresJson); }",
                                            null
                                        )
                                    }
                                }
                                loadDataWithBaseURL(
                                    "https://earthscope.local/",
                                    buildCountryDetailInteractiveMapHtml(
                                        initLat = country.centerLat,
                                        initLon = country.centerLon,
                                        initZoom = defaultZoom,
                                        initLayerMode = mapLayerMode
                                    ),
                                    "text/html",
                                    "UTF-8",
                                    null
                                )
                                webViewRef.value = this
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun CountryMapCircleButton(
    icon: ImageVector,
    contentDescription: String,
    tag: String,
    tint: Color = Color.White,
    onClick: () -> Unit
) {
    Surface(
        color = Color(0xD90F172A),
        shape = CircleShape,
        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.7f)),
        shadowElevation = 5.dp,
        modifier = Modifier
            .size(38.dp)
            .clickable { onClick() }
            .testTag(tag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = tint,
                modifier = Modifier.size(19.dp)
            )
        }
    }
}

/**
 * ============================================================================
 * SELECTED COUNTRY & NEIGHBORING COUNTRIES REGIONAL STRUCTURE CARD
 * ============================================================================
 */
@Composable
private fun CountryStructureAndNeighborsCard(
    country: CountryInfo,
    nearbyCountries: List<CountryInfo>,
    isLightTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val mapOceanBg = if (isLightTheme) {
        Brush.verticalGradient(listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFF0B192C), Color(0xFF071120)))
    }
    val gridColor = if (isLightTheme) Color(0xFF0284C7).copy(alpha = 0.16f) else Color(0xFF38BDF8).copy(alpha = 0.16f)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White

    var showRegionalNeighborsInDiagram by remember { mutableStateOf(true) }

    Surface(
        color = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF142238),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.2.dp, Color(0xFF38BDF8).copy(alpha = 0.65f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Country Structure",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Surface(
                    color = if (showRegionalNeighborsInDiagram) Color(0xFF0284C7) else Color(0xFF0284C7).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFF0284C7)),
                    modifier = Modifier.clickable {
                        showRegionalNeighborsInDiagram = !showRegionalNeighborsInDiagram
                    }
                ) {
                    Text(
                        text = if (showRegionalNeighborsInDiagram) "All Borders" else "Single Only",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (showRegionalNeighborsInDiagram) Color.White else Color(0xFF0284C7),
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Multi-Country Structural Vector Canvas (Shows Selected Country + Surrounding Countries Structures)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(mapOceanBg)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw Lat/Lon Grid lines
                    for (i in 1..4) {
                        val y = (h / 5f) * i
                        drawLine(gridColor, Offset(0f, y), Offset(w, y), strokeWidth = 1.2f)
                        val x = (w / 5f) * i
                        drawLine(gridColor, Offset(x, 0f), Offset(x, h), strokeWidth = 1.2f)
                    }

                    val mainPts = getCountryStructurePolygon(country)
                    val neighborPolygons = if (showRegionalNeighborsInDiagram) {
                        nearbyCountries.take(5).map { nb -> nb to getCountryStructurePolygon(nb) }
                    } else {
                        emptyList()
                    }

                    val allPts = buildList {
                        addAll(mainPts)
                        add(GeoPoint(country.capitalLat, country.capitalLon))
                        neighborPolygons.forEach { (nb, pts) ->
                            addAll(pts)
                            add(GeoPoint(nb.centerLat, nb.centerLon))
                        }
                    }

                    val minLat = (allPts.minOfOrNull { it.lat } ?: (country.centerLat - 4.0)) - 1.2
                    val maxLat = (allPts.maxOfOrNull { it.lat } ?: (country.centerLat + 4.0)) + 1.2
                    val minLon = (allPts.minOfOrNull { it.lon } ?: (country.centerLon - 4.0)) - 1.2
                    val maxLon = (allPts.maxOfOrNull { it.lon } ?: (country.centerLon + 4.0)) + 1.2

                    val latSpan = (maxLat - minLat).coerceAtLeast(1.5)
                    val lonSpan = (maxLon - minLon).coerceAtLeast(1.5)
                    val pad = 22f
                    val drawW = (w - pad * 2f).coerceAtLeast(10f)
                    val drawH = (h - pad * 2f).coerceAtLeast(10f)

                    fun mapX(lon: Double): Float = pad + (((lon - minLon) / lonSpan).toFloat() * drawW)
                    fun mapY(lat: Double): Float = (h - pad) - (((lat - minLat) / latSpan).toFloat() * drawH)

                    // 1. Draw Neighboring Countries Structures first
                    neighborPolygons.forEach { (nb, pts) ->
                        val nbPath = Path()
                        pts.forEachIndexed { idx, pt ->
                            val px = mapX(pt.lon)
                            val py = mapY(pt.lat)
                            if (idx == 0) nbPath.moveTo(px, py) else nbPath.lineTo(px, py)
                        }
                        nbPath.close()

                        drawPath(
                            path = nbPath,
                            color = Color(0xFF38BDF8).copy(alpha = 0.22f)
                        )
                        drawPath(
                            path = nbPath,
                            color = Color(0xFF0284C7),
                            style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                        )

                        val nbCx = mapX(nb.centerLon).coerceIn(pad, w - pad)
                        val nbCy = mapY(nb.centerLat).coerceIn(pad, h - pad)
                        val mainCx = mapX(country.centerLon).coerceIn(pad, w - pad)
                        val mainCy = mapY(country.centerLat).coerceIn(pad, h - pad)

                        drawLine(
                            color = Color(0xFF38BDF8).copy(alpha = 0.35f),
                            start = Offset(mainCx, mainCy),
                            end = Offset(nbCx, nbCy),
                            strokeWidth = 1.5f
                        )
                        drawCircle(
                            color = Color(0xFF38BDF8),
                            radius = 4.5f,
                            center = Offset(nbCx, nbCy)
                        )
                    }

                    // 2. Draw Selected Country Primary Structure
                    val mainPath = Path()
                    mainPts.forEachIndexed { idx, pt ->
                        val px = mapX(pt.lon)
                        val py = mapY(pt.lat)
                        if (idx == 0) mainPath.moveTo(px, py) else mainPath.lineTo(px, py)
                    }
                    mainPath.close()

                    drawPath(
                        path = mainPath,
                        color = Color(0xFF22C55E).copy(alpha = 0.42f)
                    )
                    drawPath(
                        path = mainPath,
                        color = Color(0xFF22C55E),
                        style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // Capital City Glowing Marker
                    val capX = mapX(country.capitalLon).coerceIn(pad, w - pad)
                    val capY = mapY(country.capitalLat).coerceIn(pad, h - pad)
                    drawCircle(
                        color = Color(0xFFFACC15).copy(alpha = 0.42f),
                        radius = 14f,
                        center = Offset(capX, capY)
                    )
                    drawCircle(
                        color = Color(0xFFFACC15),
                        radius = 6.5f,
                        center = Offset(capX, capY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 6.5f,
                        center = Offset(capX, capY),
                        style = Stroke(width = 2f)
                    )
                }
            }

            if (nearbyCountries.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    nearbyCountries.forEach { neighbor ->
                        val distKm = remember(country.id, neighbor.id) {
                            GlobeMath.exactCentroidDistanceKm(country, neighbor).roundToInt()
                        }
                        Surface(
                            color = if (isLightTheme) Color.White else Color(0xFF0F172A),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.6f)),
                            modifier = Modifier.testTag("neighbor_country_chip_${neighbor.id}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = neighbor.flagEmoji, fontSize = 15.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${neighbor.name} (${distKm} km)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryText,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compact Visual Map Box Card showing the Country's Geographic Map Outline,
 * Ocean Grid, Capital Star Pin, Flag, and Coordinates (used in Flight Route side-by-side cards).
 */
@Composable
private fun CountryMiniMapBoxCard(
    country: CountryInfo,
    roleLabel: String,
    accentColor: Color,
    isLightTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val mapOceanBg = if (isLightTheme) {
        Brush.verticalGradient(listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFF0B192C), Color(0xFF071120)))
    }
    val gridColor = if (isLightTheme) Color(0xFF0284C7).copy(alpha = 0.16f) else Color(0xFF38BDF8).copy(alpha = 0.16f)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF475569) else Color(0xFF94A3B8)

    Surface(
        color = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF142238),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.2.dp, accentColor.copy(alpha = 0.65f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${country.flagEmoji} ${country.name}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Text(
                text = "★ ${country.capital}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = accentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Visual Country Map Box Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(126.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(mapOceanBg)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Draw Lat/Lon Grid lines
                    for (i in 1..3) {
                        val y = (h / 4f) * i
                        drawLine(gridColor, Offset(0f, y), Offset(w, y), strokeWidth = 1.2f)
                        val x = (w / 4f) * i
                        drawLine(gridColor, Offset(x, 0f), Offset(x, h), strokeWidth = 1.2f)
                    }

                    val pts: List<GeoPoint> = getCountryStructurePolygon(country)

                    val minLat = minOf(pts.minOf { it.lat }, country.capitalLat) - 0.8
                    val maxLat = maxOf(pts.maxOf { it.lat }, country.capitalLat) + 0.8
                    val minLon = minOf(pts.minOf { it.lon }, country.capitalLon) - 0.8
                    val maxLon = maxOf(pts.maxOf { it.lon }, country.capitalLon) + 0.8

                    val latSpan = (maxLat - minLat).coerceAtLeast(1.0)
                    val lonSpan = (maxLon - minLon).coerceAtLeast(1.0)
                    val pad = 18f
                    val drawW = (w - pad * 2f).coerceAtLeast(10f)
                    val drawH = (h - pad * 2f).coerceAtLeast(10f)

                    fun mapX(lon: Double): Float = pad + (((lon - minLon) / lonSpan).toFloat() * drawW)
                    fun mapY(lat: Double): Float = (h - pad) - (((lat - minLat) / latSpan).toFloat() * drawH)

                    val polyPath = Path()
                    pts.forEachIndexed { idx, pt ->
                        val px = mapX(pt.lon)
                        val py = mapY(pt.lat)
                        if (idx == 0) polyPath.moveTo(px, py) else polyPath.lineTo(px, py)
                    }
                    polyPath.close()

                    // Filled country landmass
                    drawPath(
                        path = polyPath,
                        color = accentColor.copy(alpha = 0.34f)
                    )
                    // Highlighted country border
                    drawPath(
                        path = polyPath,
                        color = accentColor,
                        style = Stroke(width = 3.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // Capital City Glowing Marker
                    val capX = mapX(country.capitalLon).coerceIn(pad, w - pad)
                    val capY = mapY(country.capitalLat).coerceIn(pad, h - pad)
                    drawCircle(
                        color = Color(0xFFFACC15).copy(alpha = 0.38f),
                        radius = 13f,
                        center = Offset(capX, capY)
                    )
                    drawCircle(
                        color = Color(0xFFFACC15),
                        radius = 6f,
                        center = Offset(capX, capY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 6f,
                        center = Offset(capX, capY),
                        style = Stroke(width = 2f)
                    )
                }

                // Bottom-right coordinate pill inside map box
                Surface(
                    color = Color(0xAA0F172A),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(6.dp)
                ) {
                    Text(
                        text = "${String.format(Locale.US, "%.1f°", country.centerLat)}, ${String.format(Locale.US, "%.1f°", country.centerLon)}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = roleLabel,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = secondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun buildCountryAndNeighborsJson(
    country: CountryInfo,
    neighbors: List<CountryInfo>,
    showStructures: Boolean
): String {
    fun escapeJs(s: String): String = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ")
    fun polyJson(pts: List<GeoPoint>): String =
        pts.joinToString(prefix = "[", postfix = "]") { "[${it.lat},${it.lon}]" }

    val mainPoly = polyJson(getCountryStructurePolygon(country))
    val neighborsJson = neighbors.joinToString(prefix = "[", postfix = "]") { nb ->
        val nbPoly = polyJson(getCountryStructurePolygon(nb))
        """{"id":"${escapeJs(nb.id)}","name":"${escapeJs(nb.name)}","capital":"${escapeJs(nb.capital)}","flag":"${escapeJs(nb.flagEmoji)}","centerLat":${nb.centerLat},"centerLon":${nb.centerLon},"capitalLat":${nb.capitalLat},"capitalLon":${nb.capitalLon},"poly":$nbPoly}"""
    }

    return """{"showStructures":$showStructures,"main":{"id":"${escapeJs(country.id)}","name":"${escapeJs(country.name)}","capital":"${escapeJs(country.capital)}","flag":"${escapeJs(country.flagEmoji)}","centerLat":${country.centerLat},"centerLon":${country.centerLon},"capitalLat":${country.capitalLat},"capitalLon":${country.capitalLon},"poly":$mainPoly},"neighbors":$neighborsJson}"""
}

private fun buildCountryDetailInteractiveMapHtml(
    initLat: Double,
    initLon: Double,
    initZoom: Int,
    initLayerMode: Int
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
        html, body, #map {
            margin: 0; padding: 0; width: 100%; height: 100%;
            background: #070d19;
            touch-action: none;
        }
        body.map-mode-white #map { background: #f8fafc; }
        body.map-mode-black #map { background: #070d19; }
        body.map-mode-green #map { background: #1d63dc; }
        body.map-mode-green .leaflet-tile-pane {
            filter: url(#green-map-color-filter);
        }
        .country-badge-label {
            background: rgba(15, 23, 42, 0.88);
            color: #ffffff;
            border: 1.5px solid #22c55e;
            border-radius: 12px;
            padding: 3px 8px;
            font-family: sans-serif;
            font-size: 11px;
            font-weight: bold;
            white-space: nowrap;
            box-shadow: 0 2px 8px rgba(0,0,0,0.45);
            pointer-events: none;
        }
    </style>
</head>
<body class="map-mode-white">
    <svg width="0" height="0" style="position:absolute;z-index:-1;">
        <defs>
            <filter id="green-map-color-filter" color-interpolation-filters="sRGB">
                <feColorMatrix type="matrix" values="
                    0.95  0.10 -0.90  0.00  0.00
                    0.10  1.45 -0.45  0.00  0.02
                   -0.75 -0.25  1.95  0.00  0.00
                    0.00  0.00  0.00  1.00  0.00" />
            </filter>
        </defs>
    </svg>
    <div id="map"></div>
    <script>
        var googleRoad = L.tileLayer('https://mt1.google.com/vt/lyrs=m&x={x}&y={y}&z={z}', { maxZoom: 20 });
        var googleHybrid = L.tileLayer('https://mt1.google.com/vt/lyrs=y&x={x}&y={y}&z={z}', { maxZoom: 20 });
        var googleTerrain = L.tileLayer('https://mt1.google.com/vt/lyrs=p&x={x}&y={y}&z={z}', { maxZoom: 20 });
        var blackTiles = L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', { maxZoom: 19 });
        var greenBaseTiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { maxZoom: 19 });

        var map = L.map('map', {
            center: [$initLat, $initLon],
            zoom: $initZoom,
            zoomControl: false,
            attributionControl: false,
            dragging: true,
            inertia: true,
            touchZoom: true,
            doubleClickZoom: true,
            scrollWheelZoom: true,
            tap: false,
            layers: [googleHybrid]
        });

        var structuresLayer = L.layerGroup().addTo(map);

        window.setCountryMapLayer = function(mode) {
            document.body.className = '';
            map.eachLayer(function(l) {
                if (l !== structuresLayer) map.removeLayer(l);
            });
            if (mode === 1) {
                googleHybrid.addTo(map);
            } else if (mode === 2) {
                googleTerrain.addTo(map);
            } else if (mode === 3) {
                document.body.className = 'map-mode-black';
                blackTiles.addTo(map);
            } else if (mode === 4) {
                document.body.className = 'map-mode-green';
                greenBaseTiles.addTo(map);
            } else {
                document.body.className = 'map-mode-white';
                googleRoad.addTo(map);
            }
            structuresLayer.addTo(map);
        };

        window.flyCountryMap = function(lat, lon, zoom, bearingDeg) {
            map.flyTo([lat, lon], zoom, { duration: 0.45 });
        };

        window.renderCountryStructures = function(data) {
            structuresLayer.clearLayers();
            if (!data) return;
            var m = data.main;
            if (data.showStructures && m && m.poly && m.poly.length >= 3) {
                L.polygon(m.poly, {
                    color: '#22c55e',
                    weight: 3.5,
                    fillColor: '#22c55e',
                    fillOpacity: 0.25,
                    interactive: false
                }).addTo(structuresLayer);
            }
            if (m) {
                var mainIcon = L.divIcon({
                    className: '',
                    html: '<div class="country-badge-label">' + m.flag + ' ' + m.name + ' ★ ' + m.capital + '</div>',
                    iconSize: [140, 26],
                    iconAnchor: [70, 13]
                });
                L.marker([m.capitalLat, m.capitalLon], { icon: mainIcon, interactive: false, keyboard: false }).addTo(structuresLayer);
            }
        };

        window.setCountryMapLayer($initLayerMode);
    </script>
</body>
</html>
    """.trimIndent()
}

@Composable
private fun CountryComparisonColumn(
    label: String,
    country: CountryInfo,
    accentColor: Color,
    numberFormat: NumberFormat,
    isLightTheme: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF1E293B),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${country.flagEmoji} ${country.name}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isLightTheme) Color(0xFF0F172A) else Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComparisonField("Capital", country.capital, isLightTheme)
            ComparisonField("Continent", country.continent, isLightTheme)
            ComparisonField("Population", numberFormat.format(country.population), isLightTheme)
            ComparisonField("Area", "${numberFormat.format(country.areaKm2.toLong())} km²", isLightTheme)
            ComparisonField("Currency", country.currency, isLightTheme)
            ComparisonField("Local Time", formatLocalTime(country.timeZoneOffsetHours), isLightTheme)
        }
    }
}

@Composable
private fun ComparisonField(label: String, value: String, isLightTheme: Boolean) {
    Column(modifier = Modifier.padding(vertical = 3.dp)) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isLightTheme) Color(0xFF0F172A) else Color.White,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    isLightTheme: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isLightTheme) Color(0xFFF1F5F9) else Color(0xFF1E293B)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                color = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isLightTheme) Color(0xFF0F172A) else Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun formatLocalTime(offsetHours: Double): String {
    return try {
        val offsetMillis = (offsetHours * 3600000).toInt()
        val tz = TimeZone.getTimeZone("GMT").apply { rawOffset = offsetMillis }
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        sdf.timeZone = tz
        val sign = if (offsetHours >= 0) "+" else ""
        "${sdf.format(Date())} (UTC$sign${formatHours(offsetHours)})"
    } catch (e: Exception) {
        "UTC+$offsetHours"
    }
}

private fun formatHours(hours: Double): String {
    return if (hours % 1.0 == 0.0) {
        hours.toInt().toString()
    } else {
        String.format("%.1f", hours)
    }
}

private fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): String {
    val phi1 = lat1 * (PI / 180.0)
    val phi2 = lat2 * (PI / 180.0)
    val dLon = (lon2 - lon1) * (PI / 180.0)
    val y = sin(dLon) * cos(phi2)
    val x = cos(phi1) * sin(phi2) - sin(phi1) * cos(phi2) * cos(dLon)
    var brng = atan2(y, x) * (180.0 / PI)
    brng = (brng + 360.0) % 360.0
    val dirs = arrayOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
    val dir = dirs[((brng + 22.5) / 45.0).toInt() % 8]
    return "${brng.roundToInt()}° ($dir)"
}
