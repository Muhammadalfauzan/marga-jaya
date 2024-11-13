package com.example.margajaya.ui.autentikasi

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.example.margajaya.R
import com.example.margajaya.core.utils.NetworkUtils
import com.example.margajaya.databinding.TesBinding
import com.example.margajaya.ui.callsback.OnRetryListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoInternetBottomSheet(private val retryListener: OnRetryListener? = null) : BottomSheetDialogFragment() {

    private var _binding: TesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Mengatur tombol "Try Again"
        binding.btnTryAgain.setOnClickListener {
            // Periksa koneksi internet sebelum menjalankan onRetry dan menutup dialog
            if (NetworkUtils(requireContext()).isNetworkAvailable()) {
                retryListener?.onRetry() // Menjalankan callback jika disediakan
                dismiss() // Menutup dialog jika koneksi tersedia
            } else {
                // Tampilkan pesan atau lakukan sesuatu jika tidak ada koneksi
                Toast.makeText(requireContext(), "Tidak ada koneksi internet. Coba lagi.", Toast.LENGTH_SHORT).show()

                // Menambahkan animasi shake untuk memberikan umpan balik visual
                val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation)
                binding.root.startAnimation(shake)
            }
        }

        // Mengatur tombol "Settings"
        binding.btnSettings.setOnClickListener {
            val intent = Intent(Settings.ACTION_SETTINGS)
            startActivity(intent)
        }

        // Menutup dialog menggunakan ikon close
        binding.ivClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setWindowAnimations(R.style.CustomDialogAnimation) // Pastikan gaya animasi digunakan
        }
    }

    companion object {
        const val TAG = "NoInternetBottomSheet"
    }
}
