package com.example.expensestracker.screens

//noinspection SuspiciousImport
//import android.R

import CustomToast
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.provider.Settings.Global.putString
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentRecomposeScope
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



@Composable
fun SignUpScreen(loginViewModel: SignUpViewModel= viewModel()){


    val showToast = loginViewModel.showMessageBar.value
    val toastMessage = loginViewModel.messageText.value
    val isSuccess = loginViewModel.isSuccessMessage.value

// Show the toast



    val context = LocalContext.current

    Box(modifier=Modifier.fillMaxSize() .zIndex(2f), contentAlignment = Alignment.Center ) {
        AnimatedMessageBar(
            message = loginViewModel.messageText.value,
            isSuccess = loginViewModel.isSuccessMessage.value,
            show = loginViewModel.showMessageBar.value,
            onDismiss = { loginViewModel.showMessageBar.value = false }
        )

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
                NormalTextComponents(value = stringResource(id = com.example.expensestracker.R.string.hello))

                HeadingTextComponents(value = stringResource(id = com.example.expensestracker.R.string.create))

                Spacer(modifier = Modifier.height(30.dp))

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ){
                MyTextField(
                    labelValue = stringResource(id = com.example.expensestracker.R.string.first_name),
                    painterResource = painterResource(id = R.drawable.profile),
                    onTextSelected = { newName ->
                        val currentState = loginViewModel.fields.value ?: RegistrationUIState()
                        loginViewModel.fields.value = currentState.copy(name = newName)

                    },
                   // errorStatus = loginViewModel.registrationUIState.value.nameError
                )

                MyTextField(labelValue = stringResource(id = com.example.expensestracker.R.string.email),
                    painterResource = painterResource(id = R.drawable.mail),
                    onTextSelected = { email->
                        val emailState = loginViewModel.fields.value ?: RegistrationUIState()
                        loginViewModel.fields.value = emailState.copy(email = email)

                    }, //errorStatus = loginViewModel.registrationUIState.value.emailError
                )

                MyPasswordField(labelValue = stringResource(id = R.string.pass),
                    painterResource = painterResource(
                        id = R.drawable.lock
                    ),
                    onTextSelected = {password->
                        val passState = loginViewModel.fields.value ?: RegistrationUIState()
                        loginViewModel.fields.value = passState.copy(password = password)


                    },
              //      errorStatus = loginViewModel.registrationUIState.value.passwordError
                )

//                CheckboxComponent(value = stringResource(id = R.string.terms_and_condition),
//                    onTextSelected = {
//                        AppRouter.navigateTo(Screen.TermsAndCondnScreen)
//                    },
//                    onCheckedChange = {
//                        loginViewModel.onEvent(SignUpUIEvent.PrivacyPolicyCheckBoxClicked(it))
//
//                    })
                Spacer(modifier = Modifier.heightIn(60.dp))
                    AnimatedSwitchButton(
                        text = stringResource(id = R.string.register),
                        isEnabled =true,
                        onClick = {
                            loginViewModel.createUserInFirebase()
                        }

                    )


                    Spacer(modifier = Modifier.height(20.dp))
                DividerTextComponent()

                ClickableLoginTextComponent(tryingToLogin = true, onTextSelected = {

                    AppRouter.navigateTo(Screen.LoginScreen)
                })
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

