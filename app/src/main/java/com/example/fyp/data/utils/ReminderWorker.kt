package com.example.fyp.data.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.fyp.FYPMainActivity
import com.example.fyp.R
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import timber.log.Timber

class ReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val prescriptionName = inputData.getString("prescriptionName") ?: return Result.failure()
        val reminderId = inputData.getString("reminderId") ?: return Result.failure()

        Timber.i("ReminderWorker: Notification triggered for: $prescriptionName")

        showNotification(prescriptionName)

        deleteReminder(reminderId)

        return Result.success()
    }

    private fun showNotification(prescriptionName: String) {
        val channelId = "reminder_channel"
        val notificationId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reminders"
            val descriptionText = "Channel for prescription reminders"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, FYPMainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "reminder")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Prescription Reminder")
            .setContentText("It's almost time to pick up: $prescriptionName")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
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

    private fun deleteReminder(reminderId: String) {
        Timber.i("ReminderWorker: Attempting to delete reminderId = $reminderId")
        Tasks.await(
            FirebaseFirestore.getInstance()
                .collection("prescription_reminders")
                .document(reminderId)
                .delete()
                .addOnSuccessListener {
                    Timber.i("ReminderWorker: Reminder deleted successfully")
                }
                .addOnFailureListener {
                    Timber.e(it, "ReminderWorker: Failed to delete reminder")
                }
        )
    }
}
