package com.example.kamandanoe

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kamandanoe.core.domain.preferences.AuthPreferences
import com.example.kamandanoe.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var authPreferences: AuthPreferences

    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkLoginStatus() // Cek status login saat splash screen
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            val isLoggedIn = withContext(Dispatchers.IO) { isUserLoggedIn() }
            navigateToNextScreen(isLoggedIn)
        }
    }

    private suspend fun isUserLoggedIn(): Boolean = withContext(Dispatchers.IO) {
        val token = authPreferences.getAuthToken()
        val email = authPreferences.getEmail()
        !token.isNullOrEmpty() && !email.isNullOrEmpty()
    }

    private fun navigateToNextScreen(isLoggedIn: Boolean) {
        val nextActivity = if (isLoggedIn) MainActivity::class.java else AutentikasiActivity::class.java
        startActivity(Intent(this, nextActivity))
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // Hindari memory leak
    }
}
