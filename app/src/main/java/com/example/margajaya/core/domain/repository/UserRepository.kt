package com.example.margajaya.core.domain.repository

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.UpdateUserResponse
import com.example.margajaya.core.domain.model.ProfileModel
import com.example.margajaya.core.domain.model.UpdateUserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(): Flow<Resource<ProfileModel>>
    suspend fun updateUser(updateUserRequest: UpdateUserModel): Resource<UpdateUserResponse>
    suspend fun clearProfileCache()
}
