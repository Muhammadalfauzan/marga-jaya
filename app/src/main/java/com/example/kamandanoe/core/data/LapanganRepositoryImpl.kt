package com.example.kamandanoe.core.data

import com.example.kamandanoe.core.data.source.local.LocalDataSource
import com.example.kamandanoe.core.data.source.remote.RemoteDataSource
import com.example.kamandanoe.core.data.source.remote.network.ApiResponse
import com.example.kamandanoe.core.data.source.remote.response.Lapangan
import com.example.kamandanoe.core.data.source.remote.response.LapanganItem
import com.example.kamandanoe.core.domain.model.LapanganModel
import com.example.kamandanoe.core.domain.repository.LapanganRepository
import com.example.kamandanoe.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LapanganRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : LapanganRepository {
    override fun getLapangan(tanggal: String): Flow<Resource<List<LapanganModel>>> =
        object : NetworkBoundResource<List<LapanganModel>, List<LapanganItem>>() {
            // Memuat data dari database lokal
            override fun loadFromDB(): Flow<List<LapanganModel>> {
                return localDataSource.getAllLapangan().map { entities ->
                    DataMapper.mapEntitiesToDomain(entities)
                }
            }

            // Menentukan apakah perlu mengambil data dari API
            override fun shouldFetch(data: List<LapanganModel>?): Boolean {
                // data.isNullOrEmpty() // Ambil data dari API hanya jika data lokal kosong
                return true // Untuk selalu feth data
            }

            // Memanggil API untuk mendapatkan data lapangan
            override suspend fun createCall(): Flow<ApiResponse<List<LapanganItem>>> =
                remoteDataSource.getLapangan(tanggal)

            // Menyimpan hasil dari API ke database lokal
            override suspend fun saveCallResult(data: List<LapanganItem>) {
                val lapanganEntities = DataMapper.mapResponsesToEntities(data)
                localDataSource.insertLapangan(lapanganEntities)
            }
        }.asFlow()

    override fun getLapById(id: String, tanggal: String): Flow<Resource<LapanganModel>> =
        object : NetworkOnlyResource<Lapangan>() {

            override suspend fun createCall(): Flow<ApiResponse<Lapangan>> {
                return remoteDataSource.getLapById(id, tanggal)
            }

        }.asFlow()
            .map { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val lapanganModel = resource.data?.let { DataMapper.mapResponseToModel(it) }
                        Resource.Success(lapanganModel!!)
                    }

                    is Resource.Loading -> Resource.Loading()
                    is Resource.Error -> Resource.Error(resource.message ?: "Unknown error")
                }
            }
            .catch { e ->
                emit(Resource.Error("An error occurred: ${e.localizedMessage}"))
            }

}