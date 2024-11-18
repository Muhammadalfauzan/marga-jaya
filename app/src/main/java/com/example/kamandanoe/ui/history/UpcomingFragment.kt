package com.example.kamandanoe.ui.history

import android.app.AlertDialog
import android.content.Intent
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kamandanoe.AutentikasiActivity
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.BookingItemModel
import com.example.kamandanoe.databinding.FragmentFinishedBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale


@AndroidEntryPoint
class UpcomingFragment : Fragment() {

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!
    private val bookingViewModel: BookingViewModel by viewModels({ requireParentFragment() })
    // Adapter dengan click listener untuk navigasi
    private val adapterBooking by lazy {
        AdapterBooking { bookingItem ->
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
        observeViewModel()
        if (bookingViewModel.upcomingBookings.value == null) {
            bookingViewModel.getAllBookings() // Panggil jika belum ada data
        }

    }
    private fun navigateToDetail(bookingItem: BookingItemModel) {
        // Periksa status pembayaran
        if (bookingItem.status == "success") {
            // Format sesi: "jamMulai - jamBerakhir"
            val sesi = "${bookingItem.jamMulai ?: ""} - ${bookingItem.jamBerakhir ?: ""}"

            // Format tanggal booking dan tanggal pesan sesuai dengan gambar
            val sformat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())

            // Format tanggal booking
            val bookingDate = bookingItem.tanggal?.let {
                try {
                    val parsedDate = inputFormat.parse(it)
                    parsedDate?.let { date -> sformat.format(date) } ?: "Tanggal tidak tersedia"
                } catch (e: ParseException) {
                    "Tanggal tidak tersedia"
                }
            } ?: "Tanggal tidak tersedia"

            // Format tanggal pesan
            val bookingOrderDate = bookingItem.createdAt?.let {
                try {
                    val parsedDate = inputFormat.parse(it)
                    parsedDate?.let { date -> sformat.format(date) } ?: "Tanggal tidak tersedia"
                } catch (e: ParseException) {
                    "Tanggal tidak tersedia"
                }
            } ?: "Tanggal tidak tersedia"

            // Gunakan Safe Args untuk navigasi ke BuktiFragment dengan argumen yang sesuai
            val action = HistoryFragmentDirections.actionHistoryFragmentToBuktiFragment(
                jenisLapangan = bookingItem.jenisLapangan ?: "Jenis tidak tersedia",
                tglBooking = bookingOrderDate, // Tanggal pesan
                sesi = sesi, // Mengirim sesi yang diformat
                harga = bookingItem.harga ?: 0,
                hari = bookingDate // Tanggal booking
            )

            findNavController().navigate(action)
        } else {
            // Jika pembayaran belum sukses, buka link Midtrans
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(bookingItem.paymentLink))
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.rvFinished.apply {
            adapter = adapterBooking
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeViewModel() {
        bookingViewModel.upcomingBookings.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    adapterBooking.submitList(resource.data)
                    binding.isEmpty.visibility = if (resource.data.isNullOrEmpty()) View.VISIBLE else View.GONE
                }
                is Resource.Error -> {
                    hideLoading()
                    showError(resource.message)
                }
            }
        }

        bookingViewModel.showSessionExpiredDialog.observe(viewLifecycleOwner) { shouldShow ->
            Log.d("UpcomingFragment", "showSessionExpiredDialog observed: $shouldShow")
            if (shouldShow) {
                showSessionExpiredDialog()
                bookingViewModel.resetSessionExpiredState()
            }
        }
    }

    private fun showSessionExpiredDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Session Expired")
            .setMessage("Your session has expired. Please log in again.")
            .setPositiveButton("Login") { _, _ ->
                val intent = Intent(requireContext(), AutentikasiActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
            .setCancelable(false)
            .show()
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        Log.d("Upcoming", "Loading booking data...")
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String?) {
        Log.e("UpcomingFragment", "Error loading data: $message")
        Toast.makeText(requireContext(), message ?: "Failed to load data", Toast.LENGTH_LONG).show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}