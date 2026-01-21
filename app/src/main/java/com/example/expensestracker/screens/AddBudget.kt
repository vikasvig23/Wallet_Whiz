package com.example.expensestracker.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expensestracker.Components.MyTextField
import com.example.expensestracker.R
import com.example.expensestracker.data.AddBudgetVM
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.screens.ui.theme.DarculaBg
import com.example.expensestracker.screens.ui.theme.DarculaCard
import com.example.expensestracker.screens.ui.theme.DarculaMuted
import com.example.expensestracker.screens.ui.theme.DarculaText
import com.example.expensestracker.utils.Poppins
import com.example.expensestracker.utils.Utility

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudget(viewModel:AddBudgetVM =  viewModel()){

    val state = viewModel.uiState.collectAsState().value
    val context = LocalContext.current


    Scaffold(
        containerColor = DarculaBg,
        topBar = {
            MediumTopAppBar(

                colors = topAppBarColors(
                    containerColor = DarculaCard,
                    titleContentColor = DarculaText,
                ),

                actions = {
                    Surface(

                        color = DarculaCard

                    ) {
                        Row(modifier = Modifier.padding(10.dp),
                           ) {

                            Text("Skip",fontFamily = Poppins,
                                modifier = Modifier.clickable{ AppRouter.navigateTo(Screen.HomeScreen)}, style = TextStyle(fontSize = 15.sp, fontFamily = Poppins, color = DarculaMuted ))
                        }
                    }

                },
                title = {
                    Text("Budget", fontSize = 25.sp, fontFamily = Poppins )
                }

            )
        }
    ){ innerPadding->

        Column(modifier = Modifier.padding(innerPadding)
            .padding(16.dp)){

            Card(modifier = Modifier.fillMaxWidth().padding(20.dp),
                elevation = CardDefaults.cardElevation(5.dp),
                colors = CardDefaults.cardColors(
                    containerColor =  DarculaCard)
            ){

                Column(
                    modifier = Modifier.padding(20.dp)
                ){
                    Text(
                        text = "Set Monthly Budget",
                        fontSize = 18.sp,
                        fontFamily = Poppins,
                        color = DarculaText,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "This helps you to track your expenses better and avoid overspending.",
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        color = DarculaMuted
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    MyTextField(
                        labelValue = stringResource(R.string.enter_budget),
                        painterResource = painterResource(R.drawable.baseline_account_balance_wallet_24),
                        value = state.budgetText,
                        onTextSelected = { value ->
                            viewModel.setBudget(value)
                        },
                        errorStatus = state.isError
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            viewModel.valid(context)

                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("DONE", fontSize = 18.sp, fontFamily = Poppins)
                    }

                }

            }

        }


    }

}


@Composable
@Preview
fun BudgetPreviw(){
    AddBudget(viewModel = AddBudgetVM())
}