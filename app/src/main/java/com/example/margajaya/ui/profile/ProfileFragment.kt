package com.example.margajaya.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.margajaya.AutentikasiActivity
import com.example.margajaya.R
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.local.preferences.AuthPreferences
import com.example.margajaya.core.domain.model.ProfileModel
import com.example.margajaya.core.domain.model.UpdateUserModel
import com.example.margajaya.databinding.FragmentProfileBinding
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
    private var isUserUpdated = false
    private var originalName: String = ""
    private var originalTelp: String = ""



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
        setUpLogoutButton()
        observeUserProfile()
        setupSwipeToRefresh()
        setupSaveButton()
        setupTextWatchers()
        setUpToolbar()
    }

    private fun setUpToolbar() {
        // Pastikan MainActivity sebagai AppCompatActivity untuk mengakses supportActionBar
        val mainActivity = activity as? AppCompatActivity
        mainActivity?.supportActionBar?.apply {
            title = "Profile"
            show()
        }
    }

    private fun setUpLogoutButton() {
        binding.btnLogout.setOnClickListener {
            performLogout()
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                performLogout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun performLogout() {
        authPreferences.clearUserData()
        Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireContext(), AutentikasiActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        userViewModel.clearProfileCache()
    }
    private fun observeUserProfile(forceFetch: Boolean = false) {
        userViewModel.getUserProfile(forceFetch).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    if (forceFetch) showLoading()
                    Log.d("ProfileFragment", "Loading profile data...")
                }

                is Resource.Success -> {
                    hideLoading()
                    handleProfileSuccess(resource.data)
                }

                is Resource.Error -> {
                    hideLoading()
                    handleProfileError(resource.message)
                }
            }
        }
    }
    private fun handleProfileSuccess(profile: ProfileModel?) {
        hideLoading()
        profile?.let {
            binding.edUsername.setText(it.name)
            binding.edEmail.setText(it.email)
            binding.edEmail.isEnabled = false
            binding.edTelp.setText(it.noTelp)

            // Set original data for comparison
            originalName = it.name ?: ""
            originalTelp = it.noTelp ?: ""
            checkForChanges()
        }
    }
    private fun handleProfileError(message: String?) {
        hideLoading()
        Toast.makeText(context, message ?: "Error fetching profile", Toast.LENGTH_LONG).show()
    }

    private fun showLoading() {
        binding.cardLoading.visibility = View.VISIBLE
        binding.btnSave.isEnabled =false
    }

    private fun hideLoading() {
        binding.cardLoading.visibility = View.GONE
        binding.btnSave.isEnabled = true
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            showLoading()
            updateUser()
        }
    }
    private fun setupSwipeToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            userViewModel.clearProfileCache()
            observeUserProfile(forceFetch = true)  // Fetch ulang data
            binding.swipeRefreshLayout.isRefreshing =
                false // Hentikan refresh animation setelah data selesai diambil
        }
    }
    private fun setupTextWatchers() {
        binding.edUsername.addTextChangedListener(createTextWatcher())
        binding.edTelp.addTextChangedListener(createTextWatcher())
        binding.edPassLama.addTextChangedListener(createTextWatcher())
        binding.edPassBaru.addTextChangedListener(createTextWatcher())
    }

    private fun createTextWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                checkForChanges()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
    }

    private fun checkForChanges() {
        val isNameChanged = binding.edUsername.text.toString() != originalName
        val isTelpChanged = binding.edTelp.text.toString() != originalTelp
        val isPasswordChanged = !binding.edPassLama.text.isNullOrEmpty() && !binding.edPassBaru.text.isNullOrEmpty()

        isUserUpdated = isNameChanged || isTelpChanged || isPasswordChanged
        updateSaveButtonState()
    }
    private fun updateSaveButtonState(){
        binding.btnSave.isEnabled = isUserUpdated
        binding.btnSave.setBackgroundColor(
            if (isUserUpdated) resources.getColor(R.color.primary,null)
            else resources.getColor(R.color.second,null)
        )
    }



    private fun updateUser() {
        val updateUserRequest = UpdateUserModel(
            email = binding.edEmail.text.toString(),
            name = binding.edUsername.text.toString(),
            no_telp = binding.edTelp.text.toString(),
            password = binding.edPassLama.text.toString(),
            new_password = binding.edPassBaru.text.toString()
        )

        userViewModel.updateUser(updateUserRequest).observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> showLoading()
                is Resource.Success -> handleUpdateSuccess()
                is Resource.Error -> handleProfileError(resource.message)
            }
        }
    }
    private fun handleUpdateSuccess() {
        hideLoading()
        Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
        binding.edPassLama.text?.clear()
        binding.edPassBaru.text?.clear()
        checkForChanges()
    }
/*    private fun handleKeyboardVisibility() {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Menyimpan visibilitas asli dari BottomNavigationView sebelum keyboard muncul
        originalBottomNavVisibility = bottomNavigationView.visibility

        // Menggunakan WindowInsets untuk memantau keyboard
        view?.viewTreeObserver?.addOnGlobalLayoutListener {
            val rect = Rect()
            view?.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view?.rootView?.height ?: 0
            val keypadHeight = screenHeight - rect.bottom

            if (keypadHeight > screenHeight * 0.15) {
                // Keyboard muncul, sembunyikan BottomNavigationView hanya sementara
                bottomNavigationView.visibility = View.GONE
            } else {
                // Keyboard disembunyikan, kembalikan BottomNavigationView ke visibilitas aslinya
                bottomNavigationView.visibility = originalBottomNavVisibility
            }
        }
    }*/
}



