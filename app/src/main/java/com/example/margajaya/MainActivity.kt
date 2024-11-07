package com.example.margajaya


import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.margajaya.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Toolbar sebagai ActionBar
        setSupportActionBar(binding.toolbar)

        // Menghilangkan judul default ActionBar karena kita menggunakan TextView khusus
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Inisialisasi NavController dan menghubungkannya dengan BottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        // Mengatur visibilitas BottomNavigationView dan judul toolbar sesuai fragment yang aktif
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailFragment -> binding.bottomNav.visibility = View.GONE
                else -> binding.bottomNav.visibility = View.VISIBLE
            }

            // Mengatur judul toolbar sesuai dengan fragment
            when (destination.id) {
                R.id.homeFragment -> setToolbarTitle("Home")
                R.id.historyFragment -> setToolbarTitle("History")
                R.id.profileFragment -> setToolbarTitle("Profile")
                // Tambahkan case lain jika ada fragment lain
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Menu di inflate di MainActivity, tetapi visibilitas dikendalikan di fragment
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        val destinationId = navController.currentDestination?.id

        // Kontrol visibilitas menu berdasarkan fragment yang aktif
        menu?.findItem(R.id.toolbar)?.isVisible =
            destinationId == R.id.homeFragment || destinationId == R.id.historyFragment

        return super.onPrepareOptionsMenu(menu)
    }

    private fun setToolbarTitle(title: String) {
        binding.toolbar.findViewById<TextView>(R.id.tv_tolbar).text = title
    }
}
