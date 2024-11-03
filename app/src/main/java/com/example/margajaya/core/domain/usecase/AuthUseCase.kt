package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.source.remote.response.RegisterResponse
import com.example.margajaya.core.domain.model.RegisterModel


interface AuthUseCase {
    suspend fun execute(registerModel: RegisterModel): RegisterResponse
}
