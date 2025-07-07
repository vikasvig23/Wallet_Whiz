package com.example.expensestracker.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.expensestracker.navigation.AppRouter

import com.example.expensestracker.navigation.Screen

import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val TAG = HomeViewModel::class.simpleName


  /*  val navigationItemsList = listOf<NavigationItem>(
       NavigationItem(
           title = "Expenses",
           icon = Icons.Default.Upload,
           description = "Expense Screen",
           itemId = "expenseScreen"
        ),
        NavigationItem(
            title = "Reports",
            icon = Icons.Default.BarChart,
            description = "Report Screen",
            itemId = "reportsScreen"
        ),
        NavigationItem(
            title = "Add",
            icon = Icons.Default.Add,
            description = "Add Screen",
            itemId = "addScreen"
        ),
        NavigationItem(
            title = "Settings",
            icon = Icons.Default.Settings,
            description = "Settings Screen",
            itemId = "settingsScreen"
        ),

    )*/


    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    fun logout() {

        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "Inside sign outsuccess")
                AppRouter.navigateTo(Screen.LoginScreen)
            } else {
                Log.d(TAG, "Inside sign out is not complete")
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)

    }

    fun checkForActiveSession() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, "Valid session")
            isUserLoggedIn.value = true
        } else {
            Log.d(TAG, "User is not logged in")
            isUserLoggedIn.value = false
        }
    }


    val emailId: MutableLiveData<String> = MutableLiveData()

    fun getUserData() {
        FirebaseAuth.getInstance().currentUser?.also {
            it.email?.also { email ->
                emailId.value = email
            }
        }
    }

}
