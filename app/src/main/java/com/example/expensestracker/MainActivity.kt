package com.example.expensestracker


import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.*

import androidx.compose.ui.tooling.preview.Preview
import com.example.expensestracker.app.WalletWhiz
import com.example.expensestracker.screens.SignUpScreen
//import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
           WalletWhiz()



        }
    }
}
@Preview
@Composable
fun DefaultPreview(){
   WalletWhiz()
    SignUpScreen()

}

