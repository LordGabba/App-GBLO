package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SlateDarkPrimary,
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E293B),
    onPrimaryContainer = SlateDarkPrimary,
    secondary = SlateDarkSecondary,
    onSecondary = Color(0xFF0F172A),
    tertiary = SlateDarkTertiary,
    onTertiary = Color(0xFF0F172A),
    background = SlateDarkBackground,
    onBackground = Color(0xFFF1F5F9),
    surface = SlateDarkSurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = SlateLightPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCCFBF1),
    onPrimaryContainer = SlateLightPrimary,
    secondary = SlateLightSecondary,
    onSecondary = Color.White,
    tertiary = SlateLightTertiary,
    onTertiary = Color.White,
    background = SlateLightBackground,
    onBackground = Color(0xFF0F172A),
    surface = SlateLightSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
