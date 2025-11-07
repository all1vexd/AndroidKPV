package ru.itis.hw_3.domain.receiver

import android.app.NotificationManager
import androidx.core.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import ru.itis.hw_3.R
import ru.itis.hw_3.domain.model.BroadcastConstants
import ru.itis.hw_3.domain.model.NotificationConstants

class NotificationReplyReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            NotificationConstants.ACTION_REPLY -> {
                handleReplyAction(context, intent)
            }
        }

    }

    private fun handleReplyAction(context: Context, intent: Intent) {

        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        val replyText = remoteInput?.getCharSequence(NotificationConstants.EXTRA_REPLY_TEXT)?.toString()

        val notificationId = intent.getIntExtra(NotificationConstants.EXTRA_NOTIFICATION_ID, -1)

        if (!replyText.isNullOrEmpty() && notificationId != -1) {

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId)

            sendMessageToThirdScreen(context, replyText, notificationId)
            Toast.makeText(
                context,
                context.getString(R.string.reply_sent, replyText),
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    private fun sendMessageToThirdScreen(context: Context, replyText: String, notificationId: Int) {

        val message = context.getString(R.string.reply_message_template, notificationId.toString(), replyText)

        val updateIntent = Intent(BroadcastConstants.ACTION_REPLY_RECEIVED).apply {
            putExtra(BroadcastConstants.EXTRA_MESSAGE, message)
        }

        androidx.localbroadcastmanager.content.LocalBroadcastManager
            .getInstance(context)
            .sendBroadcast(updateIntent)

    }

}