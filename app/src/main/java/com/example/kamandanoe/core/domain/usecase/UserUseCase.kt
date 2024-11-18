package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.UpdateUserResponse
import com.example.kamandanoe.core.domain.model.ProfileModel
import com.example.kamandanoe.core.domain.model.UpdateUserModel
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun getProfile(forceFetch: Boolean = false): Flow<Resource<ProfileModel>>

    suspend fun updateUser(updateUserModel: UpdateUserModel): Resource<UpdateUserResponse>
    suspend fun clearProfileCache()
}
