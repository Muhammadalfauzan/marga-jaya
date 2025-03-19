package com.example.kamandanoe.core.domain.repository

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.LoginResponse
import com.example.kamandanoe.core.domain.model.LoginModel
import com.example.kamandanoe.core.domain.model.RegisterModel


interface AuthRepository {

    suspend fun loginUser(loginModel: LoginModel): Resource<LoginResponse>
    suspend fun registerUser(registerModel: RegisterModel): Resource<Unit>
}