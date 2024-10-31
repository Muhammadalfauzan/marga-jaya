package com.example.margajaya.core.di

import android.content.Context
import androidx.room.Room
import com.example.margajaya.core.data.source.local.room.LapanganDao
import com.example.margajaya.core.data.source.local.room.LapanganDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): LapanganDatabase = Room.databaseBuilder(
        context,
        LapanganDatabase::class.java, "Lapangan.db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideTourismDao(database: LapanganDatabase): LapanganDao = database.lapDao()
}