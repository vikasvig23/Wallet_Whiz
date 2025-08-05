package com.example.expensestracker.db_model

import android.content.Context
import android.util.Log
import com.example.expensestracker.utils.PrefDataStore
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class ExpensesFb(
    var amount: Double = 0.0,
    var _recurrenceName: String = "None",
    var _dateValue: String = "",
    var time: String = "",
    var note: String = "",
    var category: Category? = null
) {
    // Firebase-compatible constructor
    constructor(
        amount: Double,
        recurrence: Recurrence,
        date: String,
        time: String,
        note: String,
        category: Category
    ) : this(
        amount = amount,
        _recurrenceName = recurrence.name,
        _dateValue = date,
        time = time,
        note = note,
        category = category
    )
}
fun ExpensesFb.getParsedDate(): LocalDate {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
    return LocalDate.parse(this._dateValue, formatter)
}
//fun ExpensesFb.getParsedDate(): LocalDate = LocalDate.parse(this._dateValue)
fun ExpensesFb.getParsedRecurrence(): Recurrence = this._recurrenceName.toRecurrence()

data class DayExpenses(
    val expenses: MutableList<ExpensesFb>,
    var total: Double,
)

enum class FilterMode {
    TODAY, WEEK, MONTH, YEAR
}


fun filterExpensesByRange(
    expenses: List<ExpensesFb>,
    mode: FilterMode
): Map<LocalDate, DayExpenses> {
    val today = LocalDate.now()

    val filtered = when (mode) {
        FilterMode.TODAY -> expenses.filter {
            it.getParsedDate().isEqual(today)
        }
        FilterMode.WEEK -> {
            val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)
            val endOfWeek = today.with(java.time.DayOfWeek.SUNDAY)
            expenses.filter {
                val date = it.getParsedDate()
                date in startOfWeek..endOfWeek
            }
        }
        FilterMode.MONTH -> {
            val thisMonth = today.monthValue
            val thisYear = today.year
            expenses.filter {
                val date = it.getParsedDate()
                date.monthValue == thisMonth && date.year == thisYear
            }
        }
        FilterMode.YEAR -> {
            val thisYear = today.year
            expenses.filter {
                it.getParsedDate().year == thisYear
            }
        }
    }

    return filtered.groupBy { it.getParsedDate() }
        .mapValues { entry ->
            DayExpenses(
                expenses = entry.value.toMutableList(),
                total = entry.value.sumOf { it.amount }
            )
        }
}


suspend fun fetchGroupedExpensesFromFirebase(
    context: Context,
    filterMode: FilterMode
): Map<LocalDate, DayExpenses> {
    val savedEmail = PrefDataStore.getEmail(context) ?: return emptyMap()
    val snapshot = FirebaseDatabase.getInstance()
        .getReference("expenses")
        .child(savedEmail)
        .child("expenseList")
        .get()
        .await()

    val expenseList = mutableListOf<ExpensesFb>()

    snapshot.children.forEach { child ->
        val expense = child.getValue(ExpensesFb::class.java)
        expense?.let { expenseList.add(it) }
    }

    return filterExpensesByRange(expenseList, filterMode)
}



private val expeneData = MutableStateFlow(String())
val _expenseData: StateFlow<String> = expeneData.asStateFlow()

suspend fun fetchGroupByDayFromFirebase(context: Context) {
    val savedEmail = PrefDataStore.getEmail(context)
    Log.d("SecureEmail", savedEmail ?: "No email found")

    if (savedEmail != null) {
        try {
            val expenseList = FirebaseDatabase.getInstance()
                .getReference("expenses")
                .child(savedEmail)
                .child("expenseList")
                .get()
                .await()

            val amount = expenseList.child("amount").getValue(String::class.java)
            val category = expenseList.child("category").getValue(String::class.java)
            val date = expenseList.child("_dateValue").getValue(String::class.java)
            val note = expenseList.child("note").getValue(String::class.java)
            val recurrence = expenseList.child("_recurrenceName").getValue(String::class.java)
            val time = expenseList.child("time").getValue(String::class.java)

            amount?.let {
                expeneData.value =it
            }
            category?.let {
                expeneData.value =it
            }
            date?.let {
                expeneData.value =it
            }
            note?.let {
                expeneData.value =it
            }
            recurrence?.let {
                expeneData.value =it
            }
            time?.let {
                expeneData.value =it
            }
        } catch (e: Exception) {
            Log.e("Firebase", "Failed to fetch username: ${e.message}")
        }
    }
}


fun List<ExpensesFb>.groupedByDay(): Map<LocalDate, DayExpenses> {
    val dataMap: MutableMap<LocalDate, DayExpenses> = mutableMapOf()

    for (expense in this) {
        val date = expense.getParsedDate()

        if (dataMap[date] == null) {
            dataMap[date] = DayExpenses(mutableListOf(), 0.0)
        }

        dataMap[date]!!.expenses.add(expense)
        dataMap[date]!!.total += expense.amount
    }

    dataMap.values.forEach { it.expenses.sortBy { e -> e.getParsedDate() } }

    return dataMap.toSortedMap(compareByDescending { it })
}

fun List<ExpensesFb>.groupedByDayOfWeek(): Map<String, DayExpenses> {
    val dataMap: MutableMap<String, DayExpenses> = mutableMapOf()

    for (expense in this) {
        val dayOfWeek = expense.getParsedDate().dayOfWeek.name

        if (dataMap[dayOfWeek] == null) {
            dataMap[dayOfWeek] = DayExpenses(mutableListOf(), 0.0)
        }

        dataMap[dayOfWeek]!!.expenses.add(expense)
        dataMap[dayOfWeek]!!.total += expense.amount
    }

    return dataMap.toSortedMap(compareByDescending { it })
}

fun List<ExpensesFb>.groupedByDayOfMonth(): Map<Int, DayExpenses> {
    val dataMap: MutableMap<Int, DayExpenses> = mutableMapOf()

    for (expense in this) {
        val dayOfMonth = expense.getParsedDate().dayOfMonth

        if (dataMap[dayOfMonth] == null) {
            dataMap[dayOfMonth] = DayExpenses(mutableListOf(), 0.0)
        }

        dataMap[dayOfMonth]!!.expenses.add(expense)
        dataMap[dayOfMonth]!!.total += expense.amount
    }

    return dataMap.toSortedMap(compareByDescending { it })
}

fun List<ExpensesFb>.groupedByMonth(): Map<String, DayExpenses> {
    val dataMap: MutableMap<String, DayExpenses> = mutableMapOf()

    for (expense in this) {
        val month = expense.getParsedDate().month.name

        if (dataMap[month] == null) {
            dataMap[month] = DayExpenses(mutableListOf(), 0.0)
        }

        dataMap[month]!!.expenses.add(expense)
        dataMap[month]!!.total += expense.amount
    }

    return dataMap.toSortedMap(compareByDescending { it })
}
