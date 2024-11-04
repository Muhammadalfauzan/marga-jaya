package com.example.margajaya

import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.margajaya.core.data.source.local.preferences.AuthPreferences
import com.example.margajaya.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.setGraph(R.navigation.mobile_navigation) // Muat graf utama untuk pengguna yang sudah login
        setupBottomNavigation(navController) // Set up Bottom Navigation
    }

    private fun setupBottomNavigation(navController: NavController) {
        // Menghubungkan bottom navigation dengan NavController hanya untuk graf utama
        binding.buttomNav.setupWithNavController(navController)
        binding.buttomNav.visibility = View.VISIBLE
    }
}
