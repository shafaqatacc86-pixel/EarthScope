package com.example.globe

import android.graphics.Color as AndroidColor
import android.graphics.Paint as AndroidPaint
import android.graphics.Typeface
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import com.example.data.model.CountryInfo
import com.example.data.model.GeoPoint
import com.example.data.model.WorldGeographicData
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

data class Star(
    val x: Float,
    val y: Float,
    val radius: Float,
    val alpha: Float,
    val speed: Float,
    val phaseOffset: Float
)

@Composable
fun GlobeCanvas(
    centerLat: Double,
    centerLon: Double,
    zoomScale: Float,
    selectedCountry: CountryInfo?,
    highlightedCustomPoint: GeoPoint?,
    userLocation: GeoPoint?,
    routeStartCountry: CountryInfo? = null,
    routeEndCountry: CountryInfo? = null,
    compassBearingDeg: Float = 0f,
    isCompassModeActive: Boolean = false,
    isLightTheme: Boolean = false,
    modifier: Modifier = Modifier,
    onRotateDrag: (Float, Float) -> Unit,
    onZoomChange: (deltaScale: Float, focalLat: Double?, focalLon: Double?) -> Unit,
    onGlobeTap: (screenX: Float, screenY: Float, radius: Float, cx: Float, cy: Float) -> Unit,
    onFling: (vx: Float, vy: Float) -> Unit = { _, _ -> },
    onStopFling: () -> Unit = {}
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseProgress"
    )
    val starDriftProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(24000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "starDriftProgress"
    )
    val flightProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(8200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "flightProgress"
    )

    // Generate moving & twinkling stars in the cosmos
    val stars = remember {
        val rand = Random(42)
        List(115) {
            Star(
                x = rand.nextFloat(),
                y = rand.nextFloat(),
                radius = rand.nextFloat() * 1.7f + 0.6f,
                alpha = rand.nextFloat() * 0.65f + 0.25f,
                speed = rand.nextFloat() * 0.6f + 0.2f,
                phaseOffset = rand.nextFloat() * 6.28f
            )
        }
    }

    // Android paints for text rendering on canvas (adaptive for Light / Dark theme)
    val labelPaint = remember(isLightTheme) {
        AndroidPaint().apply {
            color = if (isLightTheme) {
                AndroidColor.argb(245, 15, 23, 42) // Crisp Slate-900 for White Theme
            } else {
                AndroidColor.argb(225, 241, 245, 249) // Clean Slate-100 for Dark Theme
            }
            textSize = 30f
            typeface = Typeface.create(
                Typeface.SANS_SERIF,
                if (isLightTheme) Typeface.BOLD else Typeface.NORMAL
            )
            textAlign = AndroidPaint.Align.CENTER
            isAntiAlias = true
            if (isLightTheme) {
                setShadowLayer(5f, 0f, 1f, AndroidColor.argb(220, 255, 255, 255))
            } else {
                setShadowLayer(4f, 0f, 2f, AndroidColor.argb(220, 0, 0, 0))
            }
        }
    }

    val highlightedLabelPaint = remember(isLightTheme) {
        AndroidPaint().apply {
            color = if (isLightTheme) {
                AndroidColor.argb(255, 21, 128, 61) // Deep Rich Green #15803D on light land
            } else {
                AndroidColor.argb(255, 74, 222, 128) // Neon Green #4ADE80 on dark land
            }
            textSize = 34f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = AndroidPaint.Align.CENTER
            isAntiAlias = true
            if (isLightTheme) {
                setShadowLayer(8f, 0f, 0f, AndroidColor.argb(255, 255, 255, 255))
            } else {
                setShadowLayer(8f, 0f, 0f, AndroidColor.argb(255, 34, 197, 94))
            }
        }
    }

    val oceanLabelPaint = remember(isLightTheme) {
        AndroidPaint().apply {
            color = if (isLightTheme) {
                AndroidColor.argb(175, 3, 105, 161) // Deep Ocean Blue #0369A1
            } else {
                AndroidColor.argb(130, 147, 197, 253) // Soft blue italic
            }
            textSize = 28f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.ITALIC)
            textAlign = AndroidPaint.Align.CENTER
            isAntiAlias = true
            letterSpacing = 0.15f
        }
    }

    val compassCardinalPaint = remember(isLightTheme) {
        AndroidPaint().apply {
            textSize = 30f
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textAlign = AndroidPaint.Align.CENTER
            isAntiAlias = true
        }
    }

    val currentCenterLat by rememberUpdatedState(centerLat)
    val currentCenterLon by rememberUpdatedState(centerLon)
    val currentZoomScale by rememberUpdatedState(zoomScale)
    val currentCompassBearing by rememberUpdatedState(compassBearingDeg)
    val currentOnRotateDrag by rememberUpdatedState(onRotateDrag)
    val currentOnZoomChange by rememberUpdatedState(onZoomChange)
    val currentOnGlobeTap by rememberUpdatedState(onGlobeTap)
    val currentOnFling by rememberUpdatedState(onFling)
    val currentOnStopFling by rememberUpdatedState(onStopFling)

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                awaitEachGesture {
                    val velocityTracker = VelocityTracker()
                    val down = awaitFirstDown(requireUnconsumed = false)
                    currentOnStopFling()
                    velocityTracker.addPosition(down.uptimeMillis, down.position)

                    val startTime = System.currentTimeMillis()
                    val startPos = down.position
                    var lastPos = startPos
                    var totalDragDistance = 0f
                    var isPinching = false

                    var prevCentroid = startPos
                    var prevSpan = 0f

                    while (true) {
                        val event = awaitPointerEvent()
                        val activePointers = event.changes.filter { it.pressed }
                        if (activePointers.isEmpty()) break

                        if (activePointers.size == 1) {
                            val change = activePointers[0]
                            lastPos = change.position
                            velocityTracker.addPosition(change.uptimeMillis, change.position)
                            val dragAmount = change.position - change.previousPosition
                            val dist = hypot(dragAmount.x, dragAmount.y)
                            totalDragDistance += dist

                            if (totalDragDistance > 5f && !isPinching) {
                                change.consume()
                                // Un-rotate drag vector by current compass bearing so dragging feels natural at any compass angle
                                val bRad = currentCompassBearing * (PI / 180.0)
                                val cosB = cos(bRad).toFloat()
                                val sinB = sin(bRad).toFloat()
                                val adjDx = dragAmount.x * cosB - dragAmount.y * sinB
                                val adjDy = dragAmount.x * sinB + dragAmount.y * cosB
                                currentOnRotateDrag(adjDx, adjDy)
                            }
                        } else if (activePointers.size >= 2) {
                            isPinching = true
                            totalDragDistance += 100f // prevent accidental tap

                            var sumX = 0f
                            var sumY = 0f
                            for (p in activePointers) {
                                sumX += p.position.x
                                sumY += p.position.y
                            }
                            val centroid = Offset(sumX / activePointers.size, sumY / activePointers.size)

                            var sumDist = 0f
                            for (p in activePointers) {
                                val dx = p.position.x - centroid.x
                                val dy = p.position.y - centroid.y
                                sumDist += hypot(dx, dy)
                            }
                            val span = sumDist / activePointers.size

                            if (prevSpan > 0f && span > 0f) {
                                val rawFactor = span / prevSpan
                                val zoomFactor = 1f + (rawFactor - 1f) * 0.95f
                                if (kotlin.math.abs(zoomFactor - 1f) > 0.0005f) {
                                    val cx = size.width / 2f
                                    val cy = size.height / 2f
                                    val radius = min(size.width, size.height) * 0.43f * currentZoomScale
                                    val geo = GlobeMath.unproject(
                                        centroid.x,
                                        centroid.y,
                                        currentCenterLat,
                                        currentCenterLon,
                                        radius,
                                        cx,
                                        cy,
                                        currentCompassBearing
                                    )
                                    currentOnZoomChange(zoomFactor, geo?.first, geo?.second)
                                }

                                val pan = centroid - prevCentroid
                                if (hypot(pan.x, pan.y) > 0.8f) {
                                    val bRad = currentCompassBearing * (PI / 180.0)
                                    val cosB = cos(bRad).toFloat()
                                    val sinB = sin(bRad).toFloat()
                                    val adjDx = pan.x * cosB - pan.y * sinB
                                    val adjDy = pan.x * sinB + pan.y * cosB
                                    currentOnRotateDrag(adjDx, adjDy)
                                }
                            }

                            prevCentroid = centroid
                            prevSpan = span
                            for (p in activePointers) {
                                p.consume()
                            }
                        }
                    }

                    val elapsed = System.currentTimeMillis() - startTime
                    if (totalDragDistance < 18f && elapsed < 600L && !isPinching) {
                        val cx = size.width / 2f
                        val cy = size.height / 2f
                        val radius = min(size.width, size.height) * 0.43f * currentZoomScale
                        currentOnGlobeTap(lastPos.x, lastPos.y, radius, cx, cy)
                    } else if (!isPinching && totalDragDistance > 12f) {
                        val velocity = velocityTracker.calculateVelocity()
                        val bRad = currentCompassBearing * (PI / 180.0)
                        val cosB = cos(bRad).toFloat()
                        val sinB = sin(bRad).toFloat()
                        val adjVx = velocity.x * cosB - velocity.y * sinB
                        val adjVy = velocity.x * sinB + velocity.y * cosB
                        currentOnFling(adjVx, adjVy)
                    }
                }
            }
    ) {
        val width = size.width
        val height = size.height
        val cx = width / 2f
        val cy = height / 2f
        val radius = min(width, height) * 0.43f * zoomScale

        // 1. Draw Background (Daylight Sky / Starry Deep Space) with moving particles/stars
        drawSpaceBackground(width, height, stars, starDriftProgress, centerLon, isLightTheme)

        // 2. Outer Atmospheric Glow (Halo around globe)
        drawAtmosphericHalo(cx, cy, radius, isLightTheme)

        // 3. Globe Sphere Base & Graticule & Landmasses
        val sphereClipPath = Path().apply {
            addOval(Rect(cx - radius, cy - radius, cx + radius, cy + radius))
        }

        clipPath(sphereClipPath) {
            // Ocean sphere fill with radial depth lighting
            drawSphereOcean(cx, cy, radius, isLightTheme)

            // Rotate all geographical paths, borders, beacons, and routes by -compassBearingDeg around globe center
            withTransform({
                if (kotlin.math.abs(compassBearingDeg) > 0.01f) {
                    rotate(degrees = -compassBearingDeg, pivot = Offset(cx, cy))
                }
            }) {
                // Latitude and Longitude Graticule grid
                drawGraticules(centerLat, centerLon, radius, cx, cy, isLightTheme)

                // Continents base landmasses
                drawContinents(centerLat, centerLon, radius, cx, cy, isLightTheme)

                // Countries outlines and land polygons
                drawAllCountries(centerLat, centerLon, radius, cx, cy, selectedCountry?.id, isLightTheme)

                // Highlighted Start Country (if flight route is active and different from selectedCountry)
                if (routeStartCountry != null && routeStartCountry.id != selectedCountry?.id) {
                    drawHighlightedCountry(
                        country = routeStartCountry,
                        centerLat = centerLat,
                        centerLon = centerLon,
                        radius = radius,
                        cx = cx,
                        cy = cy
                    )
                }

                // Highlighted Country (Neon Green Glow, Border and Fill)
                if (selectedCountry != null) {
                    drawHighlightedCountry(
                        country = selectedCountry,
                        centerLat = centerLat,
                        centerLon = centerLon,
                        radius = radius,
                        cx = cx,
                        cy = cy
                    )
                }

                // Active Beacon Dot (always placed at the exact clicked point or searched/selected coordinate)
                val beaconPoint = highlightedCustomPoint
                    ?: selectedCountry?.let { GeoPoint(it.centerLat, it.centerLon) }
                if (beaconPoint != null) {
                    drawLocationBeacon(
                        point = beaconPoint,
                        centerLat = centerLat,
                        centerLon = centerLon,
                        radius = radius,
                        cx = cx,
                        cy = cy,
                        pulseProgress = pulseProgress
                    )
                }

                // 3D Flight Route Line & Slowly Moving Airplane between Start and End Country
                if (routeStartCountry != null && routeEndCountry != null) {
                    drawFlightRoute(
                        startCountry = routeStartCountry,
                        endCountry = routeEndCountry,
                        centerLat = centerLat,
                        centerLon = centerLon,
                        radius = radius,
                        cx = cx,
                        cy = cy,
                        flightProgress = flightProgress
                    )
                }

                // User GPS Live Location Pin (if different from active beacon)
                if (userLocation != null && userLocation != beaconPoint) {
                    drawGpsLocationPin(
                        location = userLocation,
                        centerLat = centerLat,
                        centerLon = centerLon,
                        radius = radius,
                        cx = cx,
                        cy = cy,
                        pulseProgress = pulseProgress
                    )
                }
            }

            val beaconPoint = highlightedCustomPoint
                ?: selectedCountry?.let { GeoPoint(it.centerLat, it.centerLon) }

            // Draw Geographical Text Labels upright (horizontally readable) at their compass-rotated screen positions!
            drawLabels(
                centerLat = centerLat,
                centerLon = centerLon,
                radius = radius,
                cx = cx,
                cy = cy,
                compassBearingDeg = compassBearingDeg,
                selectedCountry = selectedCountry,
                beaconPoint = beaconPoint,
                labelPaint = labelPaint,
                highlightedLabelPaint = highlightedLabelPaint,
                oceanLabelPaint = oceanLabelPaint
            )

            // Spherical Glass Rim & Shadow (Soft internal atmosphere)
            drawInnerAtmosphereShading(cx, cy, radius, isLightTheme)
        }

        // 4. Bright outer glowing edge ring of the sphere
        drawAtmosphereRimEdge(cx, cy, radius, isLightTheme)

        // 5. When Live Compass Mode is active (or bearing != 0), draw N / S / E / W Cardinal Compass Ring around the globe
        if (isCompassModeActive || kotlin.math.abs(compassBearingDeg) > 0.5f) {
            drawCompassCardinalRing(
                cx = cx,
                cy = cy,
                radius = min(width, height) * 0.43f * zoomScale.coerceIn(0.75f, 1.08f),
                compassBearingDeg = compassBearingDeg,
                isLightTheme = isLightTheme,
                cardinalPaint = compassCardinalPaint
            )
        }
    }
}

