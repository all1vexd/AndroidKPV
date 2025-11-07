package ru.itis.hw_3.domain.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import ru.itis.hw_3.MainActivity
import ru.itis.hw_3.data.NotificationRepository
import ru.itis.hw_3.domain.channel.NotificationChannelManager
import ru.itis.hw_3.domain.model.NotificationPriority
import ru.itis.hw_3.domain.receiver.NotificationReplyReceiver
import kotlin.random.Random

class NotificationService(
    private val context: Context,
    private val notificationManager: NotificationManager,
    private val channelManager: NotificationChannelManager
) {

    private val notificationInfo = mutableMapOf<Int, NotificationInfo>()

    data class NotificationInfo(
        val channelId: String,
        val originalPriority: NotificationPriority,
        val originalTitle: String
    )

    fun createNotification(
        title: String,
        message: String?,
        priority: NotificationPriority,
        isExpandable: Boolean,
        shouldOpenApp: Boolean,
        hasReplyAction: Boolean
    ): Int {

        val notificationId = Random.nextInt(1000, 9999)
        val channelId = channelManager.getChannelId(priority)

        notificationInfo[notificationId] = NotificationInfo(
            channelId = channelId,
            originalPriority = priority,
            originalTitle = title
        )

        NotificationRepository.NotificationStorage.addNotification(notificationId)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setPriority(priority.priority)
            .setAutoCancel(true)

        if (!message.isNullOrEmpty()) {
            if (isExpandable && message.length > 100) {
                builder
                    .setContentText(message.take(100) + "...")
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(message)
                    )
            } else {
                builder.setContentText(message)
            }
        }

        if (hasReplyAction) {
            builder.addAction(createReplyAction(notificationId))
        }

        if (shouldOpenApp) {
            builder.setContentIntent(createPendingIntent())
        }

        val notification = builder.build()
        notificationManager.notify(notificationId, notification)

        return notificationId

    }

    fun createReplyAction(notificationId:Int): NotificationCompat.Action {
        val remoteInput = RemoteInput.Builder(EXTRA_REPLY_TEXT)
            .setLabel("Введите ответ...")
            .build()

        val replyIntent = Intent(context, NotificationReplyReceiver::class.java).apply {
            action = ACTION_REPLY
            putExtra(EXTRA_NOTIFICATION_ID, notificationId)
        }

        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        return NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            "Ответить",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()
    }


    fun updateNotification(
        notificationId: Int,
        newContent: String?
    ): Boolean {
        if (!NotificationRepository.NotificationStorage.containsNotification(notificationId)) {
            return false
        }

        val info = notificationInfo[notificationId] ?: return false
        val channelId = info.channelId

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Обновлено: ${info.originalTitle}")
            .setContentText(newContent)
            .setPriority(info.originalPriority.priority)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
        return true
    }

    fun cancelAllNotifications() {
        NotificationRepository.NotificationStorage.getAllNotifications().forEach { id ->
            notificationManager.cancel(id)
        }
        NotificationRepository.NotificationStorage.clearAll()
        notificationInfo.clear()
    }

    private fun createPendingIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context,
            Random.nextInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )
    }

    companion object {

        const val ACTION_REPLY = "ru.itis.hw_3.ACTION_REPLY"

        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"

        const val EXTRA_REPLY_TEXT = "extra_reply_text"

    }
}