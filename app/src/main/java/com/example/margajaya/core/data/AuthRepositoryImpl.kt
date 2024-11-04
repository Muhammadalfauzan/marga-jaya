package com.example.margajaya.core.data

import com.example.margajaya.core.data.source.local.preferences.AuthPreferences
import com.example.margajaya.core.data.source.remote.network.ApiService
import com.example.margajaya.core.data.source.remote.response.LoginResponse
import com.example.margajaya.core.domain.model.LoginModel
import com.example.margajaya.core.domain.model.RegisterModel
import com.example.margajaya.core.domain.repository.AuthRepository
import javax.inject.Inject



class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun registerUser(registerModel: RegisterModel): Resource<Unit> {
        return try {
            apiService.register(registerModel)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Registration failed")
        }
    }

    override suspend fun loginUser(loginModel: LoginModel): Resource<LoginResponse> {
        return try {
            val response = apiService.login(loginModel)
            if (response.success == true && response.data?.token != null) {
                authPreferences.saveAuthToken(response.data.token)
                authPreferences.saveEmail(loginModel.email)// Simpan token
                Resource.Success(response)
            } else {
                Resource.Error(response.message ?: "Login failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred")
        }
    }
}
