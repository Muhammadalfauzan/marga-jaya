package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.LoginResponse
import com.example.kamandanoe.core.domain.model.LoginModel
import com.example.kamandanoe.core.domain.model.RegisterModel



interface AuthUseCase {

    suspend fun loginUser(loginModel: LoginModel): Resource<LoginResponse>
    suspend fun registerUser(registerModel: RegisterModel): Resource<Unit>
}
