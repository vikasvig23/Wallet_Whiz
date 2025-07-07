package com.example.expensestracker.utils

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.fragment.app.Fragment
import kotlinx.coroutines.delay


enum class AlertType {
    SUCCESS, ERROR, WARNING
}


@Composable
fun CustomAlerter(
    title: String,
    message: String,
    type: AlertType = AlertType.SUCCESS,
    show: Boolean,
    onDismiss: () -> Unit
) {
    val transition = updateTransition(targetState = show, label = "alerter_transition")

    val offsetY by transition.animateDp(
        transitionSpec = { tween(durationMillis = 500) },
        label = "slide_offset"
    ) { visible -> if (visible) 0.dp else (-100).dp }

    val alpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 500) },
        label = "fade_alpha"
    ) { visible -> if (visible) 1f else 0f }

    if (show) {
        LaunchedEffect(Unit) {
            delay(3000)
            onDismiss()
        }
    }

    val backgroundColor = when (type) {
        AlertType.SUCCESS -> Color(0xFF4CAF50)
        AlertType.ERROR -> Color(0xFFE53935)
        AlertType.WARNING -> Color(0xFFFFB300)
    }

    val icon = when (type) {
        AlertType.SUCCESS -> Icons.Default.CheckCircle
        AlertType.ERROR -> Icons.Default.Error
        AlertType.WARNING -> Icons.Default.Warning
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = offsetY)
            .alpha(alpha)
            .zIndex(10f),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor)
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }
    }
}