private fun DrawScope.drawSpaceBackground(
    width: Float,
    height: Float,
    stars: List<Star>,
    starDriftProgress: Float,
    centerLon: Double,
    isLightTheme: Boolean
) {
    if (isLightTheme) {
        // Crisp Daylight Sky / Pearl White gradient for White Theme
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFFF0F9FF),
                    Color(0xFFE0F2FE),
                    Color(0xFFF8FAFC)
                ),
                startY = 0f,
                endY = height
            )
        )
    } else {
        // Deep midnight space gradient for Black Theme
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF060B19),
                    Color(0xFF030712),
                    Color(0xFF02040A)
                ),
                startY = 0f,
                endY = height
            )
        )
    }

    val lonParallax = (centerLon.toFloat() / 360f) * 0.25f

    // Moving & Twinkling Stars / Atmospheric Dust
    for (star in stars) {
        val driftX = (star.x + starDriftProgress * star.speed * 0.18f - lonParallax * star.speed) % 1.0f
        val normX = if (driftX < 0f) driftX + 1.0f else driftX
        val driftY = (star.y + kotlin.math.sin((starDriftProgress * 6.28f + star.phaseOffset).toDouble()).toFloat() * 0.008f).coerceIn(0f, 1f)

        val twinkle = (0.55f + 0.45f * kotlin.math.sin((starDriftProgress * 18.84f + star.phaseOffset).toDouble()).toFloat())
        val finalAlpha = (star.alpha * twinkle).coerceIn(0.15f, 1.0f)

        val particleColor = if (isLightTheme) {
            Color(0xFF0284C7).copy(alpha = finalAlpha * 0.35f)
        } else {
            Color.White.copy(alpha = finalAlpha)
        }

        drawCircle(
            color = particleColor,
            radius = star.radius,
            center = Offset(normX * width, driftY * height)
        )
    }
}

