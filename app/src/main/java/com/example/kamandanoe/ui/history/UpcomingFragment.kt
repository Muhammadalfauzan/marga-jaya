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
import com.example.kamandanoe.core.utils.NetworkMonitor
import com.example.kamandanoe.databinding.FragmentUpcomingBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class UpcomingFragment : Fragment() {

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private val bookingViewModel: BookingViewModel by viewModels()

    @Inject
    lateinit var networkStatusListener: NetworkMonitor

    private val adapterBooking by lazy {
        AdapterBooking(requireContext()) { bookingItem ->
            navigateToDetail(bookingItem)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        networkStatusListener.networkStatus.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                setupRecyclerView()
                observeViewModel()
                if (bookingViewModel.upcomingBookings.value == null) {
                    fetchBookings() // Ambil data saat pertama kali
                }
            } else {
                showNoInternetMessage()
            }
        }
    }

    fun fetchBookings() {
        bookingViewModel.getAllBookings() // Pastikan fungsi ini tersedia di ViewModel
    }

    private fun showNoInternetMessage() {
        Toast.makeText(requireContext(), "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToDetail(bookingItem: BookingItemModel) {
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
    }

    private fun setupRecyclerView() {
        binding.rvUpcoming.apply {
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
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String?) {
        Toast.makeText(requireContext(), message ?: "Failed to load data", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
