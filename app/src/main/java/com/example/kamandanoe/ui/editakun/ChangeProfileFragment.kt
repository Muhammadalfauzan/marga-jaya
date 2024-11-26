package com.example.kamandanoe.ui.editakun

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kamandanoe.R
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.ProfileModel
import com.example.kamandanoe.core.domain.model.UpdateUserModel
import com.example.kamandanoe.databinding.FragmentChangeProfileBinding
import com.example.kamandanoe.ui.profile.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeProfileFragment : Fragment() {

    private var _binding: FragmentChangeProfileBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    private var originalName: String? = null
    private var originalTelp: String? = null
    private var isUserUpdated: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        observeViewModel()
        setupTextWatchers()
        setupSaveButton()
    }

    private fun observeViewModel() {
        // Observasi profil pengguna
        userViewModel.getUserProfile(forceFetch = false).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> handleProfileSuccess(resource.data)
                is Resource.Error -> showToast(resource.message ?: "Terjadi kesalahan.")
                is Resource.Loading -> Unit // Tidak perlu loading untuk user profile
            }
        }

        // Observasi hasil pembaruan pengguna menggunakan SingleLiveEvent
        userViewModel.updateUserResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> {
                    hideLoading()
                    showUpdateSuccessDialog()
                    // Setelah pembaruan berhasil, muat ulang data profil
                    userViewModel.getUserProfile(forceFetch = true)
                }
                is Resource.Error -> {
                    hideLoading()
                    showToast(resource.message ?: "Terjadi kesalahan.")
                }
            }
        }
    }
    // Function to show AlertDialog when the update is successful
    private fun showUpdateSuccessDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Pembaruan Berhasil")
            .setMessage("Profil Anda berhasil diperbarui. Klik OK untuk kembali.")
            .setPositiveButton("OK") { _, _ ->
                // Navigate back to the previous fragment
                findNavController().popBackStack()
            }
                 // Prevent dialog from being dismissed by touching outside
            .create()

        dialog.show()
    }
    private fun handleProfileSuccess(profile: ProfileModel?) {
        profile?.let {
            originalName = it.name
            originalTelp = it.noTelp

            binding.edUsername.setText(it.name)
            binding.edEmail.setText(it.email)
            binding.edTelp.setText(it.noTelp)

            binding.edEmail.isEnabled = false
        }
    }
    private fun setupSaveButton() {
        binding.includeCustomButton.btnSave.setOnClickListener {
            if (!validateInputs()) {
                showToast("Input tidak valid.")
                return@setOnClickListener
            }

            val updateUserRequest = UpdateUserModel(
                email = binding.edEmail.text.toString().trim(),
                name = binding.edUsername.text.toString().trim(),
                no_telp = binding.edTelp.text.toString().trim()
            )
            userViewModel.updateUser(updateUserRequest) // Memulai proses pembaruan
        }
    }
    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = checkForChanges()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        binding.edUsername.addTextChangedListener(textWatcher)
        binding.edTelp.addTextChangedListener(textWatcher)
    }

    private fun checkForChanges() {
        val isNameChanged = binding.edUsername.text.toString().trim() != originalName
        val isTelpChanged = binding.edTelp.text.toString().trim() != originalTelp
        isUserUpdated = isNameChanged || isTelpChanged
        updateButtonState()
    }

    private fun updateButtonState() {
        binding.includeCustomButton.btnSave.isEnabled = isUserUpdated
        val buttonColor = if (isUserUpdated) {
            ContextCompat.getColor(requireContext(), R.color.primary)
        } else {
            ContextCompat.getColor(requireContext(), R.color.second)
        }
        binding.includeCustomButton.btnSave.setBackgroundColor(buttonColor)
    }



    private fun showLoading() {
        // Show the Lottie animation and start the animation
        binding.includeCustomButton.btnAnimation.visibility = View.VISIBLE
        binding.includeCustomButton.btnAnimation.playAnimation()

        // Change the text on the TextView
        binding.includeCustomButton.tvLoading.text = "Loading..."

        // Disable the button to prevent further clicks during loading
        binding.includeCustomButton.btnSave.isEnabled = false
    }

    private fun hideLoading() {
        // Hide the Lottie animation and stop the animation
        binding.includeCustomButton.btnAnimation.visibility = View.GONE
        binding.includeCustomButton.btnAnimation.cancelAnimation()

        // Reset the text on the TextView
        binding.includeCustomButton.tvLoading.text = "Lanjutkan"

        // Enable the button after the loading is complete
        binding.includeCustomButton.btnSave.isEnabled = true
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun validateInputs(): Boolean {
        return binding.edUsername.text.toString().trim().isNotEmpty() &&
                binding.edTelp.text.toString().trim().isNotEmpty()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
