package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.LoginResponse
import com.example.margajaya.core.data.source.remote.response.RegisterResponse
import com.example.margajaya.core.domain.model.LoginModel
import com.example.margajaya.core.domain.model.RegisterModel


interface AuthUseCase {
    suspend fun registerUser(registerModel: RegisterModel): Resource<Unit>
    suspend fun loginUser(loginModel: LoginModel): Resource<LoginResponse>

}
