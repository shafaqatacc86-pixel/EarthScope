package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.GlobeDatabase
import com.example.data.local.LocationHistoryEntity
import com.example.data.model.WorldGeographicData
import com.example.globe.GlobeMath
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("World Globe", appName)
    }

    @Test
    fun `test country search finds Pakistan`() {
        val found = WorldGeographicData.findCountry("Pakistan")
        assertNotNull(found)
        assertEquals("PK", found?.id)
        assertEquals("Islamabad", found?.capital)
    }

    @Test
    fun `test 3D globe projection math`() {
        // Center point at 0, 0 with center at 0, 0 should project to dead center (cx, cy)
        val proj = GlobeMath.project(
            latDeg = 0.0,
            lonDeg = 0.0,
            centerLatDeg = 0.0,
            centerLonDeg = 0.0,
            radius = 300f,
            cx = 500f,
            cy = 500f
        )
        assertTrue(proj.isVisible)
        assertEquals(500f, proj.offset.x, 1f)
        assertEquals(500f, proj.offset.y, 1f)
        assertTrue(proj.depth > 0.99f)
    }

    @Test
    fun `test room location history insertion and query`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = GlobeDatabase.getInstance(context)
        val dao = db.locationHistoryDao()

        val id = dao.insertLocation(
            LocationHistoryEntity(
                name = "Pakistan",
                countryCode = "PK",
                capital = "Islamabad",
                continent = "Asia",
                latitude = 30.3753,
                longitude = 69.3451,
                visitType = "SEARCHED"
            )
        )
        assertTrue(id > 0)

        val all = dao.getAllHistory().first()
        assertTrue(all.any { it.name == "Pakistan" })
    }
}
