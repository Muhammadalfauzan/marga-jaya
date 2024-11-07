package com.example.margajaya.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.margajaya.AutentikasiActivity
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.local.preferences.AuthPreferences
import com.example.margajaya.core.domain.model.UpdateUserModel
import com.example.margajaya.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


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

        observeUserProfile()
        setupSwipeToRefresh()
        binding.btnLogout.setOnClickListener {
            authPreferences.clearUserData()
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), AutentikasiActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            userViewModel.clearProfileCache()
            // Lakukan tindakan lain seperti navigasi ke layar login
        }

        binding.btnSave.setOnClickListener {
            updateUser()
        }
    }
    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            userViewModel.clearProfileCache()
            observeUserProfile() // Fetch ulang data
            binding.swipeRefreshLayout.isRefreshing = false // Hentikan refresh animation setelah data selesai diambil
        }
    }
    private fun observeUserProfile() {
        userViewModel.getUserProfile().observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Tampilkan loading indicator
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("ProfileFragment", "Loading profile data...")
                }
                is Resource.Success -> {
                    // Sembunyikan loading indicator
                    binding.progressBar.visibility = View.GONE
                    val profile = resource.data
                    if (profile != null) {
                        // Update UI dengan data profile
                        binding.edUsername.setText(profile.name)
                        binding.edEmail.setText(profile.email)
                        binding.edEmail.isEnabled = false
                        binding.edTelp.setText(profile.noTelp)
                        Log.d("ProfileFragment", "Profile data fetched successfully: $profile")
                    }
                }
                is Resource.Error -> {
                    // Sembunyikan loading indicator dan tampilkan pesan error
                    binding.progressBar.visibility = View.GONE
                    Log.e("ProfileFragment", "Error fetching profile: ${resource.message}")
                    Toast.makeText(
                        context,
                        resource.message ?: "Error fetching profile",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    private fun updateUser() {
        val name = binding.edUsername.text.toString()
        val noTelp = binding.edTelp.text.toString()
        val password = binding.edPassLama.text.toString()
        val newPassword = binding.edPassBaru.text.toString()
        val updateUserRequest = UpdateUserModel(
            email = binding.edEmail.text.toString(), // Email tidak bisa diubah
            name = name,
            no_telp = noTelp,
            password = password,
            new_password = newPassword
        )

        userViewModel.updateUser(updateUserRequest).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("ProfileFragment", "Updating user data...")
                }

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "User berhasil diupdate", Toast.LENGTH_SHORT)
                        .show()
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e("ProfileFragment", "Error updating user: ${resource.message}")
                    Toast.makeText(
                        context,
                        resource.message ?: "Error updating user",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



