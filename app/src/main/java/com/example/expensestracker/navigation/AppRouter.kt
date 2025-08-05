package com.example.expensestracker.navigation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.example.expensestracker.R
import com.example.expensestracker.data.ForgotPassVm
import com.example.expensestracker.data.currency

sealed class Screen(){
     data object SignUp:Screen()
    data object  TermsAndCondnScreen:Screen()
    data object LoginScreen:Screen()
    data object HomeScreen :Screen()
    data object SplashScreen :Screen()
    data object ForgotPass: Screen()



}

object AppRouter{
    var currentScreen: MutableState<Screen> = mutableStateOf(Screen.SignUp)

    fun navigateTo(destination: Screen) {
        currentScreen.value =destination
    }

}
/**
sealed class CurrencyScreen(val route: String){
    data object Expenses: CurrencyScreen("Expenses/{currencyCode}")
}
object CurrencyRouter{
    var currentScreen: MutableState<CurrencyScreen.Expenses> = mutableStateOf(CurrencyScreen.Expenses)

fun navigateTo(destination: CurrencyScreen) {
    CurrencyRouter.currentScreen.value = destination as CurrencyScreen.Expenses
}}
**/



sealed class BottomBarScreen(
    val route:String,
    val title: String,
    val icon:Int,
    val icon_focused: Int
){
    data object Expense:BottomBarScreen(
        route ="expenses",
        title = "Expenses",
        icon = R.drawable.upload,
        icon_focused =  R.drawable.upload_light
    )
     data object Reports:BottomBarScreen(
        route="reports",
        title = "Reports",
         icon = R.drawable.bar_chart,
         icon_focused =  R.drawable.report_light
    )
    data object Add:BottomBarScreen(
        route="add",
        title = "Add",
        icon = R.drawable.add,
        icon_focused =  R.drawable.add_light
    )
    data object Setting:BottomBarScreen(
        route="setting",
        title = "Setting",
        icon = R.drawable.settings_outlined,
        icon_focused =  R.drawable.setting_light
    )


}


/*
sealed class ScreenNavigation(
    val route:String,
    val title: String,

) {
    data object Categories : ScreenNavigation(
        route = "categories",
        title = "Categories",

        )
}
*/

