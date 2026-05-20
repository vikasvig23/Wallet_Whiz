package com.example.expensestracker.charts

import android.graphics.Paint.Style
import android.util.Log
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

import com.example.expensestracker.db_model.Recurrence
import com.example.expensestracker.db_model.groupedByDayOfMonth
import com.example.expensestracker.db_model.groupedByDayOfWeek
//import com.example.expensestracker.data.Expense
//import com.example.expensestracker.data.groupedByDayOfMonth

import com.example.expensestracker.navigation.ui.theme.LabelSecondary
import com.example.expensestracker.screens.BudgetList
import com.example.expensestracker.screens.ui.theme.DarculaPrimary
import com.example.expensestracker.screens.ui.theme.DarculaSecondary
import com.example.expensestracker.screens.ui.theme.DarculaText
import com.example.expensestracker.utils.PrefDataStore
import com.example.expensestracker.utils.Utility
import com.example.expensestracker.utils.Utility.backborder
import com.example.expensestracker.utils.simplifyNumber
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.BarDrawer

import com.github.tehras.charts.bar.renderer.label.LabelDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.log

@Composable
fun MonthlyChart(  expenses: List<ExpensesFb>, month: LocalDate

                   ) {

    val context = LocalContext.current
    val c1 = DarculaSecondary
    val c2 = DarculaPrimary
//    var budget by remember { mutableStateOf("") }
    val grouped = expenses.groupedByDayOfMonth()
    val numberOfDays = YearMonth.of(month.year, month.month).lengthOfMonth()

    val dayOrder = (1..numberOfDays).toList()

    val values = dayOrder.map { day -> grouped[day]?.total?.toFloat() ?: 0f }
    val maxVal = (values.maxOrNull() ?: 0f).coerceAtLeast(1f)

    var selectedBar by remember { mutableStateOf(-1) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .clip(RoundedCornerShape(12.dp))
            .backborder(shape = RoundedCornerShape(12.dp))

    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)) {
            // Top: optional header / legend
            Text("Monthly", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(start = 4.dp), color =  DarculaText)

            Spacer(modifier
            = Modifier.height(8.dp))

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
                        targetValue =  if (selectedBar == idx) fraction + 0.03f
                        else fraction,
                        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedBar = idx
                                Utility.performHapticAndSound(context)}
                    )

                    {
                        if (selectedBar == idx) {
                            Text(
                                simplifyNumber(rawValue),
                                color =  DarculaText,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .height(150.dp)
                                .padding(horizontal =1.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .width(
                                        if (selectedBar == idx) 28.dp else 18.dp
                                    )
                                    .fillMaxHeight()
                            ) {
                                val barHeight = if (rawValue > 0f) size.height * animatedFraction else size.height * 0.1f
                                val top = size.height - barHeight

                                val brush = if (rawValue > 0f) {
                                    Brush.verticalGradient(listOf(c1, c2))
                                } else {
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
                            text = dayOrder[idx].toString(),
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 6.sp,
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

    /*Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

    LaunchedEffect(Unit) {
         budget = PrefDataStore.getData(context, PrefDataStore.NAME).toString()
        Log.d("><", "$budget")
    }*/
  //  BudgetList(context,budget)
}
