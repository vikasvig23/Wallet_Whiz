package com.example.expensestracker.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import com.google.crypto.tink.Aead
import com.google.crypto.tink.aead.AesGcmKeyManager
import com.google.crypto.tink.config.TinkConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.google.crypto.tink.subtle.Base64
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private  const val DATASTORE_NAME = "pref_data"
private val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

class PrefDataStore private  constructor(private val context: Context){

    companion object{

        @Volatile
        private var INSTANCE: PrefDataStore? =null
        private const val KEY_EMAIL = "user_email"

        fun getInstance(context: Context): PrefDataStore{
            return INSTANCE ?: synchronized(this){
                INSTANCE ?: PrefDataStore(context.applicationContext).also { INSTANCE =it }
            }
        }

        const val NAME ="name"

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
            Log.d("SecureEmail", "Encrypted: $encryptedEmail")
            return encryptedEmail?.let { decrypt(context, it) }
        }

        suspend fun clearEmail(context: Context) {
            context.dataStore.edit {
                it.remove(stringPreferencesKey(KEY_EMAIL))
            }

        }

        suspend fun saveData(context: Context, key: String, value: String) {
            val dataStoreKey = stringPreferencesKey(key)
            context.dataStore.edit { prefs ->
                prefs[dataStoreKey] = value
            }
        }

         suspend fun getData(context: Context, key: String): String? {
            val dataKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            return preferences[dataKey]
        }

        suspend fun clearAll(context: Context) {
            context.dataStore.edit { it.clear() }
        }


    }


}