package com.example.kamandanoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.kamandanoe.databinding.ActivityAutentikasiBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AutentikasiActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAutentikasiBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout
        binding = ActivityAutentikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
        navController = navHostFragment.navController
    }
}