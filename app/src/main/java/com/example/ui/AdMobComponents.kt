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
import androidx.compose.runtime.mutableStateMapOf
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
    // Mode Switch: true = 100% Official Google AdMob Test Ads Active (Fast, 100% fill rate for testing & verification)
    // false = Real Production Google AdMob Ads Active
    const val USE_TEST_ADS: Boolean = false

    // Offline dummy fallback (if device has zero internet connectivity)
    const val IS_DUMMY_TEST_MODE: Boolean = false

    // ==========================================
    // Real Production AdMob IDs (User's Official Account)
    // ==========================================
    const val PROD_APP_ID = "ca-app-pub-9038493214458444~4203142842"
    const val PROD_BANNER_AD_UNIT_ID = "ca-app-pub-9038493214458444/4241529650"
    const val PROD_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9038493214458444/6288363636"
    const val PROD_REWARDED_AD_UNIT_ID = "ca-app-pub-9038493214458444/2681035976"
    const val PROD_REWARDED_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-9038493214458444/5307199318"

    // ==========================================
    // Official Google AdMob Sample Test Ad Unit IDs
    // (Google guarantees 100% fill rate for these IDs without account warnings)
    // ==========================================
    const val TEST_APP_ID = "ca-app-pub-3940256099942544~3347511713"
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/9214589741" // Adaptive Banner
    const val TEST_STANDARD_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111" // Standard Banner
    const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
    const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"
    const val TEST_REWARDED_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/5354046379"
    const val TEST_APP_OPEN_AD_UNIT_ID = "ca-app-pub-3940256099942544/9257395921"

    // Active App ID
    val ADMOB_APP_ID: String get() = if (USE_TEST_ADS) TEST_APP_ID else PROD_APP_ID

    // Active Banner Ads (Adaptive Banners across all screens)
    val BANNER_AD_UNIT_ID: String get() = if (USE_TEST_ADS) TEST_BANNER_AD_UNIT_ID else PROD_BANNER_AD_UNIT_ID
    val BANNER_HOME_AD_UNIT_ID: String get() = BANNER_AD_UNIT_ID
    val BANNER_SPEEDOMETER_AD_UNIT_ID: String get() = BANNER_AD_UNIT_ID
    val BANNER_COMPASS_AD_UNIT_ID: String get() = BANNER_AD_UNIT_ID
    val BANNER_SHARE_LOC_AD_UNIT_ID: String get() = BANNER_AD_UNIT_ID
    val BANNER_PIN_DETAILS_AD_UNIT_ID: String get() = BANNER_AD_UNIT_ID
    val BANNER_SAVED_PLACES_AD_UNIT_ID: String get() = BANNER_AD_UNIT_ID
    val BANNER_TRIP_HISTORY_AD_UNIT_ID: String get() = BANNER_AD_UNIT_ID

    // Active Native / Inline Feed Card Ads (using Banner unit in Card frame)
    val NATIVE_HOME_FEED_AD_UNIT_ID: String get() = if (USE_TEST_ADS) TEST_STANDARD_BANNER_AD_UNIT_ID else PROD_BANNER_AD_UNIT_ID
    val NATIVE_NEARBY_PLACES_AD_UNIT_ID: String get() = NATIVE_HOME_FEED_AD_UNIT_ID
    val NATIVE_COUNTRY_DETAILS_AD_UNIT_ID: String get() = NATIVE_HOME_FEED_AD_UNIT_ID
    val NATIVE_COUNTRY_ATLAS_AD_UNIT_ID: String get() = NATIVE_HOME_FEED_AD_UNIT_ID

    // Active Full-Screen Interstitial Ads
    val INTERSTITIAL_AD_UNIT_ID: String get() = if (USE_TEST_ADS) TEST_INTERSTITIAL_AD_UNIT_ID else PROD_INTERSTITIAL_AD_UNIT_ID
    val INTERSTITIAL_FEATURE_NAV_AD_UNIT_ID: String get() = INTERSTITIAL_AD_UNIT_ID
    val INTERSTITIAL_PIN_SAVED_AD_UNIT_ID: String get() = INTERSTITIAL_AD_UNIT_ID

    // Active Rewarded Video Ads
    val REWARDED_AD_UNIT_ID: String get() = if (USE_TEST_ADS) TEST_REWARDED_AD_UNIT_ID else PROD_REWARDED_AD_UNIT_ID
    val REWARDED_INTERSTITIAL_AD_UNIT_ID: String get() = if (USE_TEST_ADS) TEST_REWARDED_INTERSTITIAL_AD_UNIT_ID else PROD_REWARDED_INTERSTITIAL_AD_UNIT_ID

    // Active App Open / Launch Ad
    val APP_OPEN_AD_UNIT_ID: String get() = if (USE_TEST_ADS) TEST_APP_OPEN_AD_UNIT_ID else PROD_INTERSTITIAL_AD_UNIT_ID

    // Frequency Capping Settings (Google Policy Safe)
    const val INTERSTITIAL_CLICK_INTERVAL: Int = 2 // Show every 2nd feature navigation
    const val INTERSTITIAL_MIN_COOLDOWN_MS: Long = 15_000L // 15s cooldown
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
 * HIGH-PERFORMANCE BANNER AD LIFECYCLE & CACHING MANAGER
 * ============================================================================
 * Solves the navigation reload problem completely:
 * 1. Caches AdView instances per placement so navigating between pages or pressing
 *    Back DOES NOT reload, destroy, or flicker the banner.
 * 2. Preloads the primary banners on app start so the first view is instant.
 * 3. Safely detaches and reattaches views across Composables without memory leaks.
 * 4. Bridges Activity lifecycle onPause, onResume, and onDestroy to AdMob SDK.
 */
