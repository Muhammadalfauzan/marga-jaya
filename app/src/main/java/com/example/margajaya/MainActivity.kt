package com.example.margajaya

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.margajaya.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import nl.joery.animatedbottombar.AnimatedBottomBar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up Toolbar sebagai ActionBar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setupNavigation()

        // Override tombol back pada device dengan OnBackPressedCallback
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController(R.id.nav_host_fragment)
                val currentDestinationId = navController.currentDestination?.id

                when (currentDestinationId) {
                    R.id.detailFragment -> {
                        // Jika berada di detailFragment, navigasikan kembali ke home
                        navController.navigateUp() // Menggunakan navigateUp akan kembali ke fragment sebelumnya (yaitu, home)
                    }
                    R.id.historyFragment, R.id.profileFragment -> {
                        // Jika berada di history atau profile, navigasikan kembali ke home
                        navController.popBackStack(R.id.homeFragment, false)
                    }
                    else -> {
                        // Jika sudah di home atau tidak ada fragment sebelumnya, keluar aplikasi
                        finish()
                    }
                }
            }
        })
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Listener untuk perubahan tab di AnimatedBottomBar
        binding.bottomNav.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(lastIndex: Int, lastTab: AnimatedBottomBar.Tab?, newIndex: Int, newTab: AnimatedBottomBar.Tab) {
                when (newTab.id) {
                    R.id.homeFragment -> navigateToFragment(navController, R.id.homeFragment)
                    R.id.historyFragment -> navigateToFragment(navController, R.id.historyFragment)
                    R.id.profileFragment -> navigateToFragment(navController, R.id.profileFragment)
                }
            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
                // Optional: Handle tab reselection here if needed
            }
        })

        // Listener untuk setiap perubahan destinasi
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavVisibility(destination.id)
            updateToolbar(destination.id)
            syncBottomNavWithDestination(destination.id)
        }
    }

    private fun navigateBackToHome() {
        val navController = findNavController(R.id.nav_host_fragment)
        val currentDestinationId = navController.currentDestination?.id
        if (currentDestinationId == R.id.historyFragment || currentDestinationId == R.id.profileFragment) {
            // Jika berada di history atau profile, kembali ke home
            navigateToFragment(navController, R.id.homeFragment)
        } else {
            // Jika sudah di home atau fragment lain, lakukan back biasa
            finish()
        }
    }

    private fun navigateToFragment(navController: NavController, destinationId: Int) {
        val currentDestination = navController.currentDestination?.id

        if (currentDestination != destinationId) {
            // Pop back stack to prevent recreating home fragment multiple times
            if (destinationId == R.id.homeFragment) {
                navController.popBackStack(R.id.homeFragment, false)
            } else {
                navController.navigate(destinationId)
            }
        }
    }

    // Fungsi untuk mengatur visibilitas AnimatedBottomBar
    private fun updateBottomNavVisibility(destinationId: Int) {
        val shouldHide = destinationId.shouldHideBottomNav()
        binding.bottomNav.visibility = if (shouldHide) View.GONE else View.VISIBLE
    }

    // Fungsi untuk mengatur Toolbar berdasarkan ID destinasi
    private fun updateToolbar(destinationId: Int) {
        setCalendarIconVisibility(destinationId)
        setToolbarTitle(destinationId)
        invalidateOptionsMenu()
    }

    // Fungsi untuk menyinkronkan AnimatedBottomBar dengan fragment yang sedang ditampilkan
    private fun syncBottomNavWithDestination(destinationId: Int) {
        when (destinationId) {
            R.id.homeFragment -> binding.bottomNav.selectTabAt(0)
            R.id.historyFragment -> binding.bottomNav.selectTabAt(1)
            R.id.profileFragment -> binding.bottomNav.selectTabAt(2)
        }
    }

    // Fungsi untuk mengatur visibilitas ikon kalender
    private fun setCalendarIconVisibility(destinationId: Int) {
        binding.toolbar.findViewById<ImageButton>(R.id.btn_date).visibility =
            if (destinationId == R.id.homeFragment) View.VISIBLE else View.GONE
    }

    // Fungsi untuk mengatur judul toolbar berdasarkan ID destinasi
    private fun setToolbarTitle(destinationId: Int) {
        val title = when (destinationId) {
            R.id.homeFragment -> "Home"
            R.id.historyFragment -> "History"
            R.id.profileFragment -> "Profile"
            R.id.detailFragment -> "Detail"
            else -> "App Title"
        }
        binding.toolbar.findViewById<TextView>(R.id.tv_tolbar).text = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val destinationId = findNavController(R.id.nav_host_fragment).currentDestination?.id
        menu?.findItem(R.id.toolbar)?.isVisible = destinationId.shouldShowMenu()
        menu?.findItem(R.id.logout)?.isVisible = destinationId == R.id.profileFragment
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onResume() {
        super.onResume()
        val destinationId = findNavController(R.id.nav_host_fragment).currentDestination?.id
        if (destinationId != null) {
            updateBottomNavVisibility(destinationId)
        }
    }

    // Fungsi ekstensi untuk menentukan apakah BottomNavigationView harus disembunyikan
    private fun Int.shouldHideBottomNav(): Boolean {
        return this == R.id.detailFragment
    }

    // Fungsi ekstensi untuk menentukan apakah menu toolbar harus ditampilkan
    private fun Int?.shouldShowMenu(): Boolean {
        return this == R.id.homeFragment || this == R.id.historyFragment
    }
}
