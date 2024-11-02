package com.example.margajaya.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.LapanganModel
import com.example.margajaya.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs() // Use navArgs to get arguments

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        // Ambil id dan tanggal dari argumen navigasi
        val lapanganId = args.id
        val tanggal = args.tanggal.ifEmpty { getBookingDate() }
        Log.d("DetailFragment", "Fetching lapangan detail for ID: $lapanganId on date: $tanggal")

        observeLapanganData(lapanganId, tanggal) // Observe the data from ViewModel
        binding.tglDetailfrag.text = tanggal
        return binding.root
    }

    // Observe LiveData for lapangan data
    private fun observeLapanganData(id: String, tanggal: String) {
        detailViewModel.getLapById(id, tanggal).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("DetailFragment", "Loading lapangan data...")
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    resource.data?.let { lapangan ->
                        updateUI(lapangan) // Update UI with lapangan data
                        Log.d("DetailFragment", "Lapangan data loaded: $lapangan")
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Error: ${resource.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("DetailFragment", "Error loading data: ${resource.message}")
                }
            }
        }
    }

    // Update UI with lapangan data
    private fun updateUI(lapangan: LapanganModel) {
        binding.apply {
            // Update ketersediaan
            tvStatusdetailfrag.text = if (lapangan.available) "TERSEDIA" else "TIDAK TERSEDIA"
            tvStatusdetailfrag.setTextColor(
                resources.getColor(
                    if (lapangan.available) R.color.green else R.color.primary,
                    null
                )
            )

            // Update jenis lapangan
            tvJenisdeatailfrag.text = lapangan.jenisLapangan

            // Update harga
            tvHargadetailfrag.text = "Rp.${lapangan.harga}/sesi"

            // Update jam mulai dan berakhir
            tvJamdetailfrag.text = "${lapangan.jamMulai} - ${lapangan.jamBerakhir}"

            // Update deskripsi (asumsikan jenisLapangan sebagai deskripsi jika tidak ada kolom deskripsi yang lain)
            tvDescdetailfrag.text = lapangan.deskripsi

            // Update tombol pesan (aktifkan jika available)
            btnPesan.isEnabled = lapangan.available

            // Load image using Glide (ambil gambar pertama, jika ada)
            val imageUrl = lapangan.imageUrls.firstOrNull()
            Glide.with(this@DetailFragment)
                .load(imageUrl)
                .placeholder(R.drawable.appmarga) // Placeholder jika gambar belum tersedia
                .error(R.drawable.appmarga) // Gambar error jika gagal memuat
                .into(imgSlider)
        }
    }

    private fun getBookingDate(): String {
        val dateFormat = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks by clearing binding
    }
}
