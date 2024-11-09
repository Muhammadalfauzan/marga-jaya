package com.example.margajaya.core.data.source.notif

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.margajaya.R


class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val jenisLapangan = inputData.getString("jenisLapangan") ?: "Tidak diketahui"
        val jamMulai = inputData.getString("jamMulai") ?: "Tidak diketahui"

        showNotification(jenisLapangan, jamMulai)

        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(jenisLapangan: String, jamMulai: String) {
        createNotificationChannel()
        // Teks tambahan untuk detail di notifikasi yang dapat diperluas
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText("$jenisLapangan\nAkan dimulai pada $jamMulai\nJangan sampai terlewatkan ya!! Klik disini untuk melihat bukti booking")

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Ganti dengan ikon Anda
            .setContentTitle("Jangan lupa olah raga hari ini")
            .setContentText("booking $jenisLapangan Anda akan dimulai pada $jamMulai. Cek sekarang!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(bigTextStyle)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Sports Schedule Notifications"
            val descriptionText = "Channel for booking reminders"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "reminder_channel"
    }
}
