package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.BookingDataModel
import com.example.kamandanoe.core.domain.repository.BookingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BookingInteractor @Inject constructor(
    private val bookingRepository: BookingRepository
) : BookingUseCase {
    override  fun getAllBooking(): Flow<Resource<List<BookingDataModel>>> {
        return bookingRepository.getAllBooking()
    }

}
