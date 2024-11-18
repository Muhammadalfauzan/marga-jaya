package com.example.margajaya.core.data.source.remote

import android.util.Log
import com.example.margajaya.core.data.source.remote.network.ApiResponse
import com.example.margajaya.core.data.source.remote.network.ApiService
import com.example.margajaya.core.data.source.remote.response.BookingResponse
import com.example.margajaya.core.data.source.remote.response.Lapangan
import com.example.margajaya.core.data.source.remote.response.LapanganItem
import com.example.margajaya.core.data.source.remote.response.ProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getLapangan(tanggal: String): Flow<ApiResponse<List<LapanganItem>>> {
        // Mendapatkan data dari remote API
        return flow {
            try {
                // Memanggil API dengan parameter tanggal
                val response = apiService.getLapangan(tanggal)

                // Memeriksa apakah data lapangan ada dan tidak kosong
                val lapanganList = response.data?.lapangan
                if (!lapanganList.isNullOrEmpty()) {
                    // Jika data tersedia, emit sebagai Success
                    emit(ApiResponse.Success(lapanganList.filterNotNull()))
                } else {
                    // Jika data kosong, emit sebagai Empty
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                // Emit error jika ada pengecualian (exception)
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getLapById(id: String, tanggal: String): Flow<ApiResponse<Lapangan>> {
        return flow {
            val response = apiService.getLapById(id, tanggal)
            val lapangan = response.data?.lapangan
            if (lapangan != null) {
                emit(ApiResponse.Success(lapangan))
            } else {
                emit(ApiResponse.Empty)
            }
        }.catch { e ->
            emit(ApiResponse.Error(e.toString()))
            Log.e("RemoteDataSource", e.toString())
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getAllBooking(): Flow<ApiResponse<BookingResponse>> = flow {
        val response = apiService.getAllBooking()

        if (response.success == true && response.data != null) {
            emit(ApiResponse.Success(response))
        } else {
            // Periksa jika pesan error adalah "jwt expired"
            if (response.message == "jwt expired") {
                emit(ApiResponse.Error("Session expired. Please log in again."))
            } else {
                emit(ApiResponse.Empty)
            }
        }
    }.catch { e ->
        // Tangani error dari HttpException
        if (e is HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            Log.d("RemoteDataSource", "Error body: $errorBody")
            if (errorBody?.contains("jwt expired") == true) {
                emit(ApiResponse.Error("Session expired. Please log in again."))
            } else {
                emit(ApiResponse.Error("Server error: ${e.message()}"))
            }
        } else {
            Log.e("RemoteDataSource", "Error occurred: ${e.localizedMessage}", e)
            emit(ApiResponse.Error("Unexpected error occurred: ${e.localizedMessage}"))
        }
    }.flowOn(Dispatchers.IO)



    suspend fun getProfile(): Flow<ApiResponse<ProfileResponse>> = flow {
        try {
            val response = apiService.getProfile()
            if (response.success == true && response.data != null) {
                Log.d("RemoteDataSource", "Profile data from API: ${response.data}")
                emit(ApiResponse.Success(response))
            } else {
                emit(ApiResponse.Empty)
            }
        } catch (e: Exception) {
            Log.e("RemoteDataSource", "Error fetching profile: ${e.localizedMessage}")
            emit(ApiResponse.Error(e.localizedMessage ?: "An error occurred"))
        }
    }.flowOn(Dispatchers.IO)


}