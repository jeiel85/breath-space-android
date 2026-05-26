package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SoftPurple,
    onPrimary = DeepMidnight,
    primaryContainer = TransparentPurple,
    onPrimaryContainer = SoftPurple,
    secondary = SoftMint,
    onSecondary = DeepMidnight,
    secondaryContainer = TransparentMint,
    onSecondaryContainer = SoftMint,
    tertiary = WarmPink,
    onTertiary = DeepMidnight,
    background = DeepMidnight,
    onBackground = WarmIvory,
    surface = CardMidnight,
    onSurface = WarmIvory,
    outline = BorderMidnight
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit,
) {
    // Force dark theme for atmospheric "Breathe Space" calm vibe
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
