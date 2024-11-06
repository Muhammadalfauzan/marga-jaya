package com.example.margajaya.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.margajaya.core.data.source.local.entity.Converter
import com.example.margajaya.core.data.source.local.entity.LapanganEntity
import com.example.margajaya.core.data.source.local.entity.UserProfileEntity

@Database(entities = [UserProfileEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}