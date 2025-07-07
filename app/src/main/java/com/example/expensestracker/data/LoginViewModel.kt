package com.example.expensestracker.data

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.data.rules.Validator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener

class LoginViewModel: ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)


    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChange -> {
                loginUIState.value = loginUIState.value.copy(
                   email = event.email
                )
            }

            is LoginUIEvent.passwordChanged -> {
                loginUIState.value = loginUIState.value.copy(
                    password = event.password
                )
            }

            is LoginUIEvent.LoginButtonClicked -> {
                login()
            }
        }
        validateLoginUIDataWithRules()
    }

     private fun login(){

        loginInProgress.value=true
        val email=loginUIState.value.email
        val password=loginUIState.value.password
     //val context= LocalContext.current

        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                loginInProgress.value=false
                Log.d(TAG,"Inside_Login_successful")
                Log.d(TAG,"${it.isSuccessful}")

                if(it.isSuccessful){
                    loginInProgress.value=false
                    AppRouter.navigateTo(Screen.HomeScreen)

                }

                else{
                 //   Toast.makeText(this,"something went wrong",Toast.LENGTH_SHORT)
                }

            }
            .addOnFailureListener{
                Log.d(TAG,"Inside_login_failure")
                Log.d(TAG,"${it.localizedMessage}")
                loginInProgress.value=false
            }



    }

    private fun validateLoginUIDataWithRules() {
        val emailResult = Validator.validateEmail(
            email = loginUIState.value.email
        )


        val passwordResult = Validator.validatePassword(
            password = loginUIState.value.password
        )

        loginUIState.value = loginUIState.value.copy(
            emailError = emailResult.isSuccess,
            passwordError = passwordResult.isSuccess
        )


        allValidationsPassed.value = emailResult.isSuccess && passwordResult.isSuccess

    }

    private fun createUserInFirebase(email:String,password:String){

        loginInProgress.value=true
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                Log.d(TAG,"Inside_OnCompleteListener")
                Log.d(TAG,"isSuccessful= ${it.isSuccessful}")

                loginInProgress.value=false
                if(it.isSuccessful){
                    AppRouter.navigateTo(Screen.HomeScreen)
                }
            }
            .addOnFailureListener{
                Log.d(TAG,"Inside_ONFailureLListener")
                Log.d(TAG,"Exception=${it.message}")
                Log.d(TAG,"Exception=${it.localizedMessage}")
            }

    }

    fun logout(){
        val firebaseAuth=FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val authStateListner=AuthStateListener{
            if(it.currentUser==null){
                Log.d(TAG,"Inside signout Success")
                AppRouter.navigateTo(Screen.LoginScreen)
            }
            else{
                Log.d(TAG,"Inside sign out is not complete")
            }
        }
        firebaseAuth.addAuthStateListener (authStateListner)
    }
}