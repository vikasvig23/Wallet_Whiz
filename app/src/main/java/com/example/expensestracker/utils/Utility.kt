package com.example.expensestracker.utils

import android.content.Context
import android.util.Log
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.example.expensestracker.R
import com.google.crypto.tink.Aead
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.subtle.Base64
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object Utility {

    fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z._-]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    val Poppins = FontFamily(
        Font(R.font.comfortaa, FontWeight.Normal),
        Font(R.font.comfortaa_light, FontWeight.Light),
        Font(R.font.comfortaa_bold, FontWeight.Medium),
        Font(R.font.comforta_extra_bold, FontWeight.Bold)
    )

}



object SecurePrefsDataStore {

    private const val DATASTORE_NAME = "secure_datastore"
    private const val KEY_EMAIL = "user_email"

    const val NAME ="name"

    private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

    // Initialize Tink once (call this from Application class or before usage)
    fun initTink(context: Context) {
        try {
            TinkConfig.register()
            getOrCreateMasterKey(context)
        } catch (e: Exception) {

            Log.e("SecurePrefsDataStore", "Tink initialization failed", e)
        }
    }

    private fun getOrCreateMasterKey(context: Context): Aead {
        val keysetHandle = AndroidKeysetManager.Builder()
            .withSharedPref(context, "master_keyset", "master_key_preference")
            .withKeyTemplate(AesGcmKeyManager.aes256GcmTemplate())
            .withMasterKeyUri("android-keystore://tink_master_key")
            .build()
            .keysetHandle

        return keysetHandle.getPrimitive(Aead::class.java)
    }

    private fun encrypt(context: Context, plainText: String): String {
        val aead = getOrCreateMasterKey(context)
        val ciphertext = aead.encrypt(plainText.toByteArray(Charsets.UTF_8), null)
        return Base64.encodeToString(ciphertext, Base64.DEFAULT)
    }

    private fun decrypt(context: Context, encryptedText: String): String {
        val aead = getOrCreateMasterKey(context)
        val ciphertext = Base64.decode(encryptedText, Base64.DEFAULT)
        val plainText = aead.decrypt(ciphertext, null)
        return String(plainText, Charsets.UTF_8)
    }

    suspend fun saveEmail(context: Context, email: String) {
        val encryptedEmail = encrypt(context, email)
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(KEY_EMAIL)] = encryptedEmail
        }
    }

    suspend fun getEmail(context: Context): String? {
        val preferences = context.dataStore.data.first()
        val encryptedEmail = preferences[stringPreferencesKey(KEY_EMAIL)]
        return encryptedEmail?.let { decrypt(context, it) }
    }

    suspend fun clearEmail(context: Context) {
        context.dataStore.edit {
            it.remove(stringPreferencesKey(KEY_EMAIL))
        }

    }

    suspend fun saveData(context: Context, key:String, value:String) {

        val data = stringPreferencesKey(key)
        context.dataStore.edit { pref->
            pref[data] = value
        }

    }

    suspend fun getData(context: Context, key: String): Flow<String?>{

        val data = stringPreferencesKey(key)
        return context.dataStore.data.map { pref->
            pref[data]
        }
    }

    suspend fun clearAll(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}

