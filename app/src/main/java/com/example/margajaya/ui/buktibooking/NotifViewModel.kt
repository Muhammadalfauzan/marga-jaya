package com.example.margajaya.ui.buktibooking

import androidx.lifecycle.ViewModel
import com.example.margajaya.core.domain.model.BookingItemModel
import com.example.margajaya.core.domain.usecase.notif.NotificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/*
@HiltViewModel
class NotifViewModel @Inject constructor(
    private val notificationUseCase: NotificationUseCase
) : ViewModel() {

    fun scheduleNotificationForBooking(bookingItem: BookingItemModel) {
        // Tidak perlu menggunakan coroutine karena fungsi ini bukan suspend
        notificationUseCase.scheduleNotification(bookingItem)
    }
}*/
