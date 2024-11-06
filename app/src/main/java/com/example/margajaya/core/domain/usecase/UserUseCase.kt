package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.ProfileModel
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun getProfile(): Flow<Resource<ProfileModel>>

}
