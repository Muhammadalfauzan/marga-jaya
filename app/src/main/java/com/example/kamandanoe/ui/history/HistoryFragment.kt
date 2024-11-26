package com.example.kamandanoe.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kamandanoe.AutentikasiActivity
import com.example.kamandanoe.core.utils.NetworkMonitor
import com.example.kamandanoe.databinding.FragmentHistoryBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val bookingViewModel: BookingViewModel by viewModels()

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPagerAndTabs()

        // Observasi status jaringan
        networkMonitor.networkStatus.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                binding.ivNoInternet.visibility = View.GONE
                binding.tvInfo.visibility = View.GONE
                binding.pager.visibility = View.VISIBLE
                binding.tabLayout.visibility = View.VISIBLE
            } else {
                binding.ivNoInternet.visibility = View.VISIBLE
                binding.tvInfo.visibility = View.VISIBLE
                binding.pager.visibility = View.GONE
                binding.tabLayout.visibility = View.GONE
            }
        }
    }

    private fun setupViewPagerAndTabs() {
        // Set adapter for ViewPager2
        val adapter = HistoryPagerAdapter(this)
        binding.pager.adapter = adapter

        // Set PageTransformer for smooth animation
        binding.pager.setPageTransformer { page, position ->
            page.apply {
                val scale = 0.85f + (1 - Math.abs(position)) * 0.15f
                scaleX = scale
                scaleY = scale
                alpha = 0.5f + (1 - Math.abs(position)) * 0.5f
            }
        }

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Upcoming Bookings"
                1 -> "Past Bookings"
                else -> throw IllegalStateException("Invalid position $position")
            }
        }.attach()
        addTabAnimation()
    }
    private fun addTabAnimation() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val view = it.view
                    // Animasi saat tab dipilih
                    view.animate()
                        .scaleX(1.1f) // Zoom in
                        .scaleY(1.1f)
                        .alpha(1f) // Full opacity
                        .setDuration(300) // Durasi animasi
                        .start()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    val view = it.view
                    // Animasi saat tab dilepas
                    view.animate()
                        .scaleX(1f) // Kembali ke ukuran normal
                        .scaleY(1f)
                        .alpha(0.8f) // Sedikit transparan
                        .setDuration(300)
                        .start()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Tidak perlu animasi tambahan untuk tab yang dipilih ulang
            }
        })
    }



    private fun navigateToLogin() {
        // Navigasi ke AuthMainActivity jika token expired
        val intent = Intent(requireContext(), AutentikasiActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
