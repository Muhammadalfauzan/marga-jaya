package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.source.remote.response.RegisterResponse
import com.example.margajaya.core.domain.model.RegisterModel
import com.example.margajaya.core.domain.repository.AuthRepository
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository
) : AuthUseCase {
    override suspend fun execute(registerModel: RegisterModel): RegisterResponse {
        return authRepository.registerUser(registerModel)
    }
}