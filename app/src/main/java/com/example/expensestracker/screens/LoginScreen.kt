package com.example.expensestracker.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensestracker.Components.ButtonComponent
import com.example.expensestracker.Components.ClickableLoginTextComponent
import com.example.expensestracker.Components.DividerTextComponent
import com.example.expensestracker.Components.HeadingTextComponents
import com.example.expensestracker.Components.MyPasswordField
import com.example.expensestracker.Components.MyTextField
import com.example.expensestracker.Components.NormalTextComponents
import com.example.expensestracker.Components.UnderlinedTextComponents
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.R
import com.example.expensestracker.data.LoginUIEvent
import com.example.expensestracker.data.LoginViewModel

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                .padding(28.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray)
                    .padding(0.dp, 20.dp)
            ) {

                NormalTextComponents(value = stringResource(id = R.string.hello))
                HeadingTextComponents(value = stringResource(id = R.string.welcome))
                Spacer(modifier = Modifier.padding(20.dp))
                MyTextField(
                    labelValue = stringResource(id = R.string.email),
                    painterResource = painterResource(id = R.drawable.mail),
                    onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.EmailChange(it))

                    },
                    errorStatus = loginViewModel.loginUIState.value.emailError)



                MyPasswordField(labelValue = stringResource(id = R.string.pass),
                    painterResource = painterResource(id = R.drawable.lock), onTextSelected = {
                        loginViewModel.onEvent(LoginUIEvent.passwordChanged(it))

                    },
                    errorStatus = loginViewModel.loginUIState.value.emailError)

                Spacer(modifier = Modifier.padding(20.dp))
                UnderlinedTextComponents(value = stringResource(R.string.forgot_pass))
                Spacer(modifier = Modifier.padding(20.dp))
                ButtonComponent(
                    value = stringResource(id = R.string.login),
                    onButtonClicked = {
                                      loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)

                    },
                    isEnabled = loginViewModel.allValidationsPassed.value

                )
                Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()

                ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                    AppRouter.navigateTo(Screen.SignUp)
                })

            }
        }

        if (loginViewModel.loginInProgress.value) {
            CircularProgressIndicator()

        }
    }
}

@Preview
@Composable
fun LoginPreview(){
    LoginScreen()
}