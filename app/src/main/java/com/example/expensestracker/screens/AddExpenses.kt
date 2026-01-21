package com.example.expensestracker.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.marosseleng.compose.material3.datetimepickers.date.ui.dialog.DatePickerDialog
import com.example.expensestracker.Components.UnStyledTextField
import com.example.expensestracker.R
import com.example.expensestracker.data.AddExpensesViewModel
import com.example.expensestracker.db_model.Recurrence

import com.example.expensestracker.navigation.ui.theme.DividerColor
import com.example.expensestracker.navigation.ui.theme.Shapes
import com.example.expensestracker.navigation.ui.theme.WhiteColor
import com.example.expensestracker.screens.ui.theme.DarculaBg
import com.example.expensestracker.screens.ui.theme.DarculaCard
import com.example.expensestracker.screens.ui.theme.DarculaMuted
import com.example.expensestracker.utils.Utility
import com.example.expensestracker.utils.Utility.backborder

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable

fun AddExpenses(navController: NavController, vm: AddExpensesViewModel = viewModel()) {
    val state by vm.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        vm.loadCategories(context)
    }



    val recurrences = listOf(
        Recurrence.None,
        Recurrence.Daily,
        Recurrence.Weekly,
        Recurrence.Monthly,
        Recurrence.Yearly
    )
    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarHost(hostState = vm.snackbarHostState)
            }
        },
        containerColor = DarculaBg,

    ) {
             innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .backborder(shape = RoundedCornerShape(20.dp))
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                TableRow(
                    label = "Amount" , detailContent = {
                        UnStyledTextField(
                            value = state.amount,
                            onValueChange = vm::setAmount,
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("0", color = Color.White,fontSize = 16.sp, fontFamily = Utility.Poppins) },
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
                    color = DarculaMuted
                )
                TableRow(label = "Recurrence" , detailContent = {
                    var recurrenceMenuOpened by remember {
                        mutableStateOf(false)
                    }
                    TextButton(
                        onClick = { recurrenceMenuOpened = true }, shape = Shapes.large
                    ) {
                        Text(state.recurrence.name, color = Color.White, fontFamily = Utility.Poppins,fontSize = 16.sp,)
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
                    color = DarculaMuted
                )
              //  val date: LocalDate= LocalDate.now()
                var datePickerShowing by remember {
                    mutableStateOf(false)
                }
                TableRow(label = "Date", detailContent = {
                    TextButton(onClick = { datePickerShowing = true }) {
                        Text(state.date.toString(), color = Color.White, fontFamily = Utility.Poppins,fontSize = 16.sp)
                    }
                    if (datePickerShowing) {
                        DatePickerDialog(onDismissRequest = { datePickerShowing = false },
                            onDateChange = { it ->
                                vm.setDate(it)
                                datePickerShowing = false
                            },
                            initialDate = state.date,
                            title = { Text("Select date", fontFamily = Utility.Poppins,fontSize = 16.sp, style = MaterialTheme.typography.titleLarge) })
                    }
                })

                HorizontalDivider(


                    modifier = Modifier.padding(start = 16.dp),
                    thickness = 1.dp,
                    color = DarculaMuted
                )
                TableRow(label = "Note", detailContent = {
                    UnStyledTextField(
                        value = state.note,
                        placeholder = { Text("Leave some notes", color = Color.White, fontFamily = Utility.Poppins) },
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
                    color = DarculaMuted
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
                            color = state.category?.toColor() ?: Color.White,
                            fontFamily = Utility.Poppins,
                            fontSize = 16.sp,
                        )
                        DropdownMenu(expanded = categoriesMenuOpened,

                            onDismissRequest = { categoriesMenuOpened = false }) {
                            state.categories?.forEach { category ->
                                DropdownMenuItem(text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            modifier = Modifier.size(10.dp),
                                            shape = CircleShape,
                                            color = category.toColor()
                                        ) {}
                                        Text(
                                            category.name, modifier = Modifier.padding(start = 8.dp)
                                        )
                                                                                                                                                                    }
                                }, onClick = {
                                    vm.setCategory(context,category)
                                    categoriesMenuOpened = false
                                })
                            }
                        }
                    }
                })
            }
            Button(
                onClick = { vm.submitExpense(context) },

                modifier = Modifier.padding(15.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .backborder(RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarculaCard,

                )
              //  shape = Shapes.large,
             //   enabled = state.category != null
            ) {
                Text("Submit expense",color = Color.White,
                    fontFamily = Utility.Poppins,
                    fontSize = 16.sp,)
            }
        }
    }


}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun AddExpensesPreview(){
   // AddExpenses {  val navController = rememberNavController()
      //  AddExpenses(rememberNavController())
}