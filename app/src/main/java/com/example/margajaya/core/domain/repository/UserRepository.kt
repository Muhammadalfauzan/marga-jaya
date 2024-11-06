package com.example.margajaya.core.domain.repository

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getProfile(): Flow<Resource<ProfileModel>>
}
