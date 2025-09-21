package com.ccc.listadecompracriolla.Core.clientlist.lottieanimations

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ccc.listadecompracriolla.R

@Composable
fun AnimationAddList() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.add_list_animation))
    val clickComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.click_animation))


    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val clickProgress by animateLottieCompositionAsState(
        composition = clickComposition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
    )

    LottieAnimation(
        composition = clickComposition,
        progress = { clickProgress },
    )


}

@Composable
fun AnimationArrowAddList() {
    val arrowComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.arrow))

    val arrowProgress by animateLottieCompositionAsState(
        composition = arrowComposition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = arrowComposition, progress = { arrowProgress }
    )

}