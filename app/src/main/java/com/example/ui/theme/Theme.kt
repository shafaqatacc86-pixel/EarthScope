package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.ui.AppThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = EarthNeonCyan,
    onPrimary = Color(0xFF040812),
    primaryContainer = EarthSkyBlue,
    onPrimaryContainer = Color.White,
    secondary = EarthEmerald,
    onSecondary = Color.Black,
    tertiary = EarthAmber,
    onTertiary = Color.Black,
    background = SpaceBackground,
    onBackground = Color.White,
    surface = SpaceCard,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF0B1728),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = SpaceBorder
)

private val LightColorScheme = lightColorScheme(
    primary = EarthSkyBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = EarthEmerald,
    onSecondary = Color.White,
    tertiary = EarthAmber,
    onTertiary = Color.White,
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),
    outline = Color(0xFFE2E8F0)
)

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val systemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> systemDark
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
