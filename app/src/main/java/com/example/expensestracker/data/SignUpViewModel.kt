package com.example.expensestracker.data

import android.R.id.message
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.expensestracker.data.rules.Validator
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.utils.PrefDataStore

import com.example.expensestracker.utils.SecurePrefsDataStore
import com.example.expensestracker.utils.Utility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SignUpViewModel : ViewModel() {

    private val TAG = SignUpViewModel::class.simpleName


    var registrationUIState = mutableStateOf(RegistrationUIState())

    var allValidationsPassed = mutableStateOf(false)
    var signUpInProgress = mutableStateOf(false)

    val showMessageBar = mutableStateOf(false)
    val messageText = mutableStateOf("")
    val isSuccessMessage = mutableStateOf(true)

    private val _fields = MutableStateFlow(RegistrationUIState())
    val state: StateFlow<RegistrationUIState> = _fields.asStateFlow()


    fun updateField(field: String, value: String) {
        _fields.value = when (field) {
            "email" -> _fields.value.copy(email = value, touched = true)
            "password" -> _fields.value.copy(password = value, touched = true)
            "name" -> _fields.value.copy(name = value, touched = true)
            else -> _fields.value
        }
    }


    fun updatePass(password: String) {
            _fields.value = _fields.value.copy(
                password = password,
                touched = true
            )
        }


        fun updateEmail(email: String) {
            _fields.value = _fields.value.copy(
                email = email,
                touched = true
            )
        }


    private fun valid(): Boolean {
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

    private fun signUpp() {
        signUpInProgress.value = true
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(
                registrationUIState.value.email,
                registrationUIState.value.password
            )
            .addOnCompleteListener {
                signUpInProgress.value = false
                if (it.isSuccessful) {
                    messageText.value = "Registration Successful"
                    isSuccessMessage.value = true
                    showMessageBar.value = true
                    AppRouter.navigateTo(Screen.HomeScreen)
                } else {
                    messageText.value = "Registration failed"
                    isSuccessMessage.value = false
                    showMessageBar.value = true
                }
            }
            .addOnFailureListener {
                signUpInProgress.value = false
                messageText.value = it.message ?: "Something went wrong"
                isSuccessMessage.value = false
                showMessageBar.value = true
            }
    }


    private fun validateDataWithRules() {
        val fNameResult=Validator.validateName(
            fName = registrationUIState.value.name
        )
        val emailResult=Validator.validateEmail(
            email = registrationUIState.value.email
        )
        val passwordResult=Validator.validatePassword(
           password = registrationUIState.value.password
        )

//        val privacyPolicyResult=Validator.validatePrivacyPolicyAccepted(
//            statusValue = registrationUIState.value.privacyPolicyAccepted
//        )

        Log.d(TAG,"Inside_validateDataWithRules")
        Log.d(TAG,"fNameResult=$fNameResult")
        Log.d(TAG,"emailResult=$emailResult")
        Log.d(TAG,"passwordResult=$passwordResult")
     //   Log.d(TAG,"privacyPolicyResult=$privacyPolicyResult")

        registrationUIState.value=registrationUIState.value.copy(
            nameError = fNameResult.isSuccess,
            emailError = emailResult.isSuccess,
            passwordError = passwordResult.isSuccess,
       //     privacyPolicyError = privacyPolicyResult.isSuccess
        )
        allValidationsPassed.value = fNameResult.isSuccess && emailResult.isSuccess && passwordResult.isSuccess
    }

    private fun printState(){
        Log.d(TAG,"Inside_printState")
        Log.d(TAG,registrationUIState.value.toString())
    }

    fun createUserInFirebase(context: Context){

       if (valid()){
           signUpInProgress.value=true
           registrationUIState.value = _fields.value ?: RegistrationUIState()
           FirebaseAuth.getInstance()
               .createUserWithEmailAndPassword(
                   registrationUIState.value.email,
                   registrationUIState.value.password
               )
               .addOnCompleteListener {
                   Log.d(TAG,"Inside_OnCompleteListener")
                   Log.d(TAG,"isSuccessful= ${it.isSuccessful}")

                   signUpInProgress.value=false
                   if (it.isSuccessful) {
                       messageText.value = "Registration successful!"
                       isSuccessMessage.value = true
                       showMessageBar.value = true
                       AppRouter.navigateTo(Screen.HomeScreen)
                   } else {
                       messageText.value = "Registration failed!"
                       isSuccessMessage.value = false
                       showMessageBar.value = true
                   }

               }
               .addOnFailureListener{
                   messageText.value = it.message ?: "Something went wrong"
                   isSuccessMessage.value = false
                   showMessageBar.value = true

               }


                       val name =  registrationUIState.value.name
                       val email = registrationUIState.value.email ?: return



                       // Replace '.' with ',' to make it a valid Firebase key
                       val safeEmail = email.replace(".", ",")

                       val userMap = mapOf(

                           "name" to name
                       )

                       val dbRef = FirebaseDatabase.getInstance().getReference("expenses")
                       dbRef.child(safeEmail).setValue(userMap)
                           .addOnSuccessListener {

                                viewModelScope.launch {
                                    PrefDataStore.saveEmail(context, safeEmail)
                                }

                               showAlert("User info saved successfully!", true)
                           }
                           .addOnFailureListener { e ->
                               showAlert("Failed to save user info: ${e.message}", false)
                           }



       }

    }

    fun logout(){
        val firebaseAuth=FirebaseAuth.getInstance()
        firebaseAuth.signOut()
        val authStateListener=AuthStateListener{
            if(it.currentUser==null){
                Log.d(TAG,"Inside sign outsuccess")
                AppRouter.navigateTo(Screen.LoginScreen)
            }
            else{
                Log.d(TAG,"Inside sign out is not complete")
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)
    }


}