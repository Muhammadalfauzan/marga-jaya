package com.example.kamandanoe.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kamandanoe.AutentikasiActivity
import com.example.kamandanoe.R
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.ProfileModel
import com.example.kamandanoe.core.domain.model.UpdateUserModel
import com.example.kamandanoe.core.domain.preferences.AuthPreferences
import com.example.kamandanoe.databinding.FragmentProfileBinding
import com.example.kamandanoe.ui.history.BookingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class ProfileFragment : Fragment() {
    @Inject
    lateinit var authPreferences: AuthPreferences

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
       // setupToolbar()
        setupListeners()
        observeUserProfile(forceFetch = false) // Ambil data profil saat pertama kali
        setupFragmentResultListener()         // Untuk menerima hasil dari com.example.kamandanoe.ui.editakun.ChangeProfileFragment
    }

    // Menyiapkan tombol-tombol dan navigasi
    private fun setupListeners() {
        binding.tvEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changeProfileFragment)
        }

        binding.tvGantiPassword.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_changePasswordFragment)
        }

        binding.notifikasi.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_notifikasiFragment)
        }

        binding.helpCenter.setOnClickListener {
            val url = "https://makaryo-web.vercel.app/download"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }

        binding.btnLogout.setOnClickListener { performLogout() }
    }

    // Mendengarkan hasil dari fragment lain
    private fun setupFragmentResultListener() {
        parentFragmentManager.setFragmentResultListener("PROFILE_UPDATED", this) { _, bundle ->
            val isUpdated = bundle.getBoolean("IS_UPDATED", false)
            if (isUpdated) {
                val updatedName = bundle.getString("UPDATED_NAME", "")
                val updatedEmail = bundle.getString("UPDATED_EMAIL", "")
                val updatedTelp = bundle.getString("UPDATED_TELP", "")

                // Perbarui UI jika data baru tersedia
                if (updatedName.isNotEmpty()) binding.tvNamaProfile.text = updatedName
                if (updatedEmail.isNotEmpty()) binding.tvEmailProfile.text = updatedEmail
                // if (updatedTelp.isNotEmpty()) binding.tvNoTelpProfile.text = updatedTelp

                // Pastikan data lokal juga disinkronkan
                observeUserProfile(forceFetch = true)
            }
        }
    }

    // Toolbar setup
    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "Profile"
            show()
        }
    }

    // Observasi data profil dari ViewModel
    private fun observeUserProfile(forceFetch: Boolean = false) {
        userViewModel.getUserProfile(forceFetch).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (forceFetch) showLoading()
                }
                is Resource.Success -> {
                    hideLoading()
                    updateUI(resource.data)
                }
                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(context, resource.message ?: "Error fetching profile", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // Perbarui UI dengan data baru
    private fun updateUI(profile: ProfileModel?) {
        profile?.let {
            binding.tvNamaProfile.text = it.name
            binding.tvEmailProfile.text = it.email
            // if (it.noTelp.isNotEmpty()) binding.tvNoTelpProfile.text = it.noTelp
        }
    }

    // Fungsi logout
    private fun performLogout() {
        authPreferences.clearUserData()
        userViewModel.clearProfileCache()
        Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
        startActivity(Intent(requireContext(), AutentikasiActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    // Tampilkan loading indicator
    private fun showLoading() {
        binding.cardLoading.visibility = View.VISIBLE
        Log.d("ProfileFragment", "Loading indicator shown")
    }

    // Sembunyikan loading indicator
    private fun hideLoading() {
        binding.cardLoading.visibility = View.GONE
        Log.d("ProfileFragment", "Loading indicator hidden")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
