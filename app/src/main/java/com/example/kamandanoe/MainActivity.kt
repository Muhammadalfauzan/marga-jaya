package com.example.kamandanoe

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.kamandanoe.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Setup Navigation
        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = binding.navHostFragment.getFragment<NavHostFragment>()

        val navController = navHostFragment.navController

        // Connect BottomNavigationView to NavController
        binding.bottomNavigationView.setupWithNavController(navController)

        // Setup navigation indicator for BottomNavigationView
        setupNavigationIndicator(navController)

        // Handle navigation destination changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateToolbar(destination.id)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupNavigationIndicator(navController: NavController) {
        // Parent BottomNavigationView
        val menuView = binding.bottomNavigationView.getChildAt(0) as BottomNavigationMenuView

        // Listener untuk memperbarui indikator pada item yang diklik
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val index = getMenuItemIndex(menuItem.itemId)
            updateIndicator(index, menuView)
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            true
        }

        // Listener untuk menyinkronkan indikator saat fragment berubah
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val index = getMenuItemIndex(destination.id)
            updateIndicator(index, menuView)
        }

        // Menetapkan indikator default (item pertama)
        val defaultIndex = getMenuItemIndex(navController.currentDestination?.id ?: -1)
        updateIndicator(defaultIndex, menuView)
    }

    @SuppressLint("RestrictedApi")
    private fun updateIndicator(index: Int, menuView: BottomNavigationMenuView) {
        // Menghapus indikator dari semua item
        for (i in 0 until menuView.childCount) {
            val itemView = menuView.getChildAt(i) as BottomNavigationItemView
            val indicator = itemView.findViewById<View>(R.id.nav_indicator)
            indicator?.let { itemView.removeView(it) }
        }

        // Menambahkan indikator pada item yang dipilih
        if (index in 0 until menuView.childCount) {
            val selectedItemView = menuView.getChildAt(index) as BottomNavigationItemView
            val indicatorView = View(this).apply {
                id = R.id.nav_indicator
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, // Lebar indikator sesuai item
                    10.dp // Total tinggi (garis + bayangan)
                ).apply {
                    gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL // Muncul di atas ikon menu
                    topMargin = -6.dp // Atur jarak agar sedikit di atas ikon
                }
                setBackgroundResource(R.drawable.nav_indikator_home) // Mengatur background ripple
            }
            selectedItemView.addView(indicatorView)
        }
    }

    // Fungsi untuk mendapatkan indeks item berdasarkan ID
    private fun getMenuItemIndex(itemId: Int): Int {
        for (i in 0 until binding.bottomNavigationView.menu.size()) {
            if (binding.bottomNavigationView.menu.getItem(i).itemId == itemId) {
                return i
            }
        }
        return -1 // Jika item tidak ditemukan
    }

    // Ekstensi untuk konversi dp ke px
    private val Int.dp: Int
        get() = (this * resources.displayMetrics.density).toInt()

    // Update visibility of BottomNavigationView
    private fun updateBottomNavVisibility(destinationId: Int) {
        binding.bottomNavigationView.visibility = if (destinationId.shouldHideBottomNav()) View.GONE else View.VISIBLE
    }

    // Update Toolbar for specific destination
    private fun updateToolbar(destinationId: Int) {
        setToolbarTitle(destinationId)
        invalidateOptionsMenu()
    }

    // Synchronize BottomNavigationView with current fragment
    private fun syncBottomNavWithDestination(destinationId: Int) {
        when (destinationId) {
            R.id.homeFragment -> binding.bottomNavigationView.selectedItemId = R.id.homeFragment
            R.id.historyFragment -> binding.bottomNavigationView.selectedItemId = R.id.historyFragment
            R.id.profileFragment -> binding.bottomNavigationView.selectedItemId = R.id.profileFragment
        }
    }

    // Set Toolbar title based on destination ID
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
        destinationId?.let { updateBottomNavVisibility(it) }
    }

    // Extension function to check if BottomNavigationView should be hidden
    private fun Int.shouldHideBottomNav(): Boolean {
        return this == R.id.detailFragment || this == R.id.buktiFragment
    }

    // Extension function to check if toolbar menu should be shown
    private fun Int?.shouldShowMenu(): Boolean {
        return this == R.id.homeFragment || this == R.id.historyFragment
    }

}
