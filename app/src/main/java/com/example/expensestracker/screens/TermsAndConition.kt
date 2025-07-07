package com.example.expensestracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expensestracker.Components.HeadingTextComponents
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.navigation.SystemBackButtonHandler
import com.example.expensestracker.R

@Composable
fun TermsAndCondition() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(16.dp)
    ) {

        HeadingTextComponents(value = stringResource(id =R.string.terms_and_condn_header))
    }
    SystemBackButtonHandler {
        AppRouter.navigateTo(Screen.SignUp)
    }
}

@Preview
@Composable
fun TermAndCondnPreview(){
    TermsAndCondition()
}



