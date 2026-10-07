package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationHistoryDao {
    @Query("SELECT * FROM location_history ORDER BY timestamp DESC")
    fun getAllHistory(): Flow<List<LocationHistoryEntity>>

    @Query("SELECT * FROM location_history WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteLocations(): Flow<List<LocationHistoryEntity>>

    @Query("SELECT * FROM location_history WHERE name LIKE '%' || :query || '%' ORDER BY timestamp DESC")
    fun searchHistory(query: String): Flow<List<LocationHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(item: LocationHistoryEntity): Long

    @Update
    suspend fun updateLocation(item: LocationHistoryEntity)

    @Query("UPDATE location_history SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    @Query("UPDATE location_history SET userNotes = :notes WHERE id = :id")
    suspend fun updateNotes(id: Long, notes: String?)

    @Query("DELETE FROM location_history WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM location_history")
    suspend fun clearAll()
}
