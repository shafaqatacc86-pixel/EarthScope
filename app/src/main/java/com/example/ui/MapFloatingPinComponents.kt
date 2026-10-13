package com.example.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Typeface
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditLocationAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.MapFloatingPinEntity
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Checks if device GPS / Location is enabled.
 * - If already enabled, invokes [onGpsEnabled] immediately.
 * - If disabled, shows the built-in Android Google Play Services Location Settings dialog
 *   ("Turn on device location?"). If Play Services resolution is unavailable on the device,
 *   launches the system Location Settings screen.
 */
fun checkAndPromptEnableGps(
    context: Context,
    resolutionLauncher: (IntentSenderRequest) -> Unit,
    onGpsAlreadyEnabled: () -> Unit
) {
    val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    val isGpsOn = lm?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true

    if (isGpsOn) {
        onGpsAlreadyEnabled()
        return
    }

    try {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000L)
            .setMinUpdateIntervalMillis(1000L)
            .build()
        val settingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
            .build()

        LocationServices.getSettingsClient(context)
            .checkLocationSettings(settingsRequest)
            .addOnSuccessListener {
                onGpsAlreadyEnabled()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        val intentSenderRequest = IntentSenderRequest.Builder(exception.resolution).build()
                        resolutionLauncher(intentSenderRequest)
                    } catch (_: IntentSender.SendIntentException) {
                        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }
                } else {
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            }
    } catch (_: Exception) {
        context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
}

/**
 * ============================================================================
 * 1. LOCATION CATEGORIES (WITH ICONS, COLORS & EMOJIS)
 * ============================================================================
 */
enum class PinLocationCategory(
    val key: String,
    val label: String,
    val icon: ImageVector,
    val color: Color,
    val hexColor: String,
    val emoji: String
) {
    TRAVELLING("TRAVELLING", "Travelling", Icons.Default.FlightTakeoff, Color(0xFF0EA5E9), "#0EA5E9", "✈️"),
    OFFICE("OFFICE", "Office", Icons.Default.Apartment, Color(0xFF6366F1), "#6366F1", "🏢"),
    WORK("WORK", "Work", Icons.Default.Work, Color(0xFFF59E0B), "#F59E0B", "💼"),
    HOME("HOME", "Home", Icons.Default.Home, Color(0xFF10B981), "#10B981", "🏠"),
    RESTAURANT("RESTAURANT", "Food & Cafe", Icons.Default.Restaurant, Color(0xFFF97316), "#F97316", "🍽️"),
    SHOPPING("SHOPPING", "Shopping", Icons.Default.ShoppingBag, Color(0xFFEC4899), "#EC4899", "🛍️"),
    HOTEL("HOTEL", "Hotel / Stay", Icons.Default.Hotel, Color(0xFF8B5CF6), "#8B5CF6", "🏨"),
    PARK("PARK", "Park & Nature", Icons.Default.Park, Color(0xFF22C55E), "#22C55E", "🌲"),
    HOSPITAL("HOSPITAL", "Hospital", Icons.Default.LocalHospital, Color(0xFFEF4444), "#EF4444", "🏥"),
    EDUCATION("EDUCATION", "School / Uni", Icons.Default.School, Color(0xFF14B8A6), "#14B8A6", "🎓"),
    FAVORITE("FAVORITE", "Favorite Spot", Icons.Default.Star, Color(0xFFEAB308), "#EAB308", "⭐");

    companion object {
        fun fromKey(key: String?): PinLocationCategory {
            return entries.find { it.key.equals(key, ignoreCase = true) } ?: TRAVELLING
        }
    }
}

/**
 * ============================================================================
 * 2. CUSTOM MARKER STYLES (SHOWN BOTH ON MAP MARKER & IN FLOATING WINDOW)
 * ============================================================================
 */
enum class CustomMapMarkerOption(
    val key: String,
    val label: String,
    val icon: ImageVector,
    val color: Color,
    val hexColor: String,
    val badgeSymbol: String
) {
    EMERALD_PIN("EMERALD_PIN", "Emerald Pin", Icons.Default.LocationOn, Color(0xFF10B981), "#10B981", "📍"),
    OCEAN_STAR("OCEAN_STAR", "Ocean Star", Icons.Default.Star, Color(0xFF0284C7), "#0284C7", "★"),
    CRIMSON_HEART("CRIMSON_HEART", "Red Heart", Icons.Default.Favorite, Color(0xFFEF4444), "#EF4444", "♥"),
    GOLD_FLAG("GOLD_FLAG", "Gold Flag", Icons.Default.Flag, Color(0xFFF59E0B), "#F59E0B", "⚑"),
    ROYAL_CROWN("ROYAL_CROWN", "Purple Gem", Icons.Default.Bookmark, Color(0xFF8B5CF6), "#8B5CF6", "◆"),
    TEAL_TARGET("TEAL_TARGET", "GPS Target", Icons.Default.GpsFixed, Color(0xFF14B8A6), "#14B8A6", "◎"),
    PINK_DROP("PINK_DROP", "Rose Pin", Icons.Default.Place, Color(0xFFEC4899), "#EC4899", "●"),
    INDIGO_BADGE("INDIGO_BADGE", "Indigo Shield", Icons.Default.CheckCircle, Color(0xFF6366F1), "#6366F1", "✔");

    companion object {
        fun fromKey(key: String?): CustomMapMarkerOption {
            return entries.find { it.key.equals(key, ignoreCase = true) } ?: EMERALD_PIN
        }
    }
}

/**
 * Generates a distinctive, bold circular custom Google Maps Marker BitmapDescriptor (for unselected pins):
 * - Clean circular badge with thick white border & category emoji inside
 * - No extra pointer tails so there is never a double-tail!
 */
fun createCustomMarkerBitmapDescriptor(
    markerOption: CustomMapMarkerOption,
    category: PinLocationCategory,
    isSelected: Boolean
): BitmapDescriptor {
    val size = if (isSelected) 128 else 112
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val centerX = size / 2f
    val centerY = size / 2f
    val circleRadius = size * 0.38f

    val mainColor = markerOption.color.toArgb()
    val categoryColor = category.color.toArgb()

    // 1. Subtle Outer Halo Shadow Ring
    val haloPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = markerOption.color.copy(alpha = 0.28f).toArgb()
        style = Paint.Style.FILL
    }
    canvas.drawCircle(centerX, centerY, circleRadius + 8f, haloPaint)

    // 2. White Outer Border Circle
    val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawCircle(centerX, centerY, circleRadius + 5f, whitePaint)

    // 3. Main Colored Badge Circle
    val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
        style = Paint.Style.FILL
    }
    canvas.drawCircle(centerX, centerY, circleRadius, bodyPaint)

    // 4. Inner Category Ring + White Medallion
    val accentRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = categoryColor
        style = Paint.Style.STROKE
        strokeWidth = 3.5f
    }
    canvas.drawCircle(centerX, centerY, circleRadius * 0.76f, accentRingPaint)
    canvas.drawCircle(centerX, centerY, circleRadius * 0.68f, whitePaint)

    // 5. Category Emoji inside Medallion
    val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
        textSize = circleRadius * 0.90f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    val fontMetrics = textPaint.fontMetrics
    val textBaseline = centerY - (fontMetrics.ascent + fontMetrics.descent) / 2f
    canvas.drawText(category.emoji, centerX, textBaseline, textPaint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

/**
 * Draws a crisp vector-style custom marker icon inside a circular badge on an Android Canvas
 * (avoiding emoji font clipping so the marker icon inside the Info Window is 100% sharp).
 */
private fun drawCrispMarkerOptionIconOnCanvas(
    canvas: Canvas,
    markerOption: CustomMapMarkerOption,
    cx: Float,
    cy: Float,
    radius: Float
) {
    val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.FILL
        strokeWidth = radius * 0.22f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    when (markerOption) {
        CustomMapMarkerOption.EMERALD_PIN, CustomMapMarkerOption.PINK_DROP -> {
            // Classic Map Pin silhouette (head circle + pointed tip + inner white cutout)
            val headCy = cy - radius * 0.16f
            val headR = radius * 0.44f
            val pinPath = Path().apply {
                addCircle(cx, headCy, headR, Path.Direction.CW)
                moveTo(cx - headR * 0.82f, headCy + headR * 0.42f)
                lineTo(cx, cy + radius * 0.64f)
                lineTo(cx + headR * 0.82f, headCy + headR * 0.42f)
                close()
            }
            canvas.drawPath(pinPath, iconPaint)
            val holePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = markerOption.color.toArgb()
                style = Paint.Style.FILL
            }
            canvas.drawCircle(cx, headCy, headR * 0.40f, holePaint)
        }
        CustomMapMarkerOption.OCEAN_STAR -> {
            // Crisp 5-point Star
            val starPath = Path()
            val outerR = radius * 0.62f
            val innerR = radius * 0.28f
            for (i in 0 until 10) {
                val r = if (i % 2 == 0) outerR else innerR
                val angle = Math.toRadians((i * 36.0) - 90.0)
                val px = (cx + r * kotlin.math.cos(angle)).toFloat()
                val py = (cy + r * kotlin.math.sin(angle)).toFloat()
                if (i == 0) starPath.moveTo(px, py) else starPath.lineTo(px, py)
            }
            starPath.close()
            canvas.drawPath(starPath, iconPaint)
        }
        CustomMapMarkerOption.CRIMSON_HEART -> {
            // Crisp Heart icon
            val s = radius * 0.56f
            val heartPath = Path().apply {
                moveTo(cx, cy + s * 0.85f)
                cubicTo(cx - s * 1.35f, cy + s * 0.10f, cx - s * 0.95f, cy - s * 0.95f, cx, cy - s * 0.30f)
                cubicTo(cx + s * 0.95f, cy - s * 0.95f, cx + s * 1.35f, cy + s * 0.10f, cx, cy + s * 0.85f)
                close()
            }
            canvas.drawPath(heartPath, iconPaint)
        }
        CustomMapMarkerOption.GOLD_FLAG -> {
            // Crisp Flag pole + waving banner
            val poleX = cx - radius * 0.34f
            iconPaint.style = Paint.Style.STROKE
            canvas.drawLine(poleX, cy - radius * 0.54f, poleX, cy + radius * 0.56f, iconPaint)
            iconPaint.style = Paint.Style.FILL
            val flagPath = Path().apply {
                moveTo(poleX, cy - radius * 0.54f)
                lineTo(cx + radius * 0.48f, cy - radius * 0.24f)
                lineTo(poleX, cy + radius * 0.08f)
                close()
            }
            canvas.drawPath(flagPath, iconPaint)
        }
        CustomMapMarkerOption.ROYAL_CROWN -> {
            // Crisp Diamond Gem
            val d = radius * 0.58f
            val gemPath = Path().apply {
                moveTo(cx, cy - d)
                lineTo(cx + d * 0.82f, cy)
                lineTo(cx, cy + d)
                lineTo(cx - d * 0.82f, cy)
                close()
            }
            canvas.drawPath(gemPath, iconPaint)
        }
        CustomMapMarkerOption.TEAL_TARGET -> {
            // Crisp GPS Target Crosshair
            iconPaint.style = Paint.Style.STROKE
            canvas.drawCircle(cx, cy, radius * 0.44f, iconPaint)
            canvas.drawLine(cx - radius * 0.64f, cy, cx + radius * 0.64f, cy, iconPaint)
            canvas.drawLine(cx, cy - radius * 0.64f, cx, cy + radius * 0.64f, iconPaint)
        }
        CustomMapMarkerOption.INDIGO_BADGE -> {
            // Crisp Checkmark
            iconPaint.style = Paint.Style.STROKE
            val chkPath = Path().apply {
                moveTo(cx - radius * 0.44f, cy + radius * 0.02f)
                lineTo(cx - radius * 0.10f, cy + radius * 0.36f)
                lineTo(cx + radius * 0.48f, cy - radius * 0.32f)
            }
            canvas.drawPath(chkPath, iconPaint)
        }
    }
}