object BannerAdManager {
    private val cachedAdViews = mutableMapOf<String, com.google.android.gms.ads.AdView>()
    private val adLoadStatus = mutableStateMapOf<String, Boolean>()
    private val isCurrentlyLoading = mutableSetOf<String>()

    fun isAdLoaded(placement: String): Boolean {
        return adLoadStatus[placement] == true
    }

    private fun findActivity(context: Context): Activity? {
        var ctx = context
        while (ctx is android.content.ContextWrapper) {
            if (ctx is Activity) return ctx
            ctx = ctx.baseContext
        }
        return null
    }

    fun getOrCreateBanner(
        context: Context,
        placement: String,
        adUnitId: String,
        isStandardBanner: Boolean = false
    ): com.google.android.gms.ads.AdView {
        val existing = cachedAdViews[placement]
        if (existing != null) {
            (existing.parent as? android.view.ViewGroup)?.removeView(existing)
            if (adLoadStatus[placement] != true && !isCurrentlyLoading.contains(placement)) {
                loadAdInternal(placement, existing)
            }
            return existing
        }

        val targetContext = findActivity(context) ?: context
        val adView = com.google.android.gms.ads.AdView(targetContext)
        val adSize = if (isStandardBanner) {
            com.google.android.gms.ads.AdSize.BANNER
        } else {
            val displayMetrics = targetContext.resources.displayMetrics
            val adWidth = (displayMetrics.widthPixels / displayMetrics.density).toInt()
            try {
                com.google.android.gms.ads.AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(targetContext, adWidth)
            } catch (_: Throwable) {
                com.google.android.gms.ads.AdSize.BANNER
            }
        }
        adView.setAdSize(adSize)
        adView.adUnitId = adUnitId

        adView.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                adLoadStatus[placement] = true
                isCurrentlyLoading.remove(placement)
                android.util.Log.d("EarthScopeAds", "Banner [$placement] loaded successfully")
            }

