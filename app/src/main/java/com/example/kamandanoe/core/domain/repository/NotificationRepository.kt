package com.example.kamandanoe.core.domain.repository

import com.example.kamandanoe.core.domain.model.BookingItemModel


interface NotificationRepository {
    fun scheduleNotification(bookingItem: BookingItemModel, reminderTime: Long)
}
