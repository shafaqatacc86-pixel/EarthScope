package com.example.data.repository

import com.example.data.local.LocationHistoryDao
import com.example.data.local.LocationHistoryEntity
import kotlinx.coroutines.flow.Flow

class LocationHistoryRepository(private val dao: LocationHistoryDao) {
    val allHistory: Flow<List<LocationHistoryEntity>> = dao.getAllHistory()
    val favoriteLocations: Flow<List<LocationHistoryEntity>> = dao.getFavoriteLocations()

    fun search(query: String): Flow<List<LocationHistoryEntity>> = dao.searchHistory(query)

    suspend fun recordLocation(
        name: String,
        countryCode: String,
        capital: String,
        continent: String,
        latitude: Double,
        longitude: Double,
        visitType: String = "SEARCHED",
        userNotes: String? = null
    ): Long {
        val entity = LocationHistoryEntity(
            name = name,
            countryCode = countryCode,
            capital = capital,
            continent = continent,
            latitude = latitude,
            longitude = longitude,
            timestamp = System.currentTimeMillis(),
            visitType = visitType,
            userNotes = userNotes
        )
        return dao.insertLocation(entity)
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        dao.setFavorite(id, isFavorite)
    }

    suspend fun updateNotes(id: Long, notes: String?) {
        dao.updateNotes(id, notes)
    }

    suspend fun delete(id: Long) {
        dao.deleteById(id)
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
