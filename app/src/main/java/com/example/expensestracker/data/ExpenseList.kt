package com.example.expensestracker.data


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensestracker.db_model.DayExpenses

import com.example.expensestracker.db_model.ExpensesFb
import com.example.expensestracker.db_model.FilterMode
import com.example.expensestracker.db_model.fetchGroupedExpensesFromFirebase
import com.example.expensestracker.db_model.getParsedDate
import com.example.expensestracker.db_model.groupedByDay
import java.time.LocalDate

@Composable
//fun ExpensesList(expenses: List<ExpensesFb>, modifier: Modifier = Modifier) {
//    val groupedExpenses = expenses.groupedByDay()
//
//    Column(modifier = modifier.padding(20.dp, top = 0.dp)) {
//        if (groupedExpenses.isEmpty()) {
//            Text("No data for selected date range.", modifier = Modifier.padding(top = 32.dp))
//        } else {
//            groupedExpenses.keys.forEach { date ->
//                groupedExpenses[date]?.let { dayExpenses ->
//                    ExpensesDayGroup(
//                        date = date,
//                        dayExpenses = dayExpenses,
//                        modifier = Modifier.padding(top = 24.dp)
//                    )
//                }
//            }
//        }
//    }
//}


fun ExpensesList(
    expenses: List<ExpensesFb>,
    modifier: Modifier = Modifier
) {
    val groupedExpenses = expenses.groupBy { it.getParsedDate() }
        .mapValues { entry ->
            DayExpenses(
                expenses = entry.value.toMutableList(),
                total = entry.value.sumOf { it.amount }
            )
        }

    Column(modifier = modifier.padding(20.dp)) {
        if (groupedExpenses.isEmpty()) {
            Text("No data for selected date range.", modifier = Modifier.padding(top = 32.dp))
        } else {
            groupedExpenses.keys.forEach { date ->
                groupedExpenses[date]?.let { dayExpenses ->
                    ExpensesDayGroup(
                        date = date,
                        dayExpenses = dayExpenses,
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            }
        }
    }
}



@Preview()
@Composable
fun Preview() {

      //  ExpensesList(mockExpenses)

}