package com.example.expensestracker.data

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expensestracker.R
import com.example.expensestracker.db_model.ExpensesFb

import com.example.expensestracker.db_model.SharedPreferencesManager
import com.example.expensestracker.db_model.getParsedDate
import com.example.expensestracker.ui.theme.LabelSecondary
import com.example.expensestracker.ui.theme.Typography
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
@Composable
fun ExpenseRow(expense: ExpensesFb, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val selectedCurrency = SharedPreferencesManager.getSelectedCurrency(context) ?: "None"

    val Poppins = FontFamily(
        Font(R.font.comfortaa, FontWeight.Normal),
        Font(R.font.comfortaa_light, FontWeight.Light),
        Font(R.font.comfortaa_bold, FontWeight.Medium),
        Font(R.font.comforta_extra_bold, FontWeight.Bold)
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                expense.note ?: expense.category?.name.orEmpty(),
                style = MaterialTheme.typography.headlineMedium,
                        fontFamily = Poppins,
            )
            Text(
                "$selectedCurrency ${DecimalFormat("0.#").format(expense.amount)}",
                style = MaterialTheme.typography.headlineMedium,
                fontFamily = Poppins,
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
                expense.time,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = Poppins,
                color = LabelSecondary
            )

        }
    }
}
