package com.example.kamandanoe.ui.home

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.kamandanoe.R
import com.example.kamandanoe.core.utils.RetryListener
import com.example.kamandanoe.databinding.DialogNoInternetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NoInternetBottomSheet(private val retryListener: RetryListener,  private val isNetworkAvailable: () -> Boolean) : BottomSheetDialogFragment() {
    private var _binding: DialogNoInternetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        isCancelable = false
        _binding = DialogNoInternetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnTryAgain.setOnClickListener {
            if (isNetworkAvailable()) {
                dismiss()
                retryListener.onRetry()
            } else {
                // Menambahkan animasi shake untuk memberikan umpan balik visual
                val shake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake_animation)
                binding.root.startAnimation(shake)
            }
        }

        binding.btnSettings.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        binding.ivClose.setOnClickListener {
            if (isNetworkAvailable()) {
                dismiss()
            } else {
                // Menutup aplikasi jika tidak ada koneksi
                requireActivity().finishAffinity() // Menutup semua aktivitas dalam tumpukan
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}