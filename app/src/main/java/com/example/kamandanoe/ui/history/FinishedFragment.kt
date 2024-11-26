package com.example.kamandanoe.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.databinding.FragmentFinishedBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val bookingViewModel: BookingViewModel by viewModels({ requireParentFragment() })
    private val adapterBooking by lazy { AdapterBooking(requireContext()) { /* handle item click */ } }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observePastBookings()
        if (bookingViewModel.pastBookings.value == null) {
            bookingViewModel.getAllBookings() // Panggil jika belum ada data
        }

    }

    private fun setupRecyclerView() {
        binding.rvFinished.apply {
            adapter = adapterBooking
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observePastBookings() {
        bookingViewModel.pastBookings.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapterBooking.submitList(resource.data ?: emptyList())
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_LONG).show()
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
