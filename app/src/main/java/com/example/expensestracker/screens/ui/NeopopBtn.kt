package com.example.expensestracker.screens.ui



import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.MotionEvent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer

import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensestracker.R
import com.example.expensestracker.screens.ui.theme.DarculaCard
import com.example.expensestracker.utils.Utility.backborder

@SuppressLint("ResourceAsColor")
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NeoPopButton(
    text: String,
    iconRes: Int,
    textSize: TextUnit,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    var pressed by remember { mutableStateOf(false) }

    val border = 6.dp

    // Button drops diagonally = BOTH x & y offset animate
    val offset by animateDpAsState(
        targetValue = if (pressed) border else 0.dp,
        animationSpec = tween(durationMillis = 90),
        label = ""
    )

    val neonGreen = DarculaCard

    Box(
        modifier = modifier
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        pressed = true
                    }
                    MotionEvent.ACTION_UP -> {
                        pressed = false
                        vibrateHaptic(context)
                        onClick()
                    }
                }
                true
            }
    ) {

        // RIGHT GREEN BORDER (static)
//        Box(
//            modifier = Modifier
//                .matchParentSize()
//                .offset(x = border)
//                .width(border)
//                .background(neonGreen)
//        )

        // BOTTOM GREEN BORDER (static)
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(x = offset, y = offset)
                .clip(RoundedCornerShape(12.dp))
                .backborder(shape = RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    color = neonGreen,
                    shape = RoundedCornerShape(5.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {


                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = text,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(bottom = 6.dp)
                )

                Text(
                    text = text,
                    fontSize = textSize,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



fun vibrateHaptic(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(40)
    }
}