/**
 * Hit-test result when the user taps on the active unified Speech-Bubble + Marker sprite
 * or when we evaluate screen coordinates around the active marker:
 * - CLOSE_BUTTON: User tapped the top-right 'X' close button on the Info Window -> close Info Window
 * - INFO_WINDOW_CARD: User tapped inside the Info Window card -> open Location Details Page
 * - MARKER_BADGE: User tapped on the circular marker below the Info Window -> keep/toggle Info Window (do NOT open Details Page!)
 * - OUTSIDE: Tap was outside the active Info Window & Marker
 */
enum class ActiveBubbleTapZone {
    CLOSE_BUTTON,
    INFO_WINDOW_CARD,
    MARKER_BADGE,
    OUTSIDE
}

/**
 * Evaluates where a screen-space tap `(tapX, tapY)` landed relative to the active marker's
 * screen position `(markerScreenX, markerScreenY)` for the 560x280 Speech-Bubble sprite
 * anchored at `(0.5f, 1.0f)` with `infoWindowAnchor` / vertical offset of 52px above the marker center.
 */
fun detectActiveBubbleTapZone(
    markerScreenX: Float,
    markerScreenY: Float,
    tapX: Float,
    tapY: Float
): ActiveBubbleTapZone {
    // The Speech-Bubble sprite is 560x280, and its bottom-center (280, 280) sits at (markerScreenX, markerScreenY - 52f)
    // (or in the 560x340 padded sprite anchored at (280, 332)).
    val localX = tapX - markerScreenX + 280f
    val localY = tapY - markerScreenY + 332f

    // 1. Check Top-Right Close ('X') Button with generous 44px touch target around (500, 52)
    val closeDx = localX - 500f
    val closeDy = localY - 52f
    if (closeDx * closeDx + closeDy * closeDy <= 44f * 44f) {
        return ActiveBubbleTapZone.CLOSE_BUTTON
    }

    // 2. Check Circular Marker Badge below the speech-bubble tail (y > 274)
    if (localY > 274f) {
        return ActiveBubbleTapZone.MARKER_BADGE
    }

    // 3. Check Speech-Bubble Info Window Card body (x in 14..546, y in 8..274)
    if (localX in 14f..546f && localY in 8f..274f) {
        return ActiveBubbleTapZone.INFO_WINDOW_CARD
    }

    return ActiveBubbleTapZone.OUTSIDE
}

/**
 * Renders ONLY the Speech-Bubble 💬 Info Window Card + Single Triangle Pointer Tail
 * (without redrawing the circular marker badge at the bottom) so it can be permanently
 * pre-mounted for each pin at `anchor = Offset(0.5f, 332f / 340f)` and toggled via
 * `visible = isSelected` on the very first click with 100% reliability!
 */
fun createSpeechBubbleOnlyBitmapDescriptor(
    pin: MapFloatingPinEntity,
    markerOption: CustomMapMarkerOption,
    category: PinLocationCategory,
    isLightTheme: Boolean
): BitmapDescriptor {
    val width = 560
    val height = 340
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val centerX = width / 2f
    val mainColor = markerOption.color.toArgb()
    val categoryColor = category.color.toArgb()

    val cardLeft = 20f
    val cardTop = 14f
    val cardRight = width - 20f
    val cardBottom = 236f
    val cornerRadius = 38f
    val tailTipY = 272f
    val tailHalfWidth = 24f

    // Drop Shadow under Speech-Bubble Card
    val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.argb(55, 15, 23, 42)
        style = Paint.Style.FILL
    }
    val shadowRect = android.graphics.RectF(cardLeft + 2f, cardTop + 8f, cardRight + 2f, cardBottom + 8f)
    canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint)

    // Speech-Bubble Card Background Fill
    val cardBgColor = if (isLightTheme) android.graphics.Color.WHITE else android.graphics.Color.parseColor("#0E192D")
    val cardFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = cardBgColor
        style = Paint.Style.FILL
    }
    val cardRect = android.graphics.RectF(cardLeft, cardTop, cardRight, cardBottom)
    canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, cardFillPaint)

    // Speech-Bubble Card Border
    val cardBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, cardBorderPaint)

    // Single Speech-Bubble Triangle Tail pointing down to the Marker below
    val tailFillPath = Path().apply {
        moveTo(centerX - tailHalfWidth, cardBottom - 3f)
        lineTo(centerX, tailTipY)
        lineTo(centerX + tailHalfWidth, cardBottom - 3f)
        close()
    }
    canvas.drawPath(tailFillPath, cardFillPaint)

    val tailBorderPath = Path().apply {
        moveTo(centerX - tailHalfWidth, cardBottom - 1f)
        lineTo(centerX, tailTipY)
        lineTo(centerX + tailHalfWidth, cardBottom - 1f)
    }
    canvas.drawPath(tailBorderPath, cardBorderPaint)

    // Top Row inside Card: Crisp Vector Marker Icon Badge + Category Pill + Close Cross ('X') Icon
    val dotCenterX = cardLeft + 38f
    val dotCenterY = cardTop + 38f
    val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
        style = Paint.Style.FILL
    }
    canvas.drawCircle(dotCenterX, dotCenterY, 21f, dotPaint)

    drawCrispMarkerOptionIconOnCanvas(
        canvas = canvas,
        markerOption = markerOption,
        cx = dotCenterX,
        cy = dotCenterY,
        radius = 18f
    )

    // Category Pill next to Marker Icon Badge
    val catPillText = "${category.emoji} ${category.label}"
    val catTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = categoryColor
        textSize = 23f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    val catTextWidth = catTextPaint.measureText(catPillText)
    val catPillLeft = dotCenterX + 30f
    val catPillTop = dotCenterY - 19f
    val catPillRight = (catPillLeft + catTextWidth + 28f).coerceAtMost(cardRight - 72f)
    val catPillBottom = dotCenterY + 19f
    val catPillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = category.color.copy(alpha = 0.16f).toArgb()
        style = Paint.Style.FILL
    }
    canvas.drawRoundRect(
        android.graphics.RectF(catPillLeft, catPillTop, catPillRight, catPillBottom),
        14f,
        14f,
        catPillPaint
    )
    val catFm = catTextPaint.fontMetrics
    canvas.drawText(
        catPillText,
        catPillLeft + 14f,
        dotCenterY - (catFm.ascent + catFm.descent) / 2f,
        catTextPaint
    )

    // Top-Right Close Cross ('X') Button inside Info Window (at x=500, y=52)
    val closeCenterX = cardRight - 40f
    val closeCenterY = dotCenterY
    val closeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if (isLightTheme) android.graphics.Color.parseColor("#F1F5F9") else android.graphics.Color.parseColor("#1E293B")
        style = Paint.Style.FILL
    }
    canvas.drawCircle(closeCenterX, closeCenterY, 22f, closeBgPaint)

    val closeBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if (isLightTheme) android.graphics.Color.parseColor("#CBD5E1") else android.graphics.Color.parseColor("#475569")
        style = Paint.Style.STROKE
        strokeWidth = 2.5f
    }
    canvas.drawCircle(closeCenterX, closeCenterY, 22f, closeBorderPaint)

    val crossStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if (isLightTheme) android.graphics.Color.parseColor("#475569") else android.graphics.Color.parseColor("#E2E8F0")
        style = Paint.Style.STROKE
        strokeWidth = 4.2f
        strokeCap = Paint.Cap.ROUND
    }
    val crossHalf = 8.5f
    canvas.drawLine(closeCenterX - crossHalf, closeCenterY - crossHalf, closeCenterX + crossHalf, closeCenterY + crossHalf, crossStrokePaint)
    canvas.drawLine(closeCenterX + crossHalf, closeCenterY - crossHalf, closeCenterX - crossHalf, closeCenterY + crossHalf, crossStrokePaint)

    // Main Content Row: Left Thumbnail Photo (or Category Icon Box) + Right Title & Coordinates
    val thumbLeft = cardLeft + 22f
    val thumbTop = cardTop + 74f
    val thumbSize = 116f
    val thumbRect = android.graphics.RectF(thumbLeft, thumbTop, thumbLeft + thumbSize, thumbTop + thumbSize)

    val thumbBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = markerOption.color.copy(alpha = 0.14f).toArgb()
        style = Paint.Style.FILL
    }
    canvas.drawRoundRect(thumbRect, 24f, 24f, thumbBgPaint)

    var drewPhoto = false
    val imgPath = pin.primaryImagePath
    if (!imgPath.isNullOrBlank()) {
        try {
            val imgFile = File(imgPath)
            if (imgFile.exists()) {
                val opts = BitmapFactory.Options().apply { inSampleSize = 2 }
                val rawBmp = BitmapFactory.decodeFile(imgFile.absolutePath, opts)
                if (rawBmp != null) {
                    val saveCount = canvas.save()
                    val clipPath = Path().apply {
                        addRoundRect(thumbRect, 24f, 24f, Path.Direction.CW)
                    }
                    canvas.clipPath(clipPath)
                    val srcRect = android.graphics.Rect(0, 0, rawBmp.width, rawBmp.height)
                    canvas.drawBitmap(rawBmp, srcRect, thumbRect, Paint(Paint.FILTER_BITMAP_FLAG))
                    canvas.restoreToCount(saveCount)
                    drewPhoto = true
                }
            }
        } catch (_: Exception) {}
    }

    if (!drewPhoto) {
        val emojiThumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mainColor
            textSize = 52f
            textAlign = Paint.Align.CENTER
        }
        val efm = emojiThumbPaint.fontMetrics
        canvas.drawText(
            category.emoji,
            thumbRect.centerX(),
            thumbRect.centerY() - (efm.ascent + efm.descent) / 2f,
            emojiThumbPaint
        )
    }

    val textStartX = thumbLeft + thumbSize + 22f
    val maxTextWidth = (cardRight - 24f) - textStartX

    val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if (isLightTheme) android.graphics.Color.parseColor("#0F172A") else android.graphics.Color.WHITE
        textSize = 31f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    var displayTitle = pin.locationName.ifBlank { "Saved Location" }
    if (titlePaint.measureText(displayTitle) > maxTextWidth) {
        while (displayTitle.length > 3 && titlePaint.measureText("$displayTitle…") > maxTextWidth) {
            displayTitle = displayTitle.dropLast(1)
        }
        displayTitle = "$displayTitle…"
    }
    canvas.drawText(displayTitle, textStartX, thumbTop + 48f, titlePaint)

    val coordsText = String.format(Locale.US, "%.4f°, %.4f°", pin.latitude, pin.longitude)
    val coordsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    canvas.drawText(coordsText, textStartX, thumbTop + 92f, coordsPaint)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

