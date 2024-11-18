package com.example.kamandanoe.core.di

import android.content.Context
import com.example.kamandanoe.core.data.NotificationRepositoryImpl
import com.example.kamandanoe.core.domain.repository.NotificationRepository
import com.example.kamandanoe.core.domain.usecase.notif.NotificationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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