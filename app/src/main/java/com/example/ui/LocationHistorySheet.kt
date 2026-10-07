package com.example.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.data.local.LocationHistoryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationHistorySheet(
    historyList: List<LocationHistoryEntity>,
    isLightTheme: Boolean = false,
    onDismiss: () -> Unit,
    onSelectLocation: (LocationHistoryEntity) -> Unit,
    onToggleFavorite: (LocationHistoryEntity) -> Unit,
    onDeleteLocation: (Long) -> Unit,
    onClearAll: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var filterOnlyFavorites by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    val sheetBg = if (isLightTheme) Color.White else Color(0xFF0B132B)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF475569) else Color(0xFF94A3B8)
    val cardBg = if (isLightTheme) Color(0xFFF1F5F9) else Color(0xFF1E293B)

    val filteredList = remember(historyList, filterOnlyFavorites, searchQuery) {
        historyList.filter { item ->
            val matchesFav = !filterOnlyFavorites || item.isFavorite
            val matchesQuery = searchQuery.isBlank() ||
                    item.name.contains(searchQuery, ignoreCase = true) ||
                    item.capital.contains(searchQuery, ignoreCase = true) ||
                    item.continent.contains(searchQuery, ignoreCase = true)
            matchesFav && matchesQuery
        }
    }

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
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFF22C55E).copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = Color(0xFF22C55E),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Location History",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryText
                        )
                        Text(
                            text = "${historyList.size} tracked locations",
                            fontSize = 12.sp,
                            color = secondaryText
                        )
                    }
                }

                if (historyList.isNotEmpty()) {
                    IconButton(
                        onClick = { showClearConfirmDialog = true },
                        modifier = Modifier.testTag("clear_history_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Clear All",
                            tint = Color(0xFFEF4444)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search filter inside history
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search tracked history...", color = secondaryText, fontSize = 13.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = secondaryText,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("history_search_field"),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF22C55E),
                    unfocusedBorderColor = if (isLightTheme) Color(0xFFCBD5E1) else Color(0xFF1E293B),
                    focusedContainerColor = cardBg,
                    unfocusedContainerColor = cardBg,
                    focusedTextColor = primaryText,
                    unfocusedTextColor = primaryText
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = !filterOnlyFavorites,
                    onClick = { filterOnlyFavorites = false },
                    label = { Text("All (${historyList.size})") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF22C55E),
                        selectedLabelColor = Color.White,
                        containerColor = cardBg,
                        labelColor = secondaryText
                    )
                )

                val favCount = historyList.count { it.isFavorite }
                FilterChip(
                    selected = filterOnlyFavorites,
                    onClick = { filterOnlyFavorites = true },
                    label = { Text("Favorites ($favCount)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (filterOnlyFavorites) Color.White else Color(0xFFFBBF24),
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFEAB308),
                        selectedLabelColor = Color.Black,
                        containerColor = cardBg,
                        labelColor = secondaryText
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // History List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            tint = secondaryText,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (filterOnlyFavorites) "No favorite locations yet" else "No history recorded yet",
                            color = secondaryText,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        HistoryCard(
                            item = item,
                            isLightTheme = isLightTheme,
                            onClick = {
                                onSelectLocation(item)
                                onDismiss()
                            },
                            onToggleFav = { onToggleFavorite(item) },
                            onDelete = { onDeleteLocation(item.id) }
                        )
                    }
                }
            }
        }
    }

    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = { Text("Clear All History?") },
            text = { Text("Are you sure you want to delete all recorded locations and travel history?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onClearAll()
                        showClearConfirmDialog = false
                    }
                ) {
                    Text("Clear All", color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Cancel")
                }
            },
            containerColor = if (isLightTheme) Color.White else Color(0xFF1E293B),
            titleContentColor = primaryText,
            textContentColor = secondaryText
        )
    }
}

@Composable
private fun HistoryCard(
    item: LocationHistoryEntity,
    isLightTheme: Boolean,
    onClick: () -> Unit,
    onToggleFav: () -> Unit,
    onDelete: () -> Unit
) {
    val timeFormat = remember { SimpleDateFormat("MMM d, hh:mm a", Locale.getDefault()) }
    val formattedTime = remember(item.timestamp) { timeFormat.format(Date(item.timestamp)) }
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF475569) else Color(0xFF94A3B8)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("history_item_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (isLightTheme) Color(0xFFF1F5F9) else Color(0xFF1E293B)
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        when (item.visitType) {
                            "GPS" -> Color(0xFF0284C7).copy(alpha = 0.2f)
                            "VISITED" -> Color(0xFF22C55E).copy(alpha = 0.2f)
                            else -> Color(0xFF64748B).copy(alpha = 0.2f)
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (item.visitType) {
                        "GPS" -> Icons.Default.MyLocation
                        "VISITED" -> Icons.Default.LocationOn
                        else -> Icons.Default.Search
                    },
                    contentDescription = null,
                    tint = when (item.visitType) {
                        "GPS" -> Color(0xFF0284C7)
                        "VISITED" -> Color(0xFF22C55E)
                        else -> secondaryText
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                        color = if (isLightTheme) Color(0xFFE0F2FE) else Color(0xFF334155),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = item.visitType,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${item.continent} • Lat: ${String.format("%.2f", item.latitude)}°, Lon: ${String.format("%.2f", item.longitude)}°",
                    fontSize = 11.sp,
                    color = secondaryText,
                    maxLines = 1
                )

                Text(
                    text = formattedTime,
                    fontSize = 10.sp,
                    color = secondaryText
                )

                if (!item.userNotes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Note: ${item.userNotes}",
                        fontSize = 11.sp,
                        color = if (isLightTheme) Color(0xFFB45309) else Color(0xFFFDE047),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // Favorite button
            IconButton(
                onClick = onToggleFav,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = if (item.isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                    contentDescription = "Favorite",
                    tint = if (item.isFavorite) Color(0xFFFBBF24) else secondaryText,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = secondaryText,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
