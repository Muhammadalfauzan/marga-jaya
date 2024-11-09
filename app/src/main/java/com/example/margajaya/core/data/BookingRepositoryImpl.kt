package com.example.margajaya.core.data

import android.util.Log
import com.example.margajaya.core.data.source.remote.RemoteDataSource
import com.example.margajaya.core.data.source.remote.network.ApiResponse
import com.example.margajaya.core.data.source.remote.response.BookingResponse
import com.example.margajaya.core.domain.model.BookingDataModel
import com.example.margajaya.core.domain.repository.BookingRepository
import com.example.margajaya.core.utils.DataMapper
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
                        }?.let { listOf(BookingDataModel(bookings = it, success = resource.data.success)) } ?: emptyList()
                        isDataLoaded = true
                        Log.d("BookingRepositoryImpl", "Fetched booking data successfully")
                        Resource.Success(bookingDataModels)
                    }
                    is Resource.Loading -> {
                        Log.d("BookingRepositoryImpl", "Loading booking data")
                        Resource.Loading()
                    }
                    is Resource.Error -> {
                        Log.e("BookingRepositoryImpl", "Error: ${resource.message}")
                        Resource.Error(resource.message ?: "Unknown error")
                    }
                }
            }
            .catch { e ->
                Log.e("BookingRepositoryImpl", "Caught error in repository flow: ${e.localizedMessage}")
                emit(Resource.Error("An error occurred: ${e.localizedMessage}"))
            }
}
