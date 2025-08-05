package com.example.expensestracker.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.expensestracker.utils.Utility
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgotPassVm:ViewModel() {

    val showMessageBar = mutableStateOf(false)
    val messageText = mutableStateOf("")
    val isSuccessMessage = mutableStateOf(true)

    private val _fields = MutableStateFlow(RegistrationUIState())
    val state: StateFlow<RegistrationUIState> = _fields.asStateFlow()


    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _resetMessage = MutableStateFlow<String?>(null)
    val resetMessage = _resetMessage.asStateFlow()


    fun updateEmail(email: String) {
        _fields.value = _fields.value.copy(
            email = email,
            touched = true
        )
    }

    fun sendPasswordReset(email: String) {
        if (email.isBlank()) {
            _resetMessage.value = "Email field cannot be empty"
            showMessageBar.value = true
            return
        }
        else {
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    _resetMessage.value = if (task.isSuccessful) {
                        showMessageBar.value = true
                        isSuccessMessage.value = true
                        "Password reset email sent successfully"
                    } else {
                        showMessageBar.value = true
                        isSuccessMessage.value = false
                        task.exception?.message ?: "Something went wrong"
                    }
                }
        }
    }



    fun clearResetMessage() {
        _resetMessage.value = null
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
            else -> true
        }
    }

    private fun showAlert(msg: String, isSuccess: Boolean = true) {
        messageText.value = msg
        isSuccessMessage.value = isSuccess
        showMessageBar.value = true
    }

}