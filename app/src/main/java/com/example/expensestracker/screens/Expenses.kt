package com.example.expensestracker.screens

import android.content.Context.MODE_PRIVATE
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.liveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensestracker.R

import com.example.expensestracker.data.ExpensesList
import com.example.expensestracker.data.ExpensesViewModel
import com.example.expensestracker.data.RecurrenceTrigger
import com.example.expensestracker.data.RegistrationUIState
import com.example.expensestracker.data.SignUpUIEvent
import com.example.expensestracker.db_model.Recurrence
import com.example.expensestracker.db_model.SharedPreferencesManager

import com.example.expensestracker.navigation.ui.theme.LabelSecondary
import com.example.expensestracker.screens.ui.theme.DarculaBg
import com.example.expensestracker.screens.ui.theme.DarculaCard
import com.example.expensestracker.screens.ui.theme.DarculaMuted
import com.example.expensestracker.screens.ui.theme.DarculaPrimary
import com.example.expensestracker.screens.ui.theme.DarculaText
import com.example.expensestracker.utils.PrefDataStore
import com.example.expensestracker.utils.Utility
import com.example.expensestracker.utils.Utility.backborder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope

import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.coroutines.coroutineContext

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
    //val savedName = sharedPreferences.getString("name", "")
    var userName by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()


    val expenseData = vm._expenseData.collectAsState()
    LaunchedEffect(Unit){
        val name = PrefDataStore.getData(context,PrefDataStore.NAME)
        Log.d("><", "$name")
        userName =name ?: ""
    }

    val savedName by vm._name.collectAsState()
//    LaunchedEffect(Unit) {
//        vm.setRecurrence(context,recurrence) }


    LaunchedEffect(Unit) {
         vm.fetchUserNameFromFirebase(context)
    }


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
    val infiniteTransition = rememberInfiniteTransition()

    val bar1Height by infiniteTransition.animateFloat(
        initialValue = 20f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val bar2Height by infiniteTransition.animateFloat(
        initialValue = 40f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val bar3Height by infiniteTransition.animateFloat(
        initialValue = 60f,
        targetValue = 120f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Scaffold(

        containerColor = DarculaBg,
        topBar = {

            Row(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement =  Arrangement.SpaceEvenly


            ) {
            Box(

                modifier = Modifier

                    .width(250.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .backborder(shape = RoundedCornerShape(30.dp))
                    .padding(16.dp),

                ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,

                    ) {
                    Text(
                        "Welcome,", fontSize = 20.sp,
                        fontFamily = Utility.Poppins, color = DarculaMuted
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        "$userName", fontSize = 30.sp, color = DarculaText,
                                fontFamily = Utility.Poppins
                    )

                }
            }

            Box(
                modifier = Modifier
                    .padding(top = 5.dp)
                     .size(width = 60.dp, height = 115.dp)
                    .clip(RoundedCornerShape(40.dp))
                    .backborder(shape = RoundedCornerShape(40.dp)),
                contentAlignment = Alignment.BottomCenter
            ) {
                Row(
                    modifier = Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Bar(height = bar1Height.dp)
                    Bar(height = bar2Height.dp)
                    Bar(height = bar3Height.dp)
                }
            }
        }
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
                Row(modifier = Modifier.padding(top = 25.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Expenses for:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarculaMuted,
                        fontFamily = Utility.Poppins,
                    )
                    RecurrenceTrigger(
                        state.recurrence.target,
                        placeholder = "Select",
                        onClick = { recurrenceMenuOpened = !recurrenceMenuOpened },
                        modifier = Modifier.padding(start = 16.dp) .clip(RoundedCornerShape(10.dp)).backborder(shape = RoundedCornerShape(10.dp))
                    )
                    DropdownMenu(expanded = recurrenceMenuOpened,
                        onDismissRequest = { recurrenceMenuOpened = false }) {
                        recurrences.forEach { recurrence ->
                            DropdownMenuItem(text = { Text(recurrence.target) },
                                onClick = {
                               coroutineScope.launch { vm.setRecurrence(context,recurrence) }

                                recurrenceMenuOpened = false
                            })
                        }
                    }
                }

                Row(modifier = Modifier.padding(vertical = 15.dp)) {
                    Text(
                        text = " $selectedCurrency " ,
                        style = MaterialTheme.typography.titleLarge,
                        color = DarculaMuted,
                        fontFamily = Utility.Poppins,

                    )
                    Text(
                        DecimalFormat("0.#").format(state.sumTotal),
                        style = MaterialTheme.typography.titleLarge,
                        color = DarculaText,
                        fontFamily = Utility.Poppins,
                    )
                }
//                into the world
//                seret life of walter mity
//                wild
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(365.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .backborder(
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp)
                ) {
                    ExpensesList(
                        expenses = state.expenses,
                        modifier = Modifier
                            .padding(bottom = 70.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }


            }
        }
    )
}

@Composable
fun Bar(height: Dp) {
    Box(
        modifier = Modifier
            .width(10.dp)
            .height(height)
            .clip(RoundedCornerShape(3.dp))
            .background(DarculaMuted)
    )
}

    @Preview
    @Composable
    fun ExpensesPreview() {
//        val viewModel = MainViewModel("cureency")
//        val viewModel:MainViewModel by viewModel()
//        val name=name
    //    Expenses(navController = rememberNavController(), vm =ExpensesViewModel())

    }