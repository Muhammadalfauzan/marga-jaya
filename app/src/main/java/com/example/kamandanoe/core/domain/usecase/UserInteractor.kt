package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.data.source.remote.response.UpdateUserResponse
import com.example.kamandanoe.core.domain.model.ProfileModel
import com.example.kamandanoe.core.domain.model.UpdateUserModel
import com.example.kamandanoe.core.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val profileRepository: UserRepository
) : UserUseCase {


    override fun getProfile(forceFetch: Boolean): Flow<Resource<ProfileModel>> {
        return profileRepository.getProfile()
    }

    override suspend fun updateUser(updateUserModel: UpdateUserModel): Resource<UpdateUserResponse> {
        return profileRepository.updateUser(updateUserModel)
    }

    override suspend fun clearProfileCache() {
        profileRepository.clearProfileCache()  // Panggil ke repository untuk menghapus data
    }

   /* override suspend fun updateProfile(profileModel: ProfileModel): Flow<Resource<ProfileModel>> {
        return profileRepository.updateProfile(profileModel)
    }*/
}
