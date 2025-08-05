package com.example.expensestracker.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.strictmode.SetRetainInstanceUsageViolation
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.expensestracker.db_model.ExpensesFb
import com.example.expensestracker.db_model.Recurrence
import com.example.expensestracker.db_model.getParsedDate
import com.example.expensestracker.utils.PrefDataStore

import com.example.expensestracker.utils.SecurePrefsDataStore
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
import java.time.format.DateTimeFormatter
import kotlin.math.exp

data class ExpensesState(
    val recurrence: Recurrence = Recurrence.Daily,
    val sumTotal: Double = 0.0,
    val expenses: List<ExpensesFb> = listOf()
)

class ExpensesViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(ExpensesState())
    val uiState: StateFlow<ExpensesState> = _uiState.asStateFlow()

    private val name = MutableStateFlow(String())
    val _name:StateFlow<String>  = name.asStateFlow()

    private val expeneData = MutableStateFlow(String())
    val _expenseData:StateFlow<String>  = expeneData.asStateFlow()


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


            val sumTotal = filteredExpenses.sumOf { it.amount }

            _uiState.update { currentState ->
                currentState.copy(
                    recurrence = recurrence,
                    expenses = filteredExpenses,
                    sumTotal = sumTotal
                )
            }

        } catch (e: Exception) {
            Log.e("Firebase", "Failed to fetch expenses: ${e.message}")
        }
    }


    suspend fun fetchUserNameFromFirebase(context: Context) {
        val savedEmail = PrefDataStore.getEmail(context)
        Log.d("SecureEmail", savedEmail ?: "No email found")

        if (savedEmail != null) {
            try {
                val snapshot = FirebaseDatabase.getInstance()
                    .getReference("expenses")
                    .child(savedEmail)
                    .get()
                    .await()

                val nameValue = snapshot.child("name").getValue(String::class.java)

                nameValue?.let {
                    PrefDataStore.saveData(context, PrefDataStore.NAME, it)
                    name.value = it
                }



            } catch (e: Exception) {
                Log.e("Firebase", "Failed to fetch username: ${e.message}")
            }
        }
    }


}