private fun DrawScope.drawAtmosphericHalo(cx: Float, cy: Float, radius: Float, isLightTheme: Boolean) {
    val haloRadius = radius * 1.28f
    val colors = if (isLightTheme) {
        listOf(
            Color(0x0038BDF8),
            Color(0x2838BDF8),
            Color(0x500EA5E9),
            Color(0x2238BDF8),
            Color(0x00F0F9FF)
        )
    } else {
        listOf(
            Color(0x000284C7),
            Color(0x220284C7),
            Color(0x5500A3FF),
            Color(0x330284C7),
            Color(0x00060B19)
        )
    }
    drawCircle(
        brush = Brush.radialGradient(
            colors = colors,
            center = Offset(cx, cy),
            radius = haloRadius
        ),
        radius = haloRadius,
        center = Offset(cx, cy)
    )
}

private fun DrawScope.drawAtmosphereRimEdge(cx: Float, cy: Float, radius: Float, isLightTheme: Boolean) {
    // Glowing cyan/blue rim around the sphere edge
    drawCircle(
        color = if (isLightTheme) Color(0xFF0284C7).copy(alpha = 0.85f) else Color(0xFF38BDF8).copy(alpha = 0.85f),
        radius = radius,
        center = Offset(cx, cy),
        style = Stroke(width = 2.5f)
    )
    drawCircle(
        color = if (isLightTheme) Color(0xFF38BDF8).copy(alpha = 0.4f) else Color(0xFF0284C7).copy(alpha = 0.35f),
        radius = radius + 2.5f,
        center = Offset(cx, cy),
        style = Stroke(width = 4f)
    )
}

