package com.example.globe

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import com.example.data.model.GeoPoint
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

data class ProjectedPoint(
    val offset: Offset,
    val isVisible: Boolean,
    val depth: Float // > 0 is front side, 1.0 is dead center
)

object GlobeMath {
    private const val DEG_TO_RAD = PI / 180.0
    private const val RAD_TO_DEG = 180.0 / PI

    /**
     * Orthographic projection of (latDeg, lonDeg) onto a 3D sphere centered at (centerLatDeg, centerLonDeg).
     * When latDeg == centerLatDeg and lonDeg == centerLonDeg:
     * x2 = 1.0 (depth = 1.0), y2 = 0.0 (sx = cx), z2 = 0.0 (sy = cy).
     * If bearingDeg != 0f (e.g. in live Compass mode), the projected screen coordinates
     * rotate around (cx, cy) by -bearingDeg so the user's heading points to the top of the screen.
     */
    fun project(
        latDeg: Double,
        lonDeg: Double,
        centerLatDeg: Double,
        centerLonDeg: Double,
        radius: Float,
        cx: Float,
        cy: Float,
        bearingDeg: Float = 0f
    ): ProjectedPoint {
        val phi = latDeg * DEG_TO_RAD
        val lambda = lonDeg * DEG_TO_RAD
        val phi0 = centerLatDeg * DEG_TO_RAD
        val lambda0 = centerLonDeg * DEG_TO_RAD

        // 3D Cartesian coordinates on unit sphere
        val x = cos(phi) * cos(lambda)
        val y = cos(phi) * sin(lambda)
        val z = sin(phi)

        // Rotate around Z axis by -lambda0
        val cosLon = cos(-lambda0)
        val sinLon = sin(-lambda0)
        val x1 = x * cosLon - y * sinLon
        val y1 = x * sinLon + y * cosLon
        val z1 = z

        // Rotate around Y axis so (phi0, lambda0) maps to (x2=1, y2=0, z2=0)
        val cosLat = cos(phi0)
        val sinLat = sin(phi0)
        val x2 = x1 * cosLat + z1 * sinLat  // Depth forward towards camera
        val y2 = y1                         // Screen X (East)
        val z2 = z1 * cosLat - x1 * sinLat  // Screen Y (North)

        val isVisible = x2 >= -0.01

        val rotY: Double
        val rotZ: Double
        if (abs(bearingDeg) > 0.01f) {
            val bRad = bearingDeg.toDouble() * DEG_TO_RAD
            val cosB = cos(bRad)
            val sinB = sin(bRad)
            rotY = y2 * cosB - z2 * sinB
            rotZ = y2 * sinB + z2 * cosB
        } else {
            rotY = y2
            rotZ = z2
        }

        val sx = cx + (rotY.toFloat() * radius)
        val sy = cy - (rotZ.toFloat() * radius)

        return ProjectedPoint(
            offset = Offset(sx, sy),
            isVisible = isVisible,
            depth = x2.toFloat()
        )
    }

    /**
     * Builds a closed Path for a spherical polygon (continent or country) that cleanly wraps
     * behind-the-horizon vertices along the outer circular rim instead of cutting straight
     * chord lines across the visible ocean.
     * Returns Pair(fillPath, strokePath) and visible vertex count.
     */
    fun buildSphericalPolygonPaths(
        boundary: List<GeoPoint>,
        centerLatDeg: Double,
        centerLonDeg: Double,
        radius: Float,
        cx: Float,
        cy: Float
    ): Triple<Path, Path, Int> {
        val fillPath = Path()
        val strokePath = Path()
        if (boundary.size < 3) return Triple(fillPath, strokePath, 0)

        val phi0 = centerLatDeg * DEG_TO_RAD
        val lambda0 = centerLonDeg * DEG_TO_RAD
        val cosLon = cos(-lambda0)
        val sinLon = sin(-lambda0)
        val cosLat = cos(phi0)
        val sinLat = sin(phi0)

        var visibleCount = 0
        var strokeStarted = false
        var prevRimAngle: Double? = null
        val rimRadius = radius * 1.03f

        for (i in boundary.indices) {
            val geo = boundary[i]
            val phi = geo.lat * DEG_TO_RAD
            val lambda = geo.lon * DEG_TO_RAD

            val x = cos(phi) * cos(lambda)
            val y = cos(phi) * sin(lambda)
            val z = sin(phi)

            val x1 = x * cosLon - y * sinLon
            val y1 = x * sinLon + y * cosLon
            val z1 = z

            val x2 = x1 * cosLat + z1 * sinLat
            val y2 = y1
            val z2 = z1 * cosLat - x1 * sinLat

            if (x2 >= 0.0) {
                visibleCount++
                val sx = cx + (y2.toFloat() * radius)
                val sy = cy - (z2.toFloat() * radius)

                if (i == 0) {
                    fillPath.moveTo(sx, sy)
                } else {
                    fillPath.lineTo(sx, sy)
                }

                if (!strokeStarted) {
                    strokePath.moveTo(sx, sy)
                    strokeStarted = true
                } else {
                    strokePath.lineTo(sx, sy)
                }
                prevRimAngle = null
            } else {
                strokeStarted = false
                val r = sqrt(y2 * y2 + z2 * z2).coerceAtLeast(1e-6)
                val curAngle = atan2(z2 / r, y2 / r)

                if (i == 0) {
                    val sx = cx + (cos(curAngle).toFloat() * rimRadius)
                    val sy = cy - (sin(curAngle).toFloat() * rimRadius)
                    fillPath.moveTo(sx, sy)
                } else if (prevRimAngle != null) {
                    // Interpolate along circular horizon rim so polygon never cuts across the globe face
                    var diff = curAngle - prevRimAngle
                    while (diff > PI) diff -= 2.0 * PI
                    while (diff < -PI) diff += 2.0 * PI
                    val arcSteps = max(1, (abs(diff) / (PI / 12.0)).toInt())
                    for (s in 1..arcSteps) {
                        val a = prevRimAngle + diff * (s.toDouble() / arcSteps)
                        val sx = cx + (cos(a).toFloat() * rimRadius)
                        val sy = cy - (sin(a).toFloat() * rimRadius)
                        fillPath.lineTo(sx, sy)
                    }
                } else {
                    val sx = cx + (cos(curAngle).toFloat() * rimRadius)
                    val sy = cy - (sin(curAngle).toFloat() * rimRadius)
                    fillPath.lineTo(sx, sy)
                }
                prevRimAngle = curAngle
            }
        }

        if (visibleCount > 0) {
            fillPath.close()
        }
        return Triple(fillPath, strokePath, visibleCount)
    }

