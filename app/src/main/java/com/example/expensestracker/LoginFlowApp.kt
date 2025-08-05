package com.example.expensestracker

import android.app.Application
import com.example.expensestracker.utils.PrefDataStore
import com.google.firebase.FirebaseApp
import com.google.firebase.installations.installations

class LoginFlowApp:Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
        PrefDataStore.initTink(this)
    }
}