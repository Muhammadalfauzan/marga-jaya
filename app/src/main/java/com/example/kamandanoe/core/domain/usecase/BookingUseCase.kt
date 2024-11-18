package com.example.kamandanoe.core.domain.usecase

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.BookingDataModel
import kotlinx.coroutines.flow.Flow

interface BookingUseCase {

     fun getAllBooking(): Flow<Resource<List<BookingDataModel>>>
}