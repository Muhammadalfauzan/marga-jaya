package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.BookingDataModel
import com.example.margajaya.core.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookingInteractor @Inject constructor(
    private val bookingRepository: BookingRepository
) : BookingUseCase {

    override  fun getAllBooking(): Flow<Resource<List<BookingDataModel>>> {
        return bookingRepository.getAllBooking()
    }
}
