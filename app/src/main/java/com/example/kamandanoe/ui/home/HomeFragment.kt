package com.example.kamandanoe.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kamandanoe.AutentikasiActivity
import com.example.kamandanoe.MainActivity
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.LapanganModel
import com.example.kamandanoe.core.domain.preferences.AuthPreferences
import com.example.kamandanoe.core.utils.NetworkMonitor
import com.example.kamandanoe.core.utils.RetryListener
import com.example.kamandanoe.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(),  SesiBottomSheetFragment.OnSessionSelectedListener{
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val viewModel: HomeViewModel by viewModels()
    private lateinit var lapanganAdapter: AdapterHome
    private var pickerDate: String? = null

@Inject
lateinit var networkMonitor: NetworkMonitor
    @Inject
    lateinit var authPreferences: AuthPreferences
    private lateinit var bottomNavigationView: BottomNavigationView
    private var selectedSession: String = "Semua Sesi"
    private var originalDataList = listOf<LapanganModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        observeLapanganData()

        // Observasi status jaringan
        networkMonitor.networkStatus.observe(viewLifecycleOwner) { isConnected ->
            if (!isConnected) {
                // Tampilkan BottomSheet jika tidak ada koneksi
                showNoInternetBottomSheet()
            }
        }
    }

    private fun initializeUI() {
        setupTopBar()
        setupRecyclerView()
        setupBottomNavigationView()
        setupSwipeRefresh()
        setupDateAndSessionPickers()
        binding.textViewDate.text = viewModel.pickerDate
        setupObservers()
        setupSessionPicker()
    }

    private fun showNoInternetBottomSheet() {
        val noInternetBottomSheet = NoInternetBottomSheet(object : RetryListener {
            override fun onRetry() {
                // Panggil ulang fetch data saat tombol Retry ditekan
                viewModel.fetchLapanganData(viewModel.pickerDate)
            }
        }, isNetworkAvailable = { isNetworkConnected() })
        noInternetBottomSheet.show(parentFragmentManager, "NoInternetBottomSheet")
    }

    private fun isNetworkConnected(): Boolean {
        // Gunakan networkMonitor atau NetworkUtils untuk mengecek koneksi
        return networkMonitor.networkStatus.value == true
    }

    private fun setupObservers() {
        viewModel.selectedSession.observe(viewLifecycleOwner) { session ->
            binding.textViewSesi.text = session
        }
    }

    private fun setupSessionPicker() {
        binding.textViewSesi.setOnClickListener {
            val bottomSheet = SesiBottomSheetFragment()
            bottomSheet.show(childFragmentManager, "SessionBottomSheet")
        }
    }
    override fun onSessionSelected(session: String) {
        viewModel.updateSelectedSession(session)
    }
    private fun setupTopBar() {
        val mainActivity = activity as AppCompatActivity
        mainActivity.supportActionBar?.apply {
            title = "Home"
        }
    }

    private fun setupRecyclerView() {
        binding.rvHomefrag.layoutManager = LinearLayoutManager(requireContext())
        lapanganAdapter = AdapterHome { lapangan ->
            navigateToDetailFragment(lapangan.id, viewModel.pickerDate)
            Toast.makeText(requireContext(), "Detail ${lapangan.jenisLapangan}", Toast.LENGTH_SHORT).show()
        }
        binding.rvHomefrag.adapter = lapanganAdapter
        setupRecyclerViewScrollListener()
    }

    private fun setupRecyclerViewScrollListener() {
        binding.rvHomefrag.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                handleBottomNavigationVisibility(dy)
            }
        })
    }

    private fun handleBottomNavigationVisibility(dy: Int) {
        if (::bottomNavigationView.isInitialized) {
            if (dy > 0 && bottomNavigationView.translationY == 0f) {
                bottomNavigationView.animate().translationY(bottomNavigationView.height.toFloat()).setDuration(200).start()
            } else if (dy < 0 && bottomNavigationView.translationY == bottomNavigationView.height.toFloat()) {
                bottomNavigationView.animate().translationY(0f).setDuration(200).start()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Pastikan toolbar sesuai dengan konteks fragment
        setupTopBar()
    }

    private fun setupBottomNavigationView() {
        val mainActivity = activity as MainActivity
        bottomNavigationView = mainActivity.binding.bottomNavigationView
    }


    private fun setupDateAndSessionPickers() {
        setupDatePicker()
    }

    private fun setupDatePicker() {
        binding.textViewDate.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                val formattedDate = SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault()).format(selectedDate.time)
                viewModel.updatePickerDate(formattedDate)
                binding.textViewDate.text = formattedDate
                // Tampilkan shimmer saat tanggal dipilih
                showLoadingIndicator()
                viewModel.fetchLapanganData(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun observeLapanganData() {
        viewModel.lapangan.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoadingIndicator()
                is Resource.Success -> {
                    hideLoadingIndicator()
                    lapanganAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    hideLoadingIndicator()
                   // handleError(resource.message)
                }
            }
        }
    }
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            val todayDate = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault()).format(Date())
            viewModel.updatePickerDate(todayDate)
            binding.textViewDate.text = todayDate
            // Tampilkan shimmer saat melakukan refresh
            showLoadingIndicator()
            viewModel.fetchLapanganData(todayDate)
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun showLoadingIndicator() {
        binding.progressBarShimmer.startShimmer()
        binding.progressBarShimmer.visibility = View.VISIBLE
        binding.rvHomefrag.visibility = View.GONE
    }

    private fun hideLoadingIndicator() {
        binding.progressBarShimmer.stopShimmer()
        binding.progressBarShimmer.visibility = View.GONE
        binding.rvHomefrag.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        // Hentikan animasi refresh ketika fragment ini kehilangan fokus
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Pastikan untuk menghentikan animasi refresh ketika view dihancurkan
        binding.swipeRefreshLayout.isRefreshing = false
    }

    private fun showError(message: String?) {
        Log.e("HomeFragment", "Error fetching lapangan data: $message")
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
    }


    private fun navigateToDetailFragment(id: String, tanggal: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(id, tanggal)
        findNavController().navigate(action)
    }

    private fun openGoogleMapsWithLink(link: String) {
        val gmmIntentUri = Uri.parse(link)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(
                requireContext(),
                "No application available to open the link",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleLogout() {
        authPreferences.clearUserData()
        Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), AutentikasiActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun handleAdminAction() {
        Toast.makeText(requireContext(), "Admin clicked", Toast.LENGTH_SHORT).show()
    }

}