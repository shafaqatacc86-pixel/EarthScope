package com.example.data.repository

import com.example.data.local.MapFloatingPinDao
import com.example.data.local.MapFloatingPinEntity
import kotlinx.coroutines.flow.Flow

class MapFloatingPinRepository(private val dao: MapFloatingPinDao) {
    val allFloatingPins: Flow<List<MapFloatingPinEntity>> = dao.getAllFloatingPins()

    suspend fun insertPin(
        locationName: String,
        locationNotes: String,
        latitude: Double,
        longitude: Double,
        sourceType: String,
        categoryKey: String,
        markerStyleKey: String,
        formattedDateTime: String,
        primaryImagePath: String?,
        secondImagePath: String?,
        thirdImagePath: String?
    ): Long {
        val entity = MapFloatingPinEntity(
            locationName = locationName,
            locationNotes = locationNotes,
            latitude = latitude,
            longitude = longitude,
            sourceType = sourceType,
            categoryKey = categoryKey,
            markerStyleKey = markerStyleKey,
            formattedDateTime = formattedDateTime,
            timestamp = System.currentTimeMillis(),
            primaryImagePath = primaryImagePath,
            secondImagePath = secondImagePath,
            thirdImagePath = thirdImagePath
        )
        return dao.insertFloatingPin(entity)
    }

    suspend fun updatePin(entity: MapFloatingPinEntity): Long {
        return dao.insertFloatingPin(entity)
    }

    suspend fun deletePin(id: Long) {
        dao.deleteFloatingPinById(id)
    }

    suspend fun clearAllPins() {
        dao.clearAllFloatingPins()
    }
}
