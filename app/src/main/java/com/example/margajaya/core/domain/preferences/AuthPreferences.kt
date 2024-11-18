package com.example.margajaya.core.domain.preferences

interface AuthPreferences {
    fun saveAuthToken(token: String)
    fun getAuthToken(): String?
    fun saveEmail(email: String)
    fun clearUserData()
    fun getEmail(): String?
}
