package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map_floating_pins")
data class MapFloatingPinEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val locationName: String,
    val locationNotes: String,
    val latitude: Double,
    val longitude: Double,
    val sourceType: String, // "CURRENT" or "CUSTOM"
    val categoryKey: String = "TRAVELLING",
    val markerStyleKey: String = "EMERALD_PIN",
    val formattedDateTime: String,
    val timestamp: Long = System.currentTimeMillis(),
    val primaryImagePath: String? = null,
    val secondImagePath: String? = null,
    val thirdImagePath: String? = null
)