    /**
     * Convert screen tap (screenX, screenY) back to geographic (lat, lon) on the 3D globe.
     * Exact mathematical inverse of project(), accounting for compass bearingDeg rotation.
     * Returns null if tapped outside the globe circle.
     */
    fun unproject(
        screenX: Float,
        screenY: Float,
        centerLatDeg: Double,
        centerLonDeg: Double,
        radius: Float,
        cx: Float,
        cy: Float,
        bearingDeg: Float = 0f
    ): Pair<Double, Double>? {
        if (radius <= 0f) return null
        val rawDx = (screenX - cx) / radius
        val rawDy = (cy - screenY) / radius
        val r2 = rawDx * rawDx + rawDy * rawDy
        if (r2 > 1.0f) return null // Outside globe perimeter

        // Inverse of the 2D screen bearing rotation (-bearingDeg -> +bearingDeg)
        val dx: Double
        val dy: Double
        if (abs(bearingDeg) > 0.01f) {
            val bRad = bearingDeg.toDouble() * DEG_TO_RAD
            val cosB = cos(bRad)
            val sinB = sin(bRad)
            dx = rawDx * cosB + rawDy * sinB
            dy = -rawDx * sinB + rawDy * cosB
        } else {
            dx = rawDx.toDouble()
            dy = rawDy.toDouble()
        }

        val x2 = sqrt(max(0.0, 1.0 - r2.toDouble()))
        val y2 = dx
        val z2 = dy

        val phi0 = centerLatDeg * DEG_TO_RAD
        val lambda0 = centerLonDeg * DEG_TO_RAD

        // Exact inverse (transpose) of the Y rotation in project()
        val cosLat = cos(phi0)
        val sinLat = sin(phi0)
        val x1 = x2 * cosLat - z2 * sinLat
        val y1 = y2
        val z1 = x2 * sinLat + z2 * cosLat

        // Exact inverse of the Z rotation (-lambda0 -> +lambda0)
        val cosLon = cos(lambda0)
        val sinLon = sin(lambda0)
        val x = x1 * cosLon - y1 * sinLon
        val y = x1 * sinLon + y1 * cosLon
        val z = z1

        val lat = asin(min(1.0, max(-1.0, z))) * RAD_TO_DEG
        var lon = atan2(y, x) * RAD_TO_DEG
        if (lon > 180.0) lon -= 360.0
        if (lon < -180.0) lon += 360.0

        return Pair(lat, lon)
    }

    /**
     * Exact geodesic distance between two coordinates in kilometers using the WGS-84
     * ellipsoid (Vincenty's inverse formula with Haversine fallback for nearly antipodal points).
     */
    fun greatCircleDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        if (lat1 == lat2 && lon1 == lon2) return 0.0

        // WGS-84 ellipsoid parameters
        val a = 6378137.0 // semi-major axis in meters
        val f = 1.0 / 298.257223563 // flattening
        val b = (1.0 - f) * a // semi-minor axis in meters

        val phi1 = lat1 * DEG_TO_RAD
        val phi2 = lat2 * DEG_TO_RAD
        val l = (lon2 - lon1) * DEG_TO_RAD

        val u1 = atan2((1.0 - f) * sin(phi1), cos(phi1))
        val u2 = atan2((1.0 - f) * sin(phi2), cos(phi2))
        val sinU1 = sin(u1)
        val cosU1 = cos(u1)
        val sinU2 = sin(u2)
        val cosU2 = cos(u2)

