package com.example.margajaya.ui.history

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.BookingDataModel
import com.example.margajaya.core.domain.usecase.BookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase
) : ViewModel() {

    // Fungsi untuk mendapatkan semua data booking
    fun getAllBookings(): LiveData<Resource<List<BookingDataModel>>> {
        return bookingUseCase.getAllBooking().asLiveData()
    }
}
