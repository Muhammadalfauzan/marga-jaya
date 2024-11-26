package com.example.kamandanoe.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.UpdateUserResponse
import com.example.kamandanoe.core.domain.model.ProfileModel
import com.example.kamandanoe.core.domain.model.UpdateUserModel
import com.example.kamandanoe.core.domain.usecase.UserUseCase
import com.example.kamandanoe.core.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val _userProfile = MutableLiveData<Resource<ProfileModel>>()
    private val userProfile: LiveData<Resource<ProfileModel>> get() = _userProfile

    private val _updateUserResult = SingleLiveEvent<Resource<UpdateUserResponse>>()
    val updateUserResult: LiveData<Resource<UpdateUserResponse>> get() = _updateUserResult

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    /**
     * Mengambil profil pengguna dari database lokal atau remote berdasarkan parameter forceFetch.
     */
    fun getUserProfile(forceFetch: Boolean = false): LiveData<Resource<ProfileModel>> {
        viewModelScope.launch {
            try {
                userUseCase.getProfile(forceFetch).collect { result ->
                    _userProfile.postValue(result)
                    Log.d("UserViewModel", "User profile fetched: $result")
                }
            } catch (e: Exception) {
                _userProfile.postValue(Resource.Error("Terjadi kesalahan: ${e.localizedMessage}"))
                _toastMessage.postValue("Terjadi kesalahan saat mengambil profil.")
                Log.e("UserViewModel", "Error fetching user profile: ${e.localizedMessage}")
            }
        }
        return userProfile
    }

    /**
     * Memperbarui data pengguna.
     * Menggunakan API untuk update, lalu menyinkronkan data dengan database lokal.
     */
    fun updateUser(updateUserModel: UpdateUserModel): LiveData<Resource<UpdateUserResponse>> {
        viewModelScope.launch {
            try {
                _updateUserResult.postValue(Resource.Loading()) // Tampilkan status loading
                val response = userUseCase.updateUser(updateUserModel)
                _updateUserResult.postValue(response)

                if (response is Resource.Success) {
                    Log.d("UserViewModel", "Update user berhasil, memulai sinkronisasi ke lokal.")
                    // Sinkronisasi profil ke cache lokal dilakukan otomatis di repository
                    getUserProfile(forceFetch = true) // Memuat ulang profil terbaru dari database lokal
                    _toastMessage.postValue("Data pengguna berhasil diperbarui.")
                } else if (response is Resource.Error) {
                    _toastMessage.postValue("Gagal memperbarui data pengguna: ${response.message}")
                }
            } catch (e: Exception) {
                _updateUserResult.postValue(Resource.Error("Gagal memperbarui pengguna: ${e.localizedMessage}"))
                _toastMessage.postValue("Terjadi kesalahan: ${e.localizedMessage}")
                Log.e("UserViewModel", "Error updating user: ${e.localizedMessage}")
            }
        }
        return updateUserResult
    }

    /**
     * Membersihkan cache profil lokal.
     */
    fun clearProfileCache() {
        viewModelScope.launch {
            try {
                userUseCase.clearProfileCache()
                _toastMessage.postValue("Cache profil berhasil dihapus.")
                Log.d("UserViewModel", "Cache profil berhasil dihapus.")
            } catch (e: Exception) {
                _toastMessage.postValue("Gagal menghapus cache profil: ${e.localizedMessage}")
                Log.e("UserViewModel", "Gagal menghapus cache profil: ${e.localizedMessage}")
            }
        }
    }
}
