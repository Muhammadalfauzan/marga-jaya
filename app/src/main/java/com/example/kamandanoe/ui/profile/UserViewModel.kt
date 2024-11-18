package com.example.kamandanoe.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.UpdateUserResponse
import com.example.kamandanoe.core.domain.model.ProfileModel
import com.example.kamandanoe.core.domain.model.UpdateUserModel
import com.example.kamandanoe.core.domain.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    // Fungsi untuk mendapatkan data profil
    fun getUserProfile(forceFetch: Boolean = false): LiveData<Resource<ProfileModel>> {
        return userUseCase.getProfile(forceFetch).asLiveData()
    }
    fun clearProfileCache() {
        viewModelScope.launch {
            userUseCase.clearProfileCache()
            getUserProfile()
        }
    }
    fun updateUser(updateUserModel: UpdateUserModel): LiveData<Resource<UpdateUserResponse>> {
        val result = MutableLiveData<Resource<UpdateUserResponse>>()

        viewModelScope.launch {
            // Lakukan proses update user
            val response = userUseCase.updateUser(updateUserModel)
            result.postValue(response)
        }

        return result
    }
}
