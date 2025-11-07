package ru.itis.hw_3.domain.channel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import ru.itis.hw_3.domain.model.NotificationPriority

class NotificationChannelManager(
    private val context: Context,
    private val notificationManager: NotificationManager
) {

    fun createAllChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val maxChannel = NotificationChannel(
                "MAX_PRIORITY_CHANNEL",
                "Максимальный приоритет",
                NotificationPriority.Max.importance
            ).apply {
                description = "Срочные уведомления с ответом"
                enableVibration(true)
            }

            val highChannel = NotificationChannel(
                "HIGH_PRIORITY_CHANNEL",
                "Высокий приоритет",
                NotificationPriority.High.importance
            ).apply {
                description = "Важные уведомления"
                enableVibration(true)
            }

            val mediumChannel = NotificationChannel(
                "MEDIUM_PRIORITY_CHANNEL",
                "Средний приоритет",
                NotificationPriority.Medium.importance
            ).apply {
                description = "Обычные уведомления"
                enableVibration(false)
            }

            val lowChannel = NotificationChannel(
                "LOW_PRIORITY_CHANNEL",
                "Низкий приоритет",
                NotificationPriority.Low.importance
            ).apply {
                description = "Фоновые уведомления"
                enableVibration(false)
            }


            val channels = listOf(maxChannel, highChannel, mediumChannel, lowChannel)

            notificationManager.createNotificationChannels(channels)
        }
    }


    fun getChannelId(priority: NotificationPriority): String {
        return when (priority) {
            NotificationPriority.Max -> "MAX_PRIORITY_CHANNEL"
            NotificationPriority.High -> "HIGH_PRIORITY_CHANNEL"
            NotificationPriority.Medium -> "MEDIUM_PRIORITY_CHANNEL"
            NotificationPriority.Low -> "LOW_PRIORITY_CHANNEL"
        }
    }
}