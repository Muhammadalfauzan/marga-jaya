package com.example.kamandanoe.core.data

import android.util.Log
import com.example.kamandanoe.core.data.source.remote.RemoteDataSource
import com.example.kamandanoe.core.data.source.remote.network.ApiResponse
import com.example.kamandanoe.core.data.source.remote.response.BookingResponse
import com.example.kamandanoe.core.domain.model.BookingDataModel
import com.example.kamandanoe.core.domain.repository.BookingRepository
import com.example.kamandanoe.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookingRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BookingRepository {

    private var isDataLoaded = false

    override fun getAllBooking(): Flow<Resource<List<BookingDataModel>>> =
        object : NetworkOnlyResource<BookingResponse>() {

            override suspend fun createCall(): Flow<ApiResponse<BookingResponse>> {
                return remoteDataSource.getAllBooking()
            }

        }.asFlow()
            .map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val bookingDataModels = resource.data?.data?.booking?.mapNotNull {
                            DataMapper.mapBookingItemToModel(it)
                        }?.let {
                            listOf(BookingDataModel(bookings = it, success = resource.data.success))
                        } ?: emptyList()
                        isDataLoaded = true
                        Resource.Success(bookingDataModels)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Error -> {
                        Log.d(
                            "BookingRepositoryImpl",
                            "Error message from API: ${resource.message}"
                        )

                        if (resource.message == "jwt expired") {
                            Log.d("BookingRepositoryImpl", "Session expired detected in repository")
                            Resource.Error("jwt expired")
                        } else {
                            Resource.Error(resource.message ?: "Unknown error")
                        }
                    }
                }
            }
            .catch { e ->
                Log.e("BookingRepositoryImpl", "Exception caught: ${e.localizedMessage}")
            }
}
