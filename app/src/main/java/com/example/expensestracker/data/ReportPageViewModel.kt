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
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.LocalTime


data class State(
    val expenses: List<ExpensesFb> = listOf(),
    val dateStart: LocalDateTime = LocalDateTime.now(),
    val dateEnd: LocalDateTime = LocalDateTime.now(),
    val avgPerDay: Double = 0.0,
    val totalInRange: Double = 0.0
)

class ReportPageViewModel(private val context: Context,private val page: Int, val recurrence: Recurrence) :
    ViewModel() {
    private val _uiState = MutableStateFlow(State())
    val uiState: StateFlow<State> = _uiState.asStateFlow()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            val (start, end, daysInRange) = calculateDateRange(recurrence, page)
            val savedEmail = PrefDataStore.getEmail(context) ?: return@launch
            try {
                val snapshot = FirebaseDatabase.getInstance()
                    .getReference("expenses")
                    .child(savedEmail)
                    .child("expenseList")
                    .get()
                    .await()

                val expenseList = mutableListOf<ExpensesFb>()

                for (child in snapshot.children) {
                    val expense = child.getValue(ExpensesFb::class.java)
                    if (expense != null) {
                        expenseList.add(expense)
                    }
                }

                val filteredExpenses = expenseList.filter { expense ->
                    val expenseDate = expense.getParsedDate()
                    (expenseDate.isAfter(start) && expenseDate.isBefore(end))
                            || expenseDate.isEqual(start)
                            || expenseDate.isEqual(end)
                }



                val totalExpensesAmount = filteredExpenses.sumOf { it.amount }
                val avgPerDay: Double = totalExpensesAmount / daysInRange

                viewModelScope.launch(Dispatchers.Main) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            dateStart = LocalDateTime.of(start, LocalTime.MIN),
                            dateEnd = LocalDateTime.of(end, LocalTime.MAX),
                            expenses = filteredExpenses,
                            avgPerDay = avgPerDay,
                            totalInRange = totalExpensesAmount
                        )
                    }
                }

            } catch (e: Exception) {
                Log.e("Firebase", "Failed to fetch expenses: ${e.message}")
            }


        }

        suspend fun setRecurrence(context: Context, recurrence: Recurrence) {
            val savedEmail = PrefDataStore.getEmail(context) ?: return

            val (start, end) = calculateDateRange(recurrence, 0)

            try {
                val snapshot = FirebaseDatabase.getInstance()
                    .getReference("expenses")
                    .child(savedEmail)
                    .child("expenseList")
                    .get()
                    .await()

                val expenseList = mutableListOf<ExpensesFb>()

                for (child in snapshot.children) {
                    val expense = child.getValue(ExpensesFb::class.java)
                    if (expense != null) {
                        expenseList.add(expense)
                    }
                }

                val filteredExpenses = expenseList.filter { expense ->
                    val expenseDate = expense.getParsedDate()
                    (expenseDate.isAfter(start) && expenseDate.isBefore(end))
                            || expenseDate.isEqual(start)
                            || expenseDate.isEqual(end)
                }


            } catch (e: Exception) {
                Log.e("Firebase", "Failed to fetch expenses: ${e.message}")
            }
        }
    }


}