            override fun onAdFailedToLoad(error: com.google.android.gms.ads.LoadAdError) {
                adLoadStatus[placement] = false
                isCurrentlyLoading.remove(placement)
                android.util.Log.d("EarthScopeAds", "Banner [$placement] error [${error.code}]: ${error.message}")
                // Retry silently after 15 seconds if internet reconnects
                adView.postDelayed({
                    if (adLoadStatus[placement] != true && !isCurrentlyLoading.contains(placement)) {
                        if (isNetworkAvailable(targetContext)) {
                            loadAdInternal(placement, adView)
                        }
                    }
                }, 15_000L)
            }
        }

        cachedAdViews[placement] = adView
        loadAdInternal(placement, adView)
        return adView
    }

    fun isNetworkAvailable(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? android.net.ConnectivityManager
            val network = cm?.activeNetwork ?: return false
            val caps = cm.getNetworkCapabilities(network) ?: return false
            caps.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (_: Throwable) {
            true
        }
    }

    private fun loadAdInternal(placement: String, adView: com.google.android.gms.ads.AdView) {
        if (adLoadStatus[placement] == true || isCurrentlyLoading.contains(placement)) return
        isCurrentlyLoading.add(placement)
        try {
            val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
            adView.loadAd(adRequest)
        } catch (_: Throwable) {
            isCurrentlyLoading.remove(placement)
            adLoadStatus[placement] = false
        }
    }

    fun preloadInitialBanners(context: Context) {
        try {
            getOrCreateBanner(context, "Home_Bottom_Banner", AdMobConfig.BANNER_HOME_AD_UNIT_ID, false)
            getOrCreateBanner(context, "Globe_World_Banner", AdMobConfig.BANNER_HOME_AD_UNIT_ID, false)
            getOrCreateBanner(context, "Speedometer_Bottom_Banner", AdMobConfig.BANNER_SPEEDOMETER_AD_UNIT_ID, false)
            getOrCreateBanner(context, "Compass_Bottom_Banner", AdMobConfig.BANNER_COMPASS_AD_UNIT_ID, false)
            getOrCreateBanner(context, "Map_Page_Bottom_Banner", AdMobConfig.BANNER_HOME_AD_UNIT_ID, false)
            getOrCreateBanner(context, "Share_Loc_Bottom_Banner", AdMobConfig.BANNER_SHARE_LOC_AD_UNIT_ID, false)
            getOrCreateBanner(context, "Saved_Places_Bottom_Banner", AdMobConfig.BANNER_SAVED_PLACES_AD_UNIT_ID, false)
            getOrCreateBanner(context, "Trip_History_Bottom_Banner", AdMobConfig.BANNER_TRIP_HISTORY_AD_UNIT_ID, false)
            getOrCreateBanner(context, "Nearby_Places_Bottom_Banner", AdMobConfig.BANNER_HOME_AD_UNIT_ID, false)
        } catch (_: Throwable) {}
    }

    fun pauseAll() {
        cachedAdViews.values.forEach {
            try { it.pause() } catch (_: Throwable) {}
        }
    }

    fun resumeAll() {
        cachedAdViews.values.forEach {
            try { it.resume() } catch (_: Throwable) {}
        }
    }

    fun destroyAll() {
        cachedAdViews.values.forEach {
            try {
                (it.parent as? android.view.ViewGroup)?.removeView(it)
                it.destroy()
            } catch (_: Throwable) {}
        }
        cachedAdViews.clear()
        adLoadStatus.clear()
        isCurrentlyLoading.clear()
    }
}

/**
 * ============================================================================
 * 1. ADAPTIVE BANNER AD COMPONENT (Cached & Instant Zero-Reload Navigation)
 * ============================================================================
 */
@Composable
fun AdMobAdaptiveBanner(
    placementName: String,
    adUnitId: String = AdMobConfig.BANNER_HOME_AD_UNIT_ID,
    isLightTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isLoaded = BannerAdManager.isAdLoaded(placementName)

    // Trigger preload in background if not already active
    LaunchedEffect(placementName) {
        BannerAdManager.getOrCreateBanner(
            context = context,
            placement = placementName,
            adUnitId = adUnitId,
            isStandardBanner = false
        )
    }

    // When offline, without internet, or while loading: occupy ZERO space!
    // Never show an empty bar, border, or "Loading..." box!
    if (!isLoaded) {
        return
    }

    val bannerBg = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF091222)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF1E293B)

    Surface(
        color = bannerBg,
        border = BorderStroke(0.8.dp, borderColor),
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .testTag("admob_banner_$placementName")
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.ui.viewinterop.AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                factory = { ctx ->
                    BannerAdManager.getOrCreateBanner(
                        context = ctx,
                        placement = placementName,
                        adUnitId = adUnitId,
                        isStandardBanner = false
                    )
                },
                update = { adView ->
                    adView.visibility = android.view.View.VISIBLE
                },
                onRelease = { adView ->
                    // Detach from current container so it can be re-attached instantly when returning
                    (adView.parent as? android.view.ViewGroup)?.removeView(adView)
                }
            )
        }
    }
}

