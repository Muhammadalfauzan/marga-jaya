package com.example.kamandanoe.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.kamandanoe.R
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.LapanganModel
import com.example.kamandanoe.databinding.FragmentDetailBinding

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

    private var currentLapangan: LapanganModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

//    override fun onResume() {
//        super.onResume()
//     //   toggleBottomNavVisibility(View.GONE)
//    }
//
//    override fun onPause() {
//        super.onPause()
//     //   toggleBottomNavVisibility(View.VISIBLE)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        val lapanganId = args.id
        val rawDate = args.tanggal.ifEmpty { getTodayDate() }
        val formattedDate = convertToRequiredDateFormat(rawDate)
        observeLapanganData(lapanganId, formattedDate)
        binding.tglDetailfrag.text = formattedDate
        setupBookingButton(lapanganId, formattedDate)
        observePaymentResult()
        setupToolbar()
    }

    private fun convertToRequiredDateFormat(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("EEE MMM dd yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "Detail"
            show()
        }
    }

//    private fun toggleBottomNavVisibility(visibility: Int) {
//        val bottomNav = requireActivity().findViewById<View>(R.id.bottomNavigationView)
//        bottomNav.visibility = visibility
//    }

    private fun observeLapanganData(id: String, tanggal: String) {
        detailViewModel.getLapById(id, tanggal).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    resource.data?.let {
                        currentLapangan = it
                        updateUI(it)
                    }
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
            currentLapangan?.let {
                showCustomBookingDialog(it, idLapang, dateByStatus)
            } ?: showError("Data lapangan tidak tersedia.")
        }
    }

    private fun showCustomBookingDialog(lapangan: LapanganModel, idLapang: String, dateByStatus: String) {
        val dialogFragment = CustomBookingDialogFragment(lapangan, idLapang, dateByStatus) { paymentModel ->
            paymentViewModel.processPayment(paymentModel)
        }
        dialogFragment.show(parentFragmentManager, "CustomBookingDialogFragment")
    }

    private fun observePaymentResult() {
        paymentViewModel.paymentResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> showLoading(true)
                is Resource.Success -> {
                    showLoading(false)
                    result.data?.data?.redirectUrl?.let { intentMidtrans(it) }
                    Toast.makeText(requireContext(), "Berhasil melakukan booking lapangan", Toast.LENGTH_SHORT).show()
                    updateBookingButtonState()
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

    private fun updateBookingButtonState() {
        binding.btnPesan.isEnabled = false
        binding.btnPesan.text = "Pesanan Berhasil"
        binding.tvStatusdetailfrag.text = "TIDAK TERSEDIA"
        binding.tvStatusdetailfrag.setTextColor(resources.getColor(R.color.primary, null))
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

            // Prepare image list for ImageSlider
            val imageList = ArrayList<SlideModel>()

            // Ensure lapangan.imageUrls is not null or empty
            if (!lapangan.imageUrls.isNullOrEmpty()) {
                // Add each image URL to the image list as a SlideModel
                lapangan.imageUrls.forEach { imageUrl ->
                    imageList.add(SlideModel(imageUrl, scaleType = ScaleTypes.CENTER_CROP))
                }

                // Set image list to ImageSlider with auto-cycle enabled
                binding.imageSlider.setImageList(imageList)
            } else {
                // Handle case if no images are available (optional)
                binding.imageSlider.setImageList(imageList) // Empty image list
            }
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
