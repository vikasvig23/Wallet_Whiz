package com.example.expensestracker.data

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensestracker.charts.MonthlyChart
import com.example.expensestracker.charts.WeeklyChart
import com.example.expensestracker.charts.YearlyChart
import com.example.expensestracker.db_model.Recurrence
import com.example.expensestracker.db_model.SharedPreferencesManager

import com.example.expensestracker.ui.theme.LabelSecondary
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
    vm: ReportPageViewModel = viewModel(
        key = "$page-${recurrence.name}",
        factory = viewModelFactory {
            ReportPageViewModel(context, page, recurrence)
        })
)  {
    val uiState = vm.uiState.collectAsState().value
    val context = LocalContext.current
    val selectedCurrency = SharedPreferencesManager.getSelectedCurrency(context) ?: "None"

    Column(
        modifier = androidx.compose.ui.Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = androidx.compose.ui.Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    "${
                        uiState.dateStart.formatDayForRange()
                    } - ${uiState.dateEnd.formatDayForRange()}",
                    style = MaterialTheme.typography.titleSmall
                )
                Row(modifier = androidx.compose.ui.Modifier.padding(top = 4.dp)) {
                    Text(
                        selectedCurrency,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabelSecondary,
                        modifier = androidx.compose.ui.Modifier.padding(end = 4.dp)
                    )
                    Text(DecimalFormat("0.#").format(uiState.totalInRange), style = MaterialTheme.typography.headlineMedium)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Avg/day", style = MaterialTheme.typography.titleSmall)
                Row(modifier = androidx.compose.ui.Modifier.padding(top = 4.dp)) {
                    Text(
                        selectedCurrency,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabelSecondary,
                        modifier = androidx.compose.ui.Modifier.padding(end = 4.dp)
                    )
                    Text(DecimalFormat("0.#").format(uiState.avgPerDay), style = MaterialTheme.typography.headlineMedium)
                }
            }
        }

        Box(
            modifier = androidx.compose.ui.Modifier
                .height(150.dp)
                .padding(vertical = 16.dp)
        ) {
            when (recurrence) {
                Recurrence.Weekly -> WeeklyChart(expenses = uiState.expenses)
                Recurrence.Monthly -> MonthlyChart(
                    expenses = uiState.expenses,
                    LocalDate.now()
                )
                Recurrence.Yearly -> YearlyChart(expenses = uiState.expenses)
                else -> Unit
            }
        }

        ExpensesList(
            expenses = uiState.expenses, modifier = androidx.compose.ui.Modifier
                .weight(1f)
                .padding(bottom=70.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        )
    }
}
