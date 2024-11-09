package com.example.margajaya.core.domain.repository

import com.example.margajaya.core.domain.model.BookingItemModel

interface NotificationRepository {
    fun scheduleNotification(bookingItem: BookingItemModel, reminderTime: Long)
}
