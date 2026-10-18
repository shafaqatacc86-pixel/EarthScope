package com.example.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdUnits
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

/**
 * ============================================================================
 * CENTRALIZED GOOGLE ADMOB CONFIGURATION & DUMMY/TEST AD ENGINE
 * ============================================================================
 * Currently configured with Official Google AdMob Dummy/Test Ad Unit IDs
 * so you can preview every Ad placement safely without policy violations.
 *
 * LATER: When you provide your real AdMob App ID & Ad Unit IDs, simply
 * replace the IDs below and set IS_DUMMY_TEST_MODE = false!
 * ============================================================================
 */
object AdMobSdkState {
    var isInitialized by mutableStateOf(false)
        private set

    fun markInitialized() {
        isInitialized = true
    }

    fun isRunningOnEmulator(): Boolean {
        val fingerprint = android.os.Build.FINGERPRINT ?: ""
        val model = android.os.Build.MODEL ?: ""
        val product = android.os.Build.PRODUCT ?: ""
        val hardware = android.os.Build.HARDWARE ?: ""
        return fingerprint.startsWith("generic") ||
            fingerprint.lowercase().contains("emulator") ||
            model.contains("Emulator") ||
            model.contains("Android SDK built for") ||
            model.contains("sdk_gphone") ||
            product.contains("sdk") ||
            product.contains("emulator") ||
            hardware.contains("goldfish") ||
            hardware.contains("ranchu") ||
            hardware.contains("cuttlefish") ||
            hardware.contains("vsoc")
    }
}

object AdMobConfig {
    // Mode Switch: Set to true for Google Test Ads, false for Production Real Ads
    const val USE_TEST_ADS: Boolean = true

    // Offline dummy fallback (if device has zero internet connectivity)
    const val IS_DUMMY_TEST_MODE: Boolean = false

    // ==========================================
    // 1. Google Official Test Ad Unit IDs (Active)
    // ==========================================
    const val ADMOB_APP_ID: String = "ca-app-pub-3940256099942544~3347511713"

    // Banner Ads (Test Adaptive Banner)
    const val BANNER_AD_UNIT_ID: String = "ca-app-pub-3940256099942544/9214589741"
    const val BANNER_HOME_AD_UNIT_ID: String = BANNER_AD_UNIT_ID
    const val BANNER_SPEEDOMETER_AD_UNIT_ID: String = BANNER_AD_UNIT_ID
    const val BANNER_COMPASS_AD_UNIT_ID: String = BANNER_AD_UNIT_ID
    const val BANNER_SHARE_LOC_AD_UNIT_ID: String = BANNER_AD_UNIT_ID
    const val BANNER_PIN_DETAILS_AD_UNIT_ID: String = BANNER_AD_UNIT_ID
    const val BANNER_SAVED_PLACES_AD_UNIT_ID: String = BANNER_AD_UNIT_ID
    const val BANNER_TRIP_HISTORY_AD_UNIT_ID: String = BANNER_AD_UNIT_ID

    // Native / Feed Card Ads (Test Native Advanced)
    const val NATIVE_HOME_FEED_AD_UNIT_ID: String = "ca-app-pub-3940256099942544/2247696110"
    const val NATIVE_NEARBY_PLACES_AD_UNIT_ID: String = NATIVE_HOME_FEED_AD_UNIT_ID
    const val NATIVE_COUNTRY_DETAILS_AD_UNIT_ID: String = NATIVE_HOME_FEED_AD_UNIT_ID
    const val NATIVE_COUNTRY_ATLAS_AD_UNIT_ID: String = NATIVE_HOME_FEED_AD_UNIT_ID

    // Interstitial Full-Screen Ads (Test Interstitial)
    const val INTERSTITIAL_AD_UNIT_ID: String = "ca-app-pub-3940256099942544/1033173712"
    const val INTERSTITIAL_FEATURE_NAV_AD_UNIT_ID: String = INTERSTITIAL_AD_UNIT_ID
    const val INTERSTITIAL_PIN_SAVED_AD_UNIT_ID: String = INTERSTITIAL_AD_UNIT_ID

    // Rewarded & Rewarded Interstitial Ads (Test Rewarded)
    const val REWARDED_AD_UNIT_ID: String = "ca-app-pub-3940256099942544/5224354917"
    const val REWARDED_INTERSTITIAL_AD_UNIT_ID: String = "ca-app-pub-3940256099942544/5354046379"

