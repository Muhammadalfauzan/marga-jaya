package com.example.margajaya.core.domain.repository

import com.example.margajaya.core.data.source.remote.response.RegisterResponse
import com.example.margajaya.core.domain.model.RegisterModel

interface AuthRepository {
    suspend fun registerUser(registerModel: RegisterModel): RegisterResponse
}