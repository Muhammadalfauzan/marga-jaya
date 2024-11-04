package com.example.margajaya.ui.autentikasi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.margajaya.AutentikasiActivity
import com.example.margajaya.MainActivity
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.LoginModel
import com.example.margajaya.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val inputValidator = InputValidator()
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigasi ke RegisterFragment
        binding.registdlu.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        setupListeners()
        observeViewModel()
        binding.buttonlog.setOnClickListener {
            performLogin()
        }
    }

    private fun setupListeners() {
        // Set TextWatchers for validation and enabling the register button
        binding.edEmaillog.addTextChangedListener(createTextWatcher(
            afterTextChanged = {
                inputValidator.validateUsername(
                    binding.edEmaillog,
                    binding.emaillogLay
                )
            }
        ))


        binding.edPassLog.addTextChangedListener(createTextWatcher(
            afterTextChanged = {
                inputValidator.validatePassword(
                    binding.edPassLog,
                    binding.passlogLay
                )
                setEnableButton()
            }
        ))
    }

    private fun observeViewModel() {
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("LoginFragment", "Loading...")
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                    Log.d("LoginFragment", "Login successful, navigating to home")

                    (activity as? AutentikasiActivity)?.apply {
                        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_auth) as NavHostFragment
                        val navController = navHostFragment.navController
                        navController.setGraph(R.navigation.mobile_navigation)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Log.e("LoginFragment", "Login failed: ${result.message}")
                    Toast.makeText(requireContext(), result.message ?: "Login failed", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun performLogin() {
        val email = binding.edEmaillog.text.toString().trim()
        val password = binding.edPassLog.text.toString().trim()

        if (inputValidator.validateUsername(binding.edEmaillog, binding.emaillogLay) &&
            inputValidator.validatePassword(binding.edPassLog, binding.passlogLay)
        ) {
            val loginModel = LoginModel(email = email, password = password)
            loginViewModel.loginUser(loginModel)
        } else {
            Toast.makeText(requireContext(), "Please check your inputs", Toast.LENGTH_SHORT).show()
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
        binding.buttonlog.isEnabled = binding.edEmaillog.text?.isNotEmpty() == true &&
                binding.edEmaillog.text?.isNotEmpty() == true &&
                binding.edPassLog.text?.isNotEmpty() == true
    }
        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}