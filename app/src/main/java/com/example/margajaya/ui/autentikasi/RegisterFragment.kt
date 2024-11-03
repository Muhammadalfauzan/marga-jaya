package com.example.margajaya.ui.autentikasi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.RegisterModel
import com.example.margajaya.databinding.FragmentRegisterBinding
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

    }
    private fun setupListeners() {
        // Set TextWatchers for validation and enabling the register button
        binding.edUsername.addTextChangedListener(createTextWatcher(
            afterTextChanged = { inputValidator.validateUsername(binding.edUsername, binding.nameLay) }
        ))

        binding.edEmail.addTextChangedListener(createTextWatcher(
            afterTextChanged = { inputValidator.validateEmail(binding.edEmail, binding.emailLay) }
        ))

        binding.edPass.addTextChangedListener(createTextWatcher(
            afterTextChanged = { inputValidator.validatePassword(binding.edPass, binding.passLay) }
        ))

        binding.edConfirm.addTextChangedListener(createTextWatcher(
            afterTextChanged = { inputValidator.validateConfirm(binding.edConfirm, binding.confirmLay, binding.edPass) }
        ))

        binding.edTelp.addTextChangedListener(createTextWatcher(
            afterTextChanged = { inputValidator.validateTel(binding.edTelp, binding.telLay) }
        ))

        // Set listener for enabling/disabling register button based on input completeness
        listOf(binding.edUsername, binding.edEmail, binding.edPass, binding.edConfirm, binding.edTelp).forEach {
            it.addTextChangedListener(createTextWatcher(onTextChanged = { setEnableButton() }))
        }



    binding.btnRegist.setOnClickListener {
            // Handle registration
            val registerModel = RegisterModel(
                name = binding.edUsername.text.toString(),
                email = binding.edEmail.text.toString(),
                password = binding.edPass.text.toString(),
                no_telp = binding.edTelp.text.toString(),
                confirm_password = binding.edConfirm.text.toString()
            )
            registerViewModel.registerUser(registerModel)
        }
    }

    private fun observeViewModel() {
        registerViewModel.registerResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Registrasi berhasil!", Toast.LENGTH_LONG).show()

                    // Contoh tindakan setelah registrasi berhasil
                    // Misalnya, navigasi kembali ke halaman login
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
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
    private fun setEnableButton() {
        binding.btnRegist.isEnabled = binding.edUsername.text?.isNotEmpty() == true &&
                binding.edEmail.text?.isNotEmpty() == true &&
                binding.edPass.text?.isNotEmpty() == true &&
                binding.edConfirm.text?.isNotEmpty() == true &&
                binding.edTelp.text?.isNotEmpty() == true
    }

}