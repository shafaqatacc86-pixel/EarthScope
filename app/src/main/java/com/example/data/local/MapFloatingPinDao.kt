package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MapFloatingPinDao {
    @Query("SELECT * FROM map_floating_pins ORDER BY timestamp DESC")
    fun getAllFloatingPins(): Flow<List<MapFloatingPinEntity>>

    @Query("SELECT * FROM map_floating_pins WHERE id = :id LIMIT 1")
    suspend fun getPinById(id: Long): MapFloatingPinEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFloatingPin(pin: MapFloatingPinEntity): Long

    @Query("DELETE FROM map_floating_pins WHERE id = :id")
    suspend fun deleteFloatingPinById(id: Long)

    @Query("DELETE FROM map_floating_pins")
    suspend fun clearAllFloatingPins()
}
