package com.example.kamandanoe.core.data

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.kamandanoe.core.data.source.notif.ReminderWorker
import com.example.kamandanoe.core.domain.model.BookingItemModel
import com.example.kamandanoe.core.domain.repository.NotificationRepository

import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NotificationRepositoryImpl@Inject constructor(
    private val context: Context
) : NotificationRepository {

    override fun scheduleNotification(bookingItem: BookingItemModel, reminderTime: Long) {
        // Buat WorkRequest untuk mengatur pengingat menggunakan WorkManager
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(reminderTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .setInputData(
                workDataOf(
                    "jenisLapangan" to bookingItem.jenisLapangan,
                    "jamMulai" to bookingItem.jamMulai

                )
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "notification-${bookingItem.id}", // Nama unik pekerjaan
            ExistingWorkPolicy.REPLACE, // Gantikan pekerjaan yang sudah ada
            workRequest
        )
    }
}
