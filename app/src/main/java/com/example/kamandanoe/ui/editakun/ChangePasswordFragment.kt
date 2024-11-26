package com.example.kamandanoe.ui.editakun

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.kamandanoe.databinding.FragmentChangePasswordBinding
import com.example.kamandanoe.ui.profile.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserViewModel by activityViewModels()
    private var userEmail: String? = null
    private var userName: String? = null
    private var userPhone: String? = null
    private var isUserUpdated = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        observeUserData()
        setupTextWatchers()
        setupSaveButton()
    }

    private fun observeUserData() {
        // Observasi data pengguna (email, name, no_telp)
        userViewModel.getUserProfile(forceFetch = false).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> handleProfileSuccess(resource.data)
                is Resource.Error -> handlePasswordError(resource.message)
                is Resource.Loading -> Unit // Tidak perlu loading untuk user profile
            }
        }
    }

    private fun handleProfileSuccess(profile: ProfileModel?) {
        profile?.let {
            userEmail = it.email
            userName = it.name
            userPhone = it.noTelp
            // Dapatkan password lama dari profil

            // Sekarang kita sudah memiliki semua data yang diperlukan
        }
    }

    private fun setupTextWatchers() {
        binding.edPassLama.addTextChangedListener(createTextWatcher())
        binding.edPassBaru.addTextChangedListener(createTextWatcher())
        binding.edPassBaruUlang.addTextChangedListener(createTextWatcher())

        // Pastikan tombol simpan dimulai dalam keadaan nonaktif
        binding.includeCustomButtonPass.btnSave.isEnabled = false

        // Update warna tombol setelah di-disable
        updateButtonState()
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateInputs()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun validateInputs() {
        val oldPassword = binding.edPassLama.text.toString().trim()
        val newPassword = binding.edPassBaru.text.toString().trim()
        val confirmPassword = binding.edPassBaruUlang.text.toString().trim()

        // Validasi jika semua field sudah diisi
        val isInputValid = oldPassword.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()

        // Validasi jika password baru cocok dengan konfirmasi password
        val isPasswordMatch = newPassword == confirmPassword

        // Validasi jika password baru tidak sama dengan password lama
        val isPasswordChanged = oldPassword != newPassword

        // Mengaktifkan tombol simpan hanya jika kondisi valid
        isUserUpdated = isInputValid && isPasswordMatch && isPasswordChanged

        // Mengatur tombol simpan
        binding.includeCustomButtonPass.btnSave.isEnabled = isUserUpdated
        updateButtonState()
    }

    private fun updateButtonState() {
        // Tombol simpan hanya aktif jika semua input valid
        val buttonColor = if (isUserUpdated) {
            ContextCompat.getColor(requireContext(), R.color.primary)
        } else {
            ContextCompat.getColor(requireContext(), R.color.second)
        }

        // Update warna tombol
        binding.includeCustomButtonPass.btnSave.setBackgroundColor(buttonColor)
    }

    private fun setupSaveButton() {
        binding.includeCustomButtonPass.btnSave.setOnClickListener {
            // Pastikan tombol simpan dalam keadaan aktif
            if (!binding.includeCustomButtonPass.btnSave.isEnabled) return@setOnClickListener

            val oldPassword = binding.edPassLama.text.toString().trim()
            val newPassword = binding.edPassBaru.text.toString().trim()

            // Validasi panjang password baru
            if (newPassword.length < 8) {
                Toast.makeText(context, "Password baru minimal 8 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            showLoading()
            changePassword(oldPassword, newPassword) { isSuccess ->
                hideLoading()
                if (isSuccess) {
                    showSuccessDialog()
                } else {
                    Toast.makeText(context, "Gagal mengubah password", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun changePassword(oldPassword: String, newPassword: String, callback: (Boolean) -> Unit) {
        val updateUserRequest = UpdateUserModel(
            email = userEmail ?: "",  // Pastikan email tersedia
            name = userName ?: "",    // Pastikan nama tersedia
            no_telp = userPhone ?: "", // Pastikan no_telp tersedia
            password = oldPassword,   // Kirim password lama untuk validasi
            new_password = newPassword // Kirim password baru
        )

        userViewModel.updateUser(updateUserRequest).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> callback(true)
                is Resource.Error -> {
                    handlePasswordError(resource.message)
                    callback(false)
                }
                is Resource.Loading -> showLoading()
            }
        }
    }

    private fun handlePasswordError(message: String?) {
        Toast.makeText(requireContext(), message ?: "Terjadi kesalahan saat mengubah password", Toast.LENGTH_SHORT).show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Berhasil")
            .setMessage("Password Anda berhasil diubah.")
            .setPositiveButton("OK") { _, _ ->
                // Kembali ke fragment sebelumnya
                findNavController().popBackStack()
            }
            .setCancelable(false)
            .show()
    }

    private fun showLoading() {
        binding.includeCustomButtonPass.btnAnimation.visibility = View.VISIBLE
        binding.includeCustomButtonPass.btnAnimation.playAnimation()
        binding.includeCustomButtonPass.tvLoading.text = "Loading..."
        binding.includeCustomButtonPass.btnSave.isEnabled = false
    }

    private fun hideLoading() {
        binding.includeCustomButtonPass.btnAnimation.visibility = View.GONE
        binding.includeCustomButtonPass.btnAnimation.cancelAnimation()
        binding.includeCustomButtonPass.tvLoading.text = "Lanjutkan"
        binding.includeCustomButtonPass.btnSave.isEnabled = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        _binding = null
    }
}
