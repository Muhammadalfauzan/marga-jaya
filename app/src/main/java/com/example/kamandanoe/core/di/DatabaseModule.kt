package com.example.kamandanoe.core.di

import android.content.Context
import androidx.room.Room
import com.example.kamandanoe.core.data.source.local.room.LapanganDao
import com.example.kamandanoe.core.data.source.local.room.LapanganDatabase
import com.example.kamandanoe.core.data.source.local.room.UserDao
import com.example.kamandanoe.core.data.source.local.room.UserDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    // Provider untuk LapanganDatabase
    @Singleton
    @Provides
    fun provideLapanganDatabase(@ApplicationContext context: Context): LapanganDatabase =
        Room.databaseBuilder(
            context,
            LapanganDatabase::class.java, "Lapangan.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideLapanganDao(database: LapanganDatabase): LapanganDao = database.lapDao()

    // Provider untuk ProfileDatabase
    @Singleton
    @Provides
    fun provideProfileDatabase(@ApplicationContext context: Context): UserDatabase =
        Room.databaseBuilder(
            context,
            UserDatabase::class.java, "Profile.db"
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideProfileDao(database: UserDatabase): UserDao = database.userDao()
}