/**
 * ============================================================================
 * 2. INLINE CARD AD COMPONENT (Renders Cached AdMob Banner inside Material 3 Card)
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
    val isLoaded = BannerAdManager.isAdLoaded(placementName)

    if (!isLoaded) {
        val context = LocalContext.current
        LaunchedEffect(placementName) {
            BannerAdManager.getOrCreateBanner(
                context = context,
                placement = placementName,
                adUnitId = adUnitId,
                isStandardBanner = true
            )
        }
        return
    }

    Surface(
        color = cardBg,
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.2.dp, borderColor),
        shadowElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .testTag("admob_native_$placementName")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = if (isLoaded) 8.dp else 0.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.ui.viewinterop.AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = { ctx ->
                    BannerAdManager.getOrCreateBanner(
                        context = ctx,
                        placement = placementName,
                        adUnitId = adUnitId,
                        isStandardBanner = true
                    )
                },
                update = { adView ->
                    adView.visibility = android.view.View.VISIBLE
                },
                onRelease = { adView ->
                    (adView.parent as? android.view.ViewGroup)?.removeView(adView)
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
    if (activity == null) {
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
 * 3. CENTRALIZED PROFESSIONAL ADMOB MANAGER (Background Preloading & Zero-Delay Navigation)
 * ============================================================================
 */
object AdMobManager {
    var isInitialized: Boolean = false
        private set

    private var cachedInterstitial: com.google.android.gms.ads.interstitial.InterstitialAd? = null
    private var isInterstitialLoading: Boolean = false
    private var lastInterstitialShownTime: Long = 0L

    fun initialize(context: Context) {
        if (isInitialized) return
        isInitialized = true
        AdMobSdkState.markInitialized()
        preloadInterstitial(context.applicationContext)
    }

    fun preloadInterstitial(context: Context) {
        if (cachedInterstitial != null || isInterstitialLoading) return
        val appContext = context.applicationContext
        isInterstitialLoading = true

        val adRequest = com.google.android.gms.ads.AdRequest.Builder().build()
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
            appContext,
            AdMobConfig.INTERSTITIAL_FEATURE_NAV_AD_UNIT_ID,
            adRequest,
            object : com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: com.google.android.gms.ads.interstitial.InterstitialAd) {
                    cachedInterstitial = interstitialAd
                    isInterstitialLoading = false
                }

                override fun onAdFailedToLoad(loadAdError: com.google.android.gms.ads.LoadAdError) {
                    cachedInterstitial = null
                    isInterstitialLoading = false
                }
            }
        )
    }

    fun isInterstitialReady(): Boolean = cachedInterstitial != null

    /**
     * Shows preloaded Interstitial Ad if ready.
     * NEVER blocks the user with a loading modal!
     * If ready, displays the ad immediately.
     * If not ready or cooldown active, navigates IMMEDIATELY without delay.
     */
    fun showInterstitialIfAvailable(
        activity: Activity?,
        minIntervalMs: Long = 15_000L,
        onFinished: () -> Unit
    ) {
        val now = System.currentTimeMillis()
        val ad = cachedInterstitial

        if (activity == null || ad == null || (now - lastInterstitialShownTime < minIntervalMs)) {
            // Not ready or interval too short: navigate immediately with zero waiting!
            onFinished()
            if (activity != null && cachedInterstitial == null) {
                preloadInterstitial(activity.applicationContext)
            }
            return
        }

        ad.fullScreenContentCallback = object : com.google.android.gms.ads.FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                cachedInterstitial = null
                lastInterstitialShownTime = System.currentTimeMillis()
                preloadInterstitial(activity.applicationContext)
                onFinished()
            }

            override fun onAdFailedToShowFullScreenContent(adError: com.google.android.gms.ads.AdError) {
                cachedInterstitial = null
                preloadInterstitial(activity.applicationContext)
                onFinished()
            }
        }
        cachedInterstitial = null
        ad.show(activity)
    }
}

/**
 * Backward-compatible helper composable that never blocks navigation
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

    LaunchedEffect(Unit) {
        AdMobManager.showInterstitialIfAvailable(activity, minIntervalMs = 15_000L) {
            onAdClosed()
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
