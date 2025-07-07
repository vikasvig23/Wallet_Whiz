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
import com.example.expensestracker.data.rules.Validator
import com.example.expensestracker.navigation.AppRouter
import com.example.expensestracker.navigation.Screen
import com.example.expensestracker.utils.Utility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener


class SignUpViewModel : ViewModel() {

    private val TAG = SignUpViewModel::class.simpleName

    var registrationUIState = mutableStateOf(RegistrationUIState())

    var allValidationsPassed = mutableStateOf(false)
    var signUpInProgress = mutableStateOf(false)

    val showMessageBar = mutableStateOf(false)
    val messageText = mutableStateOf("")
    val isSuccessMessage = mutableStateOf(true)

    val fields = MutableLiveData<RegistrationUIState>()
    val _fields: LiveData<RegistrationUIState> = fields






    fun valid(): Boolean {
        val name = fields.value?.name ?: ""
        val email = fields.value?.email ?: ""
        val pass = fields.value?.password ?: ""
     //   val policyAccepted = fields.value?.privacyPolicyAccepted ?: false

        return when {
            name.isBlank() && email.isBlank() && pass.isBlank() -> {

                showAlert("Please fill in all fields", isSuccess =false)
                false
            }

            name.isBlank() -> {
                showAlert("Name is required", isSuccess =false)
                false
            }

            email.isBlank() -> {
                showAlert("Email is required", isSuccess =false)
                false
            }

            !Utility.isValidEmail(email) -> {
                showAlert("Enter a valid email address", isSuccess =false)
                false
            }

            pass.isBlank() -> {
                showAlert("Password is required", isSuccess =false)
                false
            }

            pass.length < 6 -> {
                showAlert("Password must be at least 6 characters", isSuccess =false)
                false
            }

//            !policyAccepted -> {
//                showAlert("Please accept the Terms and Conditions")
//                false
//            }

            else -> {
                // Valid
                showAlert("All fields are valid!", isSuccess = true)
                true
            }
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

    fun createUserInFirebase(){

       if (valid()){
           signUpInProgress.value=true
           registrationUIState.value = fields.value ?: RegistrationUIState()
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