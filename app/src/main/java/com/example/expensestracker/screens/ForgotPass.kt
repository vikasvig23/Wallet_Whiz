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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensestracker.Components.AnimatedMessageBar
import com.example.expensestracker.Components.AnimatedSwitchButton
import com.example.expensestracker.Components.HeadingTextComponents
import com.example.expensestracker.Components.MyTextField
import com.example.expensestracker.Components.SlideInToast
import com.example.expensestracker.R
import com.example.expensestracker.data.ForgotPassVm
import com.example.expensestracker.utils.Utility
import kotlinx.coroutines.delay

@Composable
fun ForgotPass(viewmodel:ForgotPassVm = viewModel()) {


    val showToast = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val emailState = remember { mutableStateOf("") }
    val resetMessage by viewmodel.resetMessage.collectAsState()
    val state by viewmodel.state.collectAsState()

    LaunchedEffect(key1 = resetMessage) {
        if (!resetMessage.isNullOrEmpty()) {
            showToast.value = true
            delay(3000)
            showToast.value = false
        }
    }
    Box(
        modifier = Modifier.fillMaxWidth().zIndex(1f),
        contentAlignment = Alignment.Center
    ) {

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

                HeadingTextComponents(value = stringResource(id = R.string.frgetPAss))
                Spacer(modifier = Modifier.padding(10.dp))
                MyTextField(
                    labelValue = stringResource(id = R.string.email),
                    painterResource = painterResource(id = R.drawable.mail),
                    value = state.email,
                    onTextSelected = {
                        viewmodel.updateEmail(it)
                    },
                    errorStatus = state.touched && Utility.isValidEmail(state.email)
                )


                AnimatedSwitchButton(
                    text = stringResource(R.string.resetEmail),
                    isEnabled = true,
                    onClick = {
                        viewmodel.sendPasswordReset(state.email)
                    }
                )
                if (!resetMessage.isNullOrEmpty()) {
                    SlideInToast(
                        message = resetMessage ?: "",
                        isSuccess = resetMessage?.contains("success", true) == true,
                        visible = showToast.value
                    )
                }


            }


        }
    }


            }