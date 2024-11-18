package com.example.kamandanoe.ui.autentikasi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.LoginResponse
import com.example.kamandanoe.core.domain.model.LoginModel
import com.example.kamandanoe.core.domain.usecase.AuthUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : ViewModel() {

    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> = _loginResult

    fun loginUser(loginModel: LoginModel) {
        viewModelScope.launch {
            _loginResult.value = Resource.Loading()
            _loginResult.value = authUseCase.loginUser(loginModel)
        }
    }
}