/**
 * Renders BOTH the Speech-Bubble 💬 Info Window AND the Custom Circular Marker below it
 * into a SINGLE unified Google Maps OpenGL Marker BitmapDescriptor!
 *
 * Why this completely solves the map-panning drift issue AND fixed-size consistency:
 * - Because both the Info Window and the Marker are in the SAME native OpenGL Marker texture
 *   anchored at the exact marker center on the map, Google Maps hardware-renders them together
 *   in the exact same OpenGL draw call. The Info Window NEVER changes size when moving the map
 *   and NEVER drifts or lags behind the marker!
 * - Features a crisp vector marker icon inside the top-left badge and a clear Close ('✕') cross
 *   icon button on the top-right of the Info Window.
 */
fun createSelectedMarkerWithSpeechBubbleBitmapDescriptor(
    pin: MapFloatingPinEntity,
    markerOption: CustomMapMarkerOption,
    category: PinLocationCategory,
    isLightTheme: Boolean
): BitmapDescriptor {
    val width = 560
    val height = 410
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val centerX = width / 2f
    val mainColor = markerOption.color.toArgb()
    val categoryColor = category.color.toArgb()

    // --- PART A: SPEECH-BUBBLE 💬 INFO WINDOW CARD AT THE TOP ---
    val cardLeft = 20f
    val cardTop = 14f
    val cardRight = width - 20f
    val cardBottom = 236f
    val cornerRadius = 38f
    val tailTipY = 272f
    val tailHalfWidth = 24f

    // Drop Shadow under Speech-Bubble Card + Tail
    val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.argb(55, 15, 23, 42)
        style = Paint.Style.FILL
    }
    val shadowRect = android.graphics.RectF(cardLeft + 2f, cardTop + 8f, cardRight + 2f, cardBottom + 8f)
    canvas.drawRoundRect(shadowRect, cornerRadius, cornerRadius, shadowPaint)

    // Speech-Bubble Card Background Fill
    val cardBgColor = if (isLightTheme) android.graphics.Color.WHITE else android.graphics.Color.parseColor("#0E192D")
    val cardFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = cardBgColor
        style = Paint.Style.FILL
    }
    val cardRect = android.graphics.RectF(cardLeft, cardTop, cardRight, cardBottom)
    canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, cardFillPaint)

    // Speech-Bubble Card Border
    val cardBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }
    canvas.drawRoundRect(cardRect, cornerRadius, cornerRadius, cardBorderPaint)

    // Single Speech-Bubble Triangle Tail pointing down to the Marker below
    val tailFillPath = Path().apply {
        moveTo(centerX - tailHalfWidth, cardBottom - 3f)
        lineTo(centerX, tailTipY)
        lineTo(centerX + tailHalfWidth, cardBottom - 3f)
        close()
    }
    canvas.drawPath(tailFillPath, cardFillPaint)

    val tailBorderPath = Path().apply {
        moveTo(centerX - tailHalfWidth, cardBottom - 1f)
        lineTo(centerX, tailTipY)
        lineTo(centerX + tailHalfWidth, cardBottom - 1f)
    }
    canvas.drawPath(tailBorderPath, cardBorderPaint)

    // Top Row inside Card: Crisp Vector Marker Icon Badge + Category Pill + Close Cross ('X') Icon
    val dotCenterX = cardLeft + 38f
    val dotCenterY = cardTop + 38f
    val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
        style = Paint.Style.FILL
    }
    canvas.drawCircle(dotCenterX, dotCenterY, 21f, dotPaint)

    // Draw crisp vector Marker Icon inside the top-left colored circle (no clipped emoji!)
    drawCrispMarkerOptionIconOnCanvas(
        canvas = canvas,
        markerOption = markerOption,
        cx = dotCenterX,
        cy = dotCenterY,
        radius = 18f
    )

    // Category Pill next to Marker Icon Badge
    val catPillText = "${category.emoji} ${category.label}"
    val catTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = categoryColor
        textSize = 23f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    val catTextWidth = catTextPaint.measureText(catPillText)
    val catPillLeft = dotCenterX + 30f
    val catPillTop = dotCenterY - 19f
    val catPillRight = (catPillLeft + catTextWidth + 28f).coerceAtMost(cardRight - 72f)
    val catPillBottom = dotCenterY + 19f
    val catPillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = category.color.copy(alpha = 0.16f).toArgb()
        style = Paint.Style.FILL
    }
    canvas.drawRoundRect(
        android.graphics.RectF(catPillLeft, catPillTop, catPillRight, catPillBottom),
        14f,
        14f,
        catPillPaint
    )
    val catFm = catTextPaint.fontMetrics
    canvas.drawText(
        catPillText,
        catPillLeft + 14f,
        dotCenterY - (catFm.ascent + catFm.descent) / 2f,
        catTextPaint
    )

    // Top-Right Close Cross ('X') Button inside Info Window (at x=500, y=52)
    val closeCenterX = cardRight - 40f // 540 - 40 = 500f
    val closeCenterY = dotCenterY      // 14 + 38 = 52f
    val closeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if (isLightTheme) android.graphics.Color.parseColor("#F1F5F9") else android.graphics.Color.parseColor("#1E293B")
        style = Paint.Style.FILL
    }
    canvas.drawCircle(closeCenterX, closeCenterY, 22f, closeBgPaint)

    val closeBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if (isLightTheme) android.graphics.Color.parseColor("#CBD5E1") else android.graphics.Color.parseColor("#475569")
        style = Paint.Style.STROKE
        strokeWidth = 2.5f
    }
    canvas.drawCircle(closeCenterX, closeCenterY, 22f, closeBorderPaint)

    // Crisp Vector Cross ('X') lines inside the Close Button
    val crossStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if (isLightTheme) android.graphics.Color.parseColor("#475569") else android.graphics.Color.parseColor("#E2E8F0")
        style = Paint.Style.STROKE
        strokeWidth = 4.2f
        strokeCap = Paint.Cap.ROUND
    }
    val crossHalf = 8.5f
    canvas.drawLine(closeCenterX - crossHalf, closeCenterY - crossHalf, closeCenterX + crossHalf, closeCenterY + crossHalf, crossStrokePaint)
    canvas.drawLine(closeCenterX + crossHalf, closeCenterY - crossHalf, closeCenterX - crossHalf, closeCenterY + crossHalf, crossStrokePaint)

    // Main Content Row: Left Thumbnail Photo (or Category Icon Box) + Right Title & Coordinates
    val thumbLeft = cardLeft + 22f
    val thumbTop = cardTop + 74f
    val thumbSize = 116f
    val thumbRect = android.graphics.RectF(thumbLeft, thumbTop, thumbLeft + thumbSize, thumbTop + thumbSize)

    val thumbBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = markerOption.color.copy(alpha = 0.14f).toArgb()
        style = Paint.Style.FILL
    }
    canvas.drawRoundRect(thumbRect, 24f, 24f, thumbBgPaint)

    var drewPhoto = false
    val imgPath = pin.primaryImagePath
    if (!imgPath.isNullOrBlank()) {
        try {
            val imgFile = File(imgPath)
            if (imgFile.exists()) {
                val opts = BitmapFactory.Options().apply { inSampleSize = 2 }
                val rawBmp = BitmapFactory.decodeFile(imgFile.absolutePath, opts)
                if (rawBmp != null) {
                    val saveCount = canvas.save()
                    val clipPath = Path().apply {
                        addRoundRect(thumbRect, 24f, 24f, Path.Direction.CW)
                    }
                    canvas.clipPath(clipPath)
                    val srcRect = android.graphics.Rect(0, 0, rawBmp.width, rawBmp.height)
                    canvas.drawBitmap(rawBmp, srcRect, thumbRect, Paint(Paint.FILTER_BITMAP_FLAG))
                    canvas.restoreToCount(saveCount)
                    drewPhoto = true
                }
            }
        } catch (_: Exception) {}
    }

    if (!drewPhoto) {
        val emojiThumbPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = mainColor
            textSize = 52f
            textAlign = Paint.Align.CENTER
        }
        val efm = emojiThumbPaint.fontMetrics
        canvas.drawText(
            category.emoji,
            thumbRect.centerX(),
            thumbRect.centerY() - (efm.ascent + efm.descent) / 2f,
            emojiThumbPaint
        )
    }

    // Location Title & Coordinates next to Thumbnail
    val textStartX = thumbLeft + thumbSize + 22f
    val maxTextWidth = (cardRight - 24f) - textStartX

    val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if (isLightTheme) android.graphics.Color.parseColor("#0F172A") else android.graphics.Color.WHITE
        textSize = 31f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    var displayTitle = pin.locationName.ifBlank { "Saved Location" }
    if (titlePaint.measureText(displayTitle) > maxTextWidth) {
        while (displayTitle.length > 3 && titlePaint.measureText("$displayTitle…") > maxTextWidth) {
            displayTitle = displayTitle.dropLast(1)
        }
        displayTitle = "$displayTitle…"
    }
    canvas.drawText(displayTitle, textStartX, thumbTop + 48f, titlePaint)

    val coordsText = String.format(Locale.US, "%.4f°, %.4f°", pin.latitude, pin.longitude)
    val coordsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    canvas.drawText(coordsText, textStartX, thumbTop + 92f, coordsPaint)

    // --- PART B: CIRCULAR CUSTOM MARKER BADGE AT THE BOTTOM (WITH CLEAR GAP BELOW SPEECH-BUBBLE TAIL) ---
    val markerCenterY = 344f
    val markerRadius = 46f

    // Outer Pulsing Glow around Marker
    val markerGlowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = markerOption.color.copy(alpha = 0.30f).toArgb()
        style = Paint.Style.FILL
    }
    canvas.drawCircle(centerX, markerCenterY, markerRadius + 14f, markerGlowPaint)

    // White Border Shield around Marker
    val whitePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        style = Paint.Style.FILL
    }
    canvas.drawCircle(centerX, markerCenterY, markerRadius + 6f, whitePaint)

    // Colored Marker Circle
    canvas.drawCircle(centerX, markerCenterY, markerRadius, dotPaint)

    // Inner Category Ring + White Medallion
    val accentRingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = categoryColor
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }
    canvas.drawCircle(centerX, markerCenterY, markerRadius * 0.76f, accentRingPaint)
    canvas.drawCircle(centerX, markerCenterY, markerRadius * 0.68f, whitePaint)

    // Category Emoji inside Marker Medallion
    val markerEmojiPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = mainColor
        textSize = markerRadius * 0.88f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    val mfm = markerEmojiPaint.fontMetrics
    canvas.drawText(
        category.emoji,
        centerX,
        markerCenterY - (mfm.ascent + mfm.descent) / 2f,
        markerEmojiPaint
    )

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

