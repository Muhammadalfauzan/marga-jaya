package com.example.kamandanoe.ui.autentikasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.RegisterModel
import com.example.kamandanoe.core.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _registerResult = MutableLiveData<Resource<Unit>>()
    val registerResult: LiveData<Resource<Unit>> get() = _registerResult

    fun registerUser(registerModel: RegisterModel) {
        viewModelScope.launch {
            _registerResult.postValue(Resource.Loading())
            val result = authUseCase.registerUser(registerModel)
            _registerResult.postValue(result)
        }
    }
}
