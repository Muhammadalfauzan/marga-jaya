package com.example.margajaya.core.data.source.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthPreferences @Inject constructor(@ApplicationContext context: Context) {

    // Membuat alias master key untuk enkripsi
    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // Membuat instance EncryptedSharedPreferences
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "user_prefs",
        masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    fun getAuthToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    fun clearAuthToken() {
        sharedPreferences.edit().remove("auth_token").apply()
    }

    // Fungsi tambahan untuk data sensitif lainnya
    fun saveEmail(email: String) {
        sharedPreferences.edit().putString("email_user", email).apply()
    }

    fun getEmail(): String? {
        return sharedPreferences.getString("email_user", null)
    }

    fun clearEmail() {
        sharedPreferences.edit().remove("email_user").apply()
    }
    fun clearUserData() {
        sharedPreferences.edit()
            .remove("auth_token")
            .remove("email_user")
            .apply()
    }
}
