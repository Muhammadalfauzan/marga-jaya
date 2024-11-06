package com.example.margajaya.core.di

import com.example.margajaya.core.data.AuthRepositoryImpl
import com.example.margajaya.core.data.BookingRepositoryImpl
import com.example.margajaya.core.data.LapanganRepositoryImpl
import com.example.margajaya.core.data.PaymentRepositoryImpl
import com.example.margajaya.core.data.UserRepositoryImpl
import com.example.margajaya.core.domain.repository.AuthRepository
import com.example.margajaya.core.domain.repository.BookingRepository
import com.example.margajaya.core.domain.repository.LapanganRepository
import com.example.margajaya.core.domain.repository.PaymentRepository
import com.example.margajaya.core.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindLapanganRepository(lapanganRepositoryImpl: LapanganRepositoryImpl): LapanganRepository

    // Bind AuthRepository interface to AuthRepositoryImpl
    @Binds
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindPaymentRepository(paymentRepositoryImpl: PaymentRepositoryImpl): PaymentRepository

    @Binds
    abstract fun bindBookingRepository(bookingRepositoryImpl: BookingRepositoryImpl): BookingRepository

    @Binds
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

}