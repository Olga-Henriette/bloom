package com.bloom.app.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bloom.app.presentation.theme.*

@Composable
fun BloomLoadingIndicator(
    message: String = "Analyzing image...",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .size(256.dp, 160.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color(0x0D171A1F),
                spotColor = Color(0x14171A1F)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(BloomColors.white),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Loading",
                modifier = Modifier
                    .size(50.dp)
                    .rotate(rotation),
                tint = BloomColors.primary500
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = message,
                style = LoadingTextStyle,
                color = BloomColors.neutral600,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun BloomSimpleLoader(
    modifier: Modifier = Modifier,
    size: Int = 40
) {
    val infiniteTransition = rememberInfiniteTransition(label = "simple_rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "simple_rotation"
    )

    Icon(
        imageVector = Icons.Default.Refresh,
        contentDescription = "Loading",
        modifier = modifier
            .size(size.dp)
            .rotate(rotation),
        tint = BloomColors.primary500
    )
}

@Composable
fun BloomFullScreenLoader(
    message: String = "Loading...",
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BloomColors.white),
        contentAlignment = Alignment.Center
    ) {
        BloomLoadingIndicator(message = message)
    }
}