package com.example.margajaya.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.margajaya.core.data.Resource
import com.example.margajaya.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val bookingViewModel: BookingViewModel by viewModels()

    private val adapterBooking by lazy {
        AdapterBooking { bookingItem ->
            Toast.makeText(requireContext(), "Clicked on ${bookingItem.jenisLapangan}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupRecyclerView()
        observeBookingData()
        bookingViewModel.getAllBookings() // Trigger fetch bookings
    }

    private fun setupToolbar() {
        // Mengatur judul Toolbar di MainActivity
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "History"
            show()
        }
    }

    private fun setupRecyclerView() {
        binding.rvHistory.apply {
            adapter = adapterBooking
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeBookingData() {
        bookingViewModel.getAllBookings().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    adapterBooking.submitList(resource.data?.flatMap { it.bookings ?: emptyList() })
                }
                is Resource.Error -> {
                    hideLoading()
                    showError(resource.message)
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        Log.d("HistoryFragment", "Loading booking data...")
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String?) {
        Log.e("HistoryFragment", "Error loading data: $message")
        Toast.makeText(requireContext(), message ?: "Failed to load data", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
