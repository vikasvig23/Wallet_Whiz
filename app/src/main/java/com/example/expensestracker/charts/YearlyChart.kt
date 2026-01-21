package com.example.expensestracker.charts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensestracker.R

import com.example.expensestracker.db_model.ExpensesFb
import com.example.expensestracker.db_model.Recurrence
import com.example.expensestracker.db_model.groupedByMonth

import com.example.expensestracker.navigation.ui.theme.LabelSecondary
import com.example.expensestracker.utils.simplifyNumber
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import com.example.expensestracker.screens.ui.theme.DarculaPrimary
import com.example.expensestracker.screens.ui.theme.DarculaSecondary
import com.example.expensestracker.screens.ui.theme.DarculaText
import com.example.expensestracker.utils.Utility
import com.example.expensestracker.utils.Utility.backborder
import java.time.Month


@Composable
fun YearlyChart(expenses: List<ExpensesFb>) {

    val context = LocalContext.current

    val c1 = DarculaSecondary
    val c2 = DarculaPrimary

    val grouped = expenses.groupedByMonth() // Your extension

    val monthEnums = Month.values().toList()
    val monthLabels = monthEnums.map { it.name.take(3) } // First letter (J F M...)

    // Get totals in correct month order
    val values = monthEnums.map { month ->
        grouped[month.name]?.total?.toFloat() ?: 0f
    }

    val maxVal = (values.maxOrNull() ?: 0f).coerceAtLeast(1f)
    var selectedBar by remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
        .clip(RoundedCornerShape(12.dp))
        .backborder(shape = RoundedCornerShape(12.dp))

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {

//            Text(
//                "Yearly Overview",
//                style = MaterialTheme.typography.titleMedium,
//                modifier = Modifier.padding(start = 4.dp)
//            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                values.forEachIndexed { idx, rawValue ->
                    val fraction = rawValue / maxVal
                    val animatedFraction by animateFloatAsState(
                        targetValue =
                        if (selectedBar == idx) fraction + 0.03f
                        else fraction,
                        animationSpec = tween(
                            durationMillis = 700,
                            easing = FastOutSlowInEasing
                        )
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedBar = idx
                            Utility.performHapticAndSound(context)},

                    ) {
                        if (selectedBar == idx) {
                            Text(
                                simplifyNumber(rawValue),
                                color = MaterialTheme.colorScheme.primary,
                                    fontSize = 10.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Box(
                            modifier = Modifier.height(180.dp)
                                .padding(2.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .width(
                                        if (selectedBar == idx) 28.dp else 18.dp
                                    )
                                    .fillMaxHeight()
                            ) {
                                val barHeight =
                                    if (rawValue > 0f) size.height * animatedFraction
                                    else size.height * 0.1f

                                drawRoundRect(
                                    brush = Brush.verticalGradient(listOf(c1, c2)),
                                    topLeft = Offset(0f, size.height - barHeight),
                                    size = Size(size.width, barHeight),
                                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                                )
                            }

                        }
                        Text(
                            text = monthLabels[idx],
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            color = DarculaText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Min: ${simplifyNumber(values.minOrNull() ?: 0f)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarculaText
                )
                Text(
                    "Max: ${simplifyNumber(values.maxOrNull() ?: 0f)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarculaText
                )
            }
        }
    }
}
