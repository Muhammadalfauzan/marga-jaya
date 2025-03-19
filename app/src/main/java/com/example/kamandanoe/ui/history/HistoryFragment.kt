package com.example.kamandanoe.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.kamandanoe.AutentikasiActivity
import com.example.kamandanoe.core.utils.NetworkMonitor
import com.example.kamandanoe.databinding.FragmentHistoryBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        observeNetworkStatus()
    }

    private fun observeNetworkStatus() {
        networkMonitor.networkStatus.observe(viewLifecycleOwner) { isConnected ->
            binding.apply {
                ivNoInternet.visibility = if (isConnected) View.GONE else View.VISIBLE
                tvInfo.visibility = if (isConnected) View.GONE else View.VISIBLE
                pager.visibility = if (isConnected) View.VISIBLE else View.GONE
                tabLayout.visibility = if (isConnected) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupViewPagerAndTabs() {
        val adapter = HistoryPagerAdapter(this)
        binding.pager.adapter = adapter

        // Animasi untuk ViewPager2
        binding.pager.setPageTransformer { page, position ->
            val scale = 0.85f + (1 - Math.abs(position)) * 0.15f
            page.scaleX = scale
            page.scaleY = scale
            page.alpha = 0.5f + (1 - Math.abs(position)) * 0.5f
        }

        // Sinkronisasi TabLayout dengan ViewPager2
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = if (position == 0) "Akan Datang" else "Selesai"
        }.attach()

        addTabAnimation()

    }

    private fun addTabAnimation() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.view?.animate()
                    ?.scaleX(1.1f)
                    ?.scaleY(1.1f)
                    ?.alpha(1f)
                    ?.setDuration(300)
                    ?.start()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.view?.animate()
                    ?.scaleX(1f)
                    ?.scaleY(1f)
                    ?.alpha(0.8f)
                    ?.setDuration(300)
                    ?.start()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        childFragmentManager.fragments.forEach { fragment ->
            when (fragment) {
                is UpcomingFragment -> fragment.fetchBookings()
                is FinishedFragment -> fragment.fetchBookings()
            }
        }
    }


    private fun navigateToLogin() {
        val intent = Intent(requireContext(), AutentikasiActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
