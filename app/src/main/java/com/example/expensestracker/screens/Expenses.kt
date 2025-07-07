package com.example.expensestracker.screens

import android.content.Context.MODE_PRIVATE
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.example.expensestracker.data.ExpensesList
import com.example.expensestracker.data.ExpensesViewModel
import com.example.expensestracker.data.RecurrenceTrigger
import com.example.expensestracker.data.RegistrationUIState
import com.example.expensestracker.data.SignUpUIEvent
import com.example.expensestracker.db_model.Recurrence
import com.example.expensestracker.db_model.SharedPreferencesManager

import com.example.expensestracker.ui.theme.LabelSecondary
import java.text.DecimalFormat

/*fun Curnt(viewModel:MainViewModel= viewModel()) {
    val currency = viewModel.currencyCode.collectAsState()
    Row(modifier = Modifier.padding(vertical = 32.dp)) {
        Text(
            text = currency.value,
            style = MaterialTheme.typography.bodyMedium,
            color = LabelSecondary,
            modifier = Modifier.padding(end = 4.dp, top = 4.dp)
        )
    }
}

 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
//val viewModel:MainViewModel by viewModels()
fun Expenses(
    navController: NavController,
    vm: ExpensesViewModel = viewModel()
) {
    val context = LocalContext.current
    val selectedCurrency = SharedPreferencesManager.getSelectedCurrency(context) ?: "None"
    val sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val savedName = sharedPreferences.getString("name", "")


    val recurrences = listOf(
        Recurrence.Daily,
        Recurrence.Weekly,
        Recurrence.Monthly,
        Recurrence.Yearly
    )
   // val currency = viewModel.currencyCode.collectAsState()


    val state by vm.uiState.collectAsState()
    var recurrenceMenuOpened by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                //  modifier = Modifier.height(90.dp),
                colors = topAppBarColors(
                    containerColor = Color.LightGray,
                    titleContentColor = Color.Black,
                ),
                title = {

                    Text("Hii $savedName")
                },
                //  scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Total for:",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    RecurrenceTrigger(
                        state.recurrence.target ?: Recurrence.None.target,
                        onClick = { recurrenceMenuOpened = !recurrenceMenuOpened },
                        modifier = Modifier.padding(start = 16.dp)
                    )
                    DropdownMenu(expanded = recurrenceMenuOpened,
                        onDismissRequest = { recurrenceMenuOpened = false }) {
                        recurrences.forEach { recurrence ->
                            DropdownMenuItem(text = { Text(recurrence.target) }, onClick = {
                                vm.setRecurrence(recurrence)
                                recurrenceMenuOpened = false
                            })
                        }
                    }
                }
                Row(modifier = Modifier.padding(vertical = 32.dp)) {
                    Text(
                        text = " $selectedCurrency" ,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LabelSecondary,
                        modifier = Modifier.padding(end = 4.dp, top = 4.dp)
                    )
                    Text(
                        DecimalFormat("0.#").format(state.sumTotal),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                ExpensesList(
                    expenses = state.expenses,
                    modifier = Modifier
                        .weight(0.4f)
                        .padding(bottom = 70.dp)
                        .verticalScroll(
                            rememberScrollState()
                        )
                )
            }
        }
    )
}

    @Preview
    @Composable
    fun ExpensesPreview() {
        //val viewModel = MainViewModel("cureency")
       // val viewModel:MainViewModel by viewModel()
        //val name=name
       // Expenses(navController = rememberNavController(), name = SignUpUIEvent.NAmeChanged())

    }