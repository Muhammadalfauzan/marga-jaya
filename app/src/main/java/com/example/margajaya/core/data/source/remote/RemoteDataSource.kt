package com.example.margajaya.core.data.source.remote

import android.util.Log
import com.example.margajaya.core.data.source.remote.network.ApiResponse
import com.example.margajaya.core.data.source.remote.network.ApiService
import com.example.margajaya.core.data.source.remote.response.Lapangan
import com.example.margajaya.core.data.source.remote.response.LapanganItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
}