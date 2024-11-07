package com.example.margajaya.core.data.source.local

import com.example.margajaya.core.data.source.local.entity.LapanganEntity
import com.example.margajaya.core.data.source.local.entity.UserProfileEntity
import com.example.margajaya.core.data.source.local.room.LapanganDao
import com.example.margajaya.core.data.source.local.room.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val lapanganDao: LapanganDao, private val userDao : UserDao) {

    fun getAllLapangan(): Flow<List<LapanganEntity>> = lapanganDao.getAllLapangan()

    suspend fun insertLapangan(lapanganList: List<LapanganEntity>) {
        lapanganDao.insertLapangan(lapanganList)
    }

    fun getProfile(): Flow<UserProfileEntity?> {
        return userDao.getProfile()
    }

    suspend fun insertProfile(profile: UserProfileEntity) {
        userDao.insertProfile(profile)
    }
    suspend fun clearProfile() = userDao.clearProfile()
    fun getLapanganById(id: String): Flow<LapanganEntity> = lapanganDao.getLapanganById(id)

/*    suspend fun deleteAllLapangan() {
        lapanganDao.deleteAllLapangan()
    }

    suspend fun updateLapangan(lapangan: LapanganEntity) {
        lapanganDao.updateLapangan(lapangan)
    }*/
}
