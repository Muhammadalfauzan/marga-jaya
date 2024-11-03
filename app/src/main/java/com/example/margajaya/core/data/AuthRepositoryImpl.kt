package com.example.margajaya.core.data

import com.example.margajaya.core.data.source.remote.network.ApiService
import com.example.margajaya.core.data.source.remote.response.RegisterResponse
import com.example.margajaya.core.domain.model.RegisterModel
import com.example.margajaya.core.domain.repository.AuthRepository
import javax.inject.Inject



class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {
    override suspend fun registerUser(registerModel: RegisterModel): RegisterResponse {
        return apiService.register(registerModel)
    }
}
