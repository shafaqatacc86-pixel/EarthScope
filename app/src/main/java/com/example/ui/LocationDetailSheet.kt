package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CountryInfo
import com.example.globe.GlobeMath
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LocationDetailSheet(
    country: CountryInfo?,
    routeStartCountry: CountryInfo? = null,
    routeEndCountry: CountryInfo? = null,
    customLabel: String,
    isLightTheme: Boolean = false,
    onDismiss: () -> Unit,
    onTrackAsVisited: (note: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isAddingNote by remember { mutableStateOf(false) }
    var noteText by remember { mutableStateOf("") }
    var markedVisited by remember { mutableStateOf(false) }
    val numberFormat = remember { NumberFormat.getNumberInstance(Locale.US) }

    val isRouteMode = routeStartCountry != null && routeEndCountry != null
    val sheetBg = if (isLightTheme) Color.White else Color(0xFF0F172A)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF475569) else Color(0xFF94A3B8)
    val sectionTitleColor = if (isLightTheme) Color(0xFF1E293B) else Color(0xFFE2E8F0)
    val cardBg = if (isLightTheme) Color(0xFFF1F5F9) else Color(0xFF1E293B)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = sheetBg,
        contentColor = primaryText,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 36.dp)
                .verticalScroll(rememberScrollState())
        ) {
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
                    abs(tzDiff) < 0.01 -> "Same Timezone (0h)"
                    tzDiff > 0 -> "+${formatHours(tzDiff)} hrs ahead"
                    else -> "${formatHours(tzDiff)} hrs behind"
                }

                // Route Header
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
                                .size(44.dp)
                                .background(Color(0xFF22C55E).copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlightTakeoff,
                                contentDescription = null,
                                tint = if (isLightTheme) Color(0xFF16A34A) else Color(0xFF4ADE80),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${routeStartCountry.flagEmoji} ${routeStartCountry.name} ✈ ${routeEndCountry.flagEmoji} ${routeEndCountry.name}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${routeStartCountry.capital} → ${routeEndCountry.capital}",
                                fontSize = 13.sp,
                                color = secondaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_sheet_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = secondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                                    text = "EXACT FLIGHT DISTANCE (${routeStartCountry.capital.uppercase()} → ${routeEndCountry.capital.uppercase()})",
                                    fontSize = 10.sp,
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
                                    text = "${String.format(Locale.US, "%,.1f", distMiles)} miles • ${String.format(Locale.US, "%,.1f", distNauticalMiles)} NM",
                                    fontSize = 12.sp,
                                    color = secondaryText
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
                                        text = "EST. FLIGHT TIME",
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
                                    Text(
                                        text = "@ 850 km/h",
                                        fontSize = 10.sp,
                                        color = secondaryText
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
                                    text = "Geographic Center-to-Center Distance:",
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

                Spacer(modifier = Modifier.height(16.dp))

                // Route Telemetry Grid
                Text(
                    text = "Flight Route & Navigation Details",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = sectionTitleColor
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "Compass Heading",
                        value = bearingInfo,
                        isLightTheme = isLightTheme,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Timezone Difference",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (country != null) {
                            Text(
                                text = country.flagEmoji,
                                fontSize = 36.sp,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(Color(0xFF22C55E).copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF22C55E),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = country?.name ?: customLabel,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (country != null && country.nativeName.isNotBlank() && country.nativeName != country.name) {
                                Text(
                                    text = "${country.nativeName} • ${country.capital}",
                                    fontSize = 13.sp,
                                    color = secondaryText,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_sheet_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = secondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                if (country != null) {
                    Surface(
                        color = cardBg,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = country.description,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            color = if (isLightTheme) Color(0xFF334155) else Color(0xFFCBD5E1),
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Country Information",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = sectionTitleColor
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    val localTimeStr = remember(country.timeZoneOffsetHours) {
                        formatLocalTime(country.timeZoneOffsetHours)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            title = "Capital City",
                            value = country.capital,
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Continent",
                            value = country.continent,
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
                            value = numberFormat.format(country.population),
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "Area (km²)",
                            value = "${numberFormat.format(country.areaKm2.toLong())} km²",
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
                            value = country.currency,
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
                            value = String.format("%.2f°, %.2f°", country.centerLat, country.centerLon),
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            title = "ISO Code",
                            value = country.id,
                            isLightTheme = isLightTheme,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Languages",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = secondaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        country.languages.forEach { lang ->
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

            Spacer(modifier = Modifier.height(20.dp))

            // Action / Note Section
            if (isAddingNote) {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    placeholder = { Text("Add personal travel memories or notes...", color = secondaryText) },
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
                        text = if (markedVisited) "Tracked in History" else "Track in History",
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
        }
    }
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
