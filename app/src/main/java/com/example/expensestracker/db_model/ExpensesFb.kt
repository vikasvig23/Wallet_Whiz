package com.example.expensestracker.db_model

import java.time.LocalDate

data class Expenses(
    val amount: Double = 0.0,
    val recurrence: String = "",
    val date: String = "",
    val time: String ="",
    val note: String = "",
    val category: String = ""
) {
    fun toLocalDate(): LocalDate = LocalDate.parse(date)
}
