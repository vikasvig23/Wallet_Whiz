package com.example.expensestracker.db_model

import android.content.Context
import com.example.expensestracker.data.Currencies
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

//data class CurrencyCode(val curencyCode: String)


object SharedPreferencesManager {
    private const val PREFS_NAME = "CurrencyPrefs"
    private const val KEY_SELECTED_CURRENCY = "selected_currency"

    fun saveSelectedCurrency(context: Context, currency: Currencies) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_SELECTED_CURRENCY, currency.currencyCode).apply()
    }

    fun getSelectedCurrency(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_SELECTED_CURRENCY, null)
    }
}