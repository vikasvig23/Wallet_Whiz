package com.example.expensestracker.charts


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensestracker.R
import com.example.expensestracker.db_model.ExpensesFb

import com.example.expensestracker.db_model.groupedByDayOfWeek
import com.example.expensestracker.screens.ui.theme.DarculaPrimary
import com.example.expensestracker.screens.ui.theme.DarculaSecondary
import com.example.expensestracker.screens.ui.theme.DarculaText
import com.example.expensestracker.utils.Utility
import com.example.expensestracker.utils.Utility.backborder

import com.example.expensestracker.utils.simplifyNumber

import java.time.DayOfWeek
@Composable
fun WeeklyChart(
    expenses: List<ExpensesFb>,

) { // build totals per day (Mon..Sun). Uses your grouping helper or compute here
    val context = LocalContext.current
    val c1 = DarculaSecondary
    val c2 = DarculaPrimary
    val grouped = expenses.groupedByDayOfWeek() // returns map keyed by DayOfWeek.name -> object with .total
    val dayOrder = listOf(
        DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY
    )

    val values = dayOrder.map { grouped[it.name]?.total?.toFloat() ?: 0f }
    val maxVal = (values.maxOrNull() ?: 0f).coerceAtLeast(1f)

    var selectedBar by remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(12.dp))
            .backborder(shape = RoundedCornerShape(12.dp))
            ,
      //  shape = RoundedCornerShape(12.dp),
      //  elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            // Top: optional header / legend
//            Text("Weekly", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 4.dp))

            Spacer(modifier
            = Modifier.height(9.dp))

            // Chart area
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                values.forEachIndexed { idx, rawValue ->
                    val fraction = rawValue / maxVal
                    // animate fraction for a nice entrance effect
                    val animatedFraction by animateFloatAsState(
                        targetValue = if (selectedBar == idx) fraction + 0.03f
                        else fraction,
                        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(2f)
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
                            modifier = Modifier
                                .height(180.dp)
                                .padding(horizontal = 4.dp)
                            ,
                            contentAlignment = Alignment.BottomCenter

                        ) {
                            Canvas(
                                modifier = Modifier
                                    .width(
                                        if (selectedBar == idx) 28.dp else 22.dp
                                    )
                                    .fillMaxHeight()
                            ) {
                                val barHeight = if (rawValue > 0f) size.height * animatedFraction else size.height * 0.1f
                                val top = size.height - barHeight

                                val brush = if (rawValue > 0f) {
                                    // Normal full opacity gradient
                                    Brush.verticalGradient(listOf(c1, c2))
                                } else {
                                    // Low opacity gradient for empty bars
                                    Brush.verticalGradient(
                                        listOf(c1.copy(alpha = 0.4f), c2.copy(alpha = 0.4f))
                                    )
                                }

                                drawRoundRect(
                                    brush = brush,
                                    topLeft = Offset(0f, top),
                                    size = Size(size.width, barHeight),
                                    cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
                                )
                            }

                        }

                        Text(
                            text = dayOrder[idx].name.take(1),
                            modifier = Modifier.align(Alignment.CenterHorizontally), // ColumnScope.align -> expects Alignment.Horizontal
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.Center,
                            color = DarculaText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // X axis / footer (e.g., totals, or legend)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
