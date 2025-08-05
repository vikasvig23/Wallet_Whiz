package com.example.expensestracker.screens

//noinspection SuspiciousImport
//import android.R

import CustomToast
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensestracker.Components.AnimatedMessageBar
import com.example.expensestracker.Components.AnimatedSwitchButton

import com.example.expensestracker.Components.CheckboxComponent
import com.example.expensestracker.Components.ClickableLoginTextComponent
import com.example.expensestracker.Components.DividerTextComponent
import com.example.expensestracker.Components.HeadingTextComponents
import com.example.expensestracker.Components.MyPasswordField
import com.example.expensestracker.Components.MyTextField
import com.example.expensestracker.Components.NormalTextComponents
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.R
import com.example.expensestracker.data.RegistrationUIState
import com.example.expensestracker.data.SignUpViewModel
import com.example.expensestracker.data.SignUpUIEvent
import com.example.expensestracker.utils.Utility


@Composable
fun SignUpScreen(loginViewModel: SignUpViewModel = viewModel()) {

    val state by loginViewModel.state.collectAsState()
    val context = LocalContext.current
    val showMessageBar by loginViewModel.showMessageBar
    val messageText by loginViewModel.messageText
    val isSuccess by loginViewModel.isSuccessMessage


    Box(
        modifier = Modifier.fillMaxWidth().zIndex(1f),
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
                    .fillMaxWidth()
                    .background(Color.White)

            ) {
                Image(
                    painter = painterResource(id = R.drawable.wallet_whiz),
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.CenterHorizontally)


                )

                //NormalTextComponents(value = stringResource(R.string.hello))
                HeadingTextComponents(value = stringResource(R.string.create))
                Spacer(modifier = Modifier.height(15.dp))

                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {


                    MyTextField(
                        labelValue = stringResource(R.string.first_name),
                        painterResource = painterResource(R.drawable.profile),
                        value = state.name,
                        onTextSelected = { name->
                            loginViewModel.updateField("name", name)  },
                        errorStatus = state.touched && state.name.length < 5
                    )

                    MyTextField(
                        labelValue = stringResource(R.string.email),
                        painterResource = painterResource(R.drawable.mail),
                        value = state.email,
                        onTextSelected = { email ->
                            loginViewModel.updateField("email", email)
                          },
                        errorStatus = state.touched && !Utility.isValidEmail(state.email)
                    )




                    MyPasswordField(
                        labelValue = stringResource(R.string.pass),
                        painterResource = painterResource(R.drawable.lock),
                        onTextSelected = {
                             password ->
                                loginViewModel.updateField("password", password)


                        },
                        errorStatus = state.touched && state.password.length < 8
                    )

                    Spacer(modifier = Modifier.heightIn(35.dp))


                        AnimatedSwitchButton(
                        text = stringResource(R.string.register),
                        isEnabled = true,
                        onClick = {
                            loginViewModel.createUserInFirebase(context)
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    DividerTextComponent()

                    ClickableLoginTextComponent(
                        tryingToLogin = true,
                        onTextSelected = {
                            AppRouter.navigateTo(Screen.LoginScreen)
                        }
                    )
                }
            }
        }

        if (loginViewModel.signUpInProgress.value) {
            CircularProgressIndicator()
        }
    }
}

fun saveNameToSharedPreferences(context: Context, name: String) {
    val sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    with(sharedPreferences.edit()) {
        putString("Name",name)
        apply()
    }
}

//@Preview
//@Composable
//fun DefaultPreviewOfSignUpScreen() {
//    SignUpScreen()
//}

