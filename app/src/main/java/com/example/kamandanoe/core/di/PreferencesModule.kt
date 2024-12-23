package com.example.kamandanoe.core.di

import android.content.Context
import com.example.kamandanoe.core.data.source.local.preferences.AuthPreferencesImpl
import com.example.kamandanoe.core.domain.preferences.AuthPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PreferencesModule {

    @Provides
    @Singleton
    fun provideAuthPreferences(@ApplicationContext context: Context): AuthPreferences {
        return AuthPreferencesImpl(context)
    }
}
