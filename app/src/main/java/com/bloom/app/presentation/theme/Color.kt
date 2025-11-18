package com.bloom.app.presentation.theme

import androidx.compose.ui.graphics.Color

// ========================================
// COULEURS PRINCIPALES (PRIMARY)
// ========================================
val Primary500 = Color(0xFF26D962)  //principal
val Primary550 = Color(0xFF22C157)  // hover
val Primary600 = Color(0xFF1EAA4D)  // pressed
val Primary800 = Color(0xFF0E4D23)  // Vert foncé (texte sur fond vert)

// ========================================
// ⚫ COULEURS NEUTRES (NEUTRAL)
// ========================================
val Neutral100 = Color(0xFFFAFAFB)  // Gris très clair (background image)
val Neutral200 = Color(0xFFF3F4F6)  // clair (background secondaire)
val Neutral300 = Color(0xFFDEE1E6)  // bordure
val Neutral600 = Color(0xFF565D6D)  // texte secondaire
val Neutral900 = Color(0xFF171A1F)  // texte principal

// ========================================
// COULEUR DE DANGER (DANGER)
// ========================================
val Danger500 = Color(0xFFE05252)   // principal
val Danger600 = Color(0xFFD02525)   // hover
val Danger650 = Color(0xFFB72020)   // pressed

// ========================================
// COULEURS DE BASE
// ========================================
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)
val Transparent = Color(0x00000000)

// ========================================
// COULEURS SPÉCIALES
// ========================================
val GradientStart = Color(0xCCFFFFFF)  // Blanc 80% opacité pour gradient
val GradientEnd = Color(0x00FFFFFF)    // Blanc 0% opacité pour gradient

// ========================================
// COULEURS THÈME CLAIR (Light Theme)
// ========================================
object LightColorScheme {
    val primary = Primary500
    val onPrimary = Primary800
    val primaryContainer = Primary550
    val onPrimaryContainer = Primary800

    val secondary = Neutral200
    val onSecondary = Neutral900
    val secondaryContainer = Neutral100
    val onSecondaryContainer = Neutral600

    val tertiary = Neutral300
    val onTertiary = Neutral600

    val error = Danger500
    val onError = White
    val errorContainer = Danger600
    val onErrorContainer = White

    val background = White
    val onBackground = Neutral900

    val surface = White
    val onSurface = Neutral900
    val surfaceVariant = Neutral100
    val onSurfaceVariant = Neutral600

    val outline = Neutral300
    val outlineVariant = Neutral200
}