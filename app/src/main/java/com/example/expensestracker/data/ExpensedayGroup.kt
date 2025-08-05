package com.example.expensestracker.data


import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.expensestracker.db_model.DayExpenses
import com.example.expensestracker.db_model.SharedPreferencesManager
import com.example.expensestracker.ui.theme.LabelSecondary
import com.example.expensestracker.utils.Utility
import com.example.expensestracker.utils.formatDay
import java.text.DecimalFormat
import java.time.LocalDate
@Composable
fun ExpensesDayGroup(
    date: LocalDate,
    dayExpenses: DayExpenses?,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedCurrency = SharedPreferencesManager.getSelectedCurrency(context) ?: "None"

    dayExpenses?.let {
        Column(modifier = modifier) {
            Text(
                date.formatDay(),
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = Utility.Poppins,
                color = LabelSecondary
            )
            HorizontalDivider(modifier = Modifier.padding(top = 10.dp, bottom = 4.dp))
            it.expenses.forEach { expense ->
                ExpenseRow(
                    expense = expense,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total :", style = MaterialTheme.typography.headlineSmall, fontFamily = Utility.Poppins, color = Color.Black)
                Text(
                    DecimalFormat("$selectedCurrency 0.#").format(it.total),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    fontFamily = Utility.Poppins
                )
            }
        }
    }
}

@Preview()
@Composable
fun dayGroupPreview(){
    val localDat = LocalDate.of(2025,7,28)

    ExpensesDayGroup(localDat, dayExpenses =DayExpenses(
        expenses = TODO(),
        total = TODO()
    ),Modifier)
}

