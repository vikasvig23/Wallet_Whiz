package com.example.expensestracker.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


import com.example.expensestracker.db_model.Category

import com.example.expensestracker.db_model.ExpensesFb
import com.example.expensestracker.db_model.Recurrence
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.utils.PrefDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
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
import java.time.format.DateTimeFormatter
import java.util.UUID


data class AddScreenState(
    val amount: String = "",
    val recurrence: Recurrence = Recurrence.None,
    val date: LocalDate = LocalDate.now(),
    val time :LocalTime =LocalTime.now(),
    val note: String = "",
    val category: Category? = null,
    val categories: List<Category> = emptyList()
       )

class AddExpensesViewModel : ViewModel() {
       // @RequiresApi(Build.VERSION_CODES.O)
        //@RequiresApi(Build.VERSION_CODES.O)
        private val _uiState = MutableStateFlow(AddScreenState())

      //  @RequiresApi(Build.VERSION_CODES.O)
        val uiState: StateFlow<AddScreenState> = _uiState.asStateFlow()

//        init {
//            _uiState.update { currentState ->
//                currentState.copy(
//                    categories = db.query<Category>().find()
//                )
//            }
//        }




     fun setAmount(amount: String) {
            var parsed = amount.toDoubleOrNull()

            if (amount.isEmpty()) {
                parsed = 0.0
            }

            if (parsed != null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        amount = amount.trim().ifEmpty { "0" },
                    )
                }
            }
        }


           fun setRecurrence(recurrence: Recurrence) {
            _uiState.update { currentState ->
                currentState.copy(
                    recurrence = recurrence,
                )
            }
        }


       // @OptIn(ExperimentalMaterial3Api::class)
        fun setDate(date: LocalDate) {
            _uiState.update { currentState ->
                currentState.copy(
                    date = date,
                )
            }
        }
    fun setTime(time: LocalTime) {
        _uiState.update { currentState ->
            currentState.copy(
                time = time,
            )
        }
    }

      //  @RequiresApi(Build.VERSION_CODES.O)
        fun setNote(note: String) {
            _uiState.update { currentState ->
                currentState.copy(
                    note = note,
                )
            }
        }

    fun loadCategories(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val email = PrefDataStore.getEmail(context)
            if (email != null) {
                try {
                    val snapshot = FirebaseDatabase.getInstance()
                        .getReference("expenses")
                        .child(email)
                        .child("category")
                        .get()
                        .await()

                    val categories = snapshot.children.mapNotNull {
                        it.getValue(Category::class.java)
                    }

                    _uiState.update { currentState ->
                        currentState.copy(categories = categories)
                    }

                } catch (e: Exception) {
                    Log.e("Firebase", "Failed to load categories: ${e.message}")
                }
            }
        }
    }


    fun setCategory(context: Context, target: Category) {

        _uiState.update { currentState ->
            currentState.copy(category = target)
        }

        viewModelScope.launch(Dispatchers.IO) {
            val email = PrefDataStore.getEmail(context)
            if (email != null) {
                try {
                    val snapshot = FirebaseDatabase.getInstance()
                        .getReference("expenses")
                        .child(email)
                        .child("category")
                        .get()
                        .await()

                    for (child in snapshot.children) {
                        val categoryFb = child.getValue(Category::class.java)
                        if (categoryFb?.name == target.name && categoryFb.colorValue == target.colorValue) {
//                            _uiState.update { currentState ->
//                                currentState.copy(category = categoryFb)
//                            }
                            break
                        }

                    }

                } catch (e: Exception) {
                    Log.e("Firebase", "Failed to fetch category: ${e.message}")
                }
            }
        }
    }



//    fun Category.toFirebaseMap(): Map<String, Any> {
//        val colorComponents = this._colorValue.split(",")
//        return mapOf(
//            "id" to this.id.toString(),   // convert ObjectId to String
//            "name" to this.name,
//            "color" to mapOf(
//                "red" to colorComponents[0].toFloat(),
//                "green" to colorComponents[1].toFloat(),
//                "blue" to colorComponents[2].toFloat()
//            )
//        )
//    }


    fun submitExpense(context: Context) {
        if (_uiState.value.category != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val now = LocalTime.now()
                val email = PrefDataStore.getEmail(context)

                if (email != null) {
                    val formattedDate = _uiState.value.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

                    val formateTime = _uiState.value.time
                    val category = _uiState.value.category

                    val expense = ExpensesFb(
                        amount = _uiState.value.amount.toDouble(),
                        recurrence = _uiState.value.recurrence,
                        date =  formattedDate,

                        time = formateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                        note = _uiState.value.note,
                        category = category!!
                    )

                    FirebaseDatabase.getInstance()
                        .getReference("expenses")
                        .child(email)
                        .child("expenseList")
                        .push()
                        .setValue(expense)
                        .addOnSuccessListener {
                            Log.d("Firebase", "Expense saved successfully")
                            resetUiState()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firebase", "Failed to save expense: ${e.message}")
                        }
                }
            }
        }
    }

    fun resetUiState() {
        _uiState.value = AddScreenState(
            amount = "",
            note = "",
            date = LocalDate.now(),
            recurrence = Recurrence.Daily,
            category = null
        )
    }


}
