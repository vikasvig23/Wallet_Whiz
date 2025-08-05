package com.example.expensestracker.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.data.rules.Validator
import com.example.expensestracker.utils.PrefDataStore
import com.example.expensestracker.utils.Utility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val TAG = LoginViewModel::class.simpleName

    var loginUIState = mutableStateOf(LoginUIState())

    var allValidationsPassed = mutableStateOf(false)

    var loginInProgress = mutableStateOf(false)


    val showMessageBar = mutableStateOf(false)
    val messageText = mutableStateOf("")
    val isSuccessMessage = mutableStateOf(true)

    private val _fields = MutableStateFlow(RegistrationUIState())
    val state: StateFlow<RegistrationUIState> = _fields.asStateFlow()


    fun updateField(field: String, value: String) {
        _fields.value = when (field) {
            "email" -> _fields.value.copy(email = value, touched = true)
            "password" -> _fields.value.copy(password = value, touched = true)
            else -> _fields.value
        }
    }

     fun valid(): Boolean {
        val current = _fields.value
        return when {

            current.email.isBlank() -> {
                showAlert("Email is required", false)
                false
            }
            !Utility.isValidEmail(current.email) -> {
                showAlert("Invalid email", false)
                false
            }
            current.password.isBlank() -> {
                showAlert("Password is required", false)
                false
            }
            current.password.length < 6 -> {
                showAlert("Password must be at least 6 characters", false)
                false
            }
            else -> true
        }

    }

    private fun showAlert(msg: String, isSuccess: Boolean = true) {
        messageText.value = msg
        isSuccessMessage.value = isSuccess
        showMessageBar.value = true
    }






    fun login(context: Context) {

        val email = _fields.value.email
        val password = _fields.value.password

        if (email.isNullOrBlank() || password.isNullOrBlank()) {
            Log.e(TAG, "Email or Password is empty! $email $password")
            return
        }

        if (valid()) {
           // _fields.value = true
            val safeEmail = email.replace(".", ",")
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                 //   _fields.value = false
                    Log.d(TAG, "Inside_Login_successful: ${it.isSuccessful}")
                    viewModelScope.launch {
                        PrefDataStore.saveEmail(context, safeEmail)
                    }

                    if (it.isSuccessful) {
                        AppRouter.navigateTo(Screen.HomeScreen)
                    }
                }
                .addOnFailureListener {
                    Log.d(TAG, "Inside_login_failure: ${it.localizedMessage}")
                 //   _fields.value = false
                }
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