package com.example.kamandanoe.core.utils.di


import com.example.kamandanoe.core.domain.usecase.AuthInteractor
import com.example.kamandanoe.core.domain.usecase.AuthUseCase
import com.example.kamandanoe.core.domain.usecase.BookingInteractor
import com.example.kamandanoe.core.domain.usecase.BookingUseCase
import com.example.kamandanoe.core.domain.usecase.LapanganInteractor
import com.example.kamandanoe.core.domain.usecase.LapanganUseCase
import com.example.kamandanoe.core.domain.usecase.PaymentInteractor
import com.example.kamandanoe.core.domain.usecase.PaymentUseCase
import com.example.kamandanoe.core.domain.usecase.UserInteractor
import com.example.kamandanoe.core.domain.usecase.UserUseCase
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


