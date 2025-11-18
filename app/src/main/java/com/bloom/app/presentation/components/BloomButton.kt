package com.bloom.app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloom.app.presentation.theme.*

// ========================================
// STYLES DE BOUTONS BLOOM
// ========================================
enum class BloomButtonStyle {
    PRIMARY,        // Bouton vert principal (Sign In, Capture Photo)
    SECONDARY,      // Bouton gris clair (Select from Gallery)
    OUTLINED,       // Bouton avec bordure (Continue with Google)
    DANGER,         // Bouton rouge (Delete Entry)
    TEXT            // Bouton sans fond (navigation)
}

// ========================================
// TAILLES DE BOUTONS
// ========================================
enum class BloomButtonSize {
    SMALL,   // 32dp height (icônes navigation)
    MEDIUM,  // 48dp height (boutons standards)
    LARGE    // 56dp height (bouton principal Sign In)
}

// ========================================
// COMPOSANT BOUTON BLOOM
// ========================================
@Composable
fun BloomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: BloomButtonStyle = BloomButtonStyle.PRIMARY,
    size: BloomButtonSize = BloomButtonSize.MEDIUM,
    icon: ImageVector? = null,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Configuration selon le style
    val buttonColors = getButtonColors(style, isPressed)
    val contentColor = getContentColor(style, isPressed)
    val borderStroke = getBorderStroke(style)
    val height = getButtonHeight(size)

    Button(
        onClick = onClick,
        modifier = modifier
            .height(height)
            .fillMaxWidth(),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColors,
            contentColor = contentColor,
            disabledContainerColor = buttonColors.copy(alpha = 0.4f),
            disabledContentColor = contentColor.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(if (size == BloomButtonSize.SMALL) 6.dp else 10.dp),
        border = borderStroke,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(horizontal = 12.dp),
        interactionSource = interactionSource
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(if (style == BloomButtonStyle.OUTLINED) 12.dp else 16.dp))
            }

            Text(
                text = text,
                fontSize = when (size) {
                    BloomButtonSize.LARGE -> 18.sp
                    BloomButtonSize.MEDIUM -> 16.sp
                    BloomButtonSize.SMALL -> 14.sp
                },
                fontWeight = when (style) {
                    BloomButtonStyle.DANGER -> FontWeight.Bold
                    else -> FontWeight.Medium
                },
                lineHeight = when (size) {
                    BloomButtonSize.LARGE -> 28.sp
                    BloomButtonSize.MEDIUM -> 26.sp
                    BloomButtonSize.SMALL -> 22.sp
                }
            )
        }
    }
}

// ========================================
// FONCTIONS UTILITAIRES
// ========================================

@Composable
private fun getButtonColors(style: BloomButtonStyle, isPressed: Boolean): Color {
    return when (style) {
        BloomButtonStyle.PRIMARY -> when {
            isPressed -> BloomColors.primary600
            else -> BloomColors.primary500
        }
        BloomButtonStyle.SECONDARY -> when {
            isPressed -> Color(0xFF8791A5)
            else -> BloomColors.neutral200
        }
        BloomButtonStyle.OUTLINED -> BloomColors.white
        BloomButtonStyle.DANGER -> when {
            isPressed -> BloomColors.danger650
            else -> BloomColors.danger500
        }
        BloomButtonStyle.TEXT -> BloomColors.transparent
    }
}

@Composable
private fun getContentColor(style: BloomButtonStyle, isPressed: Boolean): Color {
    return when (style) {
        BloomButtonStyle.PRIMARY -> BloomColors.primary800
        BloomButtonStyle.SECONDARY -> Color(0xFF323742)
        BloomButtonStyle.OUTLINED -> BloomColors.neutral900
        BloomButtonStyle.DANGER -> BloomColors.white
        BloomButtonStyle.TEXT -> BloomColors.neutral900
    }
}

private fun getBorderStroke(style: BloomButtonStyle): BorderStroke? {
    return when (style) {
        BloomButtonStyle.OUTLINED -> BorderStroke(2.dp, Neutral300)
        else -> null
    }
}

private fun getButtonHeight(size: BloomButtonSize): Dp {
    return when (size) {
        BloomButtonSize.SMALL -> 32.dp
        BloomButtonSize.MEDIUM -> 48.dp
        BloomButtonSize.LARGE -> 56.dp
    }
}

// ========================================
// BOUTON ICÔNE UNIQUEMENT
// ========================================
@Composable
fun BloomIconButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(32.dp),
        enabled = enabled
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier.size(16.dp),
            tint = if (enabled) BloomColors.neutral900 else BloomColors.neutral600
        )
    }
}