private fun DrawScope.drawSphereOcean(cx: Float, cy: Float, radius: Float, isLightTheme: Boolean) {
    val oceanColors = if (isLightTheme) {
        // Bright Crystal Daytime Ocean for White Theme
        listOf(
            Color(0xFFBAE6FD),
            Color(0xFF7DD3FC),
            Color(0xFF38BDF8),
            Color(0xFF0284C7)
        )
    } else {
        // Spherical ocean gradient: illuminated deep blue at front, dark navy at limbs
        listOf(
            Color(0xFF19324F),
            Color(0xFF102138),
            Color(0xFF0A1626),
            Color(0xFF050C16)
        )
    }
    drawCircle(
        brush = Brush.radialGradient(
            colors = oceanColors,
            center = Offset(cx - radius * 0.15f, cy - radius * 0.15f),
            radius = radius * 1.15f
        ),
        radius = radius,
        center = Offset(cx, cy)
    )
}

private fun DrawScope.drawGraticules(
    centerLat: Double,
    centerLon: Double,
    radius: Float,
    cx: Float,
    cy: Float,
    isLightTheme: Boolean
) {
    val gridColor = if (isLightTheme) {
        Color(0xFF0369A1).copy(alpha = 0.25f)
    } else {
        Color(0xFF1E3A5F).copy(alpha = 0.35f)
    }
    val equatorColor = if (isLightTheme) {
        Color(0xFF0284C7).copy(alpha = 0.55f)
    } else {
        Color(0xFF2563EB).copy(alpha = 0.5f)
    }

    // Parallels (Latitudes)
    val latitudes = listOf(-60.0, -30.0, 0.0, 30.0, 60.0)
    for (lat in latitudes) {
        val path = Path()
        var started = false
        for (lon in -180..180 step 4) {
            val pt = GlobeMath.project(lat, lon.toDouble(), centerLat, centerLon, radius, cx, cy)
            if (pt.isVisible) {
                if (!started) {
                    path.moveTo(pt.offset.x, pt.offset.y)
                    started = true
                } else {
                    path.lineTo(pt.offset.x, pt.offset.y)
                }
            } else {
                started = false
            }
        }
        drawPath(
            path = path,
            color = if (lat == 0.0) equatorColor else gridColor,
            style = Stroke(width = if (lat == 0.0) 1.5f else 1.0f)
        )
    }

    // Meridians (Longitudes)
    for (lon in -180 until 180 step 30) {
        val path = Path()
        var started = false
        for (lat in -85..85 step 4) {
            val pt = GlobeMath.project(lat.toDouble(), lon.toDouble(), centerLat, centerLon, radius, cx, cy)
            if (pt.isVisible) {
                if (!started) {
                    path.moveTo(pt.offset.x, pt.offset.y)
                    started = true
                } else {
                    path.lineTo(pt.offset.x, pt.offset.y)
                }
            } else {
                started = false
            }
        }
        drawPath(
            path = path,
            color = gridColor,
            style = Stroke(width = 1.0f)
        )
    }
}

