package com.example.margajaya.ui.history


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.margajaya.core.data.Resource
import com.example.margajaya.core.domain.model.BookingDataModel
import com.example.margajaya.core.domain.model.BookingItemModel
import com.example.margajaya.core.domain.usecase.BookingUseCase
import com.example.margajaya.core.domain.usecase.notif.NotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {
    private val _bookings = MutableLiveData<Resource<List<BookingDataModel>>>()
    val bookings: LiveData<Resource<List<BookingDataModel>>> get() = _bookings

    fun testNotification() {
        val dummyBookingItem = BookingItemModel(
            jenisLapangan = "Lapangan Line Satu",
            jamMulai = "10:00",
            createdAt = null,
            paymentLink = null,
            paymentType = null,
            harga = null,
            idLapangan = null,
            name = null,
            transactionTime = null,
            id = null,
            jamBerakhir = null,
            updatedAt = null,
            status = null,
            tanggal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
                .format(Date(System.currentTimeMillis() + 5 * 1000)) // 5 detik dari sekarang
        )

        // Panggil fungsi untuk menjadwalkan notifikasi dengan delay 5 detik
        notificationUseCase.scheduleTestNotification(dummyBookingItem)
    }
    fun getAllBookings() {
        _bookings.value = Resource.Loading()

        viewModelScope.launch {
            bookingUseCase.getAllBooking().collect { resource ->
                _bookings.value = resource

                // Hanya jadwalkan notifikasi jika pemesanan berhasil
                if (resource is Resource.Success) {
                    resource.data?.forEach { bookingData ->
                        bookingData.bookings?.forEach { bookingItem ->
                            scheduleNotification(bookingItem)
                        }
                    }
                }
            }
        }
    }

    private fun scheduleNotification(bookingItem: BookingItemModel) {
        // Notifikasi kedua: 1 menit sebelum jam mulai (gunakan fungsi terpisah)
        notificationUseCase.scheduleNotificationMinutesBeforeStartTime(bookingItem, 1)
    }
}


    //    // Fungsi untuk mendapatkan semua data booking
//    fun getAllBookings(): LiveData<Resource<List<BookingDataModel>>> {
//        return bookingUseCase.getAllBooking().asLiveData()
//    }
//
//    // Flag untuk mengecek apakah data sudah dimuat
//    private var isDataLoaded = false
//
//    // LiveData untuk memantau status pemanggilan data
//    private val _bookings = MutableLiveData<Resource<List<BookingDataModel>>>()
//    val bookings: LiveData<Resource<List<BookingDataModel>>> get() = _bookings

//    // Fungsi untuk mendapatkan semua data booking
//    fun getAllBookings() {
//        if (isDataLoaded) {
//            return // Jika data sudah dimuat, jangan fetch lagi
//        }
//
//        _bookings.value = Resource.Loading() // Set status loading
//        // Fetch data booking dari use case
//        bookingUseCase.getAllBooking().onEach { resource ->
//            when (resource) {
//                is Resource.Success -> {
//                    _bookings.postValue(resource)  // Mengirim data berhasil ke LiveData
//                    isDataLoaded = true  // Tandai bahwa data sudah dimuat
//                }
//                is Resource.Error -> {
//                    _bookings.postValue(resource)  // Mengirim error ke LiveData
//                }
//                is Resource.Loading -> {
//                    _bookings.postValue(resource)  // Tetap kirim status loading
//                }
//            }
//        }.launchIn(viewModelScope) // Lakukan pemanggilan API secara asinkron
//    }
