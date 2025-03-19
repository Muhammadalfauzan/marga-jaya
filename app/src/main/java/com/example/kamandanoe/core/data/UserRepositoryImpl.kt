package com.example.kamandanoe.core.data

import android.util.Log
import com.example.kamandanoe.core.data.source.local.LocalDataSource
import com.example.kamandanoe.core.data.source.local.entity.UserProfileEntity
import com.example.kamandanoe.core.data.source.remote.RemoteDataSource
import com.example.kamandanoe.core.data.source.remote.network.ApiResponse
import com.example.kamandanoe.core.data.source.remote.network.ApiService
import com.example.kamandanoe.core.data.source.remote.response.ProfileResponse
import com.example.kamandanoe.core.data.source.remote.response.UpdateUserResponse
import com.example.kamandanoe.core.domain.model.ProfileModel
import com.example.kamandanoe.core.domain.model.UpdateUserModel
import com.example.kamandanoe.core.domain.repository.UserRepository
import com.example.kamandanoe.core.utils.AppExecutors
import com.example.kamandanoe.core.utils.DataMapper
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val apiService: ApiService
) : UserRepository {

    override fun getProfile(forceFetch: Boolean): Flow<Resource<ProfileModel>> {
        return object : NetworkBoundResource<ProfileModel, ProfileResponse>() {

            override fun loadFromDB(): Flow<ProfileModel> {
                return localDataSource.getProfile()
                    .map { entity ->
                        Log.d("UserRepositoryImpl", "Data from DB: $entity")
                        entity?.let {
                            DataMapper.entityToModel(it)
                        } ?: ProfileModel(
                            id = "",
                            name = "",
                            email = "",
                            role = "",
                            noTelp = ""
                        )
                    }
            }

            override fun shouldFetch(data: ProfileModel?): Boolean {
                val shouldFetch = forceFetch || data == null || data.name!!.isEmpty()
                Log.d("UserRepositoryImpl", "Should fetch from API: $shouldFetch")
                return shouldFetch
            }

            override suspend fun createCall(): Flow<ApiResponse<ProfileResponse>> {
                Log.d("UserRepositoryImpl", "Fetching profile from API...")
                return remoteDataSource.getProfile()
            }

            override suspend fun saveCallResult(data: ProfileResponse) {
                val profileEntity = data.data?.user?.let {
                    UserProfileEntity(
                        id = it.id ?: "",
                        name = it.name ?: "",
                        email = it.email ?: "",
                        role = it.role ?: "",
                        noTelp = it.noTelp ?: ""
                    )
                }
                profileEntity?.let {
                    withContext(appExecutors.diskIO().asCoroutineDispatcher()) {
                        localDataSource.insertProfile(it)
                        Log.d("UserRepositoryImpl", "Profile saved to local database: $it")
                    }
                }
            }
        }.asFlow()
    }

    override suspend fun updateUser(updateUserRequest: UpdateUserModel): Resource<UpdateUserResponse> {
        return try {
            // Ambil profil yang sudah ada dari database lokal
            val existingProfile = localDataSource.getProfile().firstOrNull()

            // Panggil API untuk memperbarui data
            val response = apiService.updateUser(updateUserRequest)

            if (response.success) {
                // Jika pembaruan berhasil, perbarui data lokal
                existingProfile?.let {
                    val updatedProfile = ProfileModel(
                        id = it.id, // Gunakan ID yang sama
                        name = updateUserRequest.name ?: it.name,
                        email = updateUserRequest.email ?: it.email,
                        role = it.role, // Gunakan role lama
                        noTelp = updateUserRequest.no_telp ?: it.noTelp
                    )
                    saveProfileToLocal(updatedProfile) // Simpan ke database lokal
                }
                Resource.Success(response)
            } else {
                Resource.Error(response.message)
            }
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.localizedMessage}")
        }
    }


    override suspend fun clearProfileCache() {
        withContext(appExecutors.diskIO().asCoroutineDispatcher()) {
            localDataSource.clearProfile()
            Log.d("UserRepositoryImpl", "Profile cache cleared")
        }
    }

    private suspend fun saveProfileToLocal(profileModel: ProfileModel) {
        val profileEntity = UserProfileEntity(
            id = profileModel.id,
            name = profileModel.name,
            email = profileModel.email,
            role = profileModel.role,
            noTelp = profileModel.noTelp
        )
        withContext(appExecutors.diskIO().asCoroutineDispatcher()) {
            localDataSource.insertProfile(profileEntity)
            Log.d("UserRepositoryImpl", "Profile saved to local: $profileEntity")
        }
    }
}

