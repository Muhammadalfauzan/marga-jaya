package com.example.kamandanoe.core.domain.repository

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.UpdateUserResponse
import com.example.kamandanoe.core.domain.model.ProfileModel
import com.example.kamandanoe.core.domain.model.UpdateUserModel

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(forceFetch: Boolean = false): Flow<Resource<ProfileModel>>
    suspend fun updateUser(updateUserRequest: UpdateUserModel): Resource<UpdateUserResponse>
    suspend fun clearProfileCache()

}
