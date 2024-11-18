package com.example.margajaya.core.data

import android.util.Log
import com.example.margajaya.core.data.source.local.LocalDataSource
import com.example.margajaya.core.data.source.local.entity.UserProfileEntity
import com.example.margajaya.core.data.source.remote.RemoteDataSource
import com.example.margajaya.core.data.source.remote.network.ApiResponse
import com.example.margajaya.core.data.source.remote.network.ApiService
import com.example.margajaya.core.data.source.remote.response.ProfileResponse
import com.example.margajaya.core.data.source.remote.response.UpdateUserResponse
import com.example.margajaya.core.domain.model.ProfileModel
import com.example.margajaya.core.domain.model.UpdateUserModel

import com.example.margajaya.core.domain.repository.UserRepository
import com.example.margajaya.core.utils.AppExecutors
import com.example.margajaya.core.utils.DataMapper
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors,
    private val apiService : ApiService
) : UserRepository {

    override fun getProfile(): Flow<Resource<ProfileModel>> {
        return object : NetworkBoundResource<ProfileModel, ProfileResponse>() {

            override fun loadFromDB(): Flow<ProfileModel> {
                return localDataSource.getProfile()
                    .map { entity ->
                        Log.d("UserRepositoryImpl", "Data from DB: $entity")
                        entity?.let { DataMapper.entityToModel(it) } ?: ProfileModel(
                            id = "",
                            name = "",
                            email = "",
                            role = "",
                            noTelp = ""
                        )
                    }
            }

            override fun shouldFetch(data: ProfileModel?): Boolean {
                val shouldFetch = data == null || data.name!!.isEmpty() // Fetch jika data belum ada atau kosong
                Log.d("UserRepositoryImpl", "Should fetch from API: $shouldFetch")
                return shouldFetch
            }

            override suspend fun createCall(): Flow<ApiResponse<ProfileResponse>> {
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
                Log.d("UserRepositoryImpl", "Data to save in DB: $profileEntity")
                profileEntity?.let {
                    withContext(appExecutors.diskIO().asCoroutineDispatcher()) {
                        localDataSource.insertProfile(it)
                    }
                }
            }
        }.asFlow()
    }

    override suspend fun updateUser(updateUserRequest: UpdateUserModel): Resource<UpdateUserResponse> {
        return try {
            val response = apiService.updateUser(updateUserRequest)
            if (response.success) {
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
        }
    }
}



