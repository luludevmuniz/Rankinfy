package com.alpaca.rankify.presentation.anim

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.alpaca.rankify.R

@Composable
fun GhostAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.ghost))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}