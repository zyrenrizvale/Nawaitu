package com.nawaitu.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val NawaituDarkColorScheme = darkColorScheme(
    primary = NeonGreen,
    onPrimary = DarkBackground,
    primaryContainer = Color(0xFF00804A),
    onPrimaryContainer = Color(0xFFCCFFE8),
    secondary = EmeraldGreen,
    onSecondary = DarkBackground,
    secondaryContainer = Color(0xFF1A5C38),
    onSecondaryContainer = Color(0xFFC8F5DC),
    tertiary = InfoBlue,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceBorder,
    outlineVariant = SurfaceBorder.copy(alpha = 0.5f),
    error = DangerRed,
    onError = TextPrimary,
    errorContainer = DangerRed.copy(alpha = 0.2f),
)

@Composable
fun NawaituTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NawaituDarkColorScheme,
        typography = NawaituTypography,
        content = content
    )
}
