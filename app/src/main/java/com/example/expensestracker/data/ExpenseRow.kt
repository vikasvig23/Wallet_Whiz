package com.example.expensestracker.data

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.expensestracker.db_model.Expense
import com.example.expensestracker.db_model.SharedPreferencesManager
import com.example.expensestracker.ui.theme.LabelSecondary
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
@Composable
fun ExpenseRow(expense: Expense, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val selectedCurrency = SharedPreferencesManager.getSelectedCurrency(context) ?: "None"

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                expense.note ?: expense.category?.name.orEmpty(),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                "$selectedCurrency ${DecimalFormat("0.#").format(expense.amount)}",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            expense.category?.let {
                CategoryBadge(category = it)  // Only show badge if category is not null
            }
            Text(
                expense.date.format(DateTimeFormatter.ofPattern("HH:mm")),
                style = MaterialTheme.typography.bodyMedium,
                color = LabelSecondary
            )
        }
    }
}
