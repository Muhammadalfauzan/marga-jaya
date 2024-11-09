package com.example.margajaya.core.di

import android.content.Context
import com.example.margajaya.core.data.NotificationRepositoryImpl
import com.example.margajaya.core.domain.repository.NotificationRepository
import com.example.margajaya.core.domain.usecase.notif.NotificationUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideNotificationRepository(
        @ApplicationContext context: Context
    ): NotificationRepository {
        return NotificationRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideNotificationUseCase(
        notificationRepository: NotificationRepository
    ): NotificationUseCase {
        return NotificationUseCase(notificationRepository)
    }
}