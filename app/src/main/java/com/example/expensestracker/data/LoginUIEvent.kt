package com.example.expensestracker.data

sealed class LoginUIEvent {

    data class EmailChange(val email:String):LoginUIEvent()
    data class passwordChanged(val password:String):LoginUIEvent()


    data object LoginButtonClicked:LoginUIEvent()
}