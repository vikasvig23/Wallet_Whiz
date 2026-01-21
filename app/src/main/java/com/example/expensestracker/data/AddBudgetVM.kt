package com.example.expensestracker.data

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensestracker.db_model.ExpensesFb
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.utils.PrefDataStore
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

data class Budget(
    val amount: Int
)

data class BudgetState(
    val budgetText: String = "",
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isSaved: Boolean = false
)


class AddBudgetVM: ViewModel() {

    private  val _uiState = MutableStateFlow(BudgetState())
    val uiState : StateFlow<BudgetState> = _uiState


    private val database =
        FirebaseDatabase.getInstance().reference

    private val auth = FirebaseAuth.getInstance()

    fun setBudget(value : String){

        _uiState.value = _uiState.value.copy(
            budgetText =  value,
            isError = false,
            errorMessage = ""
        )
    }

    fun valid(context: Context){
        val budget = _uiState.value.budgetText


        if (budget.isBlank()) {
            _uiState.value = _uiState.value.copy(
                isError = true,
                errorMessage = "Please add the amount first."
            )
            return
        }

        val budgetAmount = budget.toIntOrNull()
        if (budgetAmount == null) {
            _uiState.value = _uiState.value.copy(
                isError = true,
                errorMessage = "Enter a valid amount"
            )
            return
        }

     viewModelScope.launch { saveBudgetFirebase(context ,budgetAmount) }
    }


    suspend fun saveBudgetFirebase(context: Context, amount : Int){

        val email = PrefDataStore.getEmail(context)

        if (email != null) {



            val expense = Budget(
                amount = amount,

            )

            FirebaseDatabase.getInstance()
                .getReference("expenses")
                .child(email)
                .push()
                .setValue(expense)
                .addOnSuccessListener {
                    Log.d("Firebase", "Budget Amount saved successfully")

                    AppRouter.navigateTo(Screen.HomeScreen)
                   // resetUiState()
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Failed to save budget amount : ${e.message}")
                }
        }

    }

}