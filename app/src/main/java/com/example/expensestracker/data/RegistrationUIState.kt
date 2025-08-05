package com.example.expensestracker.data

data class RegistrationUIState (
    var name:String="",
    var email:String="",
    var password:String="",
 ///   var privacyPolicyAccepted:Boolean=false,
    val touched: Boolean = false,
    var nameError:Boolean=false,
    var emailError:Boolean=false,
    var passwordError: Boolean=false,
   // var privacyPolicyError:Boolean=false

    )