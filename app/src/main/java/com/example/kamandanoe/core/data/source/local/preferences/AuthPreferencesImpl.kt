package com.example.kamandanoe.core.data.source.local.preferences

import android.content.Context

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.kamandanoe.core.domain.preferences.AuthPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AuthPreferencesImpl @Inject constructor(@ApplicationContext context: Context) :
    AuthPreferences {

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

    override fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
    }

    override fun getAuthToken(): String? {
        return sharedPreferences.getString("auth_token", null)
    }

    override fun saveEmail(email: String) {
        sharedPreferences.edit().putString("email_user", email).apply()
    }

    override fun clearUserData() {
        sharedPreferences.edit()
            .remove("auth_token")
            .remove("email_user")
            .apply()
    }

    override fun getEmail(): String? {
        return sharedPreferences.getString("email_user", null)
    }
}

