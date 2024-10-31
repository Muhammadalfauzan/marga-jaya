package com.example.margajaya.ui.home

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var lapanganAdapter: AdapterHome
    private var pickerDate: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.topBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.logout -> {
                   handleLogout()
                    true
                }

                R.id.location -> {
                    openGoogleMapsWithLink("https://maps.app.goo.gl/1fkt4sGrzVpF2uQR7")
                    true
                }

                R.id.admin -> {
                  handleAdminAction()
                    true
                }

                else -> false
            }
        }
        binding.btnDate.setOnClickListener {
            getDatePicker()
        }

        setupRecyclerView()
        observeLapanganData()

        return binding.root
    }

    private fun observeLapanganData() {
        viewModel.lapangan.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("HomeFragment", "Fetching lapangan data...")
                }
                is Resource.Success -> {
                    // Update adapter dengan data baru
                    binding.progressBar.visibility = View.GONE
                    lapanganAdapter.submitList(resource.data)
                }
                is Resource.Error -> {
                    // Tampilkan pesan error jika ada
                    binding.progressBar.visibility = View.GONE
                    Log.e("HomeFragment", "Error fetching lapangan data: ${resource.message}")
                    Toast.makeText(requireContext(), "Error: ${resource.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvHomefrag.layoutManager = LinearLayoutManager(requireContext())

        // Inisialisasi adapter dengan data awal kosong
        lapanganAdapter = AdapterHome { lapangan ->
            // Aksi ketika item diklik, misalnya membuka detail lapangan
            Toast.makeText(requireContext(), "Detail ${lapangan.jenisLapangan}", Toast.LENGTH_SHORT).show()
        }

        binding.rvHomefrag.adapter = lapanganAdapter
    }

    private fun getDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year: Int, month: Int, dayOfMonth: Int ->
                val selectedData = Calendar.getInstance()
                selectedData.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Ganti format jika perlu
                pickerDate = dateFormat.format(selectedData.time)

                // Panggil fungsi untuk mendapatkan data lapangan berdasarkan tanggal yang dipilih
                if (pickerDate != null) {
                    viewModel.fetchLapanganData(pickerDate!!) // Fetch data menggunakan ViewModel
                }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    private fun openGoogleMapsWithLink(link: String) {
        val gmmIntentUri = Uri.parse(link)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            Toast.makeText(requireContext(), "No application available to open the link", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleLogout() {
        // Tambahkan logika untuk logout
        Toast.makeText(requireContext(), "Logout clicked", Toast.LENGTH_SHORT).show()
    }

    private fun handleAdminAction() {
        // Tambahkan logika untuk membuka layar admin atau aksi admin lainnya
        Toast.makeText(requireContext(), "Admin clicked", Toast.LENGTH_SHORT).show()
    }
}