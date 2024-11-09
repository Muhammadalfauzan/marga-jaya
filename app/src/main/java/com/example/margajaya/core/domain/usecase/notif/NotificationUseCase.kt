package com.example.margajaya.core.domain.usecase.notif

import android.util.Log
import com.example.margajaya.core.domain.model.BookingItemModel
import com.example.margajaya.core.domain.repository.NotificationRepository
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class NotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    fun scheduleNotificationMinutesBeforeStartTime(bookingItem: BookingItemModel, minutesBefore: Int) {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        // Parsing tanggal dan jamMulai dari bookingItem
        val bookingDate = bookingItem.tanggal?.let { inputDateFormat.parse(it) }
        val startTime = bookingItem.jamMulai?.let { timeFormat.parse(it) }

        if (bookingDate != null && startTime != null) {
            // Menggabungkan tanggal dan waktu
            val calendar = Calendar.getInstance().apply {
                time = bookingDate
                val timeCalendar = Calendar.getInstance().apply { time = startTime }
                set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
                set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
                set(Calendar.SECOND, 0)
            }

            // Menghitung waktu pengingat (beberapa menit sebelum jam mulai)
            val reminderTime = calendar.timeInMillis - minutesBefore * 60 * 1000 // Mengurangi menit

            if (reminderTime > System.currentTimeMillis()) {
                Log.d("NotificationUseCase", "Scheduling notification $minutesBefore minutes before start time.")
                notificationRepository.scheduleNotification(bookingItem, reminderTime)
            } else {
                Log.d("NotificationUseCase", "Minutes-before reminder time is in the past. No notification scheduled.")
            }
        } else {
            Log.e("NotificationUseCase", "Failed to parse date or start time for minutes-before reminder.")
        }
    }


    fun scheduleTestNotification(bookingItem: BookingItemModel) {
        // Jadwalkan notifikasi untuk 5 detik dari sekarang (untuk testing)
        val reminderTime = System.currentTimeMillis() + 5 * 1000 // 5 detik dari sekarang
        Log.d("NotificationUseCase", "Scheduling test notification for 5 seconds from now.")

        notificationRepository.scheduleNotification(bookingItem, reminderTime)
    }
}

