package com.example.margajaya.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.margajaya.MainActivity
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.LapanganModel
import com.example.margajaya.core.domain.model.PaymentModel
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
    private val paymentViewModel: PaymentViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lapanganId = args.id
        val tanggal = args.tanggal.ifEmpty { getTodayDate() }
        observeLapanganData(lapanganId, tanggal)

        binding.tglDetailfrag.text = tanggal
        setupBookingButton(lapanganId, tanggal)
        observePaymentResult()
        setupToolbar()

        toggleBottomNavVisibility(View.GONE)
    }

    override fun onResume() {
        super.onResume()
        toggleBottomNavVisibility(View.GONE)
    }

    override fun onPause() {
        super.onPause()
        toggleBottomNavVisibility(View.VISIBLE)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "Detail"
            show()
        }
    }

    private fun toggleBottomNavVisibility(visibility: Int) {
        val bottomNav = requireActivity().findViewById<View>(R.id.bottom_nav)
        bottomNav.visibility = visibility
    }

    private fun observeLapanganData(id: String, tanggal: String) {
        detailViewModel.getLapById(id, tanggal).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    resource.data?.let { updateUI(it) }
                }
                is Resource.Error -> {
                    showLoading(false)
                    showError(resource.message)
                }
            }
        }
    }

    private fun setupBookingButton(idLapang: String, dateByStatus: String) {
        binding.btnPesan.setOnClickListener {
            val paymentModel = PaymentModel(id_lapangan = idLapang, tanggal = dateByStatus)
            paymentViewModel.processPayment(paymentModel)
        }
    }

    private fun observePaymentResult() {
        paymentViewModel.paymentResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    result.data?.data?.redirectUrl?.let { intentMidtrans(it) }
                    Toast.makeText(requireContext(), "Berhasil melakukan booking lapangan", Toast.LENGTH_SHORT).show()
                    binding.btnPesan.isEnabled = false
                    binding.btnPesan.text = "Pesanan Berhasil"
                    // Ubah status lapangan menjadi "Tidak Tersedia"
                    binding.tvStatusdetailfrag.text = "TIDAK TERSEDIA"
                    binding.tvStatusdetailfrag.setTextColor(resources.getColor(R.color.primary, null))
                }
                is Resource.Error -> {
                    showLoading(false)
                    showError(result.message)
                }
            }
        }
    }

    private fun intentMidtrans(redirectUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
        startActivity(intent)
    }

    private fun updateUI(lapangan: LapanganModel) {
        binding.apply {
            tvStatusdetailfrag.text = if (lapangan.available) "TERSEDIA" else "TIDAK TERSEDIA"
            tvStatusdetailfrag.setTextColor(
                resources.getColor(if (lapangan.available) R.color.green else R.color.primary, null)
            )
            tvJenisdeatailfrag.text = lapangan.jenisLapangan
            tvHargadetailfrag.text = "Rp.${lapangan.harga}/sesi"
            tvJamdetailfrag.text = "${lapangan.jamMulai} - ${lapangan.jamBerakhir}"
            tvDescdetailfrag.text = lapangan.deskripsi
            btnPesan.isEnabled = lapangan.available

            val imageUrl = lapangan.imageUrls.firstOrNull()
            Glide.with(this@DetailFragment)
                .load(imageUrl)
                .placeholder(R.drawable.appmarga)
                .error(R.drawable.appmarga)
                .into(imgSlider)
        }
    }

    private fun getTodayDate(): String {
        val dateFormat = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String?) {
        Toast.makeText(requireContext(), message ?: "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
    }
}
