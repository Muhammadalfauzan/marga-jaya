package com.example.margajaya.core.domain.usecase

import androidx.lifecycle.LiveData
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.BookingDataModel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BookingUseCase {

     fun getAllBooking(): Flow<Resource<List<BookingDataModel>>>
}