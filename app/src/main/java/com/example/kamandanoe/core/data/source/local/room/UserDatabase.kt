package com.example.kamandanoe.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kamandanoe.core.data.source.local.entity.UserProfileEntity

@Database(entities = [UserProfileEntity::class], version = 1, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}