package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [LocationHistoryEntity::class, MapFloatingPinEntity::class],
    version = 4,
    exportSchema = false
)
abstract class GlobeDatabase : RoomDatabase() {
    abstract fun locationHistoryDao(): LocationHistoryDao
    abstract fun mapFloatingPinDao(): MapFloatingPinDao

    companion object {
        @Volatile
        private var INSTANCE: GlobeDatabase? = null

        fun getInstance(context: Context): GlobeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GlobeDatabase::class.java,
                    "globe_locations.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
