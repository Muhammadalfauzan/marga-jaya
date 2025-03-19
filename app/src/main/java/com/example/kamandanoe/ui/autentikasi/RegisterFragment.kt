package com.example.kamandanoe.ui.autentikasi

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kamandanoe.R
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.RegisterModel
import com.example.kamandanoe.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()
    private val inputValidator = InputValidator()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupListeners()
        binding.includeCustomButtonPass.btnLogin.isEnabled = false
        setButtonColor(isEnabled = false)
        binding.includeCustomButtonPass.tvLoading.text = getString(R.string.register)

        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun setupListeners() {
        binding.edUsername.addTextChangedListener(createTextWatcher(
            afterTextChanged = { inputValidator.validateUsername(binding.edUsername, binding.nameLay) }
        ))

        binding.edEmail.addTextChangedListener(createTextWatcher(
            afterTextChanged = { inputValidator.validateEmail(binding.edEmail, binding.emailLay) }
        ))

        binding.edPass.addTextChangedListener(createTextWatcher(
            afterTextChanged = { inputValidator.validatePassword(binding.edPass, binding.passLay) }
        ))

        binding.edConfirm.addTextChangedListener(createTextWatcher()) // Tidak perlu validasi konfirmasi password

        binding.edTelp.addTextChangedListener(createTextWatcher(
            afterTextChanged = { inputValidator.validateTel(binding.edTelp, binding.telLay) }
        ))

        // Aktifkan tombol hanya jika semua input telah diisi
        listOf(binding.edUsername, binding.edEmail, binding.edPass, binding.edConfirm, binding.edTelp).forEach {
            it.addTextChangedListener(createTextWatcher(onTextChanged = { validateForm() }))
        }

        binding.includeCustomButtonPass.btnLogin.setOnClickListener {
            if (validateForm()) {
                showProgressBar()
                performRegister()
            }
        }
    }

    private fun observeViewModel() {
        registerViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> showProgressBar()
                is Resource.Success -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), "Registrasi berhasil!", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(requireContext(), result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun createTextWatcher(
        onTextChanged: (() -> Unit)? = null,
        afterTextChanged: (() -> Unit)? = null
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onTextChanged?.invoke()
            }

            override fun afterTextChanged(s: Editable?) {
                afterTextChanged?.invoke()
            }
        }
    }

    private fun validateForm(): Boolean {
        val isUsernameFilled = binding.edUsername.text?.isNotEmpty() == true
        val isEmailFilled = binding.edEmail.text?.isNotEmpty() == true
        val isPasswordFilled = binding.edPass.text?.isNotEmpty() == true
        val isConfirmFilled = binding.edConfirm.text?.isNotEmpty() == true
        val isTelpFilled = binding.edTelp.text?.isNotEmpty() == true

        val isFormValid = isUsernameFilled && isEmailFilled && isPasswordFilled && isConfirmFilled && isTelpFilled

        binding.includeCustomButtonPass.btnLogin.isEnabled = isFormValid
        setButtonColor(isFormValid)

        // Pastikan teks tombol selalu "Register"
        binding.includeCustomButtonPass.tvLoading.text = getString(R.string.register)

        return isFormValid
    }

    private fun setButtonColor(isEnabled: Boolean) {
        val buttonColor = if (isEnabled) {
            ContextCompat.getColor(requireContext(), R.color.primary)
        } else {
            ContextCompat.getColor(requireContext(), R.color.second)
        }

        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.bg_button) as GradientDrawable
        drawable.setColor(buttonColor)
        binding.includeCustomButtonPass.btnLogin.background = drawable
    }

    private fun showProgressBar() {
        binding.includeCustomButtonPass.progressBar.visibility = View.VISIBLE
        binding.includeCustomButtonPass.btnLogin.isEnabled = false
    }

    private fun hideProgressBar() {
        binding.includeCustomButtonPass.progressBar.visibility = View.GONE
        binding.includeCustomButtonPass.btnLogin.isEnabled = true
    }

    private fun performRegister() {
        val registerModel = RegisterModel(
            name = binding.edUsername.text.toString(),
            email = binding.edEmail.text.toString(),
            password = binding.edPass.text.toString(),
            confirm_password = binding.edConfirm.text.toString(), // Tetap dikirim tapi tidak divalidasi
            no_telp = binding.edTelp.text.toString()
        )

        registerViewModel.registerUser(registerModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}