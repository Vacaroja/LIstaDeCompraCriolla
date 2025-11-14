package com.ccc.listadecompracriolla.ui.buttoncolor

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import com.ccc.listadecompracriolla.ui.theme.purpleHeart
import com.ccc.listadecompracriolla.ui.theme.purpleHeartblack

@Composable
fun animateColorButtonClientList(
    isPressed: Int,
    targetPressValue: Int = 1,
    primaryColor: Color = purpleHeart,
    secondaryColor: Color = purpleHeartblack
): State<Color> {
    // Define la lógica de color para varias condiciones de presión
    val targetColor = if (isPressed == targetPressValue) {
        primaryColor
    } else {
        secondaryColor
    }

    return animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 200),
        label = "ColoAnimationClientList" // Es buena práctica añadir una etiqueta
    )
}