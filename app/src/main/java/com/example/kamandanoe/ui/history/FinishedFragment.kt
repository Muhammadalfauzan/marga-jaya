package com.example.kamandanoe.ui.history

import android.content.Intent
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.BookingItemModel
import com.example.kamandanoe.databinding.FragmentFinishedBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class FinishedFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val bookingViewModel: BookingViewModel by viewModels()

    private val adapterBooking by lazy {
        AdapterBooking(requireContext()) { bookingItem ->
            navigateToDetail(bookingItem)
        }
    }
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
            fetchBookings() // Ambil data jika belum ada
        }
    }

    fun fetchBookings() {
        bookingViewModel.getAllBookings() // Pastikan fungsi ini tersedia di ViewModel
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
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    adapterBooking.submitList(resource.data ?: emptyList())
                    binding.isEmpty.visibility = if (resource.data.isNullOrEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error -> {
                    hideLoading()
                    showError(resource.message)
                }
            }
        }
    }
    private fun navigateToDetail(bookingItem: BookingItemModel) =
        if (bookingItem.status == "success") {
            val sesi = "${bookingItem.jamMulai ?: ""} - ${bookingItem.jamBerakhir ?: ""}"
            val sformat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
            val bookingDate = bookingItem.tanggal?.let {
                try {
                    val parsedDate = inputFormat.parse(it)
                    parsedDate?.let { date -> sformat.format(date) } ?: "Tanggal tidak tersedia"
                } catch (e: ParseException) {
                    "Tanggal tidak tersedia"
                }
            } ?: "Tanggal tidak tersedia"

            val bookingOrderDate = bookingItem.createdAt?.let {
                try {
                    val parsedDate = inputFormat.parse(it)
                    parsedDate?.let { date -> sformat.format(date) } ?: "Tanggal tidak tersedia"
                } catch (e: ParseException) {
                    "Tanggal tidak tersedia"
                }
            } ?: "Tanggal tidak tersedia"

            val action = HistoryFragmentDirections.actionHistoryFragmentToBuktiFragment(
                jenisLapangan = bookingItem.jenisLapangan ?: "Jenis tidak tersedia",
                tglBooking = bookingOrderDate,
                sesi = sesi,
                harga = bookingItem.harga ?: 0,
                hari = bookingDate,
                name = bookingItem.name ?: "Nama pengguna"
            )

            findNavController().navigate(action)
        } else {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookingItem.paymentLink))
            startActivity(intent)
        }
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        Log.d("FinishedFragment", "Loading booking data...")
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String?) {
        Log.e("FinishedFragment", "Error loading data: $message")
        Toast.makeText(requireContext(), message ?: "Failed to load data", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
