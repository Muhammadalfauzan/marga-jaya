package com.example.margajaya.core.utils.di


import com.example.margajaya.core.domain.usecase.AuthInteractor
import com.example.margajaya.core.domain.usecase.AuthUseCase
import com.example.margajaya.core.domain.usecase.BookingInteractor
import com.example.margajaya.core.domain.usecase.BookingUseCase
import com.example.margajaya.core.domain.usecase.LapanganInteractor
import com.example.margajaya.core.domain.usecase.LapanganUseCase
import com.example.margajaya.core.domain.usecase.PaymentInteractor
import com.example.margajaya.core.domain.usecase.PaymentUseCase
import com.example.margajaya.core.domain.usecase.UserInteractor
import com.example.margajaya.core.domain.usecase.UserUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideLapanganUseCase(lapanganInteractor: LapanganInteractor): LapanganUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideBookingUseCase(bookingInteractor: BookingInteractor): BookingUseCase

    @Binds
    @ViewModelScoped
    abstract fun providePaymentUseCase(paymentInteractor: PaymentInteractor): PaymentUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase

}


