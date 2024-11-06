package com.example.margajaya.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : Fragment() {

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
                        binding.nameProfile.text = profile.name
                        binding.emailProfile.text = profile.email
                        binding.telpProfile.text = profile.noTelp
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



