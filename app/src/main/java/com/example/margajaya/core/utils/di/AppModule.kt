package com.example.margajaya.core.utils.di

import com.example.margajaya.core.domain.usecase.LapanganInteractor
import com.example.margajaya.core.domain.usecase.LapanganUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideLapanganUseCase(lapanganInteractor: LapanganInteractor): LapanganUseCase
}
