package com.example.margajaya.core.domain.repository

import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.BookingDataModel

import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun getAllBooking():Flow<Resource<List<BookingDataModel>>>
}
