package com.example.margajaya.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.ProfileModel
import com.example.margajaya.core.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    // Fungsi untuk mendapatkan data profil
    fun getUserProfile(): LiveData<Resource<ProfileModel>> {
        return userUseCase.getProfile().asLiveData()
    }
}
