package com.example.expensestracker.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.example.expensestracker.Components.UnStyledTextField
import com.example.expensestracker.data.AddExpensesViewModel
import com.example.expensestracker.db_model.Recurrence

import com.example.expensestracker.ui.theme.DividerColor
import com.example.expensestracker.ui.theme.Shapes

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable

fun AddExpenses(navController: NavController, vm: AddExpensesViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()


    val recurrences = listOf(
        Recurrence.None,
        Recurrence.Daily,
        Recurrence.Weekly,
        Recurrence.Monthly,
        Recurrence.Yearly
    )
    Scaffold(
        topBar = {
            MediumTopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.LightGray,
                    titleContentColor = Color.Black,
                ), title = {
                    Text("Add Expenses")
                })
        }

    ) {
             innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(Shapes.large)
                    .background(color = Color.LightGray)
                    .fillMaxWidth()
            ) {
                TableRow(
                    label = "Amount" , detailContent = {
                        UnStyledTextField(
                            value = state.amount,
                            onValueChange = vm::setAmount,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("0") },
                            arrangement = Arrangement.End,
                            maxLines = 1,
                         //   colors=Color.Black,
                            textStyle = TextStyle(
                                textAlign = TextAlign.Right,
                            ),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                            )
                        )
                    }

                    )

                HorizontalDivider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DividerColor
                )
                TableRow(label = "Recurrence" , detailContent = {
                    var recurrenceMenuOpened by remember {
                        mutableStateOf(false)
                    }
                    TextButton(
                        onClick = { recurrenceMenuOpened = true }, shape = Shapes.large
                    ) {
                        Text(state.recurrence.name)
                        DropdownMenu(expanded = recurrenceMenuOpened,
                          onDismissRequest = { recurrenceMenuOpened = false }) {
                            recurrences.forEach { recurrence ->
                                DropdownMenuItem(text = { Text(recurrence.name) }, onClick = {
                                    vm.setRecurrence(recurrence)
                                    recurrenceMenuOpened = false
                                })
                            }
                        }


                    }
                })
                HorizontalDivider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DividerColor
                )
              //  val date: LocalDate= LocalDate.now()
                var datePickerShowing by remember {
                    mutableStateOf(false)
                }
                TableRow(label = "Date", detailContent = {
                    TextButton(onClick = { datePickerShowing = true }) {
                        Text(state.date.toString())
                    }
                    if (datePickerShowing) {
                        DatePickerDialog(onDismissRequest = { datePickerShowing = false },
                            onDateChange = { it ->
                                vm.setDate(it)
                                datePickerShowing = false
                            },
                            initialDate = state.date,
                            title = { Text("Select date", style = MaterialTheme.typography.titleLarge) })
                    }
                })



                HorizontalDivider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DividerColor
                )
                TableRow(label = "Note", detailContent = {
                    UnStyledTextField(
                        value = state.note,
                        placeholder = { Text("Leave some notes") },
                        arrangement = Arrangement.End,
                        onValueChange = vm::setNote,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(
                            textAlign = TextAlign.Right,
                        ),
                    )
                })
                HorizontalDivider(
                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DividerColor
                )
             //   val categories= listOf("Groceries","Bills","Dairy","Take Out")
                TableRow(label = "Category", detailContent = {
                    var categoriesMenuOpened by remember {
                        mutableStateOf(false)
                    }
                    TextButton(
                        onClick = { categoriesMenuOpened = true }, shape = Shapes.large
                    ) {
                        Text(
                            state.category?.name ?: "Select a category first",

                            color = state.category?.color ?: Color.White
                        )
                        DropdownMenu(expanded = categoriesMenuOpened,
                            onDismissRequest = { categoriesMenuOpened = false }) {
                            state.categories?.forEach { category ->
                                DropdownMenuItem(text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            modifier = Modifier.size(10.dp),
                                            shape = CircleShape,
                                            color = category.color
                                        ) {}
                                        Text(
                                            category.name, modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                }, onClick = {
                                    vm.setCategory(category)
                                    categoriesMenuOpened = false
                                })
                            }
                        }
                    }
                })
            }
            Button(
                onClick = vm::submitExpense,
                modifier = Modifier.padding(16.dp),
                shape = Shapes.large,
                enabled = state.category != null
            ) {
                Text("Submit expense")
            }
        }
    }


}






@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AddExpensesPreview(){
   // AddExpenses {  val navController = rememberNavController()
        AddExpenses(rememberNavController())
}