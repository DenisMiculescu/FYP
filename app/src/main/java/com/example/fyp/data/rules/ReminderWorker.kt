package com.example.fyp.data.rules

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.fyp.R
import timber.log.Timber

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val prescriptionName = inputData.getString("prescriptionName") ?: return Result.failure()

        Timber.i("ReminderWorker: Notification triggered for: $prescriptionName")

        showNotification(prescriptionName)
        return Result.success()
    }

    private fun showNotification(prescriptionName: String) {
        val channelId = "reminder_channel"
        val notificationId = 1

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Prescription Reminder")
            .setContentText("It's almost time to pick up: $prescriptionName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            androidx.core.content.ContextCompat.checkSelfPermission(
                context, android.Manifest.permission.POST_NOTIFICATIONS
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(notificationId, notification)
        }
    }

}
