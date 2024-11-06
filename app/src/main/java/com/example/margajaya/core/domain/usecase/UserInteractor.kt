package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.ProfileModel
import com.example.margajaya.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val profileRepository: UserRepository
) : UserUseCase {

    override fun getProfile(): Flow<Resource<ProfileModel>> {
        return profileRepository.getProfile()
    }

   /* override suspend fun updateProfile(profileModel: ProfileModel): Flow<Resource<ProfileModel>> {
        return profileRepository.updateProfile(profileModel)
    }*/
}
