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
import com.example.kamandanoe.core.domain.preferences.AuthPreferences
import com.example.kamandanoe.core.utils.NetworkUtils
import com.example.kamandanoe.databinding.FragmentHomeBinding
import com.example.kamandanoe.ui.autentikasi.NoInternetBottomSheet
import com.example.kamandanoe.ui.callsback.OnRetryListener
import dagger.hilt.android.AndroidEntryPoint
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.text.SimpleDateFormat
import java.util.Date
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
    lateinit var networkUtils: NetworkUtils
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
        setupSwipeRefresh()
        setupDateAndSessionPickers()

        binding.textViewDate.text = viewModel.pickerDate

    }
    private fun setupBottomNavigationView() {
        bottomNavigationView = (activity as MainActivity).binding.bottomNav
    }
    private fun setupTopBar() {
        val mainActivity = activity as AppCompatActivity // Pastikan ada id topBar di FragmentHomeBinding
        mainActivity.supportActionBar?.apply {
            title = "Home" // Set judul sesuai kebutuhan
        }

//        // Akses tombol "btnDate" di Toolbar melalui MainActivity dan set OnClickListener
//        val btnDate = mainActivity.findViewById<ImageButton>(R.id.btn_date)
//        btnDate.visibility = View.VISIBLE
//        btnDate.setOnClickListener { showDatePicker() }
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
    override fun onResume() {
        super.onResume()
        bottomNavigationView.viewTreeObserver.addOnGlobalLayoutListener {
            bottomNavigationView.translationY = 0f
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
                    if (resource.message == "No internet connection") {
                        showNoInternetDialog()
                    } else {
                        showError(resource.message)
                    }
                }
            }
        }
    }
    private fun showNoInternetDialog() {
        val noInternetBottomSheet = NoInternetBottomSheet(object : OnRetryListener {
            override fun onRetry() {
                // Periksa koneksi sebelum mencoba memuat data lagi
                if (networkUtils.isNetworkAvailable()) { // Menggunakan NetworkUtils yang diinject
                    // Jika koneksi tersedia, panggil ulang metode untuk fetch data
                    val todayDate = SimpleDateFormat("EEE,MMM dd yyyy", Locale.getDefault()).format(Date())
                    pickerDate = todayDate
                    viewModel.fetchLapanganData(pickerDate ?: "")
                } else {
                    // Tampilkan dialog atau feedback lain jika koneksi masih belum ada
                    Toast.makeText(requireContext(), "Tidak ada koneksi internet. Coba lagi.", Toast.LENGTH_SHORT).show()
                }
            }
        })
        noInternetBottomSheet.show(childFragmentManager, NoInternetBottomSheet.TAG)
    }


    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Set dan tampilkan tanggal hari ini di TextView
            val todayDate = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault()).format(Date())
            pickerDate = todayDate
            binding.textViewDate.text = todayDate


            // Perbarui tanggal di ViewModel agar sinkron dengan tampilan
            viewModel.updatePickerDate(todayDate)

            // Fetch data dengan tanggal yang diperbarui
            viewModel.fetchLapanganData(todayDate)
            // Pantau perubahan data dan hentikan animasi setelah data di-load
            viewModel.lapangan.observe(viewLifecycleOwner) { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        // Tampilkan animasi jika data sedang di-load
                        binding.swipeRefreshLayout.isRefreshing = true
                    }
                    is Resource.Success -> {
                        // Hentikan animasi saat data selesai di-load
                        binding.swipeRefreshLayout.isRefreshing = false
                        lapanganAdapter.submitList(resource.data)
                    }
                    is Resource.Error -> {
                        // Hentikan animasi saat ada error
                        binding.swipeRefreshLayout.isRefreshing = false
                        showError(resource.message)
                    }
                }
            }
        }
    }

    private fun setupDateAndSessionPickers() {
        setupDatePicker()
    }

    private fun setupDatePicker() {
        binding.textViewDate.setOnClickListener { showDatePicker() }
    }

   /* private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                pickerDate = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault()).format(selectedDate.time)
                // Mengupdate TextView dengan tanggal yang dipilih
                binding.textViewDate.text = pickerDate
                viewModel.fetchLapanganData(pickerDate!!)
                Toast.makeText(
                    requireContext(),
                    "Tanggal terpilih: $pickerDate",
                    Toast.LENGTH_SHORT
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
*/
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

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                pickerDate = SimpleDateFormat(
                    "EEE, MMM dd yyyy",
                    Locale.getDefault()
                ).format(selectedDate.time)

                val formattedDate = SimpleDateFormat("EEE, MMM dd yyyy", Locale.getDefault()).format(selectedDate.time)
                viewModel.updatePickerDate(formattedDate)
                binding.textViewDate.text = formattedDate
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