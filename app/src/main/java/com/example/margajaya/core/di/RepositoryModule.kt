package com.example.margajaya.core.di

import com.example.margajaya.core.data.LapanganRepositoryImpl
import com.example.margajaya.core.domain.repository.LapanganRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLapanganRepository(lapanganRepositoryImpl: LapanganRepositoryImpl): LapanganRepository
}