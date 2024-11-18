package com.example.kamandanoe.core.domain.repository

import com.example.kamandanoe.core.data.Resource
import com.example.kamandanoe.core.domain.model.BookingDataModel


import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun getAllBooking():Flow<Resource<List<BookingDataModel>>>
}