/**
 * Copies a selected photo URI to internal app storage so that the photo persists
 * reliably across app restarts in Room local database.
 */
fun copyPhotoUriToInternalFile(context: Context, uri: Uri): String? {
    return try {
        val dir = File(context.filesDir, "floating_pin_photos")
        if (!dir.exists()) dir.mkdirs()
        val outFile = File(dir, "pin_img_${System.currentTimeMillis()}_${(100..999).random()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(outFile).use { output ->
                input.copyTo(output)
            }
        }
        outFile.absolutePath
    } catch (_: Exception) {
        null
    }
}

/**
 * Loads a scaled ImageBitmap safely from a local file path.
 */
@Composable
fun rememberSavedPhotoBitmap(filePath: String?, maxDim: Int = 800): androidx.compose.ui.graphics.ImageBitmap? {
    return remember(filePath, maxDim) {
        if (filePath.isNullOrBlank()) return@remember null
        try {
            val file = File(filePath)
            if (!file.exists()) return@remember null
            val boundsOpts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(file.absolutePath, boundsOpts)
            var sampleSize = 1
            while (boundsOpts.outWidth / sampleSize > maxDim || boundsOpts.outHeight / sampleSize > maxDim) {
                sampleSize *= 2
            }
            val decodeOpts = BitmapFactory.Options().apply { inSampleSize = sampleSize }
            BitmapFactory.decodeFile(file.absolutePath, decodeOpts)?.asImageBitmap()
        } catch (_: Exception) {
            null
        }
    }
}

/**
 * Sleek, single-line label, aligned compact input field so side-by-side fields
 * NEVER misalign due to label wrapping.
 */
