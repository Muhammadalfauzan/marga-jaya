package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.data.source.remote.response.UpdateUserResponse
import com.example.margajaya.core.domain.model.ProfileModel
import com.example.margajaya.core.domain.model.UpdateUserModel
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun getProfile(): Flow<Resource<ProfileModel>>

    suspend fun updateUser(updateUserModel: UpdateUserModel): Resource<UpdateUserResponse>
    suspend fun clearProfileCache()
}
