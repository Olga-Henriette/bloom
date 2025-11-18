package com.bloom.app.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BloomLightColorScheme = lightColorScheme(
    primary = LightColorScheme.primary,
    onPrimary = LightColorScheme.onPrimary,
    primaryContainer = LightColorScheme.primaryContainer,
    onPrimaryContainer = LightColorScheme.onPrimaryContainer,
    secondary = LightColorScheme.secondary,
    onSecondary = LightColorScheme.onSecondary,
    secondaryContainer = LightColorScheme.secondaryContainer,
    onSecondaryContainer = LightColorScheme.onSecondaryContainer,
    tertiary = LightColorScheme.tertiary,
    onTertiary = LightColorScheme.onTertiary,
    error = LightColorScheme.error,
    onError = LightColorScheme.onError,
    errorContainer = LightColorScheme.errorContainer,
    onErrorContainer = LightColorScheme.onErrorContainer,
    background = LightColorScheme.background,
    onBackground = LightColorScheme.onBackground,
    surface = LightColorScheme.surface,
    onSurface = LightColorScheme.onSurface,
    surfaceVariant = LightColorScheme.surfaceVariant,
    onSurfaceVariant = LightColorScheme.onSurfaceVariant,
    outline = LightColorScheme.outline,
    outlineVariant = LightColorScheme.outlineVariant,
)

@Composable
fun BloomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = BloomLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = BloomTypography,
        content = content
    )
}

object BloomColors {
    val primary500 = Primary500
    val primary550 = Primary550
    val primary600 = Primary600
    val primary800 = Primary800
    val neutral100 = Neutral100
    val neutral200 = Neutral200
    val neutral300 = Neutral300
    val neutral600 = Neutral600
    val neutral900 = Neutral900
    val danger500 = Danger500
    val danger600 = Danger600
    val danger650 = Danger650
    val white = White
    val black = Black
    val transparent = Transparent
    val gradientStart = GradientStart
    val gradientEnd = GradientEnd
}