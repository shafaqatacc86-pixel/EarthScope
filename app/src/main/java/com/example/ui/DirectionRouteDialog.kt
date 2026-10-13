package com.example.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.CountryInfo
import com.example.data.model.WorldGeographicData
import com.example.globe.GlobeMath

@Composable
fun DirectionRouteDialog(
    initialStartCountry: CountryInfo?,
    initialEndCountry: CountryInfo?,
    hasActiveRoute: Boolean,
    isLightTheme: Boolean = false,
    onDismiss: () -> Unit,
    onShowDirection: (start: CountryInfo, end: CountryInfo) -> Unit,
    onClearRoute: () -> Unit
) {
    val context = LocalContext.current

    var startQuery by remember { mutableStateOf(initialStartCountry?.name ?: "Pakistan") }
    var endQuery by remember { mutableStateOf(initialEndCountry?.name ?: "") }
    var activeField by remember { mutableStateOf(0) } // 1 = Start field focused/editing, 2 = End field focused/editing

    val dialogBg = if (isLightTheme) Color.White else Color(0xFF0F172A)
    val dialogBorder = if (isLightTheme) Color(0xFFCBD5E1) else Color(0xFF263955)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF475569) else Color(0xFF94A3B8)
    val fieldBgFocused = if (isLightTheme) Color(0xFFF1F5F9) else Color(0xFF1E293B).copy(alpha = 0.6f)
    val fieldBgUnfocused = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF1E293B).copy(alpha = 0.4f)
    val fieldBorderUnfocused = if (isLightTheme) Color(0xFFCBD5E1) else Color(0xFF334155)
    val dropdownBg = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF162238)

    val suggestions = remember(startQuery, endQuery, activeField) {
        val q = when (activeField) {
            1 -> startQuery.trim()
            2 -> endQuery.trim()
            else -> ""
        }
        if (q.isEmpty()) {
            if (activeField == 1 || activeField == 2) {
                WorldGeographicData.countries.take(5)
            } else {
                emptyList()
            }
        } else {
            WorldGeographicData.countries.filter {
                it.name.contains(q, ignoreCase = true) ||
                        it.capital.contains(q, ignoreCase = true) ||
                        it.nativeName.contains(q, ignoreCase = true)
            }.take(5)
        }
    }

    val resolvedStart = remember(startQuery) { WorldGeographicData.findCountry(startQuery) }
    val resolvedEnd = remember(endQuery) { WorldGeographicData.findCountry(endQuery) }
    val capitalDistanceKm = remember(resolvedStart, resolvedEnd) {
        if (resolvedStart != null && resolvedEnd != null && resolvedStart.id != resolvedEnd.id) {
            GlobeMath.exactCapitalDistanceKm(resolvedStart, resolvedEnd)
        } else null
    }
    val centerDistanceKm = remember(resolvedStart, resolvedEnd) {
        if (resolvedStart != null && resolvedEnd != null && resolvedStart.id != resolvedEnd.id) {
            GlobeMath.exactCentroidDistanceKm(resolvedStart, resolvedEnd)
        } else null
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = dialogBg,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, dialogBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp)
                .testTag("direction_route_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp)
            ) {
                // Header Row (Responsive with weight(1f) so title never cuts off)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color(0xFF0284C7).copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Directions,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Flight Directions",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Select start & destination country",
                                fontSize = 12.sp,
                                color = secondaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("close_direction_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = secondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 1. Starting Country Search Input
                OutlinedTextField(
                    value = startQuery,
                    onValueChange = {
                        startQuery = it
                        activeField = 1
                    },
                    label = { Text("Starting Country", fontSize = 12.sp, color = secondaryText) },
                    placeholder = { Text("e.g., Pakistan", fontSize = 13.sp, color = secondaryText) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = null,
                            tint = Color(0xFF0284C7)
                        )
                    },
                    trailingIcon = {
                        if (startQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    startQuery = ""
                                    activeField = 1
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Start",
                                    tint = secondaryText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = primaryText,
                        unfocusedTextColor = primaryText,
                        focusedBorderColor = Color(0xFF0284C7),
                        unfocusedBorderColor = fieldBorderUnfocused,
                        focusedContainerColor = fieldBgFocused,
                        unfocusedContainerColor = fieldBgUnfocused
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("start_country_input")
                )

                // Swap button & live distance preview row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (capitalDistanceKm != null && centerDistanceKm != null && resolvedStart != null && resolvedEnd != null) {
                        val flightHours = capitalDistanceKm / 850.0
                        val hrs = flightHours.toInt()
                        val mins = ((flightHours - hrs) * 60).toInt()
                        val capFormatted = String.format(java.util.Locale.US, "%,.1f", capitalDistanceKm)
                        val cenFormatted = String.format(java.util.Locale.US, "%,.1f", centerDistanceKm)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "✈ ${resolvedStart.capital} → ${resolvedEnd.capital}: $capFormatted km (~${hrs}h ${mins}m)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isLightTheme) Color(0xFFD97706) else Color(0xFFFACC15)
                            )
                            Text(
                                text = "Geographic Center Distance: $cenFormatted km",
                                fontSize = 11.sp,
                                color = secondaryText
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }

                    IconButton(
                        onClick = {
                            val temp = startQuery
                            startQuery = endQuery
                            endQuery = temp
                            activeField = 0
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .background(if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E293B), CircleShape)
                            .testTag("swap_countries_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SwapVert,
                            contentDescription = "Swap Countries",
                            tint = Color(0xFF0284C7),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // 2. Destination (End) Country Search Input
                OutlinedTextField(
                    value = endQuery,
                    onValueChange = {
                        endQuery = it
                        activeField = 2
                    },
                    label = { Text("Destination Country", fontSize = 12.sp, color = secondaryText) },
                    placeholder = { Text("e.g., Saudi Arabia, Turkey", fontSize = 13.sp, color = secondaryText) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFF16A34A)
                        )
                    },
                    trailingIcon = {
                        if (endQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    endQuery = ""
                                    activeField = 2
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Destination",
                                    tint = secondaryText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = primaryText,
                        unfocusedTextColor = primaryText,
                        focusedBorderColor = Color(0xFF22C55E),
                        unfocusedBorderColor = fieldBorderUnfocused,
                        focusedContainerColor = fieldBgFocused,
                        unfocusedContainerColor = fieldBgUnfocused
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("end_country_input")
                )

                // Suggestions list for quick tap selection
                if (suggestions.isNotEmpty() && activeField != 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = dropdownBg,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, dialogBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 155.dp)
                    ) {
                        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
                            items(suggestions) { country ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            if (activeField == 1) {
                                                startQuery = country.name
                                                if (endQuery.isBlank()) {
                                                    activeField = 2
                                                } else {
                                                    activeField = 0
                                                }
                                            } else {
                                                endQuery = country.name
                                                activeField = 0
                                            }
                                        }
                                        .padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = country.flagEmoji,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(end = 10.dp)
                                    )
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = country.name,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = primaryText,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "${country.capital} • ${country.continent}",
                                            fontSize = 11.sp,
                                            color = secondaryText,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons Row (Responsive text & buttons that never cut off)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hasActiveRoute) {
                        OutlinedButton(
                            onClick = {
                                onClearRoute()
                                onDismiss()
                            },
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, Color(0xFFEF4444)),
                            modifier = Modifier
                                .weight(0.95f)
                                .height(48.dp)
                                .testTag("clear_route_button")
                        ) {
                            Text(
                                text = "Clear",
                                fontSize = 14.sp,
                                color = Color(0xFFEF4444),
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val startCountry = WorldGeographicData.findCountry(startQuery)
                            val endCountry = WorldGeographicData.findCountry(endQuery)
                            when {
                                startCountry == null -> {
                                    Toast.makeText(
                                        context,
                                        "Please select a valid Starting Country",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                endCountry == null -> {
                                    Toast.makeText(
                                        context,
                                        "Please select a valid Destination Country",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                startCountry.id == endCountry.id -> {
                                    Toast.makeText(
                                        context,
                                        "Please choose two different countries",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    onShowDirection(startCountry, endCountry)
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF22C55E),
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1.4f)
                            .height(48.dp)
                            .testTag("show_direction_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlightTakeoff,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Show Direction",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
