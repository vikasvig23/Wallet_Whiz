package com.example.expensestracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.expensestracker.R

@Composable
fun Splash(){
   // val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.walletasplash))
 //   val progress by animateLottieCompositionAsState(composition)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
        Text(text = "Wallet Whiz", fontSize = 50.sp)
       // Spacer(modifier = Modifier.padding(20.dp))
        LoaderAnimation(
            modifier = Modifier.size(500.dp),
            anim= R.raw.wallet
        )
    }

}

@Composable
fun LoaderAnimation(modifier: Modifier, anim: Int) {
    val composition by rememberLottieComposition(spec=LottieCompositionSpec.RawRes(anim))
    LottieAnimation(modifier = Modifier.size(400.dp),
        composition = composition, iterations = LottieConstants.IterateForever)
}

@Preview
@Composable
fun PreviewSplash(){
    Splash()
}