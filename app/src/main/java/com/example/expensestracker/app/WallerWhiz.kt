package com.example.expensestracker.app

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.expensestracker.data.HomeViewModel

import com.example.expensestracker.data.currency
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.screens.AddCurrency
import com.example.expensestracker.screens.ForgotPass
import com.example.expensestracker.screens.HomeScreen
import com.example.expensestracker.screens.LoginScreen
import com.example.expensestracker.screens.SignUpScreen
import com.example.expensestracker.screens.Splash
import com.example.expensestracker.screens.TermsAndCondition
//import com.example.expensestracker.screens.Categories

//@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WalletWhiz(context: Context,homeViewModel: HomeViewModel=viewModel()) {
    homeViewModel.checkForActiveSession()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.LightGray
    ) {

        if(homeViewModel.isUserLoggedIn.value==true){
            AppRouter.navigateTo(Screen.HomeScreen)
        }
        Crossfade(targetState = AppRouter.currentScreen, label = "") { currentState ->
            val navController = rememberNavController()
            when (currentState.value) {

                is Screen.SignUp -> {
                    SignUpScreen()
                }

                is Screen.TermsAndCondnScreen -> {
                    TermsAndCondition()
                }
                is Screen.LoginScreen -> {
                    LoginScreen()
                }
                is Screen.HomeScreen->{
                    HomeScreen(context)
                }
                is Screen.SplashScreen->{
                    Splash()
                }
                is Screen.ForgotPass->{
                    ForgotPass()
                }
              /*  is Screen.CurrencyList->{
                   // val navController = rememberNavController()
                    AddCurrency(onCurrencySelected = {}, navController = navController,
                        currency = currency
                    )
                }

               */




            }
        }
    }
}

