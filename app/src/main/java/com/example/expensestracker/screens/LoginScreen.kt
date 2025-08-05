package com.example.expensestracker.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensestracker.Components.AnimatedMessageBar
import com.example.expensestracker.Components.AnimatedSwitchButton
import com.example.expensestracker.Components.ClickableLoginTextComponent
import com.example.expensestracker.Components.DividerTextComponent
import com.example.expensestracker.Components.HeadingTextComponents
import com.example.expensestracker.Components.MyPasswordField
import com.example.expensestracker.Components.MyTextField
import com.example.expensestracker.Components.UnderlinedTextComponents
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.R
import com.example.expensestracker.data.LoginViewModel
import com.example.expensestracker.utils.Utility

@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel()) {

    val state by loginViewModel.state.collectAsState()
    val context = LocalContext.current
    val showMessageBar by loginViewModel.showMessageBar
    val messageText by loginViewModel.messageText
    val isSuccess by loginViewModel.isSuccessMessage


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f),
        contentAlignment = Alignment.Center
    ) {
        AnimatedMessageBar(
            message = messageText,
            isSuccess = isSuccess,
            show = showMessageBar,
            onDismiss = { loginViewModel.showMessageBar.value = false }
        )


        LaunchedEffect(showMessageBar) {
            if (showMessageBar) {
                Toast.makeText(context, messageText, Toast.LENGTH_SHORT).show()
            }
        }

        Surface(
            color = Color.White,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(start = 28.dp, end = 28.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)

            ) {

                Image(
                    painter = painterResource(id = R.drawable.wallet_whiz),
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.CenterHorizontally)

                )

                HeadingTextComponents(value = stringResource(id = R.string.welcome))
                Spacer(modifier = Modifier.padding(10.dp))
                MyTextField(
                    labelValue = stringResource(id = R.string.email),
                    painterResource = painterResource(id = R.drawable.mail),
                    value = state.email,
                    onTextSelected = { mail ->
                        loginViewModel.updateField("email", mail)
                    },
                    errorStatus = state.touched && !Utility.isValidEmail(state.email)
                )


                MyPasswordField(
                    labelValue = stringResource(R.string.pass),
                    painterResource = painterResource(R.drawable.lock),
                    onTextSelected = { password ->
                        loginViewModel.updateField("password", password)


                    },
                    errorStatus = state.touched && state.password.length < 8
                )

                Spacer(modifier = Modifier.padding(10.dp))
                UnderlinedTextComponents(
                    value = stringResource(R.string.forgot_pass),
                    onTextSelected = { AppRouter.navigateTo(Screen.ForgotPass) }
                )

                AnimatedSwitchButton(
                    text = stringResource(R.string.login),
                    isEnabled = true,
                    onClick = {
                        loginViewModel.login(context)


                    }
                )
                Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()

                ClickableLoginTextComponent(tryingToLogin = false, onTextSelected = {
                    AppRouter.navigateTo(Screen.SignUp)
                })



                if (loginViewModel.loginInProgress.value) {
                    CircularProgressIndicator()

                }
            }
        }
    }
}

@Preview
@Composable
fun LoginPreview(){
    LoginScreen()
}