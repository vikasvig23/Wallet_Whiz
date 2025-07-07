package com.example.expensestracker.data

sealed class SignUpUIEvent {

    data class NAmeChanged(val Name:String):SignUpUIEvent()
    data class EmailChange(val email:String):SignUpUIEvent()
    data class passwordChanged(val password:String):SignUpUIEvent()

  //  data class  PrivacyPolicyCheckBoxClicked(val status:Boolean):SignUpUIEvent()
    object RegistrationButtonClicked:SignUpUIEvent()
}