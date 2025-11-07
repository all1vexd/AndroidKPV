package ru.itis.hw_3.domain.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import ru.itis.hw_3.MainActivity
import ru.itis.hw_3.R
import ru.itis.hw_3.data.repository.NotificationRepository
import ru.itis.hw_3.domain.channel.NotificationChannelManager
import ru.itis.hw_3.domain.model.NotificationConstants
import ru.itis.hw_3.domain.model.NotificationInfo
import ru.itis.hw_3.domain.model.NotificationPriority
import ru.itis.hw_3.domain.receiver.NotificationReplyReceiver
import kotlin.random.Random

class NotificationService(
    private val context: Context,
    private val notificationManager: NotificationManager,
    private val channelManager: NotificationChannelManager
) {

    private val notificationInfo = mutableMapOf<Int, NotificationInfo>()

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
            originalTitle = title,
            isExpandable = isExpandable,
            shouldOpenApp = shouldOpenApp,
            hasReplyAction = hasReplyAction
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
        val remoteInput = RemoteInput.Builder(NotificationConstants.EXTRA_REPLY_TEXT)
            .setLabel(context.getString(R.string.reply_label))
            .build()

        val replyIntent = Intent(context, NotificationReplyReceiver::class.java).apply {
            action = NotificationConstants.ACTION_REPLY
            putExtra(NotificationConstants.EXTRA_NOTIFICATION_ID, notificationId)
        }

        val replyPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            replyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )

        return NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_send,
            context.getString(R.string.reply_button),
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()
    }


    fun updateNotification(
        notificationId: Int,
        newContent: String?
    ): Boolean {

        NotificationRepository.NotificationStorage.syncWithSystem(notificationManager)

        if (!NotificationRepository.NotificationStorage.containsNotification(notificationId)) {
            return false
        }

        if (!NotificationRepository.NotificationStorage.isNotificationActive(notificationManager, notificationId)) {
            NotificationRepository.NotificationStorage.removeNotification(notificationId)
            notificationInfo.remove(notificationId)
            return false
        }

        val info = notificationInfo[notificationId] ?: return false
        val channelId = info.channelId

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(context.getString(R.string.updated_prefix, info.originalTitle))
            .setContentText(newContent)
            .setPriority(info.originalPriority.priority)
            .setAutoCancel(true)

        if (!newContent.isNullOrEmpty()) {
            if (info.isExpandable && newContent.length > 100) {
                builder
                    .setContentText(newContent.take(100) + context.getString(R.string.ellipsis))
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(newContent)
                    )
            } else {
                builder.setContentText(newContent)
            }
        }

        if (info.hasReplyAction) {
            builder.addAction(createReplyAction(notificationId))
        }

        if (info.shouldOpenApp) {
            builder.setContentIntent(createPendingIntent())
        }

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

}