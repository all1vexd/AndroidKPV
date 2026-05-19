package ru.itis.hw6.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.itis.hw6.App
import ru.itis.hw6.R
import ru.itis.hw6.presentation.MainActivity

class PushNotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data
        val kind = data["kind"] ?: return
        val title = data["title"].orEmpty()
        val body = data["message"].orEmpty()

        when (kind) {
            "promo" -> showNotification(
                channelId = App.PROMO_CHANNEL_ID,
                notificationId = PROMO_NOTIFICATION_ID,
                title = title,
                body = body,
                priority = NotificationCompat.PRIORITY_DEFAULT
            )
            "auth" -> showNotification(
                channelId = App.AUTH_CHANNEL_ID,
                notificationId = AUTH_NOTIFICATION_ID,
                title = title,
                body = body,
                priority = NotificationCompat.PRIORITY_HIGH
            )
            else -> showNotification(
                channelId = App.DEFAULT_CHANNEL_ID,
                notificationId = DEFAULT_NOTIFICATION_ID,
                title = title,
                body = body,
                priority = NotificationCompat.PRIORITY_DEFAULT
            )
        }
    }

    private fun showNotification(
        channelId: String,
        notificationId: Int,
        title: String,
        body: String,
        priority: Int
    ) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(priority)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    companion object {
        private const val PROMO_NOTIFICATION_ID = 1001
        private const val AUTH_NOTIFICATION_ID = 1002
        private const val DEFAULT_NOTIFICATION_ID = 1003
    }
}
