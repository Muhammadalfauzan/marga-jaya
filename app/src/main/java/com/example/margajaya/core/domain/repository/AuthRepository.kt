package com.example.margajaya.core.domain.repository

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.LoginResponse
import com.example.margajaya.core.domain.model.LoginModel
import com.example.margajaya.core.domain.model.RegisterModel

interface AuthRepository {
    suspend fun registerUser(registerModel: RegisterModel): Resource<Unit>
    suspend fun loginUser(loginModel: LoginModel): Resource<LoginResponse>
}