    // App Open Ads (Test App Open)
    const val APP_OPEN_AD_UNIT_ID: String = "ca-app-pub-3940256099942544/9257395921"

    // ==========================================
    // 2. User's Real Production AdMob IDs (Saved for Production Release)
    // ==========================================
    const val PROD_APP_ID = "ca-app-pub-9038493214458444~4203142842"
    const val PROD_BANNER_AD_UNIT_ID = "ca-app-pub-9038493214458444/4241529650"
    const val PROD_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9038493214458444/6288363636"
    const val PROD_REWARDED_AD_UNIT_ID = "ca-app-pub-9038493214458444/2681035976"
    const val PROD_REWARDED_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9038493214458444/5307199318"

    // Frequency Capping Settings (Google Policy Safe)
    const val INTERSTITIAL_CLICK_INTERVAL: Int = 3 // Show every 3rd feature navigation
    const val INTERSTITIAL_MIN_COOLDOWN_MS: Long = 45_000L // Minimum 45 sec gap between interstitials
}

/**
 * Sample Dummy Ad Creative Data for fallback preview when offline or while AdMob inventory is warming up
 */
data class DummyAdCreative(
    val headline: String,
    val body: String,
    val advertiser: String,
    val rating: String,
    val priceOrTag: String,
    val ctaText: String,
    val accentColor: Color,
    val icon: ImageVector
)

private val DUMMY_AD_CREATIVES = listOf(
    DummyAdCreative(
        headline = "Google Maps Cloud Pro • Satellite HD",
        body = "Test Ad: Upgrade your outdoor navigation with offline 3D terrain & live traffic alerts.",
        advertiser = "Google AdMob Test Network",
        rating = "4.9 ★",
        priceOrTag = "FREE",
        ctaText = "Install Now",
        accentColor = Color(0xFF0284C7),
        icon = Icons.Default.Public
    ),
    DummyAdCreative(
        headline = "Smart Dashcam & GPS Trip Logger",
        body = "Test Ad: Record HD road trips, speed graphs & automatic cloud backup while driving.",
        advertiser = "AdMob Dummy Advertiser",
        rating = "4.8 ★",
        priceOrTag = "App Store",
        ctaText = "Learn More",
        accentColor = Color(0xFF10B981),
        icon = Icons.Default.Speed
    ),
    DummyAdCreative(
        headline = "World Traveler Flights & Hotels",
        body = "Test Ad: Compare global flights, book hotels near your GPS pin and save up to 40%.",
        advertiser = "TravelScope Sponsored",
        rating = "4.7 ★",
        priceOrTag = "Sponsored",
        ctaText = "Open Offer",
        accentColor = Color(0xFF8B5CF6),
        icon = Icons.Default.Explore
    ),
    DummyAdCreative(
        headline = "TrailFinder 3D Compass & Topo Maps",
        body = "Test Ad: Precision altimeter, magnetic compass & offline hiking trails worldwide.",
        advertiser = "AdMob Test Creative",
        rating = "4.9 ★",
        priceOrTag = "Editor's Choice",
        ctaText = "Try Free",
        accentColor = Color(0xFFF59E0B),
        icon = Icons.Default.Map
    )
)

/**
 * ============================================================================
 * 1. ADAPTIVE BANNER AD COMPONENT (Real Google AdMob AdView + Safe Container)
 * ============================================================================
 */
