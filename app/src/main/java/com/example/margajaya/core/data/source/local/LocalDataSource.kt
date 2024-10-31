package com.example.margajaya.core.data.source.local

import com.example.margajaya.core.data.source.local.entity.LapanganEntity
import com.example.margajaya.core.data.source.local.room.LapanganDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val lapanganDao: LapanganDao) {

    fun getAllLapangan(): Flow<List<LapanganEntity>> = lapanganDao.getAllLapangan()

    suspend fun insertLapangan(lapanganList: List<LapanganEntity>) {
        lapanganDao.insertLapangan(lapanganList)
    }

    fun getLapanganById(id: String): Flow<LapanganEntity> = lapanganDao.getLapanganById(id)

/*    suspend fun deleteAllLapangan() {
        lapanganDao.deleteAllLapangan()
    }

    suspend fun updateLapangan(lapangan: LapanganEntity) {
        lapanganDao.updateLapangan(lapangan)
    }*/
}