private fun DrawScope.drawContinents(
    centerLat: Double,
    centerLon: Double,
    radius: Float,
    cx: Float,
    cy: Float,
    isLightTheme: Boolean
) {
    val continentFillColor = if (isLightTheme) {
        Color(0xFFF8FAFC).copy(alpha = 0.96f) // Crisp pearl white landmass for White Theme
    } else {
        Color(0xFF1B2B3E).copy(alpha = 0.95f)
    }
    val continentBorderColor = if (isLightTheme) {
        Color(0xFF64748B).copy(alpha = 0.65f)
    } else {
        Color(0xFF2A4365).copy(alpha = 0.7f)
    }

    for (continent in WorldGeographicData.continents) {
        val (fillPath, strokePath, visibleCount) = GlobeMath.buildSphericalPolygonPaths(
            boundary = continent,
            centerLatDeg = centerLat,
            centerLonDeg = centerLon,
            radius = radius,
            cx = cx,
            cy = cy
        )

        if (visibleCount > 2) {
            drawPath(path = fillPath, color = continentFillColor)
            drawPath(
                path = strokePath,
                color = continentBorderColor,
                style = Stroke(width = 1.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}

private fun DrawScope.drawAllCountries(
    centerLat: Double,
    centerLon: Double,
    radius: Float,
    cx: Float,
    cy: Float,
    selectedCountryId: String?,
    isLightTheme: Boolean
) {
    for (country in WorldGeographicData.countries) {
        if (country.id == selectedCountryId) continue // Highlighted country drawn separately

        if (country.boundary.isNotEmpty()) {
            val (fillPath, strokePath, visibleCount) = GlobeMath.buildSphericalPolygonPaths(
                boundary = country.boundary,
                centerLatDeg = centerLat,
                centerLonDeg = centerLon,
                radius = radius,
                cx = cx,
                cy = cy
            )

            if (visibleCount > 2) {
                val borderColor = Color(WorldGeographicData.getBorderColor(country))
                val countryFill = if (isLightTheme) {
                    borderColor.copy(alpha = 0.28f)
                } else {
                    borderColor.copy(alpha = 0.22f)
                }

                // Fill with subtle matching country tint
                drawPath(path = fillPath, color = countryFill)

                // Draw distinct colored border line
                drawPath(
                    path = strokePath,
                    color = borderColor.copy(alpha = 0.92f),
                    style = Stroke(width = 2.0f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }
        }
    }
}

private fun DrawScope.drawHighlightedCountry(
    country: CountryInfo,
    centerLat: Double,
    centerLon: Double,
    radius: Float,
    cx: Float,
    cy: Float
) {
    if (country.boundary.isEmpty()) return

    val (fillPath, strokePath, visibleCount) = GlobeMath.buildSphericalPolygonPaths(
        boundary = country.boundary,
        centerLatDeg = centerLat,
        centerLonDeg = centerLon,
        radius = radius,
        cx = cx,
        cy = cy
    )

    if (visibleCount > 2) {
        // 1. Semi-translucent vibrant green fill
        drawPath(
            path = fillPath,
            color = Color(0xFF22C55E).copy(alpha = 0.45f)
        )

        // 2. Wide glowing green halo outline
        drawPath(
            path = strokePath,
            color = Color(0xFF15803D).copy(alpha = 0.5f),
            style = Stroke(width = 7.5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // 3. Crisp neon green boundary border
        drawPath(
            path = strokePath,
            color = Color(0xFF4ADE80),
            style = Stroke(width = 2.8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

private fun DrawScope.drawLocationBeacon(
    point: GeoPoint,
    centerLat: Double,
    centerLon: Double,
    radius: Float,
    cx: Float,
    cy: Float,
    pulseProgress: Float
) {
    val pt = GlobeMath.project(point.lat, point.lon, centerLat, centerLon, radius, cx, cy)
    if (pt.isVisible && pt.depth > 0.02f) {
        val beaconCenter = pt.offset
        val maxPulseRadius = 26f
        val currentPulse = pulseProgress * maxPulseRadius
        val pulseAlpha = (1f - pulseProgress).coerceIn(0f, 1f)

        // Outer pulsing radar ring
        drawCircle(
            color = Color(0xFF4ADE80).copy(alpha = pulseAlpha * 0.85f),
            radius = currentPulse,
            center = beaconCenter,
            style = Stroke(width = 2.2f)
        )

        // Glowing aura around the dot
        drawCircle(
            color = Color(0xFF22C55E).copy(alpha = 0.45f),
            radius = 10f,
            center = beaconCenter
        )

        // Core vibrant green dot
        drawCircle(
            color = Color(0xFF22C55E),
            radius = 6f,
            center = beaconCenter
        )

        // Crisp white inner pin center
        drawCircle(
            color = Color.White,
            radius = 2.6f,
            center = beaconCenter
        )
    }
}

private fun DrawScope.drawGpsLocationPin(
    location: GeoPoint,
    centerLat: Double,
    centerLon: Double,
    radius: Float,
    cx: Float,
    cy: Float,
    pulseProgress: Float
) {
    val pt = GlobeMath.project(location.lat, location.lon, centerLat, centerLon, radius, cx, cy)
    if (pt.isVisible && pt.depth > 0.05f) {
        val beaconCenter = pt.offset
        val maxPulse = 28f
        val curPulse = pulseProgress * maxPulse
        val alpha = (1f - pulseProgress).coerceIn(0f, 1f)

        drawCircle(
            color = Color(0xFF60A5FA).copy(alpha = alpha * 0.8f),
            radius = curPulse,
            center = beaconCenter,
            style = Stroke(width = 2.2f)
        )
        drawCircle(
            color = Color(0xFF2563EB),
            radius = 6f,
            center = beaconCenter
        )
        drawCircle(
            color = Color.White,
            radius = 2.5f,
            center = beaconCenter
        )
    }
}

private fun DrawScope.drawLabels(
    centerLat: Double,
    centerLon: Double,
    radius: Float,
    cx: Float,
    cy: Float,
    compassBearingDeg: Float,
    selectedCountry: CountryInfo?,
    beaconPoint: GeoPoint?,
    labelPaint: AndroidPaint,
    highlightedLabelPaint: AndroidPaint,
    oceanLabelPaint: AndroidPaint
) {
    val canvas = drawContext.canvas.nativeCanvas
    val placedRects = mutableListOf<Rect>()

    // 1. Always draw the selected country label first with highest priority
    if (selectedCountry != null) {
        val anchorLat = beaconPoint?.lat ?: selectedCountry.centerLat
        val anchorLon = beaconPoint?.lon ?: selectedCountry.centerLon
        val pt = GlobeMath.project(anchorLat, anchorLon, centerLat, centerLon, radius, cx, cy, compassBearingDeg)
        if (pt.isVisible && pt.depth > 0.05f) {
            val x = pt.offset.x
            val y = pt.offset.y - 18f
            highlightedLabelPaint.alpha = 255
            canvas.drawText(selectedCountry.name, x, y, highlightedLabelPaint)
            val textWidth = highlightedLabelPaint.measureText(selectedCountry.name)
            placedRects.add(
                Rect(
                    left = x - textWidth / 2f - 14f,
                    top = y - 36f,
                    right = x + textWidth / 2f + 14f,
                    bottom = y + 14f
                )
            )
        }
    }

    // 2. Sort remaining visible countries by depth (closest to center first) and avoid label collisions
    val visibleCountries = WorldGeographicData.countries
        .filter { it.id != selectedCountry?.id }
        .mapNotNull { country ->
            val pt = GlobeMath.project(country.centerLat, country.centerLon, centerLat, centerLon, radius, cx, cy, compassBearingDeg)
            if (pt.isVisible && pt.depth > 0.35f) Pair(country, pt) else null
        }
        .sortedByDescending { it.second.depth }

    for ((country, pt) in visibleCountries) {
        val x = pt.offset.x
        val y = pt.offset.y
        val textWidth = labelPaint.measureText(country.name)
        val candidateRect = Rect(
            left = x - textWidth / 2f - 10f,
            top = y - 28f,
            right = x + textWidth / 2f + 10f,
            bottom = y + 10f
        )

        val overlaps = placedRects.any { it.overlaps(candidateRect) }
        if (!overlaps) {
            val alpha = ((pt.depth - 0.35f) / 0.65f * 225f).toInt().coerceIn(55, 225)
            labelPaint.alpha = alpha
            canvas.drawText(country.name, x, y, labelPaint)
            placedRects.add(candidateRect)
        }
    }

    // 3. Prominent Ocean Labels
    val oceans = listOf(
        Pair("Indian\nOcean", GeoPoint(-12.0, 75.0)),
        Pair("Atlantic Ocean", GeoPoint(0.0, -25.0)),
        Pair("Pacific Ocean", GeoPoint(0.0, 160.0))
    )

    for (ocean in oceans) {
        val pt = GlobeMath.project(ocean.second.lat, ocean.second.lon, centerLat, centerLon, radius, cx, cy, compassBearingDeg)
        if (pt.isVisible && pt.depth > 0.45f) {
            val alpha = ((pt.depth - 0.45f) / 0.55f * 150f).toInt().coerceIn(30, 150)
            oceanLabelPaint.alpha = alpha
            val lines = ocean.first.split("\n")
            lines.forEachIndexed { idx, line ->
                canvas.drawText(line, pt.offset.x, pt.offset.y + (idx * 30f), oceanLabelPaint)
            }
        }
    }
}

private fun DrawScope.drawFlightRoute(
    startCountry: CountryInfo,
    endCountry: CountryInfo,
    centerLat: Double,
    centerLon: Double,
    radius: Float,
    cx: Float,
    cy: Float,
    flightProgress: Float
) {
    val steps = 64
    val fullRoutePath = Path()
    val traveledPath = Path()
    var fullStarted = false
    var traveledStarted = false

    for (i in 0..steps) {
        val t = i.toFloat() / steps
        val geo = GlobeMath.interpolateGreatCircle(
            lat1 = startCountry.capitalLat,
            lon1 = startCountry.capitalLon,
            lat2 = endCountry.capitalLat,
            lon2 = endCountry.capitalLon,
            t = t
        )
        val pt = GlobeMath.project(geo.lat, geo.lon, centerLat, centerLon, radius, cx, cy)
        if (pt.isVisible && pt.depth > 0.0f) {
            if (!fullStarted) {
                fullRoutePath.moveTo(pt.offset.x, pt.offset.y)
                fullStarted = true
            } else {
                fullRoutePath.lineTo(pt.offset.x, pt.offset.y)
            }

            if (t <= flightProgress) {
                if (!traveledStarted) {
                    traveledPath.moveTo(pt.offset.x, pt.offset.y)
                    traveledStarted = true
                } else {
                    traveledPath.lineTo(pt.offset.x, pt.offset.y)
                }
            }
        } else {
            fullStarted = false
            traveledStarted = false
        }
    }

    // 1. Soft outer glow along full Great-Circle route
    drawPath(
        path = fullRoutePath,
        color = Color(0xFF38BDF8).copy(alpha = 0.28f),
        style = Stroke(width = 8f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // 2. Dashed full route line from Start Country to End Country
    drawPath(
        path = fullRoutePath,
        color = Color(0xFF7DD3FC).copy(alpha = 0.78f),
        style = Stroke(
            width = 2.6f,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
        )
    )

    // 3. Solid glowing trail behind the moving plane
    drawPath(
        path = traveledPath,
        color = Color(0xFFFACC15),
        style = Stroke(width = 3.6f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // 4. Start & End Country endpoint rings
    val startPt = GlobeMath.project(startCountry.capitalLat, startCountry.capitalLon, centerLat, centerLon, radius, cx, cy)
    if (startPt.isVisible && startPt.depth > 0.02f) {
        drawCircle(
            color = Color(0xFF38BDF8),
            radius = 7f,
            center = startPt.offset
        )
        drawCircle(
            color = Color.White,
            radius = 3.2f,
            center = startPt.offset
        )
    }

    val endPt = GlobeMath.project(endCountry.capitalLat, endCountry.capitalLon, centerLat, centerLon, radius, cx, cy)
    if (endPt.isVisible && endPt.depth > 0.02f) {
        drawCircle(
            color = Color(0xFFFACC15),
            radius = 7.5f,
            center = endPt.offset
        )
        drawCircle(
            color = Color.White,
            radius = 3.2f,
            center = endPt.offset
        )
    }

    // 5. Airplane position & heading along the 3D Great-Circle curve
    val planeGeo = GlobeMath.interpolateGreatCircle(
        lat1 = startCountry.capitalLat,
        lon1 = startCountry.capitalLon,
        lat2 = endCountry.capitalLat,
        lon2 = endCountry.capitalLon,
        t = flightProgress
    )
    val aheadT = (flightProgress + 0.02f).coerceAtMost(1.0f)
    val behindT = (flightProgress - 0.02f).coerceAtLeast(0.0f)
    val aheadGeo = GlobeMath.interpolateGreatCircle(
        lat1 = startCountry.capitalLat,
        lon1 = startCountry.capitalLon,
        lat2 = endCountry.capitalLat,
        lon2 = endCountry.capitalLon,
        t = aheadT
    )
    val behindGeo = GlobeMath.interpolateGreatCircle(
        lat1 = startCountry.capitalLat,
        lon1 = startCountry.capitalLon,
        lat2 = endCountry.capitalLat,
        lon2 = endCountry.capitalLon,
        t = behindT
    )

    val planePt = GlobeMath.project(planeGeo.lat, planeGeo.lon, centerLat, centerLon, radius, cx, cy)
    val aheadPt = GlobeMath.project(aheadGeo.lat, aheadGeo.lon, centerLat, centerLon, radius, cx, cy)
    val behindPt = GlobeMath.project(behindGeo.lat, behindGeo.lon, centerLat, centerLon, radius, cx, cy)

    if (planePt.isVisible && planePt.depth > 0.02f) {
        val dx = aheadPt.offset.x - behindPt.offset.x
        val dy = aheadPt.offset.y - behindPt.offset.y
        val angleDeg = (atan2(dy.toDouble(), dx.toDouble()) * (180.0 / PI)).toFloat() + 90f

        withTransform({
            translate(left = planePt.offset.x, top = planePt.offset.y)
            rotate(degrees = angleDeg, pivot = Offset.Zero)
        }) {
            val s = 1.35f
            val planePath = Path().apply {
                // Nose tip pointing up (0, -14)
                moveTo(0f * s, -14f * s)
                // Right fuselage front
                lineTo(2.4f * s, -6f * s)
                // Right main wing tip
                lineTo(13f * s, 1.5f * s)
                // Right wing trailing edge
                lineTo(13f * s, 4.2f * s)
                lineTo(2.4f * s, 1.5f * s)
                // Right rear fuselage
                lineTo(1.8f * s, 9f * s)
                // Right tail fin
                lineTo(6.5f * s, 12.5f * s)
                lineTo(6.5f * s, 14.2f * s)
                // Center tail notch
                lineTo(0f * s, 12.2f * s)
                // Left tail fin
                lineTo(-6.5f * s, 14.2f * s)
                lineTo(-6.5f * s, 12.5f * s)
                // Left rear fuselage
                lineTo(-1.8f * s, 9f * s)
                // Left wing trailing edge
                lineTo(-2.4f * s, 1.5f * s)
                lineTo(-13f * s, 4.2f * s)
                // Left main wing tip
                lineTo(-13f * s, 1.5f * s)
                // Left fuselage front
                lineTo(-2.4f * s, -6f * s)
                close()
            }

            // Soft glowing aura around the airplane
            drawCircle(
                color = Color(0xFFFACC15).copy(alpha = 0.28f),
                radius = 19f,
                center = Offset.Zero
            )

            // Airplane golden-white silhouette with dark navy border for high contrast
            drawPath(
                path = planePath,
                color = Color(0xFFFEF08A)
            )
            drawPath(
                path = planePath,
                color = Color(0xFF0284C7),
                style = Stroke(width = 1.6f, join = StrokeJoin.Round)
            )
        }
    }
}

private fun DrawScope.drawInnerAtmosphereShading(cx: Float, cy: Float, radius: Float, isLightTheme: Boolean) {
    // Soft vignette / inner atmospheric limb glow
    val shadingColors = if (isLightTheme) {
        listOf(
            Color.Transparent,
            Color.Transparent,
            Color(0x180284C7),
            Color(0x400369A1)
        )
    } else {
        listOf(
            Color.Transparent,
            Color.Transparent,
            Color(0x220284C7),
            Color(0x660284C7)
        )
    }
    drawCircle(
        brush = Brush.radialGradient(
            colors = shadingColors,
            center = Offset(cx, cy),
            radius = radius
        ),
        radius = radius,
        center = Offset(cx, cy)
    )
}

/**
 * Draws a live Compass Cardinal Ring (N, E, S, W + degree ticks) around the globe perimeter
 * that rotates in real time with the user's phone orientation (compassBearingDeg).
 */
private fun DrawScope.drawCompassCardinalRing(
    cx: Float,
    cy: Float,
    radius: Float,
    compassBearingDeg: Float,
    isLightTheme: Boolean,
    cardinalPaint: AndroidPaint
) {
    val ringRadius = radius + 22f
    val tickInner = ringRadius - 7f
    val tickOuter = ringRadius + 7f
    val canvas = drawContext.canvas.nativeCanvas

    // Subtle outer compass track circle
    drawCircle(
        color = if (isLightTheme) Color(0x550284C7) else Color(0x5538BDF8),
        radius = ringRadius,
        center = Offset(cx, cy),
        style = Stroke(width = 1.5f)
    )

    // Degree tick marks every 15 degrees
    for (deg in 0 until 360 step 15) {
        val screenAngleDeg = deg - compassBearingDeg - 90f
        val rad = screenAngleDeg * (PI / 180.0)
        val isCardinal = deg % 90 == 0
        val isIntercardinal = deg % 45 == 0
        val innerR = when {
            isCardinal -> tickInner - 4f
            isIntercardinal -> tickInner - 1f
            else -> tickInner + 2f
        }
        val outerR = when {
            isCardinal -> tickOuter + 4f
            isIntercardinal -> tickOuter + 1f
            else -> tickOuter - 2f
        }
        val sx1 = cx + cos(rad).toFloat() * innerR
        val sy1 = cy + sin(rad).toFloat() * innerR
        val sx2 = cx + cos(rad).toFloat() * outerR
        val sy2 = cy + sin(rad).toFloat() * outerR

        val tickColor = when {
            deg == 0 -> Color(0xFFEF4444) // Red for North
            isCardinal -> if (isLightTheme) Color(0xFF0F172A) else Color.White
            else -> if (isLightTheme) Color(0x88475569) else Color(0x8894A3B8)
        }
        drawLine(
            color = tickColor,
            start = Offset(sx1, sy1),
            end = Offset(sx2, sy2),
            strokeWidth = if (isCardinal) 3.2f else 1.5f,
            cap = StrokeCap.Round
        )
    }

    // Cardinal Badges: N (0°), E (90°), S (180°), W (270°)
    val cardinals = listOf(
        Triple("N", 0f, Color(0xFFEF4444)),
        Triple("E", 90f, if (isLightTheme) Color(0xFF0284C7) else Color(0xFF38BDF8)),
        Triple("S", 180f, if (isLightTheme) Color(0xFF1E293B) else Color(0xFFE2E8F0)),
        Triple("W", 270f, if (isLightTheme) Color(0xFF0284C7) else Color(0xFF38BDF8))
    )

    val labelDist = ringRadius + 26f
    for ((label, deg, badgeColor) in cardinals) {
        val screenAngleDeg = deg - compassBearingDeg - 90f
        val rad = screenAngleDeg * (PI / 180.0)
        val lx = cx + cos(rad).toFloat() * labelDist
        val ly = cy + sin(rad).toFloat() * labelDist

        // Badge pill circle behind N, E, S, W
        drawCircle(
            color = if (isLightTheme) Color(0xEEFFFFFF) else Color(0xEE0F172A),
            radius = 18f,
            center = Offset(lx, ly)
        )
        drawCircle(
            color = badgeColor,
            radius = 18f,
            center = Offset(lx, ly),
            style = Stroke(width = 2.2f)
        )

        cardinalPaint.color = if (label == "N") {
            AndroidColor.rgb(239, 68, 68) // Crisp Red for North
        } else if (isLightTheme) {
            AndroidColor.rgb(15, 23, 42)
        } else {
            AndroidColor.WHITE
        }
        cardinalPaint.textSize = 24f
        canvas.drawText(label, lx, ly + 8.5f, cardinalPaint)
    }
}

