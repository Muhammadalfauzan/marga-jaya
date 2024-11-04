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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.margajaya.AutentikasiActivity
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.local.preferences.AuthPreferences
import com.example.margajaya.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat

import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var lapanganAdapter: AdapterHome
    private var pickerDate: String? = null
    @Inject
    lateinit var authPreferences: AuthPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setupTopBar()
        setupRecyclerView()
        observeLapanganData()
        setupDatePickerButton()
        return binding.root
    }

    private fun setupTopBar() {
        binding.topBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
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
    }

    private fun setupRecyclerView() {
        binding.rvHomefrag.layoutManager = LinearLayoutManager(requireContext())
        lapanganAdapter = AdapterHome { lapangan ->
            navigateToDetailFragment(lapangan.id,pickerDate?:"")
            Toast.makeText(requireContext(), "Detail ${lapangan.jenisLapangan}", Toast.LENGTH_SHORT).show()
        }
        binding.rvHomefrag.adapter = lapanganAdapter
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
                    showError(resource.message)
                }
            }
        }
    }

    private fun showLoadingIndicator() {
        binding.progressBar.visibility = View.VISIBLE
        Log.d("HomeFragment", "Fetching lapangan data...")
    }

    private fun hideLoadingIndicator() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String?) {
        Log.e("HomeFragment", "Error fetching lapangan data: $message")
        Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
    }

    private fun setupDatePickerButton() {
        binding.btnDate.setOnClickListener { showDatePicker() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                pickerDate = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault()).format(selectedDate.time)
                viewModel.fetchLapanganData(pickerDate!!) // Fetch data dengan tanggal yang dipilih
                Toast.makeText(requireContext(), "Tanggal terpilih: $pickerDate", Toast.LENGTH_SHORT).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
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
            Toast.makeText(requireContext(), "No application available to open the link", Toast.LENGTH_SHORT).show()
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