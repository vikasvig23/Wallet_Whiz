package com.example.expensestracker.data

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon

import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensestracker.R
import com.example.expensestracker.charts.MonthlyChart
import com.example.expensestracker.charts.WeeklyChart
import com.example.expensestracker.charts.YearlyChart
import com.example.expensestracker.db_model.Recurrence
import com.example.expensestracker.db_model.SharedPreferencesManager
import com.example.expensestracker.screens.BudgetList
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp

import com.example.expensestracker.navigation.ui.theme.LabelSecondary
import com.example.expensestracker.screens.ui.theme.DarculaCard
import com.example.expensestracker.screens.ui.theme.DarculaMuted
import com.example.expensestracker.screens.ui.theme.DarculaText
import com.example.expensestracker.utils.Utility
import com.example.expensestracker.utils.Utility.backborder
import com.example.expensestracker.utils.formatDayForRange
import java.lang.reflect.Modifier
import java.text.DecimalFormat
import java.time.LocalDate

@Composable
fun ReportPage(
    context: Context,
    innerPadding: PaddingValues,
    page: Int,
    recurrence: Recurrence,
    reportvm: ReportsViewModel = viewModel(),
    vm: ReportPageViewModel = viewModel(
        key = "$page-${recurrence.name}",
        factory = viewModelFactory {
            ReportPageViewModel(context, page, recurrence)
        })
)  {
    val uiState = vm.uiState.collectAsState().value
    val reportuiState = reportvm.uiState.collectAsState().value
    val context = LocalContext.current
    val selectedCurrency = SharedPreferencesManager.getSelectedCurrency(context) ?: "None"

    val recurrences = listOf(
        Recurrence.Weekly,
        Recurrence.Monthly,
        Recurrence.Yearly
    )

    Column(
        modifier = androidx.compose.ui.Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(top = 25.dp,)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = androidx.compose.ui.Modifier.fillMaxWidth()
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    OutlinedButton(
                        onClick = reportvm::openRecurrenceMenu,
                        shape = RoundedCornerShape(12.dp), // radius
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline), // outline border
                    ) {

                        Icon(
                            painter = painterResource(id = R.drawable.ic_today),
                            tint = DarculaMuted ,
                            contentDescription = "Change recurrence",
                       //     modifier = Modifier.size(20.dp)
                        )
                       // Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "${uiState.dateStart.formatDayForRange()} - ${uiState.dateEnd.formatDayForRange()}",
                            style = MaterialTheme.typography.titleSmall,
                            color = DarculaMuted,
                            fontSize = 16.sp
                        )


                    }

                    DropdownMenu(
                        expanded = reportuiState.recurrenceMenuOpened,
                        onDismissRequest = reportvm::closeRecurrenceMenu
                    ) {
                        recurrences.forEach { recurrence ->
                            DropdownMenuItem(
                                text = { Text(recurrence.name) },
                                onClick = {
                                    reportvm.setRecurrence(recurrence)
                                    reportvm.closeRecurrenceMenu()
                                }
                            )
                        }
                    }
                }

                Row(modifier = androidx.compose.ui.Modifier.padding(top = 4.dp)) {
                    Text(
                       "Total expenses $selectedCurrency ${DecimalFormat(" 0.#").format(uiState.avgPerDay)}" ,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarculaText,
                        fontSize = 22.sp,
                        fontFamily = Utility.Poppins,
                        modifier = androidx.compose.ui.Modifier.padding(top = 5.dp)
                    )
                    //Text(DecimalFormat("0.#").format(uiState.totalInRange), fontFamily = Utility.Poppins, fontSize = 22.sp, style = MaterialTheme.typography.headlineMedium)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Avg/day", style = MaterialTheme.typography.titleSmall,color = DarculaText,fontSize = 15.sp, fontFamily = Utility.Poppins)
                Row(modifier = androidx.compose.ui.Modifier.padding(top = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        "$selectedCurrency ${DecimalFormat(" 0.#").format(uiState.avgPerDay)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarculaText,
                        fontSize = 15.sp,
                        fontFamily = Utility.Poppins,
                     //   modifier = androidx.compose.ui.Modifier.padding(top = 5.dp)
                    )
                  //  Text(DecimalFormat(" 0.#").format(uiState.avgPerDay),fontSize = 15.sp,fontFamily = Utility.Poppins, style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        Box(
            modifier = androidx.compose.ui.Modifier
                .height(280.dp)
                .padding(vertical = 16.dp),

        ) {
            when (recurrence) {
                Recurrence.Weekly -> WeeklyChart(expenses = uiState.expenses)
                Recurrence.Monthly -> MonthlyChart(
                    expenses = uiState.expenses,
                    month = uiState.dateStart
                )

                Recurrence.Yearly -> YearlyChart(expenses = uiState.expenses)
                else -> Unit
            }
        }

        Spacer(modifier = androidx.compose.ui.Modifier.height(16.dp))

        BudgetList()

//        ExpensesList(
//            expenses = uiState.expenses, modifier = androidx.compose.ui.Modifier
//                .weight(1f)
//                .padding(bottom=70.dp)
//                .verticalScroll(
//                    rememberScrollState()
//                )
//        )
    }
}
