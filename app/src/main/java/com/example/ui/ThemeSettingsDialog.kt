package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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

private enum class SettingsSubPage {
    MAIN,
    PRIVACY_POLICY,
    RATE_US,
    ABOUT_US
}

@Composable
fun ThemeSettingsDialog(
    currentThemeMode: AppThemeMode,
    isLightTheme: Boolean,
    onSelectTheme: (AppThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var currentSubPage by remember { mutableStateOf(SettingsSubPage.MAIN) }

    val pageBg = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF0B1325)
    val cardBg = if (isLightTheme) Color.White else Color(0xFF142038)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF263955)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF475569) else Color(0xFF94A3B8)

    // Full-screen Settings Overlay rendered directly inside the Activity's safe area
    // (Never creates a separate Window that ignores Android's 3-button bottom navigation bar!)
    BackHandler {
        if (currentSubPage != SettingsSubPage.MAIN) {
            currentSubPage = SettingsSubPage.MAIN
        } else {
            onDismiss()
        }
    }

    Surface(
        color = pageBg,
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = true, onClick = {})
            .testTag("theme_settings_dialog")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top App Bar (Single clean Back button on the left; no duplicate X or bottom back buttons)
            Surface(
                color = cardBg,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                if (currentSubPage != SettingsSubPage.MAIN) {
                                    currentSubPage = SettingsSubPage.MAIN
                                } else {
                                    onDismiss()
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color(0xFF0284C7).copy(alpha = 0.14f), CircleShape)
                                .testTag("settings_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF0284C7)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = when (currentSubPage) {
                                    SettingsSubPage.MAIN -> "Settings"
                                    SettingsSubPage.PRIVACY_POLICY -> "Privacy Policy"
                                    SettingsSubPage.RATE_US -> "Rate Earth Scope"
                                    SettingsSubPage.ABOUT_US -> "About Us"
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = when (currentSubPage) {
                                    SettingsSubPage.MAIN -> "Earth Scope • com.shafaqat.earthscope"
                                    SettingsSubPage.PRIVACY_POLICY -> "Data protection & permissions"
                                    SettingsSubPage.RATE_US -> "Share your experience with us"
                                    SettingsSubPage.ABOUT_US -> "App info, developer & features"
                                },
                                fontSize = 12.sp,
                                color = secondaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Responsive Scrollable Content Container
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                when (currentSubPage) {
                    SettingsSubPage.MAIN -> {
                        SettingsMainContent(
                            currentThemeMode = currentThemeMode,
                            isLightTheme = isLightTheme,
                            cardBg = cardBg,
                            borderColor = borderColor,
                            primaryText = primaryText,
                            secondaryText = secondaryText,
                            onSelectTheme = onSelectTheme,
                            onOpenPrivacyPolicy = { currentSubPage = SettingsSubPage.PRIVACY_POLICY },
                            onOpenRateUs = { currentSubPage = SettingsSubPage.RATE_US },
                            onShareApp = { shareEarthScopeApp(context) },
                            onOpenAboutUs = { currentSubPage = SettingsSubPage.ABOUT_US }
                        )
                    }
                    SettingsSubPage.PRIVACY_POLICY -> {
                        PrivacyPolicySubPage(
                            cardBg = cardBg,
                            borderColor = borderColor,
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )
                    }
                    SettingsSubPage.RATE_US -> {
                        RateUsSubPage(
                            context = context,
                            isLightTheme = isLightTheme,
                            cardBg = cardBg,
                            borderColor = borderColor,
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )
                    }
                    SettingsSubPage.ABOUT_US -> {
                        AboutUsSubPage(
                            isLightTheme = isLightTheme,
                            cardBg = cardBg,
                            borderColor = borderColor,
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )
                    }
                }
            }
        }
    }
}

/**
 * First-Launch Privacy Policy & Terms and Conditions Consent Dialog
 * Rendered inside the Activity's safe area (respecting systemBarsPadding) so it NEVER
 * collides with Android's 3-button bottom navigation bar or top status bar.
 */
