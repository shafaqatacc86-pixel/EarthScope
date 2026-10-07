package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ThemeSettingsDialog(
    currentThemeMode: AppThemeMode,
    isLightTheme: Boolean,
    onSelectTheme: (AppThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    val dialogBg = if (isLightTheme) Color(0xFFFFFFFF) else Color(0xFF0F172A)
    val borderColor = if (isLightTheme) Color(0xFFCBD5E1) else Color(0xFF263955)
    val primaryText = if (isLightTheme) Color(0xFF0F172A) else Color.White
    val secondaryText = if (isLightTheme) Color(0xFF475569) else Color(0xFF94A3B8)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = dialogBg,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, borderColor),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .testTag("theme_settings_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFF0284C7).copy(alpha = 0.16f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Appearance & Theme",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Choose White, Black, or System theme",
                                fontSize = 12.sp,
                                color = secondaryText,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("close_settings_dialog_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Settings",
                            tint = secondaryText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // 1. System Default Theme Option
                ThemeOptionRow(
                    title = "System Default",
                    subtitle = "Automatically match device white or black theme",
                    icon = Icons.Default.BrightnessAuto,
                    iconTint = Color(0xFF0284C7),
                    selected = currentThemeMode == AppThemeMode.SYSTEM,
                    isLightTheme = isLightTheme,
                    tag = "theme_option_system",
                    onClick = { onSelectTheme(AppThemeMode.SYSTEM) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 2. White Theme (Light Mode) Option
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

                Spacer(modifier = Modifier.height(10.dp))

                // 3. Black Theme (Dark Space Mode) Option
                ThemeOptionRow(
                    title = "Black Theme (Dark Space)",
                    subtitle = "Original deep space cosmos, stars & neon globe UI",
                    icon = Icons.Default.DarkMode,
                    iconTint = Color(0xFF38BDF8),
                    selected = currentThemeMode == AppThemeMode.DARK,
                    isLightTheme = isLightTheme,
                    tag = "theme_option_dark",
                    onClick = { onSelectTheme(AppThemeMode.DARK) }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF22C55E),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("done_settings_button")
                ) {
                    Text(
                        text = "Done",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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
        isLightTheme -> Color(0xFFF8FAFC)
        else -> Color(0xFF1E293B)
    }
    val borderColor = when {
        selected -> Color(0xFF22C55E)
        isLightTheme -> Color(0xFFE2E8F0)
        else -> Color(0xFF334155)
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
                .padding(horizontal = 14.dp, vertical = 14.dp),
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
