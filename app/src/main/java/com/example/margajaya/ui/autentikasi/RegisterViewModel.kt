package com.example.margajaya.ui.autentikasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.RegisterResponse
import com.example.margajaya.core.domain.model.RegisterModel
import com.example.margajaya.core.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: AuthUseCase
) : ViewModel() {

    private val _registerResult = MutableLiveData<Resource<RegisterResponse>>()
    val registerResult: LiveData<Resource<RegisterResponse>> get() = _registerResult

    fun registerUser(registerModel: RegisterModel) {
        viewModelScope.launch {
            _registerResult.postValue(Resource.Loading())
            try {
                val response = registerUseCase.execute(registerModel)
                _registerResult.postValue(Resource.Success(response))
            } catch (e: Exception) {
                _registerResult.postValue(Resource.Error(e.localizedMessage ?: "An error occurred"))
            }
        }
    }
}
