package com.example.margajaya.core.domain.usecase

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.BookingDataModel

import kotlinx.coroutines.flow.Flow

interface BookingUseCase {
     fun getAllBooking(): Flow<Resource<List<BookingDataModel>>>
}