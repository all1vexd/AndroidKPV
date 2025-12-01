package ru.itis.hw_3.domain.channel

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import ru.itis.hw_3.R
import ru.itis.hw_3.domain.model.ChannelConstants
import ru.itis.hw_3.domain.model.NotificationPriority

class NotificationChannelManager(
    private val context: Context,
    private val notificationManager: NotificationManager
) {

    fun createAllChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val maxChannel = NotificationChannel(
                ChannelConstants.CHANNEL_MAX,
                context.getString(R.string.channel_max),
                NotificationPriority.Max.importance
            ).apply {
                description = context.getString(R.string.channel_max_desc)
                enableVibration(true)
            }

            val highChannel = NotificationChannel(
                ChannelConstants.CHANNEL_HIGH,
                context.getString(R.string.channel_high),
                NotificationPriority.High.importance
            ).apply {
                description = context.getString(R.string.channel_high_desc)
                enableVibration(true)
            }

            val mediumChannel = NotificationChannel(
                ChannelConstants.CHANNEL_MEDIUM,
                context.getString(R.string.channel_medium),
                NotificationPriority.Medium.importance
            ).apply {
                description = context.getString(R.string.channel_medium_desc)
                enableVibration(false)
            }

            val lowChannel = NotificationChannel(
                ChannelConstants.CHANNEL_LOW,
                context.getString(R.string.channel_low),
                NotificationPriority.Low.importance
            ).apply {
                description = context.getString(R.string.channel_low_desc)
                enableVibration(false)
            }


            val channels = listOf(maxChannel, highChannel, mediumChannel, lowChannel)

            notificationManager.createNotificationChannels(channels)
        }
    }


    fun getChannelId(priority: NotificationPriority): String {
        return when (priority) {
            NotificationPriority.Max -> ChannelConstants.CHANNEL_MAX
            NotificationPriority.High -> ChannelConstants.CHANNEL_HIGH
            NotificationPriority.Medium -> ChannelConstants.CHANNEL_MEDIUM
            NotificationPriority.Low -> ChannelConstants.CHANNEL_LOW
        }
    }
}