@Composable
fun FirstLaunchPrivacyTermsDialog(
    isLightTheme: Boolean,
    onAcceptAndContinue: () -> Unit
) {
    val cardBg = if (isLightTheme) Color.White else Color(0xFF0F1A2E)
    val innerBg = if (isLightTheme) Color(0xFFF8FAFC) else Color(0xFF16243D)
    val borderColor = if (isLightTheme) Color(0xFFE2E8F0) else Color(0xFF24385A)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF475569) else Color(0xFF94A3B8)

    // Block back press until user accepts Privacy Policy & Terms
    BackHandler(enabled = true) { }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.68f))
            .clickable(enabled = true, onClick = {})
            .windowInsetsPadding(WindowInsets.statusBars)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = cardBg,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.5.dp, Color(0xFF0284C7).copy(alpha = 0.55f)),
            shadowElevation = 18.dp,
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth()
                .fillMaxSize(0.88f)
                .testTag("first_launch_privacy_terms_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                // Fixed Top Header with Logo + Title
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EarthScopeTransparentLogo(modifier = Modifier.size(44.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Privacy Policy & Terms",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = primaryText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Welcome to Earth Scope • Please Review",
                            fontSize = 11.5.sp,
                            color = Color(0xFF0284C7),
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Responsive Scrollable Middle Card: ALL 5 Sections Shown Completely (Nothing Hidden!)
                Surface(
                    color = innerBg,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Earth Scope – 3D World Globe, Maps & Navigation Suite\nDeveloper: Shafaqat Ali • Package: com.shafaqat.earthscope\nEffective Date: October 11, 2026 • Email: apps.shafaqat@gmail.com",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF0284C7),
                            lineHeight = 16.sp
                        )

                        PolicySection(
                            title = "PRIVACY SUMMARY",
                            body = "Earth Scope respects your privacy. Your saved map pins, attached photos, speedometer trip records, and other app-created content are stored locally on your device. We do not sell, rent, or trade your personal data to third parties.\nEarth Scope uses third-party services, including Google Maps and Google AdMob, to provide mapping features and advertisements. These services may process certain information as described in this policy.",
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )

                        PolicySection(
                            title = "1. INTRODUCTION",
                            body = "Welcome to Earth Scope (\"we,\" \"our,\" or \"us\"). This Privacy Policy explains how the Earth Scope mobile application (com.shafaqat.earthscope) accesses, uses, stores, and protects information when you use its interactive 3D World Globe, Google Maps with custom location pins, GPS Speedometer, Digital Compass, Altitude and Coordinates HUD, Nearby Places Finder, Distance Calculator, and World Country Atlas.",
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )

                        PolicySection(
                            title = "2. PERMISSIONS AND DATA WE ACCESS",
                            body = "• 2.1 Location Permission (ACCESS_FINE_LOCATION & ACCESS_COARSE_LOCATION): Used while the app is in the foreground to display your GPS location on the globe and map, calculate real-time speed, display altitude/coordinates, find nearby places, and save/share your location pin. Your GPS coordinates are processed on your device and are not uploaded to a developer tracking server.\n• 2.2 Photos & Media Selection: Uses Android's system Photo Picker to optionally attach up to 3 photos to a saved pin without requiring broad gallery access. Selected photos stay in private local storage.\n• 2.3 Device Compass & Motion Sensors: Reads rotation vector, accelerometer, and magnetometer sensors temporarily in memory for globe and compass orientation.\n• 2.4 Internet & Vibration (INTERNET & VIBRATE): Used to load interactive map tiles, geocoding, Google AdMob advertisements, and optional speedometer vibration alerts.",
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )

                        PolicySection(
                            title = "3. LOCAL DATA STORAGE & USER CONTROL",
                            body = "Saved location pins, custom markers, categories (Home, Office, Shop, Property, Travel), notes, attached photos, recorded speedometer trips, country history, and theme preferences are stored locally via Android Room (SQLite) Database and SharedPreferences. You can edit or delete saved pins, photos, and trips anytime inside the app, or clear app storage / uninstall the app.",
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )

                        PolicySection(
                            title = "4. THIRD-PARTY SERVICES & ADVERTISEMENTS",
                            body = "• 4.1 Google Maps & Google Play Services: Used for interactive maps and location features (https://policies.google.com/privacy).\n• 4.2 Google AdMob: Displays adaptive banner, native, and interstitial ads. Google AdMob and its partners may process Advertising ID, device diagnostics, IP address, and ad interaction signals to serve personalized or non-personalized ads.",
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )

                        PolicySection(
                            title = "5–8. DATA SHARING, SECURITY, CHILDREN & RETENTION",
                            body = "• Data Sharing: The developer does not sell, rent, or trade your personal data.\n• Data Security: Local data is protected in Android private storage and third-party network calls use HTTPS/TLS encryption.\n• Children's Privacy: Earth Scope does not knowingly collect personal information from children under 13.\n• Retention & Deletion: Local data remains on your device until you delete it, clear app storage, or uninstall the app.",
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )

                        HorizontalDivider(color = borderColor, modifier = Modifier.padding(vertical = 2.dp))

                        PolicySection(
                            title = "9–11. TERMS OF USE & CONTACT US",
                            body = "• Road & Driving Safety: Always pay attention to the road and traffic laws. Never operate complex map controls, type notes, or select photos while driving.\n• Accuracy & No Critical Navigation Guarantee: GPS speed, altitude, compass heading, and calculated flight distances are informational estimates only and must not be used for aviation, marine, or safety-critical navigation.\n• Contact Us: Developer: Shafaqat Ali • Email: apps.shafaqat@gmail.com\n© 2026 Earth Scope. Developed by Shafaqat Ali. All rights reserved.",
                            primaryText = primaryText,
                            secondaryText = secondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Pinned Bottom Agreement Text + Accept Button (Always visible & safely above bottom nav)
                Text(
                    text = "By tapping 'Accept & Continue', you agree to our Privacy Policy and Terms & Conditions.",
                    fontSize = 11.sp,
                    lineHeight = 15.sp,
                    color = secondaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onAcceptAndContinue,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22C55E),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("accept_privacy_terms_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Accept & Continue",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsMainContent(
    currentThemeMode: AppThemeMode,
    isLightTheme: Boolean,
    cardBg: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color,
    onSelectTheme: (AppThemeMode) -> Unit,
    onOpenPrivacyPolicy: () -> Unit,
    onOpenRateUs: () -> Unit,
    onShareApp: () -> Unit,
    onOpenAboutUs: () -> Unit
) {
    Column(
        modifier = Modifier
            .widthIn(max = 560.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // App Banner Header (Transparent-background Earth Scope Globe Logo)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EarthScopeTransparentLogo(modifier = Modifier.size(52.dp))
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Earth Scope",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Interactive 3D World Globe & Navigation",
                    fontSize = 12.sp,
                    color = secondaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Section 1: Theme (White / Dark / System)
        Text(
            text = "THEME & APPEARANCE",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0284C7),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        ThemeOptionRow(
            title = "System Default",
            subtitle = "Automatically match device white or dark theme",
            icon = Icons.Default.BrightnessAuto,
            iconTint = Color(0xFF0284C7),
            selected = currentThemeMode == AppThemeMode.SYSTEM,
            isLightTheme = isLightTheme,
            tag = "theme_option_system",
            onClick = { onSelectTheme(AppThemeMode.SYSTEM) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeOptionRow(
            title = "White Theme (Light Mode)",
            subtitle = "Bright daylight globe, white landmasses & clean light UI",
            icon = Icons.Default.LightMode,
            iconTint = Color(0xFFF59E0B),
            selected = currentThemeMode == AppThemeMode.LIGHT,
            isLightTheme = isLightTheme,
            tag = "theme_option_light",
            onClick = { onSelectTheme(AppThemeMode.LIGHT) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        ThemeOptionRow(
            title = "Dark Theme (Black Space)",
            subtitle = "Deep space cosmos, twinkling stars & neon globe UI",
            icon = Icons.Default.DarkMode,
            iconTint = Color(0xFF38BDF8),
            selected = currentThemeMode == AppThemeMode.DARK,
            isLightTheme = isLightTheme,
            tag = "theme_option_dark",
            onClick = { onSelectTheme(AppThemeMode.DARK) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Section 2: General & Support (Privacy Policy & Terms, Rate Us, Share, About Us)
        Text(
            text = "GENERAL & INFORMATION",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0284C7),
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        Surface(
            color = cardBg,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                SettingsMenuItemRow(
                    title = "Privacy Policy & Terms",
                    subtitle = "How Earth Scope handles GPS, photos, sensors, local DB & ads",
                    icon = Icons.Default.PrivacyTip,
                    iconTint = Color(0xFF16A34A),
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    tag = "settings_privacy_policy_item",
                    onClick = onOpenPrivacyPolicy
                )

                HorizontalDivider(color = borderColor, thickness = 0.8.dp)

                SettingsMenuItemRow(
                    title = "Rate Us",
                    subtitle = "Love Earth Scope? Give us a 5-star rating & feedback",
                    icon = Icons.Default.Star,
                    iconTint = Color(0xFFF59E0B),
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    tag = "settings_rate_us_item",
                    onClick = onOpenRateUs
                )

                HorizontalDivider(color = borderColor, thickness = 0.8.dp)

                SettingsMenuItemRow(
                    title = "Share",
                    subtitle = "Share Earth Scope with friends and family",
                    icon = Icons.Default.Share,
                    iconTint = Color(0xFF0284C7),
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    tag = "settings_share_item",
                    onClick = onShareApp
                )

                HorizontalDivider(color = borderColor, thickness = 0.8.dp)

                SettingsMenuItemRow(
                    title = "About Us",
                    subtitle = "Learn about Earth Scope, version info & developer",
                    icon = Icons.Default.Info,
                    iconTint = Color(0xFF8B5CF6),
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    tag = "settings_about_us_item",
                    onClick = onOpenAboutUs
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SettingsMenuItemRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    primaryText: Color,
    secondaryText: Color,
    tag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 13.dp)
            .testTag(tag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconTint.copy(alpha = 0.15f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = primaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = secondaryText,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = secondaryText,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun PrivacyPolicySubPage(
    cardBg: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color
) {
    Column(
        modifier = Modifier
            .widthIn(max = 560.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Surface(
            color = cardBg,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "PRIVACY POLICY & TERMS AND CONDITIONS",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Earth Scope – 3D World Globe, Maps & Navigation Suite",
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0284C7)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Application Name: Earth Scope\nPackage Name: com.shafaqat.earthscope\nDeveloper Name: Shafaqat Ali\nEffective Date: October 11, 2026\nContact Email: apps.shafaqat@gmail.com",
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    color = secondaryText
                )

                Spacer(modifier = Modifier.height(14.dp))

                PolicySection(
                    title = "PRIVACY SUMMARY",
                    body = "Earth Scope respects your privacy. Your saved map pins, attached photos, speedometer trip records, and other app-created content are stored locally on your device. We do not sell, rent, or trade your personal data to third parties.\n\nEarth Scope uses third-party services, including Google Maps and Google AdMob, to provide mapping features and advertisements. These services may process certain information as described in this policy.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "1. INTRODUCTION",
                    body = "Welcome to Earth Scope (\"we,\" \"our,\" or \"us\"). This Privacy Policy explains how the Earth Scope mobile application, package name com.shafaqat.earthscope, accesses, uses, stores, and protects information when you use its features.\n\nEarth Scope provides an interactive 3D World Globe, Google Maps with custom location pins, a GPS Speedometer, Digital Compass, Altitude and Coordinates HUD, Nearby Places Finder, Distance Calculator, and World Country Atlas.\n\nBy installing or using Earth Scope, you acknowledge this Privacy Policy and the Terms and Conditions described below.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "2. PERMISSIONS AND DATA WE ACCESS",
                    body = "Earth Scope requests the permissions and device access necessary to provide its core features.\n\n2.1 Location Permission (ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION)\nLocation access is used to:\n• Display your current GPS location on the globe and map.\n• Calculate real-time speed using GPS.\n• Display GPS altitude, latitude, longitude, and location accuracy.\n• Find nearby places based on your location.\n• Save or share your current location pin when you choose to do so.\nLocation access is intended for use while the app is in the foreground. Your GPS coordinates are processed on your device for the app's local features and are not uploaded to a tracking server operated by the developer. However, third-party mapping or location services may process information when you use their features.\n\n2.2 Photos and Media Selection\nEarth Scope uses Android's system Photo Picker to allow you to select images for your saved map pins:\n• You may optionally attach up to three photos to a location pin.\n• You select which images the app can access.\n• The app does not require broad access to your entire photo gallery for this feature.\n• Selected photos are stored in the app's private local storage on your device.\nYour photos are not uploaded to a server operated by the developer through this local pin-storage feature.\n\n2.3 Device Compass and Motion Sensors\nEarth Scope may access your device's rotation vector, accelerometer, and magnetometer sensors to support globe orientation and digital compass functionality. Sensor readings are processed temporarily in memory to calculate direction and orientation. The app does not intentionally store or transmit these readings.\n\n2.4 Internet and Network Access (INTERNET)\nInternet access is used to:\n• Load interactive map tiles and mapping services.\n• Retrieve readable location names through supported geocoding services.\n• Connect to third-party services required by certain app features.\n• Display advertisements through Google AdMob.\nThird-party providers may process technical information and requests according to their own privacy policies.\n\n2.5 Vibration (VIBRATE)\nVibration is used for optional haptic feedback and speed-limit alerts within the Speedometer feature.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "3. LOCAL DATA STORAGE AND USER CONTROL",
                    body = "Earth Scope stores user-created content locally on your device using Android Room (SQLite) Database and SharedPreferences, where applicable.\n\nLocally stored information may include:\n• Saved location pins and custom marker styles.\n• Pin categories, such as Home, Office, Shop, Property, and Travel.\n• Notes and photos attached to saved pins.\n• Saved places and location details.\n• Recorded speedometer trips and trip statistics.\n• Searched country history.\n• Theme preferences, including Light, Dark, and System Default.\n\nYour Control Over Stored Data:\n• Edit or delete individual saved pins and related information.\n• Remove saved photos and trip records where supported.\n• Clear the app's storage through Android Settings.\n• Uninstall the app to remove its locally stored application data.\nUninstalling the app or clearing its storage generally removes locally stored records. Data separately retained by third-party services, if any, is governed by those providers' policies.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "4. THIRD-PARTY SERVICES AND ADVERTISEMENTS",
                    body = "Earth Scope uses third-party services to provide mapping functionality and support the app through advertising.\n\n4.1 Google Maps and Google Play Services\nEarth Scope may use Google Maps SDK, Google Play Services, and related Google services to provide interactive maps and location-related features. When you use these services, Google may process technical information, location-related requests, device information, or other data necessary to operate the service.\nGoogle Privacy Policy: https://policies.google.com/privacy\n\n4.2 Google AdMob\nEarth Scope displays advertisements through Google AdMob (adaptive banner ads, native ad cards, and interstitial advertisements). Depending on the advertising configuration, your device, your settings, and applicable consent requirements, Google AdMob and its partners may collect or process:\n• Advertising ID or other advertising identifiers, where available and permitted.\n• Device information and technical diagnostics.\n• IP address and related network information.\n• Advertising impressions, interactions, and usage signals.\n• Information used to serve personalized or non-personalized advertisements.\nThe developer does not directly control all data collection and processing performed by Google AdMob or its advertising partners.\nGoogle Privacy Policy: https://policies.google.com/privacy\nYou can review advertising privacy options and available controls in your Android device settings.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "5. DATA SHARING AND DISCLOSURE",
                    body = "The developer does not sell, rent, or trade your personal data.\nEarth Scope's saved pins, notes, photos, and trip records are designed to remain in local app storage rather than being uploaded to a developer-operated server.\nHowever, certain information may be processed by third-party services when you use online maps, geocoding, advertising, or other network-dependent features. Information may also be disclosed if required by applicable law or a valid legal process.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "6. DATA SECURITY",
                    body = "Earth Scope is designed to protect locally stored information through Android's private application storage. The app uses local database storage for user-created content. Network communications by third-party services may use HTTPS/TLS encryption, depending on the service and connection.\nAlthough reasonable measures are used to protect information, no storage method or internet transmission can be guaranteed to be completely secure. Users should keep their devices protected and use the device's available security features.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "7. CHILDREN'S PRIVACY",
                    body = "Earth Scope is intended as a general-audience geographic, educational, and navigation utility. The developer does not knowingly collect personal information directly from children under the age of 13 through a developer-operated server.\nHowever, third-party services integrated into the app, including advertising and mapping providers, may process information under their own policies and applicable requirements.\nIf you believe a child has provided personal information directly to the developer, please contact apps.shafaqat@gmail.com so we can review the matter and take appropriate action.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "8. DATA RETENTION AND DELETION",
                    body = "Locally stored data remains on your device until you delete it, clear the app's storage, or uninstall the application, subject to Android's storage behavior and any device backups.\nYou can delete saved information using the app's available controls. Clearing app storage or uninstalling the app may permanently remove locally stored pins, photos, preferences, and trip records.\nFor information held by third-party services, please consult the relevant provider's privacy policy and available data-management options.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "9. TERMS AND CONDITIONS OF USE",
                    body = "By using Earth Scope, you agree to use the application responsibly and understand its limitations.\n\n9.1 Road and Driving Safety\nAlways pay attention to the road and follow applicable traffic laws while driving, cycling, or walking:\n• Do not operate complex map controls while driving.\n• Do not type notes, select photos, or interact with the app while operating a moving vehicle.\n• Set your destination and preferences before beginning your journey.\n• Follow official road signs, traffic regulations, and emergency instructions.\nEarth Scope is a supplementary utility and must not distract you from safe travel.\n\n9.2 Accuracy of Information\nGPS speed, altitude, compass heading, coordinates, and calculated geographic distances may vary depending on your device hardware, sensor calibration, GPS signal quality, environmental conditions, map data, and calculation methods. Flight distances and estimated flight times are informational estimates and do not represent verified airline routes, actual flight schedules, or guaranteed travel times. Country information, nearby places, map data, and other geographic details may occasionally be incomplete, outdated, or inaccurate.\n\n9.3 No Critical Navigation Guarantee\nEarth Scope is intended for general reference, education, and everyday convenience. Do not rely on the app as the sole source of information for aviation, marine navigation, emergency response, hazardous environments, or other safety-critical activities.\n\n9.4 Availability and Changes to Features\nThe developer may update, modify, suspend, or discontinue app features to improve performance, address technical issues, or comply with applicable requirements. The developer does not guarantee uninterrupted availability of all features or external services.\n\n9.5 Limitation of Liability\nTo the extent permitted by applicable law, the developer is not responsible for losses or damages arising from inaccurate GPS readings, compass errors, estimated distances, outdated third-party map data, service interruptions, or reliance on the app for safety-critical decisions. Nothing in these Terms excludes or limits liability where such exclusion or limitation is prohibited by applicable law.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "10. CHANGES TO THIS PRIVACY POLICY",
                    body = "We may update this Privacy Policy and Terms and Conditions to reflect changes in app functionality, third-party services, or applicable legal requirements. Updates will be published on the page where this policy is hosted, with a revised effective date where appropriate, and may also be made available through the app.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )

                PolicySection(
                    title = "11. CONTACT US & COPYRIGHT NOTICE",
                    body = "If you have any questions, suggestions, privacy concerns, or requests related to Earth Scope, please contact the developer:\n\nDeveloper Name: Shafaqat Ali\nApplication Name: Earth Scope\nPackage Name: com.shafaqat.earthscope\nEmail: apps.shafaqat@gmail.com\n\n© 2026 Earth Scope. Developed by Shafaqat Ali. All rights reserved.",
                    primaryText = primaryText,
                    secondaryText = secondaryText
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PolicySection(
    title: String,
    body: String,
    primaryText: Color,
    secondaryText: Color
) {
    Column(modifier = Modifier.padding(vertical = 7.dp)) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = primaryText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = body,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            color = secondaryText
        )
    }
}

@Composable
private fun RateUsSubPage(
    context: Context,
    isLightTheme: Boolean,
    cardBg: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color
) {
    var selectedStars by remember { mutableIntStateOf(5) }
    var feedbackText by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .widthIn(max = 560.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = cardBg,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                EarthScopeTransparentLogo(modifier = Modifier.size(60.dp))

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Enjoying Earth Scope?",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryText,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tap a star to rate your experience with our 3D Globe, Flight Directions, and Live Compass.",
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    color = secondaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 5-Star Interactive Selector (Responsive spacing)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (star in 1..5) {
                        IconButton(
                            onClick = { selectedStars = star },
                            modifier = Modifier
                                .size(46.dp)
                                .testTag("rate_star_$star")
                        ) {
                            Icon(
                                imageVector = if (star <= selectedStars) Icons.Default.Star else Icons.Outlined.StarOutline,
                                contentDescription = "$star Stars",
                                tint = if (star <= selectedStars) Color(0xFFF59E0B) else secondaryText,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                    }
                }

                // Clean centered status pill without duplicate stars or orphan text wrapping
                Surface(
                    color = Color(0xFFF59E0B).copy(alpha = 0.14f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = when (selectedStars) {
                            5 -> "Excellent! We're glad you love it!"
                            4 -> "Great! Thank you for your support!"
                            3 -> "Good! Tell us how we can improve."
                            2 -> "Fair — let us know what we can fix."
                            else -> "Please share your feedback below."
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isLightTheme) Color(0xFFD97706) else Color(0xFFFBBF24),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = feedbackText,
                    onValueChange = { feedbackText = it },
                    placeholder = {
                        Text(
                            text = "Optional feedback or feature request...",
                            fontSize = 13.sp,
                            color = secondaryText
                        )
                    },
                    minLines = 2,
                    maxLines = 4,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = primaryText,
                        unfocusedTextColor = primaryText,
                        focusedBorderColor = Color(0xFF0284C7),
                        unfocusedBorderColor = borderColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("rate_feedback_input")
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        submitted = true
                        Toast.makeText(
                            context,
                            "Thank you for rating Earth Scope ($selectedStars/5 stars)!",
                            Toast.LENGTH_SHORT
                        ).show()
                        openPlayStoreListing(context)
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22C55E),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_rating_button")
                ) {
                    Text(
                        text = if (submitted) "Thank You! Open Play Store" else "Submit Rating & Rate on Store",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AboutUsSubPage(
    isLightTheme: Boolean,
    cardBg: Color,
    borderColor: Color,
    primaryText: Color,
    secondaryText: Color
) {
    Column(
        modifier = Modifier
            .widthIn(max = 560.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = cardBg,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Pure Transparent-Background Earth Scope Logo
                EarthScopeTransparentLogo(modifier = Modifier.size(68.dp))

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Earth Scope",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = primaryText,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Version 1.1 • com.shafaqat.earthscope",
                    fontSize = 12.sp,
                    color = Color(0xFF0284C7),
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Earth Scope is an interactive 3D world globe and geographic navigation app built for explorers, students, and travelers. Experience smooth Google Maps-inspired 3D momentum rotation, exact WGS-84 capital-to-capital air distance calculations, animated flight routes, and real-time hardware compass orientation.",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = secondaryText,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = borderColor)

                Spacer(modifier = Modifier.height(12.dp))

                // Responsive Info Cards Grid (Never collides or misaligns on any screen width)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AboutInfoBox(
                        label = "Application Name",
                        value = "Earth Scope",
                        isLightTheme = isLightTheme,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                    AboutInfoBox(
                        label = "Developer",
                        value = "Shafaqat Ali",
                        isLightTheme = isLightTheme,
                        primaryText = primaryText,
                        secondaryText = secondaryText,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                AboutInfoBox(
                    label = "Package ID",
                    value = "com.shafaqat.earthscope",
                    isLightTheme = isLightTheme,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                AboutInfoBox(
                    label = "Contact Email",
                    value = "apps.shafaqat@gmail.com",
                    isLightTheme = isLightTheme,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                AboutInfoBox(
                    label = "Geodesic Distance Engine",
                    value = "WGS-84 Vincenty Ellipsoid (Exact Capital-to-Capital KM)",
                    isLightTheme = isLightTheme,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                AboutInfoBox(
                    label = "Compass & Orientation Sensors",
                    value = "3D Rotation Vector & Magnetometer (Live N/S/E/W)",
                    isLightTheme = isLightTheme,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                AboutInfoBox(
                    label = "Themes Supported",
                    value = "White Theme, Dark Space & System Default",
                    isLightTheme = isLightTheme,
                    primaryText = primaryText,
                    secondaryText = secondaryText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AboutInfoBox(
    label: String,
    value: String,
    isLightTheme: Boolean,
    primaryText: Color,
    secondaryText: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isLightTheme) Color(0xFFF1F5F9) else Color(0xFF1E293B),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                color = secondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = primaryText
            )
        }
    }
}

/**
 * Pure transparent-background Earth Scope vector logo (no background box behind the globe).
 */
@Composable
fun EarthScopeTransparentLogo(modifier: Modifier = Modifier) {
    androidx.compose.foundation.Image(
        painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.earth_scope_icon_1791783006650),
        contentDescription = "Earth Scope Logo",
        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
        modifier = modifier.clip(RoundedCornerShape(12.dp))
    )
}

private fun shareEarthScopeApp(context: Context) {
    try {
        val shareText = "Explore the world in 3D with Earth Scope! Check exact country-to-country flight distances, live compass orientation, and world geography:\nhttps://play.google.com/store/apps/details?id=com.shafaqat.earthscope"
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Earth Scope - 3D World Globe")
            putExtra(Intent.EXTRA_TEXT, shareText)
        }
        context.startActivity(Intent.createChooser(intent, "Share Earth Scope via"))
    } catch (_: Exception) {
        Toast.makeText(context, "Unable to open share menu", Toast.LENGTH_SHORT).show()
    }
}

private fun openPlayStoreListing(context: Context) {
    try {
        val marketIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=com.shafaqat.earthscope")
        )
        context.startActivity(marketIntent)
    } catch (_: Exception) {
        try {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.shafaqat.earthscope")
            )
            context.startActivity(webIntent)
        } catch (_: Exception) {
            // Ignored if no browser installed
        }
    }
}

@Composable
private fun ThemeOptionRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    selected: Boolean,
    isLightTheme: Boolean,
    tag: String,
    onClick: () -> Unit
) {
    val cardBg = when {
        selected && isLightTheme -> Color(0xFFECFDF5)
        selected && !isLightTheme -> Color(0xFF162B28)
        isLightTheme -> Color.White
        else -> Color(0xFF142038)
    }
    val borderColor = when {
        selected -> Color(0xFF22C55E)
        isLightTheme -> Color(0xFFE2E8F0)
        else -> Color(0xFF263955)
    }
    val titleColor = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val subtitleColor = if (isLightTheme) Color(0xFF64748B) else Color(0xFF94A3B8)

    Surface(
        color = cardBg,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(if (selected) 1.5.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconTint.copy(alpha = 0.16f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = subtitleColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Icon(
                imageVector = if (selected) Icons.Default.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (selected) Color(0xFF22C55E) else subtitleColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
