package com.example.data.local

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class SavedPlaceItem(
    val id: String,
    val title: String,
    val category: String, // "HOME", "WORK", "FAVORITE", "PIN"
    val latitude: Double,
    val longitude: Double,
    val addressOrNote: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

data class SavedTripItem(
    val id: String,
    val startTimeMs: Long,
    val durationSec: Long,
    val distanceKm: Double,
    val avgSpeedKmh: Float,
    val maxSpeedKmh: Float,
    val startRoadOrCoords: String,
    val endRoadOrCoords: String
)

class LocalFreeFeaturesStore(context: Context) {
    private val prefs = context.getSharedPreferences("earthscope_free_features_prefs", Context.MODE_PRIVATE)

    fun getSpeedLimitKmh(): Int = prefs.getInt("speed_limit_kmh", 80)

    fun setSpeedLimitKmh(limitKmh: Int) {
        prefs.edit().putInt("speed_limit_kmh", limitKmh.coerceIn(20, 220)).apply()
    }

    fun isSpeedSoundAlertEnabled(): Boolean = prefs.getBoolean("speed_sound_alert", true)

    fun setSpeedSoundAlertEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("speed_sound_alert", enabled).apply()
    }

    fun isSpeedVibrationAlertEnabled(): Boolean = prefs.getBoolean("speed_vibration_alert", true)

    fun setSpeedVibrationAlertEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("speed_vibration_alert", enabled).apply()
    }

    fun hasAcceptedPrivacyAndTerms(): Boolean = prefs.getBoolean("accepted_privacy_and_terms_v2", false)

    fun setAcceptedPrivacyAndTerms(accepted: Boolean) {
        prefs.edit().putBoolean("accepted_privacy_and_terms_v2", accepted).apply()
    }

    fun getHomePlace(): SavedPlaceItem? = getPlaceByKey("saved_home_place")

    fun setHomePlace(lat: Double, lon: Double, label: String) {
        val item = SavedPlaceItem(
            id = "home",
            title = "Home",
            category = "HOME",
            latitude = lat,
            longitude = lon,
            addressOrNote = label
        )
        savePlaceByKey("saved_home_place", item)
    }

    fun getWorkPlace(): SavedPlaceItem? = getPlaceByKey("saved_work_place")

    fun setWorkPlace(lat: Double, lon: Double, label: String) {
        val item = SavedPlaceItem(
            id = "work",
            title = "Work",
            category = "WORK",
            latitude = lat,
            longitude = lon,
            addressOrNote = label
        )
        savePlaceByKey("saved_work_place", item)
    }

    private fun getPlaceByKey(key: String): SavedPlaceItem? {
        val raw = prefs.getString(key, null) ?: return null
        return try {
            val obj = JSONObject(raw)
            SavedPlaceItem(
                id = obj.optString("id", key),
                title = obj.optString("title", "Saved"),
                category = obj.optString("category", "FAVORITE"),
                latitude = obj.optDouble("latitude", 0.0),
                longitude = obj.optDouble("longitude", 0.0),
                addressOrNote = obj.optString("addressOrNote", ""),
                timestamp = obj.optLong("timestamp", System.currentTimeMillis())
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun savePlaceByKey(key: String, item: SavedPlaceItem) {
        val obj = JSONObject().apply {
            put("id", item.id)
            put("title", item.title)
            put("category", item.category)
            put("latitude", item.latitude)
            put("longitude", item.longitude)
            put("addressOrNote", item.addressOrNote)
            put("timestamp", item.timestamp)
        }
        prefs.edit().putString(key, obj.toString()).apply()
    }

    fun getFavoritePlaces(): List<SavedPlaceItem> {
        val raw = prefs.getString("favorite_places_list", null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            val list = mutableListOf<SavedPlaceItem>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    SavedPlaceItem(
                        id = obj.optString("id"),
                        title = obj.optString("title"),
                        category = obj.optString("category", "FAVORITE"),
                        latitude = obj.optDouble("latitude"),
                        longitude = obj.optDouble("longitude"),
                        addressOrNote = obj.optString("addressOrNote", ""),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis())
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun addFavoritePlace(title: String, lat: Double, lon: Double, category: String = "FAVORITE", note: String = "") {
        val current = getFavoritePlaces().toMutableList()
        val item = SavedPlaceItem(
            id = "fav_${System.currentTimeMillis()}",
            title = title,
            category = category,
            latitude = lat,
            longitude = lon,
            addressOrNote = note,
            timestamp = System.currentTimeMillis()
        )
        current.add(0, item)
        saveFavoriteList(current)
    }

    fun removeFavoritePlace(id: String) {
        val current = getFavoritePlaces().filterNot { it.id == id }
        saveFavoriteList(current)
    }

    private fun saveFavoriteList(list: List<SavedPlaceItem>) {
        val arr = JSONArray()
        list.take(100).forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("title", item.title)
                put("category", item.category)
                put("latitude", item.latitude)
                put("longitude", item.longitude)
                put("addressOrNote", item.addressOrNote)
                put("timestamp", item.timestamp)
            }
            arr.put(obj)
        }
        prefs.edit().putString("favorite_places_list", arr.toString()).apply()
    }

    fun getSavedTrips(): List<SavedTripItem> {
        val raw = prefs.getString("saved_trips_list", null) ?: return emptyList()
        return try {
            val arr = JSONArray(raw)
            val list = mutableListOf<SavedTripItem>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    SavedTripItem(
                        id = obj.optString("id"),
                        startTimeMs = obj.optLong("startTimeMs"),
                        durationSec = obj.optLong("durationSec"),
                        distanceKm = obj.optDouble("distanceKm"),
                        avgSpeedKmh = obj.optDouble("avgSpeedKmh", 0.0).toFloat(),
                        maxSpeedKmh = obj.optDouble("maxSpeedKmh", 0.0).toFloat(),
                        startRoadOrCoords = obj.optString("startRoadOrCoords", ""),
                        endRoadOrCoords = obj.optString("endRoadOrCoords", "")
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    fun addSavedTrip(trip: SavedTripItem) {
        val current = getSavedTrips().toMutableList()
        current.add(0, trip)
        saveTripsList(current)
    }

    fun deleteSavedTrip(id: String) {
        val current = getSavedTrips().filterNot { it.id == id }
        saveTripsList(current)
    }

    fun clearAllTrips() {
        prefs.edit().remove("saved_trips_list").apply()
    }

    private fun saveTripsList(list: List<SavedTripItem>) {
        val arr = JSONArray()
        list.take(100).forEach { item ->
            val obj = JSONObject().apply {
                put("id", item.id)
                put("startTimeMs", item.startTimeMs)
                put("durationSec", item.durationSec)
                put("distanceKm", item.distanceKm)
                put("avgSpeedKmh", item.avgSpeedKmh.toDouble())
                put("maxSpeedKmh", item.maxSpeedKmh.toDouble())
                put("startRoadOrCoords", item.startRoadOrCoords)
                put("endRoadOrCoords", item.endRoadOrCoords)
            }
            arr.put(obj)
        }
        prefs.edit().putString("saved_trips_list", arr.toString()).apply()
    }
}
