package com.example.margajaya

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.margajaya.core.data.source.local.preferences.AuthPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var authPreferences: AuthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // Cek apakah pengguna sudah login
        Handler().postDelayed({
            if (isUserLoggedIn()) {
                // Arahkan ke MainActivity jika sudah login
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                // Arahkan ke AutentikasiActivity jika belum login
                startActivity(Intent(this, AutentikasiActivity::class.java))
            }
            finish() // Tutup SplashActivity setelah navigasi})
        },3000)
    }

    private fun isUserLoggedIn(): Boolean {
        val token = authPreferences.getAuthToken()
        val email = authPreferences.getEmail()
        return !token.isNullOrEmpty() && !email.isNullOrEmpty()
    }
}
