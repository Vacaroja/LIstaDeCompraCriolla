package com.ccc.listadecompracriolla.Core.clientlist.principal

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ccc.listadecompracriolla.R

@Composable
fun AnimationAddList() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.add_list_animation))
    val arrowComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.up_arrow_animation))


    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    val Arrowprogress by animateLottieCompositionAsState(
        composition = arrowComposition,
        iterations = LottieConstants.IterateForever
    )

    Column {
        LottieAnimation(
            composition = composition,
            progress = { progress }
        )
        LottieAnimation(
            composition = arrowComposition,
            progress = { Arrowprogress }
        )
    }
}

@Composable
fun AnimationArrowAddList() {
    val arrowComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.up_arrow_animation))

    val arrowProgress by animateLottieCompositionAsState(
        composition = arrowComposition,
        iterations = LottieConstants.IterateForever
    )

        LottieAnimation(
            composition = arrowComposition,
            progress = { arrowProgress }
        )

}