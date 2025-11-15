package com.ccc.listadecompracriolla.ui.animation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ccc.listadecompracriolla.ui.theme.black
/*
    -First Icon for the no principal Icon
    -Second Icon for the principal Icon
 */
@Composable
fun InfiniteAnimationIconRates(
    modifier: Modifier,
    animatedColorNoPressed: Color = black,
    secondaryIcon: Int,
    primaryIcon: Int = secondaryIcon
) {
    val duration = 5000

    val infiniteTransitionIcon = rememberInfiniteTransition(label = "iconSwitchTransition")

    val animatedProgress by infiniteTransitionIcon.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(duration, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "icon animation progress"
    )

    Box(modifier = modifier) {
        Icon(
            painter = painterResource(id = primaryIcon),
            contentDescription = "DollarBCV",
            modifier = modifier
                .size(50.dp)
                .padding(5.dp)
                .graphicsLayer(
                    alpha = 1f - animatedProgress,
                    rotationY = 360f * animatedProgress
                ),
            tint = animatedColorNoPressed
        )
        Icon(
            painter = painterResource(id = secondaryIcon),
            contentDescription = "DollarBCV",
            modifier = modifier
                .size(50.dp)
                .padding(5.dp)
                .graphicsLayer(
                    alpha = animatedProgress,
                    rotationY = 360f * (1f - animatedProgress)
                ),
            tint = animatedColorNoPressed
        )
    }
}