        var lambda = l
        var iterLimit = 100
        var cosSqAlpha = 0.0
        var sinSigma = 0.0
        var cos2SigmaM = 0.0
        var cosSigma = 0.0
        var sigma = 0.0

        do {
            val sinLambda = sin(lambda)
            val cosLambda = cos(lambda)
            val term1 = cosU2 * sinLambda
            val term2 = cosU1 * sinU2 - sinU1 * cosU2 * cosLambda
            sinSigma = sqrt(term1 * term1 + term2 * term2)
            if (sinSigma == 0.0) return 0.0 // Co-incident points
            cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda
            sigma = atan2(sinSigma, cosSigma)
            val sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma
            cosSqAlpha = 1.0 - sinAlpha * sinAlpha
            cos2SigmaM = if (cosSqAlpha != 0.0) {
                cosSigma - 2.0 * sinU1 * sinU2 / cosSqAlpha
            } else {
                0.0
            }
            val c = f / 16.0 * cosSqAlpha * (4.0 + f * (4.0 - 3.0 * cosSqAlpha))
            val lambdaP = lambda
            lambda = l + (1.0 - c) * f * sinAlpha * (
                sigma + c * sinSigma * (
                    cos2SigmaM + c * cosSigma * (-1.0 + 2.0 * cos2SigmaM * cos2SigmaM)
                )
            )
            if (abs(lambda - lambdaP) <= 1e-12) break
        } while (--iterLimit > 0)

        if (iterLimit == 0) {
            // Fallback to spherical Haversine if Vincenty fails to converge
            val dLat = (lat2 - lat1) * DEG_TO_RAD
            val dLon = (lon2 - lon1) * DEG_TO_RAD
            val h = sin(dLat / 2.0) * sin(dLat / 2.0) +
                cos(phi1) * cos(phi2) * sin(dLon / 2.0) * sin(dLon / 2.0)
            return 6371.0088 * 2.0 * atan2(sqrt(h), sqrt(max(0.0, 1.0 - h)))
        }

        val uSq = cosSqAlpha * (a * a - b * b) / (b * b)
        val bigA = 1.0 + uSq / 16384.0 * (4096.0 + uSq * (-768.0 + uSq * (320.0 - 175.0 * uSq)))
        val bigB = uSq / 1024.0 * (256.0 + uSq * (-128.0 + uSq * (74.0 - 47.0 * uSq)))
        val deltaSigma = bigB * sinSigma * (
            cos2SigmaM + bigB / 4.0 * (
                cosSigma * (-1.0 + 2.0 * cos2SigmaM * cos2SigmaM) -
                    bigB / 6.0 * cos2SigmaM * (-3.0 + 4.0 * sinSigma * sinSigma) * (-3.0 + 4.0 * cos2SigmaM * cos2SigmaM)
            )
        )

        val distanceMeters = b * bigA * (sigma - deltaSigma)
        return distanceMeters / 1000.0
    }

    /**
     * Exact capital-to-capital distance in kilometers between two countries.
     */
    fun exactCapitalDistanceKm(start: com.example.data.model.CountryInfo, end: com.example.data.model.CountryInfo): Double {
        return greatCircleDistanceKm(
            start.capitalLat,
            start.capitalLon,
            end.capitalLat,
            end.capitalLon
        )
    }

    /**
     * Exact geographical center-to-center distance in kilometers between two countries.
     */
    fun exactCentroidDistanceKm(start: com.example.data.model.CountryInfo, end: com.example.data.model.CountryInfo): Double {
        return greatCircleDistanceKm(
            start.centerLat,
            start.centerLon,
            end.centerLat,
            end.centerLon
        )
    }

    /**
     * Spherical linear interpolation (SLERP) along the shortest Great-Circle route
     * between (lat1, lon1) and (lat2, lon2) at fraction t in [0.0, 1.0].
     */
    fun interpolateGreatCircle(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double,
        t: Float
    ): GeoPoint {
        val phi1 = lat1 * DEG_TO_RAD
        val lam1 = lon1 * DEG_TO_RAD
        val phi2 = lat2 * DEG_TO_RAD
        val lam2 = lon2 * DEG_TO_RAD

        val x1 = cos(phi1) * cos(lam1)
        val y1 = cos(phi1) * sin(lam1)
        val z1 = sin(phi1)

        val x2 = cos(phi2) * cos(lam2)
        val y2 = cos(phi2) * sin(lam2)
        val z2 = sin(phi2)

        val dot = (x1 * x2 + y1 * y2 + z1 * z2).coerceIn(-1.0, 1.0)
        val omega = kotlin.math.acos(dot)
        if (omega < 1e-5) return GeoPoint(lat2, lon2)

        val sinOmega = sin(omega)
        val f1 = sin((1.0 - t) * omega) / sinOmega
        val f2 = sin(t * omega) / sinOmega

        val x = f1 * x1 + f2 * x2
        val y = f1 * y1 + f2 * y2
        val z = f1 * z1 + f2 * z2

        val lat = asin(z.coerceIn(-1.0, 1.0)) * RAD_TO_DEG
        val lon = atan2(y, x) * RAD_TO_DEG
        return GeoPoint(lat, lon)
    }
}

