package com.example.expensestracker.data.rules

object Validator {

    fun validateName(fName:String):ValidationResult{
        if((!fName.isNullOrEmpty() && fName.length>=4)){

        }
        return ValidationResult(
            (!fName.isNullOrEmpty() && fName.length>=4)
        )

    }

    fun validateEmail(email: String): ValidationResult {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        val isValid = email.matches(Regex(emailPattern))

        return ValidationResult(
            isSuccess = isValid,
            errorMessage = if (isValid) null else "Invalid email format"
        )
    }


    fun validatePassword(password:String):ValidationResult{
        return ValidationResult(
            (!password.isNullOrEmpty() && password.length>=6)
        )
    }

//    fun validatePrivacyPolicyAccepted(statusValue:Boolean):ValidationResult{
//        return ValidationResult(
//             statusValue
//         )
//    }
}

data class ValidationResult(
    val isSuccess: Boolean,
    val errorMessage: String? = null
)
