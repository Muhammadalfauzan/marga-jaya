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
import java.util.Calendar
import java.util.Locale

import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingUseCase: BookingUseCase,
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {

    private val _bookings = MutableLiveData<Resource<List<BookingDataModel>>>()
    val bookings: LiveData<Resource<List<BookingDataModel>>> get() = _bookings

    private val _pastBookings = MutableLiveData<Resource<List<BookingItemModel>>>()
    val pastBookings: LiveData<Resource<List<BookingItemModel>>> get() = _pastBookings

    private val _upcomingBookings = MutableLiveData<Resource<List<BookingItemModel>>>()
    val upcomingBookings: LiveData<Resource<List<BookingItemModel>>> get() = _upcomingBookings

    fun getAllBookings() {
        // Set initial loading state
        _bookings.value = Resource.Loading()
        _pastBookings.value = Resource.Loading()
        _upcomingBookings.value = Resource.Loading()

        viewModelScope.launch {
            bookingUseCase.getAllBooking().collect { resource ->
                _bookings.value = resource

                if (resource is Resource.Success) {
                    val allBookings = resource.data?.flatMap { it.bookings.orEmpty() } ?: emptyList()

                    // Filter data booking untuk past dan upcoming bookings
                    val past = filterPastBookings(allBookings)
                    val upcoming = filterUpcomingBookings(allBookings)

                    _pastBookings.value = Resource.Success(past)
                    _upcomingBookings.value = Resource.Success(upcoming)

                    _pastBookings.value = Resource.Success(filterPastBookings(allBookings))
                    _upcomingBookings.value = Resource.Success(filterUpcomingBookings(allBookings))

                    resource.data?.forEach { bookingData ->
                        bookingData.bookings?.forEach { bookingItem ->
                            scheduleNotification(bookingItem)
                        }
                    }
                } else if (resource is Resource.Error) {
                    _pastBookings.value = Resource.Error(resource.message ?: "Unknown error")
                    _upcomingBookings.value = Resource.Error(resource.message ?: "Unknown error")
                }
            }
        }
    }

    // Fungsi untuk memfilter booking yang sudah terlewatkan
    private fun filterPastBookings(bookings: List<BookingItemModel>): List<BookingItemModel> {
        return bookings.filterNot { isBookingUpcoming(it) }
    }

    // Fungsi untuk memfilter booking yang belum terlewatkan
    private fun filterUpcomingBookings(bookings: List<BookingItemModel>): List<BookingItemModel> {
        return bookings.filter { isBookingUpcoming(it) }
    }

    // Fungsi untuk memeriksa apakah booking belum terlewatkan
    private fun isBookingUpcoming(bookingItem: BookingItemModel): Boolean {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        val bookingDate = bookingItem.tanggal?.let { inputDateFormat.parse(it) }
        val startTime = bookingItem.jamMulai?.let { timeFormat.parse(it) }

        if (bookingDate != null && startTime != null) {
            val calendar = Calendar.getInstance().apply {
                time = bookingDate
                val timeCalendar = Calendar.getInstance().apply { time = startTime }
                set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
            }
            return calendar.timeInMillis > System.currentTimeMillis()
        }
        return false
    }
    private fun scheduleNotification(bookingItem: BookingItemModel) {
        // Notifikasi kedua: 1 menit sebelum jam mulai (gunakan fungsi terpisah)
        if (bookingItem.status == "success"){
        notificationUseCase.scheduleNotificationMinutesBeforeStartTime(bookingItem, 1)
    }
    }
}



/*// Menggunakan LiveData untuk diamati di UI
val bookings = bookingUseCase.getAllBooking().asLiveData()

// LiveData terpisah jika diperlukan untuk fragment aktif dan lewat
val activeBookings: LiveData<List<BookingDataModel>> =
    Transformations.map(bookings) { resource ->
        if (resource is Resource.Success) {
            resource.data?.getOrNull(0)?.bookings ?: emptyList()
        } else {
            emptyList()
        }
    }

val pastBookings: LiveData<List<BookingDataModel>> = Transformations.map(bookings) { resource ->
    if (resource is Resource.Success) {
        resource.data?.getOrNull(1)?.bookings ?: emptyList()
    } else {
        emptyList()
    }
}
}*/
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

//fun testNotification() {
//    val dummyBookingItem = BookingItemModel(
//        jenisLapangan = "Lapangan Line Satu",
//        jamMulai = "10:00",
//        createdAt = null,
//        paymentLink = null,
//        paymentType = null,
//        harga = null,
//        idLapangan = null,
//        name = null,
//        transactionTime = null,
//        id = null,
//        jamBerakhir = null,
//        updatedAt = null,
//        status = null,
//        tanggal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
//            .format(Date(System.currentTimeMillis() + 5 * 1000)) // 5 detik dari sekarang
//    )
//
//    // Panggil fungsi untuk menjadwalkan notifikasi dengan delay 5 detik
//    notificationUseCase.scheduleTestNotification(dummyBookingItem)
//}