@Composable
fun AdMobAdaptiveBanner(
    placementName: String,
    adUnitId: String = AdMobConfig.BANNER_HOME_AD_UNIT_ID,
    isLightTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val bannerBg = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF0D1729)
    val borderColor = if (isLightTheme) Color(0xFFCBD5E1) else Color(0xFF1E3252)
    val isEmulator = remember { AdMobSdkState.isRunningOnEmulator() }
    var adLoadFailed by remember(adUnitId) { mutableStateOf(false) }
    var adLoadedSuccessfully by remember(adUnitId) { mutableStateOf(false) }

    if (isEmulator || adLoadFailed) {
        // Automatically hide and collapse space if running on emulator or ad load fails/offline
        return
    }

    Surface(
        color = bannerBg,
        border = if (adLoadedSuccessfully) BorderStroke(1.dp, borderColor) else null,
        shadowElevation = if (adLoadedSuccessfully) 4.dp else 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = if (adLoadedSuccessfully) 8.dp else 0.dp)
            .testTag("admob_banner_$placementName")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = if (adLoadedSuccessfully) 4.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.ui.viewinterop.AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { ctx ->
                    com.google.android.gms.ads.AdView(ctx).apply {
                        setAdSize(com.google.android.gms.ads.AdSize.BANNER)
                        this.adUnitId = adUnitId
                        adListener = object : com.google.android.gms.ads.AdListener() {
                            override fun onAdLoaded() {
                                adLoadedSuccessfully = true
                                adLoadFailed = false
                            }

                            override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                                adLoadFailed = true
                                adLoadedSuccessfully = false
                            }
                        }
                        try {
                            loadAd(com.google.android.gms.ads.AdRequest.Builder().build())
                        } catch (_: Throwable) {
                            adLoadFailed = true
                        }
                    }
                }
            )
        }
    }
}

/**
 * ============================================================================
 * 2. INLINE CARD AD COMPONENT (Renders Real AdMob Banner inside Material 3 Card)
 * ============================================================================
 */
@Composable
fun AdMobNativeAdCard(
    placementName: String,
    adUnitId: String = AdMobConfig.NATIVE_HOME_FEED_AD_UNIT_ID,
    isLightTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val cardBg = if (isLightTheme) Color.White else Color(0xFF111E34)
    val borderColor = if (isLightTheme) Color(0xFFCBD5E1) else Color(0xFF23385E)
    val isEmulator = remember { AdMobSdkState.isRunningOnEmulator() }
    var adLoadFailed by remember(adUnitId) { mutableStateOf(false) }
    var adLoadedSuccessfully by remember(adUnitId) { mutableStateOf(false) }

    if (isEmulator || adLoadFailed) {
        // Automatically hide and collapse space if running on emulator or ad load fails/offline
        return
    }

    Surface(
        color = if (adLoadedSuccessfully) cardBg else Color.Transparent,
        shape = RoundedCornerShape(20.dp),
        border = if (adLoadedSuccessfully) BorderStroke(1.2.dp, borderColor) else null,
        shadowElevation = if (adLoadedSuccessfully) 4.dp else 0.dp,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .testTag("admob_native_$placementName")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = if (adLoadedSuccessfully) 8.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.ui.viewinterop.AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { ctx ->
                    com.google.android.gms.ads.AdView(ctx).apply {
                        setAdSize(com.google.android.gms.ads.AdSize.BANNER)
                        this.adUnitId = adUnitId
                        adListener = object : com.google.android.gms.ads.AdListener() {
                            override fun onAdLoaded() {
                                adLoadedSuccessfully = true
                                adLoadFailed = false
                            }

                            override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                                adLoadFailed = true
                                adLoadedSuccessfully = false
                            }
                        }
                        try {
                            loadAd(com.google.android.gms.ads.AdRequest.Builder().build())
                        } catch (_: Throwable) {
                            adLoadFailed = true
                        }
                    }
                }
            )
        }
    }
}

/**
 * Helper to load and show a Real Google AdMob Rewarded Ad or Rewarded Interstitial Ad
 */
fun showRealAdMobRewardedAd(
    context: Context,
    useRewardedInterstitial: Boolean = false,
    onRewardEarned: () -> Unit = {},
    onDismissed: () -> Unit = {}
) {
    val activity = context as? Activity
    if (activity == null || AdMobSdkState.isRunningOnEmulator()) {
        onRewardEarned()
        onDismissed()
        return
    }
    val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
    if (useRewardedInterstitial) {
        com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd.load(
            context,
            AdMobConfig.REWARDED_INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd) {
                    ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            onDismissed()
                        }
                        override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                            onDismissed()
                        }
                    }
                    ad.show(activity) { onRewardEarned() }
                }
                override fun onAdFailedToLoad(loadAdError: com.google.android.gms.ads.LoadAdError) {
                    onDismissed()
                }
            }
        )
    } else {
        com.google.android.gms.ads.rewarded.RewardedAd.load(
            context,
            AdMobConfig.REWARDED_AD_UNIT_ID,
            adRequest,
            object : com.google.android.gms.ads.rewarded.RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: com.google.android.gms.ads.rewarded.RewardedAd) {
                    ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            onDismissed()
                        }
                        override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                            onDismissed()
                        }
                    }
                    ad.show(activity) { onRewardEarned() }
                }
                override fun onAdFailedToLoad(loadAdError: com.google.android.gms.ads.LoadAdError) {
                    onDismissed()
                }
            }
        )
    }
}

