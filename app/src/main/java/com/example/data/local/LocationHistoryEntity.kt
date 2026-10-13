package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_history")
data class LocationHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val countryCode: String,
    val capital: String,
    val continent: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val userNotes: String? = null,
    val visitType: String = "SEARCHED" // "SEARCHED", "VISITED", "GPS"
)
