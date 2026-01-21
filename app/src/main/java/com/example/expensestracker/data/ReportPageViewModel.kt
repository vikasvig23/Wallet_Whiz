package com.example.expensestracker.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensestracker.db_model.ExpensesFb
import com.example.expensestracker.db_model.Recurrence
import com.example.expensestracker.db_model.getParsedDate
import com.example.expensestracker.utils.PrefDataStore
import com.example.expensestracker.utils.calculateDateRange
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class State(
    val expenses: List<ExpensesFb> = listOf(),
    val dateStart: LocalDate = LocalDate.now(),
    val dateEnd: LocalDate = LocalDate.now(),
    val avgPerDay: Double = 0.0,
    val totalInRange: Double = 0.0
)

class ReportPageViewModel(
    private val context: Context,
    private val page: Int,
    private val recurrence: Recurrence
) : ViewModel() {

    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    init {
        fetchReportData()
    }

    private fun fetchReportData() {
        viewModelScope.launch(Dispatchers.IO) {
            val (start, end, daysInRange) = calculateDateRange(recurrence, page)
            Log.d("ReportRange", "Start: $start | End: $end")

            val savedEmail = PrefDataStore.getEmail(context) ?: return@launch
            try {
                val snapshot = FirebaseDatabase.getInstance()
                    .getReference("expenses")
                    .child(savedEmail)
                    .child("expenseList")
                    .get()
                    .await()

                val expenseList = snapshot.children.mapNotNull {
                    it.getValue(ExpensesFb::class.java)
                }

                val filteredExpenses = expenseList.filter { expense ->
                    val date = expense.getParsedDate()
                    !date.isBefore(start) && !date.isAfter(end)
                }

                val totalAmount = filteredExpenses.sumOf { it.amount }
                val avgPerDay = if (daysInRange > 0) totalAmount / daysInRange else 0.0

                viewModelScope.launch(Dispatchers.Main) {
                    _uiState.update {
                        it.copy(
                            dateStart = start,
                            dateEnd = end,
                            expenses = filteredExpenses,
                            avgPerDay = avgPerDay,
                            totalInRange = totalAmount
                        )
                    }
                }

            } catch (e: Exception) {
                Log.e("Firebase", "Error fetching data: ${e.message}")
            }
        }
    }
}
