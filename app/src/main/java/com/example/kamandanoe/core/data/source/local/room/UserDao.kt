package com.example.kamandanoe.core.data.source.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kamandanoe.core.data.source.local.entity.UserProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM profile LIMIT 1")
    fun getProfile(): Flow<UserProfileEntity?>


    @Insert(onConflict = OnConflictStrategy.REPLACE) // Ini menggantikan data yang sudah ada
    suspend fun insertProfile(profile: UserProfileEntity)

    @Query("DELETE FROM profile") // Query untuk menghapus data
    suspend fun clearProfile()

    // Update profile based on id
    @Update
    suspend fun updateProfile(profile: UserProfileEntity)
}
