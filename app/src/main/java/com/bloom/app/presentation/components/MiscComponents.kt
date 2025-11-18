package com.bloom.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bloom.app.presentation.theme.*

// ========================================
// ICÔNE FEUILLE BLOOM
// ========================================
@Composable
fun BloomLeafIcon(
    modifier: Modifier = Modifier,
    size: Int = 42,
    tint: Color = BloomColors.white,
    withBackground: Boolean = true
) {
    if (withBackground) {
        // Avec fond vert arrondi (pour l'authentification)
        Box(
            modifier = modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BloomColors.primary500),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Eco,
                contentDescription = "Bloom Logo",
                modifier = Modifier.size(size.dp),
                tint = tint
            )
        }
    } else {
        // Icône seule (pour les cartes)
        Icon(
            imageVector = Icons.Default.Eco,
            contentDescription = "Plant Icon",
            modifier = modifier.size(size.dp),
            tint = tint
        )
    }
}

// ========================================
// DIVIDER "OR" (pour l'authentification)
// ========================================
@Composable
fun BloomOrDivider(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = BloomColors.neutral300
        )

        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(BloomColors.white)
        ) {
            Text(
                text = "or",
                style = DividerTextStyle,
                color = BloomColors.neutral600
            )
        }

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = BloomColors.neutral300
        )
    }
}

// ========================================
// BARRE DE STATUT (simulation)
// ========================================
@Composable
fun BloomStatusBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(BloomColors.transparent)
    ) {
        // Pour la simulation, on peut laisser vide
        // Android gère automatiquement la vraie barre de statut
    }
}

// ========================================
// DIVIDER HORIZONTAL SIMPLE
// ========================================
@Composable
fun BloomDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        thickness = 1.dp,
        color = BloomColors.neutral300
    )
}

// ========================================
// 🏷BADGE DE DATE
// ========================================
@Composable
fun BloomDateBadge(
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Default.DateRange,
            contentDescription = "Date",
            modifier = Modifier.size(16.dp),
            tint = BloomColors.neutral600
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = date,
            style = MetadataStyle,
            color = BloomColors.neutral600
        )
    }
}