/**
 * ============================================================================
 * 3. FULL-SCREEN INTERSTITIAL AD HANDLER (Real Google AdMob InterstitialAd)
 * ============================================================================
 */
@Composable
fun AdMobDummyInterstitialDialog(
    placementTrigger: String,
    adUnitId: String = AdMobConfig.INTERSTITIAL_FEATURE_NAV_AD_UNIT_ID,
    onAdClosed: () -> Unit
) {
    val context = LocalContext.current
    val activity = remember(context) {
        var ctx = context
        while (ctx is android.content.ContextWrapper) {
            if (ctx is Activity) break
            ctx = ctx.baseContext
        }
        ctx as? Activity
    }

    LaunchedEffect(adUnitId) {
        if (activity == null) {
            onAdClosed()
            return@LaunchedEffect
        }
        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd) {
                    interstitialAd.fullScreenContentCallback =
                        object : com.google.android.gms.ads.FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                onAdClosed()
                            }

                            override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                                onAdClosed()
                            }
                        }
                    interstitialAd.show(activity)
                }

                override fun onAdFailedToLoad(loadAdError: com.google.android.gms.ads.LoadAdError) {
                    onAdClosed()
                }
            }
        )
    }

    // Centered non-intrusive loading dialog while Google prepares the full-screen ad
    Dialog(
        onDismissRequest = { onAdClosed() },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Surface(
            color = Color(0xFF0F172A),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color(0xFF38BDF8),
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Loading Ad...",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}



/**
 * ============================================================================
 * 4. APP OPEN AD OVERLAY BANNER/MODAL (Shown once after Splash Screen)
 * ============================================================================
 */
@Composable
fun AdMobDummyAppOpenDialog(
    adUnitId: String = AdMobConfig.APP_OPEN_AD_UNIT_ID,
    onContinueToApp: () -> Unit
) {
    var autoProgress by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        for (i in 1..100) {
            delay(28L)
            autoProgress = i
        }
    }

    Surface(
        color = Color(0xFF040B18),
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = true, onClick = {})
            .testTag("admob_app_open_dialog")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top App Header + "Continue to App" Button (Standard Google App Open Ad Bar)
            Surface(
                color = Color(0xFF111E34),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, Color(0xFF1E3252)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        EarthScopeTransparentLogo(modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Earth Scope",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "App Open Test Ad",
                                fontSize = 10.sp,
                                color = Color(0xFF38BDF8)
                            )
                        }
                    }

                    Button(
                        onClick = onContinueToApp,
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF22C55E),
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                        modifier = Modifier
                            .height(36.dp)
                            .testTag("continue_to_app_from_app_open_ad")
                    ) {
                        Text(
                            text = "Continue to App →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // Center App Open Ad Creative
            Surface(
                color = Color(0xFF0F1B30),
                shape = RoundedCornerShape(26.dp),
                border = BorderStroke(1.5.dp, Color(0xFFF59E0B).copy(alpha = 0.55f)),
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = Color(0xFFF59E0B),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "APP OPEN AD",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Google AdMob Dummy Test",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF59E0B).copy(alpha = 0.16f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(0xFFF59E0B),
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Welcome to EarthScope 3D",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "This is your App Open Ad placement shown when the app launches. Replace APP_OPEN_AD_UNIT_ID in AdMobConfig.kt with your real ID anytime.",
                        fontSize = 12.5.sp,
                        color = Color(0xFFCBD5E1),
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Test ID: $adUnitId",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF38BDF8)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = { autoProgress / 100f },
                        color = Color(0xFF22C55E),
                        trackColor = Color(0xFF1E293B),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                    )
                }
            }

            Text(
                text = "Tap 'Continue to App' above to close App Open Ad",
                fontSize = 11.sp,
                color = Color(0xFF94A3B8)
            )
        }
    }
}
