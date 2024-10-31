package com.example.margajaya.core.data

import com.example.margajaya.core.data.source.local.LocalDataSource
import com.example.margajaya.core.data.source.remote.RemoteDataSource
import com.example.margajaya.core.data.source.remote.network.ApiResponse
import com.example.margajaya.core.data.source.remote.response.LapanganItem
import com.example.margajaya.core.domain.model.Lapangan
import com.example.margajaya.core.domain.repository.LapanganRepository
import com.example.margajaya.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LapanganRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : LapanganRepository {

    override fun getLapangan(tanggal: String): Flow<Resource<List<Lapangan>>> =
        object : NetworkBoundResource<List<Lapangan>, List<LapanganItem>>() {

            // Memuat data dari database lokal
            override fun loadFromDB(): Flow<List<Lapangan>> {
                return localDataSource.getAllLapangan().map { entities ->
                    DataMapper.mapEntitiesToDomain(entities)
                }
            }

            // Menentukan apakah perlu mengambil data dari API
            override fun shouldFetch(data: List<Lapangan>?): Boolean {
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
}