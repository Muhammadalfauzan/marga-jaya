package com.example.margajaya.ui.history

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.margajaya.R
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
            // Handle item click, if needed
            Toast.makeText(requireContext(), "Clicked on ${bookingItem.jenisLapangan}", Toast.LENGTH_SHORT).show()
            // You can also navigate to a details screen if necessary
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

        setupRecyclerView()
        observeBookingData()

        // Trigger fetch all bookings
        bookingViewModel.getAllBookings()
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
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("HistoryFragment", "Loading booking data...")
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapterBooking.submitList(resource.data?.flatMap { it.bookings ?: emptyList() })
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e("HistoryFragment", "Error loading data: ${resource.message}")
                    Toast.makeText(requireContext(), resource.message ?: "Failed to load data", Toast.LENGTH_LONG).show()
                }
            }
        }
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
