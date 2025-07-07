package com.example.expensestracker.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensestracker.data.AddExpensesViewModel
import com.example.expensestracker.data.Currencies
import com.example.expensestracker.data.CurrencyCard
import com.example.expensestracker.data.SignUpUIEvent
import com.example.expensestracker.data.currency
import com.example.expensestracker.db_model.SharedPreferencesManager
import com.example.expensestracker.ui.theme.Shapes
import com.example.expensestracker.ui.theme.TopAppBarBackground


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyList(currency: List<Currencies>,onCurrencySelected: (Currencies) -> Unit) {
    var selectedItem by remember { mutableStateOf(false) }
    //  val state by vm.uiState.collectAsState()
    var selectedCurrency by remember { mutableStateOf("") }
    LazyColumn(
    ) {


        items(currency) { item ->
            CurrencyCard(
                item.country,
                item.currencyCode){
                onCurrencySelected(item)}
                //viewModel = MainViewModel(currencyPref = viewModel())

        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCurrency(navController: NavController){
    val context = LocalContext.current
   // val curenci: List<Currencies>
    var selectedCurrency by remember { mutableStateOf("") }
    var isCurrencySelected by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(title = { Text("Select Currency") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = TopAppBarBackground
                ),
                navigationIcon = {
                    Surface(
                        onClick = navController::popBackStack,
                        color = Color.Transparent,
                    ) {
                        Row(modifier = Modifier.padding(vertical = 10.dp)) {
                            Icon(
                                Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                contentDescription = "Settings"
                            )
                            Text("Settings")
                        }
                    }
                })
        },

      content = {innerPadding->
          Column(
              modifier = Modifier
                  .fillMaxSize()
                  .padding(16.dp)
                  .padding(innerPadding)
          ) {
              CurrencyList(currency) { currency ->
                  selectedCurrency = currency.currencyCode
                  isCurrencySelected =true
                  SharedPreferencesManager.saveSelectedCurrency(context, currency)
                  Toast.makeText(context, "Selected currency: ${currency.currencyCode}", Toast.LENGTH_SHORT)
                      .show()
              }
              Spacer(modifier = Modifier.height(16.dp))
              Expenses(navController = rememberNavController())

              if(isCurrencySelected){
                  Button(onClick = {
                      navController.navigate("Expenses")
                  }) {

                  }
              }
          }
      }
        )
    }

@Preview
@Composable
fun PreviewCurrency(){
    //val onCurrencySelected:String
  AddCurrency(navController = rememberNavController())
}