@Composable
fun SleekCompactInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector? = null,
    accentColor: Color = Color(0xFF10B981),
    isLightTheme: Boolean,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLines: Int = if (singleLine) 1 else 3,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    val fieldBg = if (isLightTheme) Color(0xFFF1F5F9) else Color(0xFF16243D)
    val borderColor = when {
        isError -> Color(0xFFEF4444)
        isLightTheme -> Color(0xFFCBD5E1)
        else -> Color(0xFF263B5E)
    }
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    Column(modifier = modifier) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isError) Color(0xFFEF4444) else secondaryText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 2.dp, bottom = 4.dp)
        )
        Surface(
            color = fieldBg,
            shape = RoundedCornerShape(11.dp),
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier)
        ) {
            Row(
                verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = if (singleLine) 9.dp else 8.dp)
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (isError) Color(0xFFEF4444) else accentColor,
                        modifier = Modifier
                            .padding(top = if (singleLine) 0.dp else 2.dp)
                            .size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                }
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 12.sp,
                            color = secondaryText.copy(alpha = 0.72f),
                            maxLines = maxLines,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        singleLine = singleLine,
                        minLines = minLines,
                        maxLines = maxLines,
                        textStyle = TextStyle(
                            color = primaryText,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        cursorBrush = SolidColor(accentColor),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * ============================================================================
 * STEP 1 DIALOG:
 * Triggered when clicking the right-side circular '+' Floating Button on the Map.
 * Asks user: "Current Location (GPS)" or "Custom Location" (Pick on map or Enter Lat/Long).
 * ============================================================================
 */
@Composable
fun AddFloatingLocationSourceDialog(
    isLightTheme: Boolean,
    onSelectCurrentLocation: () -> Unit,
    onSelectCustomLocationOnMap: () -> Unit,
    onSelectCustomCoordinatesNow: () -> Unit,
    onDismiss: () -> Unit
) {
    val cardBg = if (isLightTheme) Color.White else Color(0xFF0F1A2E)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = cardBg,
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.dp, borderColor),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("add_floating_location_source_dialog")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981).copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Add Map Location",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = primaryText
                            )
                            Text(
                                text = "Choose Current GPS or Custom Coordinates",
                                fontSize = 11.sp,
                                color = secondaryText
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = secondaryText, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Option 1: Current Location (Checks GPS -> prompts built-in dialog if off -> auto-fills Lat/Long)
                Surface(
                    color = Color(0xFF10B981).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.55f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectCurrentLocation() }
                        .testTag("choose_current_gps_location_button")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Current Location",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Current Location (GPS)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Text(
                                text = "Checks GPS & auto-fills live Latitude & Longitude",
                                fontSize = 11.sp,
                                color = secondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Option 2: Custom Location (Enter Empty Lat/Long Manually)
                Surface(
                    color = Color(0xFFF59E0B).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.55f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectCustomCoordinatesNow() }
                        .testTag("choose_custom_coordinates_dialog_button")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF59E0B)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.EditLocationAlt,
                                contentDescription = "Custom Coordinates",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Custom Lat / Long (Manual Input)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Text(
                                text = "Opens empty Latitude & Longitude fields to type",
                                fontSize = 11.sp,
                                color = secondaryText
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Option 3: Custom Location (Tap on Map)
                Surface(
                    color = Color(0xFF0284C7).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.55f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectCustomLocationOnMap() }
                        .testTag("choose_custom_map_tap_location_button")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0284C7)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = "Custom Map Tap",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Custom Location (Pick on Map)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText
                            )
                            Text(
                                text = "Tap any point on the map to pick coordinates",
                                fontSize = 11.sp,
                                color = secondaryText
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * ============================================================================
 * STEP 2 DIALOG: CREATE FLOATING LOCATION ENTRY DIALOG
 * Fixes all alignment issues shown in the user's screenshot:
 * 1. Top Coordinates Card:
 *    - Clean header row with "COORDINATES *" on left and compact Date/Time pill on right (never wraps or collides!)
 *    - Single-line aligned labels: "Latitude *" and "Longitude *" so both input boxes sit at the exact same vertical level!
 *    - When Custom Lat/Long is chosen, initialLat/initialLon are null so fields start EMPTY, while blocking save until valid Lat/Long are entered.
 * 2. Location Category selector & Custom Marker Icon selector
 * 3. Location Title & Location Notes inputs
 * 4. Max 3 Photos (`Photos (0/3)`) — as soon as the user selects an image in the picker,
 *    the picker closes immediately and displays the selected image(s) right inside this dialog!
 * ============================================================================
 */
@Composable
fun CreateFloatingPinFormDialog(
    isLightTheme: Boolean,
    initialLat: Double?,
    initialLon: Double?,
    initialSuggestedName: String,
    sourceType: String, // "CURRENT" or "CUSTOM"
    onCancel: () -> Unit,
    onSave: (
        locationName: String,
        locationNotes: String,
        latitude: Double,
        longitude: Double,
        categoryKey: String,
        markerStyleKey: String,
        formattedDateTime: String,
        primaryImagePath: String?,
        secondImagePath: String?,
        thirdImagePath: String?
    ) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val cardBg = if (isLightTheme) Color.White else Color(0xFF0E1829)
    val innerBg = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF15233B)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF223656)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val formattedDateTime = remember {
        SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.US).format(Date())
    }

    var latText by remember(initialLat) {
        mutableStateOf(if (initialLat != null) String.format(Locale.US, "%.6f", initialLat) else "")
    }
    var lonText by remember(initialLon) {
        mutableStateOf(if (initialLon != null) String.format(Locale.US, "%.6f", initialLon) else "")
    }
    var locationName by remember { mutableStateOf(initialSuggestedName) }
    LaunchedEffect(initialSuggestedName) {
        if (locationName.isBlank() && initialSuggestedName.isNotBlank()) {
            locationName = initialSuggestedName
        }
    }

    var locationNotes by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(PinLocationCategory.TRAVELLING) }
    var selectedMarkerStyle by remember { mutableStateOf(CustomMapMarkerOption.EMERALD_PIN) }
    var validationErrorMessage by remember { mutableStateOf<String?>(null) }

    val attachedImagePaths = remember { mutableStateListOf<String>() }

    // Validate Latitude & Longitude strictly (cannot save empty or invalid coordinates)
    val parsedLat = latText.trim().toDoubleOrNull()
    val parsedLon = lonText.trim().toDoubleOrNull()
    val isLatValid = parsedLat != null && !parsedLat.isNaN() && parsedLat in -90.0..90.0
    val isLonValid = parsedLon != null && !parsedLon.isNaN() && parsedLon in -180.0..180.0
    val areCoordinatesValid = isLatValid && isLonValid

    // Single Photo Picker (`PickVisualMedia`):
    // As soon as the user taps ANY image in the gallery picker, the picker window immediately
    // closes and returns to this dialog showing the selected photo preview right away! (Up to 3 photos max)
    val immediatePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null && attachedImagePaths.size < 3) {
            val savedPath = copyPhotoUriToInternalFile(context, uri)
            if (savedPath != null) {
                attachedImagePaths.add(savedPath)
            }
        }
    }

    // Auto-scroll to bottom when a photo is added so user immediately sees the newly attached photo in the dialog
    LaunchedEffect(attachedImagePaths.size) {
        if (attachedImagePaths.isNotEmpty()) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = cardBg,
            shape = RoundedCornerShape(22.dp),
            border = BorderStroke(1.dp, borderColor),
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth(0.93f)
                .widthIn(max = 420.dp)
                .heightIn(max = 650.dp)
                .testTag("create_floating_pin_form_dialog")
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Sleek Compact Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(selectedMarkerStyle.color.copy(alpha = 0.16f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = selectedMarkerStyle.icon,
                                contentDescription = null,
                                tint = selectedMarkerStyle.color,
                                modifier = Modifier.size(19.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (sourceType == "CURRENT") "Save Current GPS Location" else "Save Custom Location",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${selectedCategory.emoji} ${selectedCategory.label} • ${selectedMarkerStyle.label}",
                                fontSize = 11.sp,
                                color = secondaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    IconButton(onClick = onCancel, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = secondaryText, modifier = Modifier.size(18.dp))
                    }
                }

                // Scrollable Body
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 1. TOP CARD: LATITUDE & LONGITUDE (MANDATORY) + DATE & TIME (FIXED ALIGNMENT!)
                    Surface(
                        color = innerBg,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(
                            1.dp,
                            if (!areCoordinatesValid && validationErrorMessage != null) Color(0xFFEF4444)
                            else selectedMarkerStyle.color.copy(alpha = 0.45f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(11.dp)) {
                            // Clean Single-Line Header Row: Left = Badge, Right = Date & Time Pill
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.GpsFixed,
                                        contentDescription = null,
                                        tint = selectedMarkerStyle.color,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "COORDINATES *",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = selectedMarkerStyle.color,
                                        maxLines = 1
                                    )
                                }

                                Surface(
                                    color = cardBg.copy(alpha = 0.8f),
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(0.5.dp, borderColor)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            tint = Color(0xFF0284C7),
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = formattedDateTime,
                                            fontSize = 9.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = secondaryText,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Perfectly Aligned Side-by-Side Latitude & Longitude Inputs
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                SleekCompactInputField(
                                    value = latText,
                                    onValueChange = {
                                        latText = it
                                        validationErrorMessage = null
                                    },
                                    label = "Latitude (-90..90) *",
                                    placeholder = "Enter Latitude",
                                    accentColor = selectedMarkerStyle.color,
                                    isLightTheme = isLightTheme,
                                    keyboardType = KeyboardType.Decimal,
                                    isError = latText.isNotEmpty() && !isLatValid,
                                    modifier = Modifier.weight(1f),
                                    testTag = "floating_pin_lat_input"
                                )
                                SleekCompactInputField(
                                    value = lonText,
                                    onValueChange = {
                                        lonText = it
                                        validationErrorMessage = null
                                    },
                                    label = "Longitude (-180..180) *",
                                    placeholder = "Enter Longitude",
                                    accentColor = selectedMarkerStyle.color,
                                    isLightTheme = isLightTheme,
                                    keyboardType = KeyboardType.Decimal,
                                    isError = lonText.isNotEmpty() && !isLonValid,
                                    modifier = Modifier.weight(1f),
                                    testTag = "floating_pin_lon_input"
                                )
                            }

                            if (!areCoordinatesValid) {
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "Please enter valid Latitude & Longitude to save location.",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (validationErrorMessage != null) Color(0xFFEF4444) else secondaryText
                                )
                            }
                        }
                    }

                    // 2. LOCATION CATEGORY SELECTOR (Travelling, Office, Work, Home, etc.)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Location Category",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = secondaryText,
                            modifier = Modifier.padding(start = 2.dp, bottom = 5.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PinLocationCategory.entries.forEach { cat ->
                                val isSelected = selectedCategory == cat
                                Surface(
                                    color = if (isSelected) cat.color else innerBg,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) cat.color else borderColor
                                    ),
                                    modifier = Modifier
                                        .clickable { selectedCategory = cat }
                                        .testTag("select_category_${cat.key.lowercase()}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = cat.icon,
                                            contentDescription = cat.label,
                                            tint = if (isSelected) Color.White else cat.color,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = cat.label,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else primaryText
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 3. CUSTOM MARKER ICON SELECTOR (Used on both Map & Floating Window)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Custom Marker Icon (Map & Floating Window)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = secondaryText,
                            modifier = Modifier.padding(start = 2.dp, bottom = 5.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            CustomMapMarkerOption.entries.forEach { markerOpt ->
                                val isSelected = selectedMarkerStyle == markerOpt
                                Surface(
                                    color = if (isSelected) markerOpt.color.copy(alpha = 0.18f) else innerBg,
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(
                                        if (isSelected) 1.5.dp else 1.dp,
                                        if (isSelected) markerOpt.color else borderColor
                                    ),
                                    modifier = Modifier
                                        .clickable { selectedMarkerStyle = markerOpt }
                                        .testTag("select_marker_style_${markerOpt.key.lowercase()}")
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(CircleShape)
                                                .background(markerOpt.color),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = markerOpt.icon,
                                                contentDescription = markerOpt.label,
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = markerOpt.label,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                            color = if (isSelected) markerOpt.color else primaryText
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 4. SLEEK COMPACT LOCATION NAME & NOTES INPUTS
                    SleekCompactInputField(
                        value = locationName,
                        onValueChange = {
                            locationName = it
                            validationErrorMessage = null
                        },
                        label = "Location Title",
                        placeholder = "e.g. City Office, Northern Trip Point...",
                        leadingIcon = selectedCategory.icon,
                        accentColor = selectedCategory.color,
                        isLightTheme = isLightTheme,
                        singleLine = true,
                        testTag = "floating_pin_name_input"
                    )

                    SleekCompactInputField(
                        value = locationNotes,
                        onValueChange = { locationNotes = it },
                        label = "Location Notes",
                        placeholder = "Add short notes or details...",
                        leadingIcon = Icons.Default.Notes,
                        accentColor = selectedMarkerStyle.color,
                        isLightTheme = isLightTheme,
                        singleLine = false,
                        minLines = 2,
                        maxLines = 3,
                        testTag = "floating_pin_notes_input"
                    )

                    // 5. ATTACH UP TO 3 PICTURES (Immediate Selection & Live Preview in Dialog)
                    Surface(
                        color = innerBg,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = null,
                                        tint = Color(0xFFF59E0B),
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Photos (${attachedImagePaths.size}/3 Max)",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryText
                                    )
                                }

                                if (attachedImagePaths.size < 3) {
                                    Surface(
                                        color = Color(0xFFF59E0B).copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp),
                                        border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.7f)),
                                        modifier = Modifier
                                            .clickable {
                                                immediatePhotoLauncher.launch(
                                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                )
                                            }
                                            .testTag("attach_floating_pin_photo_button")
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AddPhotoAlternate,
                                                contentDescription = "Add Photo",
                                                tint = Color(0xFFF59E0B),
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = if (attachedImagePaths.isEmpty()) "Select Photo" else "Add Photo (${attachedImagePaths.size}/3)",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFF59E0B)
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            if (attachedImagePaths.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(cardBg.copy(alpha = 0.65f))
                                        .clickable {
                                            immediatePhotoLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AddPhotoAlternate,
                                            contentDescription = "Select Photos",
                                            tint = secondaryText,
                                            modifier = Modifier.size(17.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Tap to select photo (up to 3 photos)",
                                            fontSize = 11.sp,
                                            color = secondaryText,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    attachedImagePaths.forEachIndexed { idx, path ->
                                        val bmp = rememberSavedPhotoBitmap(path, maxDim = 420)
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(82.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(cardBg)
                                        ) {
                                            if (bmp != null) {
                                                Image(
                                                    bitmap = bmp,
                                                    contentDescription = "Selected Photo ${idx + 1}",
                                                    contentScale = ContentScale.Crop,
                                                    modifier = Modifier.fillMaxSize()
                                                )
                                            }
                                            Surface(
                                                color = Color.Black.copy(alpha = 0.68f),
                                                shape = RoundedCornerShape(6.dp),
                                                modifier = Modifier
                                                    .align(Alignment.BottomStart)
                                                    .padding(4.dp)
                                            ) {
                                                Text(
                                                    text = if (idx == 0) "Main Pic" else "Pic ${idx + 1}",
                                                    fontSize = 8.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                                )
                                            }
                                            Surface(
                                                color = Color(0xFFEF4444),
                                                shape = CircleShape,
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(4.dp)
                                                    .size(19.dp)
                                                    .clickable { attachedImagePaths.removeAt(idx) }
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Remove Photo",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(11.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Quick '+' slot if fewer than 3 photos attached
                                    if (attachedImagePaths.size < 3) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(82.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(cardBg.copy(alpha = 0.65f))
                                                .clickable {
                                                    immediatePhotoLauncher.launch(
                                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                    )
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(
                                                    imageVector = Icons.Default.AddPhotoAlternate,
                                                    contentDescription = "Add Another Photo",
                                                    tint = Color(0xFFF59E0B),
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "+ Pic ${attachedImagePaths.size + 1}",
                                                    fontSize = 9.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFFF59E0B)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (validationErrorMessage != null) {
                        Text(
                            text = validationErrorMessage!!,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFEF4444)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                // 6. BOTTOM CANCEL & SAVE BUTTONS
                Surface(
                    color = innerBg,
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                attachedImagePaths.forEach { path ->
                                    try { File(path).delete() } catch (_: Exception) {}
                                }
                                onCancel()
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.6f)),
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("cancel_floating_pin_dialog_button")
                        ) {
                            Text(
                                text = "Cancel",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFEF4444),
                                fontSize = 13.sp
                            )
                        }

                        Button(
                            onClick = {
                                val finalLat = latText.trim().toDoubleOrNull()
                                val finalLon = lonText.trim().toDoubleOrNull()
                                if (finalLat == null || finalLon == null ||
                                    finalLat.isNaN() || finalLon.isNaN() ||
                                    finalLat !in -90.0..90.0 || finalLon !in -180.0..180.0
                                ) {
                                    validationErrorMessage = "Latitude & Longitude cannot be empty!"
                                    Toast.makeText(
                                        context,
                                        "Please enter valid Latitude & Longitude first!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                val finalName = locationName.trim().ifBlank { "${selectedCategory.label} Spot" }
                                val finalNotes = locationNotes.trim().ifBlank { "${selectedCategory.label} location" }
                                onSave(
                                    finalName,
                                    finalNotes,
                                    finalLat,
                                    finalLon,
                                    selectedCategory.key,
                                    selectedMarkerStyle.key,
                                    formattedDateTime,
                                    attachedImagePaths.getOrNull(0),
                                    attachedImagePaths.getOrNull(1),
                                    attachedImagePaths.getOrNull(2)
                                )
                            },
                            enabled = areCoordinatesValid,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = selectedMarkerStyle.color,
                                contentColor = Color.White,
                                disabledContainerColor = secondaryText.copy(alpha = 0.28f)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp)
                                .testTag("save_floating_pin_dialog_button")
                        ) {
                            Text(
                                text = "Save Location",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * ============================================================================
 * COMPACT FLOATING WINDOW CONNECTED DIRECTLY TO MARKER ON MAP
 * ============================================================================
 */
@Composable
fun MapFloatingWindowOverlayCard(
    pin: MapFloatingPinEntity,
    isLightTheme: Boolean,
    onOpenDetailsPage: (MapFloatingPinEntity) -> Unit,
    onCloseFloatingWindow: () -> Unit,
    pointerTipOffsetXPx: Float = 0f,
    modifier: Modifier = Modifier
) {
    val category = remember(pin.categoryKey) { PinLocationCategory.fromKey(pin.categoryKey) }
    val markerStyle = remember(pin.markerStyleKey) { CustomMapMarkerOption.fromKey(pin.markerStyleKey) }

    val cardBg = if (isLightTheme) Color.White else Color(0xFF0E192D)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)
    val thumbBitmap = rememberSavedPhotoBitmap(pin.primaryImagePath, maxDim = 260)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(220.dp)
    ) {
        // Speech Bubble 💬 Info Window Card
        Surface(
            color = cardBg,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(2.dp, markerStyle.color),
            shadowElevation = 12.dp,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenDetailsPage(pin) }
                .testTag("map_floating_window_card")
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(markerStyle.color),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = markerStyle.icon,
                                contentDescription = markerStyle.label,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = category.color.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = category.icon,
                                    contentDescription = null,
                                    tint = category.color,
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = category.label,
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = category.color,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = onCloseFloatingWindow,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Hide Info Window",
                            tint = secondaryText,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(markerStyle.color.copy(alpha = 0.14f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (thumbBitmap != null) {
                            Image(
                                bitmap = thumbBitmap,
                                contentDescription = pin.locationName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Icon(
                                imageVector = markerStyle.icon,
                                contentDescription = null,
                                tint = markerStyle.color,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = pin.locationName,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = String.format(Locale.US, "%.4f°, %.4f°", pin.latitude, pin.longitude),
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = markerStyle.color,
                            maxLines = 1
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Surface(
                        color = markerStyle.color.copy(alpha = 0.14f),
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Details",
                            tint = markerStyle.color,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(13.dp)
                        )
                    }
                }
            }
        }

        // Single Clean Speech Bubble 💬 Triangle Tail pointing directly to the marker below
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        ) {
            val centerX = size.width / 2f
            val tipX = (centerX + pointerTipOffsetXPx).coerceIn(22.dp.toPx(), size.width - 22.dp.toPx())
            val tailBaseHalfW = 10.dp.toPx()
            val tailBaseCenterX = (centerX + pointerTipOffsetXPx * 0.45f)
                .coerceIn(26.dp.toPx(), size.width - 26.dp.toPx())
            val bubbleTailBottomY = size.height

            // Speech-Bubble Triangle Tail (Filled with cardBg + bordered with markerStyle.color)
            val tailFillPath = androidx.compose.ui.graphics.Path().apply {
                moveTo(tailBaseCenterX - tailBaseHalfW, 0f)
                lineTo(tipX, bubbleTailBottomY)
                lineTo(tailBaseCenterX + tailBaseHalfW, 0f)
                close()
            }
            drawPath(
                path = tailFillPath,
                color = cardBg
            )

            // Left & right border strokes of the speech-bubble triangle tail
            drawLine(
                color = markerStyle.color,
                start = androidx.compose.ui.geometry.Offset(tailBaseCenterX - tailBaseHalfW, 0f),
                end = androidx.compose.ui.geometry.Offset(tipX, bubbleTailBottomY),
                strokeWidth = 2.dp.toPx()
            )
            drawLine(
                color = markerStyle.color,
                start = androidx.compose.ui.geometry.Offset(tailBaseCenterX + tailBaseHalfW, 0f),
                end = androidx.compose.ui.geometry.Offset(tipX, bubbleTailBottomY),
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}

/**
 * ============================================================================
 * DEDICATED FULL DETAILS & EDIT PAGE FOR A SINGLE SAVED FLOATING LOCATION
 * - Shows ONLY the selected location's details (no other locations list below)
 * - Includes built-in Edit Mode to edit Name, Category, Marker Icon, Notes, Coordinates, and Photos (1-3)
 * - Saving updates the shared Room database and immediately updates the Map Floating Window & All Locations list
 * ============================================================================
 */
@Composable
fun FloatingLocationDetailsPageScreen(
    pin: MapFloatingPinEntity,
    isLightTheme: Boolean,
    onBack: () -> Unit,
    onShowOnMap: (MapFloatingPinEntity) -> Unit,
    onUpdatePin: (MapFloatingPinEntity) -> Unit,
    onDeletePin: (MapFloatingPinEntity) -> Unit
) {
    var isEditMode by remember(pin.id) { mutableStateOf(false) }

    BackHandler {
        if (isEditMode) {
            isEditMode = false
        } else {
            onBack()
        }
    }

    val context = LocalContext.current

    val category = remember(pin.categoryKey) { PinLocationCategory.fromKey(pin.categoryKey) }
    val markerStyle = remember(pin.markerStyleKey) { CustomMapMarkerOption.fromKey(pin.markerStyleKey) }

    // Editable states initialized from `pin`
    var editName by remember(pin) { mutableStateOf(pin.locationName) }
    var editNotes by remember(pin) { mutableStateOf(pin.locationNotes) }
    var editLatText by remember(pin) { mutableStateOf(String.format(Locale.US, "%.6f", pin.latitude)) }
    var editLonText by remember(pin) { mutableStateOf(String.format(Locale.US, "%.6f", pin.longitude)) }
    var editCategory by remember(pin) { mutableStateOf(PinLocationCategory.fromKey(pin.categoryKey)) }
    var editMarkerStyle by remember(pin) { mutableStateOf(CustomMapMarkerOption.fromKey(pin.markerStyleKey)) }
    val editPhotos = remember(pin) {
        mutableStateListOf<String>().apply {
            listOfNotNull(pin.primaryImagePath, pin.secondImagePath, pin.thirdImagePath)
                .filter { it.isNotBlank() }
                .forEach { add(it) }
        }
    }

    val editPhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null && editPhotos.size < 3) {
            val savedPath = copyPhotoUriToInternalFile(context, uri)
            if (savedPath != null) {
                editPhotos.add(savedPath)
            }
        }
    }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val innerBg = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF172744)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    val bitmap1 = rememberSavedPhotoBitmap(pin.primaryImagePath, maxDim = 1024)
    val bitmap2 = rememberSavedPhotoBitmap(pin.secondImagePath, maxDim = 1024)
    val bitmap3 = rememberSavedPhotoBitmap(pin.thirdImagePath, maxDim = 1024)

    fun saveEditedChanges() {
        val parsedLat = editLatText.trim().toDoubleOrNull()
        val parsedLon = editLonText.trim().toDoubleOrNull()
        if (parsedLat == null || parsedLon == null ||
            parsedLat.isNaN() || parsedLon.isNaN() ||
            parsedLat !in -90.0..90.0 || parsedLon !in -180.0..180.0
        ) {
            Toast.makeText(context, "Please enter valid Latitude (-90..90) and Longitude (-180..180)", Toast.LENGTH_SHORT).show()
            return
        }
        val finalName = editName.trim().ifBlank { "${editCategory.label} Spot" }
        val finalNotes = editNotes.trim().ifBlank { "${editCategory.label} location" }

        val updatedEntity = pin.copy(
            locationName = finalName,
            locationNotes = finalNotes,
            latitude = parsedLat,
            longitude = parsedLon,
            categoryKey = editCategory.key,
            markerStyleKey = editMarkerStyle.key,
            primaryImagePath = editPhotos.getOrNull(0),
            secondImagePath = editPhotos.getOrNull(1),
            thirdImagePath = editPhotos.getOrNull(2)
        )
        onUpdatePin(updatedEntity)
        isEditMode = false
        Toast.makeText(context, "Location updated successfully!", Toast.LENGTH_SHORT).show()
    }

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("floating_location_details_page")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            // Top Header Bar with Back, Title, Edit/Save button, and Delete button
            Surface(
                color = cardBg,
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = {
                                if (isEditMode) {
                                    isEditMode = false
                                } else {
                                    onBack()
                                }
                            },
                            modifier = Modifier.testTag("floating_details_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = primaryText
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isEditMode) "Edit Location" else "Location Details",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = if (isEditMode) {
                                    "${editCategory.emoji} ${editCategory.label} • ${editMarkerStyle.label}"
                                } else {
                                    "${category.emoji} ${category.label} • ${markerStyle.label}"
                                },
                                fontSize = 11.sp,
                                color = secondaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Edit / Save Toggle Button
                        val editBtnColor = if (isEditMode) Color(0xFF10B981) else Color(0xFF0284C7)
                        Surface(
                            color = editBtnColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, editBtnColor.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .clickable {
                                    if (isEditMode) {
                                        saveEditedChanges()
                                    } else {
                                        isEditMode = true
                                    }
                                }
                                .testTag("edit_floating_pin_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isEditMode) Icons.Default.CheckCircle else Icons.Default.Edit,
                                    contentDescription = if (isEditMode) "Save Changes" else "Edit Location",
                                    tint = editBtnColor,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isEditMode) "Save" else "Edit",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = editBtnColor
                                )
                            }
                        }

                        // Delete Button
                        Surface(
                            color = Color(0xFFEF4444).copy(alpha = 0.14f),
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clickable { onDeletePin(pin) }
                                .testTag("delete_floating_pin_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "Delete",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFEF4444)
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isEditMode) {
                    // =========================================================================
                    // EDIT MODE UI (Edit Name, Category, Marker Icon, Coordinates, Notes, Photos)
                    // =========================================================================
                    Surface(
                        color = cardBg,
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(1.5.dp, editMarkerStyle.color.copy(alpha = 0.65f)),
                        modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Edit Location",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = primaryText
                            )

                            // 1. Edit Location Name
                            SleekCompactInputField(
                                value = editName,
                                onValueChange = { editName = it },
                                label = "Location Name",
                                placeholder = "Enter location name",
                                leadingIcon = editCategory.icon,
                                accentColor = editCategory.color,
                                isLightTheme = isLightTheme,
                                singleLine = true,
                                testTag = "edit_pin_name_input"
                            )

                            // 2. Edit Category Selector
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Category",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = secondaryText,
                                    modifier = Modifier.padding(start = 2.dp, bottom = 5.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    PinLocationCategory.entries.forEach { cat ->
                                        val isSelected = editCategory == cat
                                        Surface(
                                            color = if (isSelected) cat.color else innerBg,
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, if (isSelected) cat.color else borderColor),
                                            modifier = Modifier
                                                .clickable { editCategory = cat }
                                                .testTag("edit_category_${cat.key.lowercase()}")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = cat.icon,
                                                    contentDescription = cat.label,
                                                    tint = if (isSelected) Color.White else cat.color,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(5.dp))
                                                Text(
                                                    text = cat.label,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isSelected) Color.White else primaryText
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 3. Edit Marker Style Selector
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "Marker Icon",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = secondaryText,
                                    modifier = Modifier.padding(start = 2.dp, bottom = 5.dp)
                                )
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    CustomMapMarkerOption.entries.forEach { markerOpt ->
                                        val isSelected = editMarkerStyle == markerOpt
                                        Surface(
                                            color = if (isSelected) markerOpt.color.copy(alpha = 0.18f) else innerBg,
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(
                                                if (isSelected) 1.5.dp else 1.dp,
                                                if (isSelected) markerOpt.color else borderColor
                                            ),
                                            modifier = Modifier
                                                .clickable { editMarkerStyle = markerOpt }
                                                .testTag("edit_marker_style_${markerOpt.key.lowercase()}")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(20.dp)
                                                        .clip(CircleShape)
                                                        .background(markerOpt.color),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = markerOpt.icon,
                                                        contentDescription = markerOpt.label,
                                                        tint = Color.White,
                                                        modifier = Modifier.size(12.dp)
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = markerOpt.label,
                                                    fontSize = 11.sp,
                                                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                                    color = if (isSelected) markerOpt.color else primaryText
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // 4. Edit Latitude & Longitude
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SleekCompactInputField(
                                    value = editLatText,
                                    onValueChange = { editLatText = it },
                                    label = "Latitude",
                                    placeholder = "Latitude",
                                    accentColor = editMarkerStyle.color,
                                    isLightTheme = isLightTheme,
                                    keyboardType = KeyboardType.Decimal,
                                    modifier = Modifier.weight(1f),
                                    testTag = "edit_pin_lat_input"
                                )
                                SleekCompactInputField(
                                    value = editLonText,
                                    onValueChange = { editLonText = it },
                                    label = "Longitude",
                                    placeholder = "Longitude",
                                    accentColor = editMarkerStyle.color,
                                    isLightTheme = isLightTheme,
                                    keyboardType = KeyboardType.Decimal,
                                    modifier = Modifier.weight(1f),
                                    testTag = "edit_pin_lon_input"
                                )
                            }

                            // 5. Edit Location Notes
                            SleekCompactInputField(
                                value = editNotes,
                                onValueChange = { editNotes = it },
                                label = "Notes",
                                placeholder = "Enter notes...",
                                leadingIcon = Icons.Default.Notes,
                                accentColor = editMarkerStyle.color,
                                isLightTheme = isLightTheme,
                                singleLine = false,
                                minLines = 2,
                                maxLines = 4,
                                testTag = "edit_pin_notes_input"
                            )

                            // 6. Edit Photos (Add / Remove up to 3 photos)
                            Surface(
                                color = innerBg,
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, borderColor),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.Image,
                                                contentDescription = null,
                                                tint = Color(0xFFF59E0B),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "Photos (${editPhotos.size}/3)",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = primaryText
                                            )
                                        }
                                        if (editPhotos.size < 3) {
                                            Surface(
                                                color = Color(0xFFF59E0B).copy(alpha = 0.16f),
                                                shape = RoundedCornerShape(8.dp),
                                                border = BorderStroke(1.dp, Color(0xFFF59E0B)),
                                                modifier = Modifier
                                                    .clickable {
                                                        editPhotoPickerLauncher.launch(
                                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                        )
                                                    }
                                                    .testTag("edit_pin_add_photo_button")
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.AddPhotoAlternate,
                                                        contentDescription = "Add Photo",
                                                        tint = Color(0xFFF59E0B),
                                                        modifier = Modifier.size(13.dp)
                                                    )
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text(
                                                        text = "Add",
                                                        fontSize = 10.5.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFF59E0B)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    if (editPhotos.isEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(56.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(cardBg.copy(alpha = 0.65f))
                                                .clickable {
                                                    editPhotoPickerLauncher.launch(
                                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                    )
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Tap to add photo",
                                                fontSize = 11.sp,
                                                color = secondaryText
                                            )
                                        }
                                    } else {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            editPhotos.forEachIndexed { idx, path ->
                                                val bmp = rememberSavedPhotoBitmap(path, maxDim = 420)
                                                Box(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .height(88.dp)
                                                        .clip(RoundedCornerShape(10.dp))
                                                        .background(cardBg)
                                                ) {
                                                    if (bmp != null) {
                                                        Image(
                                                            bitmap = bmp,
                                                            contentDescription = "Photo ${idx + 1}",
                                                            contentScale = ContentScale.Crop,
                                                            modifier = Modifier.fillMaxSize()
                                                        )
                                                    }
                                                    Surface(
                                                        color = Color(0xFFEF4444),
                                                        shape = CircleShape,
                                                        modifier = Modifier
                                                            .align(Alignment.TopEnd)
                                                            .padding(4.dp)
                                                            .size(20.dp)
                                                            .clickable { editPhotos.removeAt(idx) }
                                                    ) {
                                                        Box(contentAlignment = Alignment.Center) {
                                                            Icon(
                                                                imageVector = Icons.Default.Close,
                                                                contentDescription = "Remove Photo",
                                                                tint = Color.White,
                                                                modifier = Modifier.size(12.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Save & Cancel Edit Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = { isEditMode = false },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, borderColor),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                ) {
                                    Text("Cancel", fontWeight = FontWeight.Bold, color = secondaryText)
                                }

                                Button(
                                    onClick = { saveEditedChanges() },
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = editMarkerStyle.color,
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(44.dp)
                                        .testTag("save_edited_pin_button")
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Save", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                } else {
                    // =========================================================================
                    // VIEW MODE UI (Single Location Details Only + Quick Edit Button)
                    // =========================================================================
                    Surface(
                        color = cardBg,
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(1.5.dp, markerStyle.color.copy(alpha = 0.65f)),
                        modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            markerStyle.color.copy(alpha = 0.14f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(markerStyle.color),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = markerStyle.icon,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Surface(
                                        color = category.color.copy(alpha = 0.16f),
                                        shape = RoundedCornerShape(50),
                                        border = BorderStroke(1.dp, category.color.copy(alpha = 0.5f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = category.icon,
                                                contentDescription = null,
                                                tint = category.color,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = category.label,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = category.color
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = pin.formattedDateTime.substringBefore("•").trim(),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = secondaryText,
                                    maxLines = 1
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = pin.locationName,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = primaryText,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Surface(
                                color = innerBg,
                                shape = RoundedCornerShape(14.dp),
                                border = BorderStroke(1.dp, borderColor),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "LATITUDE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = secondaryText
                                        )
                                        Text(
                                            text = String.format(Locale.US, "%.5f°", pin.latitude),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = markerStyle.color,
                                            maxLines = 1
                                        )
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text(
                                            text = "LONGITUDE",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = secondaryText
                                        )
                                        Text(
                                            text = String.format(Locale.US, "%.5f°", pin.longitude),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF0284C7),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (pin.locationNotes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        // Location Notes Card
                        Surface(
                            color = cardBg,
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, borderColor),
                            modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Notes,
                                            contentDescription = null,
                                            tint = Color(0xFF0284C7),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Notes",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = primaryText
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = pin.locationNotes,
                                    fontSize = 13.sp,
                                    color = primaryText.copy(alpha = 0.9f),
                                    lineHeight = 19.sp
                                )
                            }
                        }
                    }

                    if (bitmap1 != null || bitmap2 != null || bitmap3 != null) {
                        Spacer(modifier = Modifier.height(12.dp))

                        // Attached Photos Section
                        Surface(
                            color = cardBg,
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, borderColor),
                            modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Image,
                                        contentDescription = null,
                                        tint = Color(0xFFF59E0B),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Photos",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = primaryText
                                    )
                                }

                                if (bitmap1 != null) {
                                    Image(
                                        bitmap = bitmap1,
                                        contentDescription = "Photo 1",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                    )
                                }

                                if (bitmap2 != null) {
                                    Image(
                                        bitmap = bitmap2,
                                        contentDescription = "Photo 2",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                    )
                                }

                                if (bitmap3 != null) {
                                    Image(
                                        bitmap = bitmap3,
                                        contentDescription = "Photo 3",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(14.dp))
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Action Buttons: Map / Edit / Copy
                    Row(
                        modifier = Modifier.widthIn(max = 520.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onShowOnMap(pin) },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = markerStyle.color,
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                        ) {
                            Icon(Icons.Default.Map, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Map", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, maxLines = 1)
                        }

                        Button(
                            onClick = { isEditMode = true },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Edit", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, maxLines = 1)
                        }

                        Button(
                            onClick = {
                                val shareBody = buildString {
                                    append("${category.emoji} ${pin.locationName} (${category.label})\n")
                                    append("📝 Notes: ${pin.locationNotes}\n")
                                    append("🌐 Coordinates: ${String.format(Locale.US, "%.6f, %.6f", pin.latitude, pin.longitude)}\n")
                                    append("🕒 Saved: ${pin.formattedDateTime}\n")
                                    append("🗺️ https://maps.google.com/?q=${pin.latitude},${pin.longitude}")
                                }
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                clipboard?.setPrimaryClip(ClipData.newPlainText("Saved Location", shareBody))
                                Toast.makeText(context, "Copied location details!", Toast.LENGTH_SHORT).show()
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0284C7),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copy", fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, maxLines = 1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Sticky Bottom Adaptive Banner Ad on Saved Location Details Page
            AdMobAdaptiveBanner(
                placementName = "Pin_Details_Bottom_Banner",
                adUnitId = AdMobConfig.BANNER_PIN_DETAILS_AD_UNIT_ID,
                isLightTheme = isLightTheme
            )
        }
    }
}

/**
 * ============================================================================
 * DEDICATED "ALL LOCATIONS" LIST PAGE (Opened from Right Side Button on Map Page)
 * - Displays all saved locations in a top-to-bottom vertical list
 * - Includes Search Bar (search by location name, notes, or category)
 * - Includes Category Filter Chips at the top ("All", Travelling, Office, Work, Home, etc.)
 * - Clicking any location opens that location's dedicated Details Page (with Edit Mode)
 * ============================================================================
 */
@Composable
fun AllLocationsListPageScreen(
    allSavedPins: List<MapFloatingPinEntity>,
    isLightTheme: Boolean,
    onBackToMap: () -> Unit,
    onSelectLocationForDetails: (MapFloatingPinEntity) -> Unit,
    onShowLocationOnMap: (MapFloatingPinEntity) -> Unit,
    onAddNewLocation: () -> Unit
) {
    BackHandler { onBackToMap() }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryKey by remember { mutableStateOf("ALL") }

    val filteredPins = remember(allSavedPins, searchQuery, selectedCategoryKey) {
        allSavedPins.filter { pin ->
            val catMatch = selectedCategoryKey == "ALL" ||
                    pin.categoryKey.equals(selectedCategoryKey, ignoreCase = true)
            val q = searchQuery.trim()
            val searchMatch = if (q.isEmpty()) {
                true
            } else {
                val catLabel = PinLocationCategory.fromKey(pin.categoryKey).label
                pin.locationName.contains(q, ignoreCase = true) ||
                        pin.locationNotes.contains(q, ignoreCase = true) ||
                        catLabel.contains(q, ignoreCase = true)
            }
            catMatch && searchMatch
        }
    }

    val pageBg = if (isLightTheme) Color(0xFFF4F7FB) else Color(0xFF060D1A)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val innerBg = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF172744)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E3252)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .testTag("all_locations_list_page")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            // Top Header Bar
            Surface(
                color = cardBg,
                border = BorderStroke(1.dp, borderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onBackToMap,
                            modifier = Modifier.testTag("all_locations_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back to Map",
                                tint = primaryText
                            )
                        }
                        Column {
                            Text(
                                text = "Saved Locations",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = primaryText
                            )
                        }
                    }

                    Surface(
                        color = Color(0xFF10B981),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .clickable { onAddNewLocation() }
                            .testTag("all_locations_add_new_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Location",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Add",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Search Bar + Category Filter Bar at the Top
            Surface(
                color = cardBg.copy(alpha = 0.96f),
                border = BorderStroke(0.5.dp, borderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    // Search Input Box
                    Surface(
                        color = innerBg,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("all_locations_search_input")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 14.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        text = "Search location...",
                                        fontSize = 12.5.sp,
                                        color = secondaryText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                BasicTextField(
                                    value = searchQuery,
                                    onValueChange = { searchQuery = it },
                                    singleLine = true,
                                    textStyle = TextStyle(
                                        color = primaryText,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    cursorBrush = SolidColor(Color(0xFF10B981)),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { searchQuery = "" },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear Search",
                                        tint = secondaryText,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Horizontal Category Filter Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val isAllSelected = selectedCategoryKey == "ALL"
                        Surface(
                            color = if (isAllSelected) Color(0xFF10B981) else innerBg,
                            shape = RoundedCornerShape(50),
                            border = BorderStroke(1.dp, if (isAllSelected) Color(0xFF10B981) else borderColor),
                            modifier = Modifier
                                .clickable { selectedCategoryKey = "ALL" }
                                .testTag("all_locations_filter_all")
                        ) {
                            Text(
                                text = "All (${allSavedPins.size})",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isAllSelected) Color.White else primaryText,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        PinLocationCategory.entries.forEach { cat ->
                            val count = allSavedPins.count { it.categoryKey.equals(cat.key, ignoreCase = true) }
                            val isSelected = selectedCategoryKey == cat.key
                            Surface(
                                color = if (isSelected) cat.color else innerBg,
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, if (isSelected) cat.color else borderColor),
                                modifier = Modifier
                                    .clickable {
                                        selectedCategoryKey = if (isSelected) "ALL" else cat.key
                                    }
                                    .testTag("all_locations_filter_${cat.key.lowercase()}")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = cat.icon,
                                        contentDescription = cat.label,
                                        tint = if (isSelected) Color.White else cat.color,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = if (count > 0) "${cat.label} ($count)" else cat.label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else primaryText
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Top-to-Bottom Vertical List of Locations
            if (filteredPins.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF10B981).copy(alpha = 0.14f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (allSavedPins.isEmpty()) "No Saved Locations" else "No Matches Found",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryText
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredPins, key = { it.id }) { item ->
                        val cat = PinLocationCategory.fromKey(item.categoryKey)
                        val markerStyle = CustomMapMarkerOption.fromKey(item.markerStyleKey)
                        val thumbBitmap = rememberSavedPhotoBitmap(item.primaryImagePath, maxDim = 240)

                        Surface(
                            color = cardBg,
                            shape = RoundedCornerShape(18.dp),
                            border = BorderStroke(1.dp, markerStyle.color.copy(alpha = 0.45f)),
                            shadowElevation = 3.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onSelectLocationForDetails(item) }
                                .testTag("all_locations_item_${item.id}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Thumbnail or Marker Icon
                                Box(
                                    modifier = Modifier
                                        .size(54.dp)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(markerStyle.color.copy(alpha = 0.14f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (thumbBitmap != null) {
                                        Image(
                                            bitmap = thumbBitmap,
                                            contentDescription = item.locationName,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    } else {
                                        Icon(
                                            imageVector = markerStyle.icon,
                                            contentDescription = null,
                                            tint = markerStyle.color,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.locationName,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = primaryText,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Spacer(modifier = Modifier.height(3.dp))

                                    Surface(
                                        color = cat.color.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = cat.icon,
                                                contentDescription = null,
                                                tint = cat.color,
                                                modifier = Modifier.size(11.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = cat.label,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.ExtraBold,
                                                color = cat.color,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Surface(
                                    color = markerStyle.color.copy(alpha = 0.14f),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.clickable { onShowLocationOnMap(item) }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Map,
                                            contentDescription = "Show on Map",
                                            tint = markerStyle.color,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Map",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = markerStyle.color
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Sticky Bottom Adaptive Banner Ad
            AdMobAdaptiveBanner(
                placementName = "All_Locations_List_Bottom_Banner",
                adUnitId = AdMobConfig.BANNER_PIN_DETAILS_AD_UNIT_ID,
                isLightTheme = isLightTheme
            )
        }
    }
}

