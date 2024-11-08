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
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.margajaya.AutentikasiActivity
import com.example.margajaya.MainActivity
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.local.preferences.AuthPreferences
import com.example.margajaya.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var lapanganAdapter: AdapterHome
    private var pickerDate: String? = null

    @Inject
    lateinit var authPreferences: AuthPreferences
    private lateinit var bottomNavigationView: AnimatedBottomBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTopBar()
        setupRecyclerView()
        observeLapanganData()
        setupBottomNavigationView()
    }
    private fun setupBottomNavigationView() {
        bottomNavigationView = (activity as MainActivity).binding.bottomNav
    }
    private fun setupTopBar() {
        val mainActivity = activity as AppCompatActivity // Pastikan ada id topBar di FragmentHomeBinding
        mainActivity.supportActionBar?.apply {
            title = "Home" // Set judul sesuai kebutuhan
        }

        // Akses tombol "btnDate" di Toolbar melalui MainActivity dan set OnClickListener
        val btnDate = mainActivity.findViewById<ImageButton>(R.id.btn_date)
        btnDate.visibility = View.VISIBLE
        btnDate.setOnClickListener { showDatePicker() }
    }

    private fun setupRecyclerView() {
        binding.rvHomefrag.layoutManager = LinearLayoutManager(requireContext())
        lapanganAdapter = AdapterHome { lapangan ->
            navigateToDetailFragment(lapangan.id, pickerDate ?: "")
            Toast.makeText(requireContext(), "Detail ${lapangan.jenisLapangan}", Toast.LENGTH_SHORT)
                .show()
        }
        binding.rvHomefrag.adapter = lapanganAdapter
        setupRecyclerViewScrollListener()
    }

    private fun setupRecyclerViewScrollListener() {
        binding.rvHomefrag.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                handleBottomNavigationVisibility(dy)
            }
        })
    }

    private fun handleBottomNavigationVisibility(dy: Int) {
        if (dy > 0 && bottomNavigationView.translationY == 0f) {
            // Scroll ke bawah - geser BottomNavigationView keluar dari layar
            bottomNavigationView.animate()
                .translationY(bottomNavigationView.height.toFloat())
                .setDuration(200)
                .start()
        } else if (dy < 0 && bottomNavigationView.translationY == bottomNavigationView.height.toFloat()) {
            // Scroll ke atas - kembalikan BottomNavigationView ke posisi semula
            bottomNavigationView.animate()
                .translationY(0f)
                .setDuration(200)
                .start()
        }
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

 /*   private fun setupDatePickerButton() {
        binding.btnDate.setOnClickListener { showDatePicker() }
    }*/

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                pickerDate = SimpleDateFormat(
                    "EEE MMM dd yyyy",
                    Locale.getDefault()
                ).format(selectedDate.time)
                viewModel.fetchLapanganData(pickerDate!!) // Fetch data dengan tanggal yang dipilih
                Toast.makeText(
                    requireContext(),
                    "Tanggal terpilih: $pickerDate",
                    Toast.LENGTH_SHORT
                ).show()
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