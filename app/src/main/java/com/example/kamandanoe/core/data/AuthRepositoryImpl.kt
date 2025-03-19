package com.example.kamandanoe.core.data

import com.example.kamandanoe.core.data.source.remote.network.ApiService
import com.example.kamandanoe.core.data.source.remote.response.LoginResponse
import com.example.kamandanoe.core.domain.model.LoginModel
import com.example.kamandanoe.core.domain.model.RegisterModel
import com.example.kamandanoe.core.domain.preferences.AuthPreferences
import com.example.kamandanoe.core.domain.repository.AuthRepository
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val authPreferences: AuthPreferences
) : AuthRepository {

    override suspend fun loginUser(loginModel: LoginModel): Resource<LoginResponse> {
        return try {
            val response = apiService.login(loginModel)
            if (response.success == true && response.data?.token != null) {
                authPreferences.saveAuthToken(response.data.token)
                authPreferences.saveEmail(loginModel.email) // Simpan token
                Resource.Success(response)
            } else {
                Resource.Error(response.message ?: "Login failed")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "An error occurred")
        }
    }
    override suspend fun registerUser(registerModel: RegisterModel): Resource<Unit> {
        return try {
            apiService.register(registerModel)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Registration failed")
        }
    }

}
