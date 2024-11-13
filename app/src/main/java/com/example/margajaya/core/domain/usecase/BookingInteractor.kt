package com.example.margajaya.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.margajaya.core.data.BookingRepositoryImpl
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.BookingDataModel
import com.example.margajaya.core.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BookingInteractor @Inject constructor(
    private val bookingRepository: BookingRepository
) : BookingUseCase {
    override  fun getAllBooking(): Flow<Resource<List<BookingDataModel>>> {
        return bookingRepository.getAllBooking()
    }

}
