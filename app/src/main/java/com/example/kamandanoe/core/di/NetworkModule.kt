package com.example.kamandanoe.core.di

import android.content.Context
import com.example.kamandanoe.core.data.source.remote.network.ApiService
import com.example.kamandanoe.core.domain.preferences.AuthPreferences
import com.example.kamandanoe.core.utils.NetworkMonitor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideAuthInterceptor(authPreferences: AuthPreferences): Interceptor {
        return Interceptor { chain ->
            val token = authPreferences.getAuthToken() ?: ""
            val req = chain.request()
            val requestHeader = req.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("User-Agent", "margajaya-app")
                .build()
            chain.proceed(requestHeader)
        }
    }

    @Provides
    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor) // Tambahkan authInterceptor di sini
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://margajaya.vercel.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }
}
