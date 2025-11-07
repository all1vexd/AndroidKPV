package ru.itis.hw_3.domain.receiver

import android.app.NotificationManager
import androidx.core.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import ru.itis.hw_3.domain.service.NotificationService

class NotificationReplyReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            NotificationService.ACTION_REPLY -> {
                handleReplyAction(context, intent)
            }
        }

    }

    private fun handleReplyAction(context: Context, intent: Intent) {

        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        val replyText = remoteInput?.getCharSequence(NotificationService.EXTRA_REPLY_TEXT)?.toString()

        val notificationId = intent.getIntExtra(NotificationService.EXTRA_NOTIFICATION_ID, -1)

        if (!replyText.isNullOrEmpty() && notificationId != -1) {

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)

            sendMessageToThirdScreen(context, replyText, notificationId)
            Toast.makeText(context, "Ответ отправлен: $replyText", Toast.LENGTH_SHORT).show()

        }
    }

    private fun sendMessageToThirdScreen(context: Context, replyText: String, notificationId: Int) {

        val message = "Ответ на уведомление #${notificationId}: ${replyText}"

        val updateIntent = Intent("ru.itis.hw_3.REPLY_RECEIVED").apply {
            putExtra("message", message)
        }

        androidx.localbroadcastmanager.content.LocalBroadcastManager
            .getInstance(context)
            .sendBroadcast(updateIntent)

    }

}