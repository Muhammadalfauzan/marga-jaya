package com.example.kamandanoe.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kamandanoe.core.data.source.local.entity.LapanganEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LapanganDao {

    @Query("SELECT * FROM lapangan_table")
    fun getAllLapangan(): Flow<List<LapanganEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLapangan(lapanganList: List<LapanganEntity>)

    @Query("SELECT * FROM lapangan_table WHERE id = :id")
    fun getLapanganById(id: String): Flow<LapanganEntity>

 /*   @Query("DELETE FROM lapangan_table")
    suspend fun deleteAllLapangan()

    @Update
    suspend fun updateLapangan(lapangan: LapanganEntity)*/
}
