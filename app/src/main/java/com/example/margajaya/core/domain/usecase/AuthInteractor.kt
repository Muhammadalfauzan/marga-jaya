package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.LoginResponse
import com.example.margajaya.core.data.source.remote.response.RegisterResponse
import com.example.margajaya.core.domain.model.LoginModel
import com.example.margajaya.core.domain.model.RegisterModel
import com.example.margajaya.core.domain.repository.AuthRepository
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