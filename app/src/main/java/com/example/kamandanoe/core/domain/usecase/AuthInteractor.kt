package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.LoginResponse
import com.example.kamandanoe.core.domain.model.LoginModel
import com.example.kamandanoe.core.domain.model.RegisterModel
import com.example.kamandanoe.core.domain.repository.AuthRepository

import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository
) : AuthUseCase {

    override suspend fun registerUser(registerModel: RegisterModel): Resource<Unit> {
        return try {
            authRepository.registerUser(registerModel)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Registration failed")
        }
    }
    override suspend fun loginUser(loginModel: LoginModel): Resource<LoginResponse> {
        return try {
            // Memanggil login dari repository dan mengembalikan hasil sebagai Resource
            authRepository.loginUser(loginModel)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Login failed")
        }
    }
}