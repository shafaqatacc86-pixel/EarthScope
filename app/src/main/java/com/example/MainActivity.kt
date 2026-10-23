package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.EarthScopeAppHost
import com.example.ui.GlobeViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val globeViewModel: GlobeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                val processName = android.app.Application.getProcessName()
                if (packageName != processName) {
                    android.webkit.WebView.setDataDirectorySuffix(processName)
                }
            }
        } catch (_: Throwable) {}
        try {
            com.google.android.gms.maps.MapsInitializer.initialize(
                applicationContext,
                com.google.android.gms.maps.MapsInitializer.Renderer.LATEST,
                null
            )
        } catch (_: Throwable) {}
        try {
            val reqConfig = com.google.android.gms.ads.RequestConfiguration.Builder()
                .setTestDeviceIds(listOf(com.google.android.gms.ads.AdRequest.DEVICE_ID_EMULATOR))
                .build()
            com.google.android.gms.ads.MobileAds.setRequestConfiguration(reqConfig)
        } catch (_: Throwable) {}
        try {
            com.google.android.gms.ads.MobileAds.initialize(this) {
                com.example.ui.AdMobManager.initialize(applicationContext)
                com.example.ui.BannerAdManager.preloadInitialBanners(this)
            }
        } catch (_: Throwable) {
            com.example.ui.AdMobManager.initialize(applicationContext)
            com.example.ui.BannerAdManager.preloadInitialBanners(this)
        }
        setContent {
            val uiState by globeViewModel.uiState.collectAsStateWithLifecycle()
            MyApplicationTheme(themeMode = uiState.themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    EarthScopeAppHost(viewModel = globeViewModel)
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        com.example.ui.BannerAdManager.pauseAll()
    }

    override fun onResume() {
        super.onResume()
        com.example.ui.BannerAdManager.resumeAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        com.example.ui.BannerAdManager.destroyAll()
    }
}
