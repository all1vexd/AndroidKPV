package ru.itis.hw_3.domain.receiver

import androidx.core.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import ru.itis.hw_3.domain.service.NotificationService

class NotificationReplyReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {


        try {
            Log.d("NotificationReply", "Received intent: ${intent.action}")

            when (intent.action) {
                NotificationService.ACTION_REPLY -> {
                    handleReplyAction(context, intent)
                }
            }
        } catch (e: Exception) {
            Log.e("NotificationReply", "Error in onReceive", e)
            e.printStackTrace()
        }

    }

    private fun handleReplyAction(context: Context, intent: Intent) {

        //Получаем введенный текст из ответа на уведомление
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        //Достаем текст ответа по ключу
        val replyText = remoteInput?.getCharSequence(NotificationService.EXTRA_REPLY_TEXT)?.toString()

        //Достаем id уведомления
        val notificationId = intent.getIntExtra(NotificationService.EXTRA_NOTIFICATION_ID, -1)

        if (!replyText.isNullOrEmpty() && notificationId != -1) {

            sendMessageToThirdScreen(context, replyText, notificationId)

            Toast.makeText(context, "Ответ отправлен: $replyText", Toast.LENGTH_SHORT).show()

        }
    }

    private fun sendMessageToThirdScreen(context: Context, replyText: String, notificationId: Int) {

        //Храним файл "notification_replies"
        val sharedPrefs = context.getSharedPreferences("notification_replies", Context.MODE_PRIVATE)

        //Сообщение для отображения
        val message = "Ответ на уведомление #${notificationId}: ${replyText}"

        //Открываем редактор для изменения данных
        sharedPrefs.edit().apply {
            putString("latest_reply", message)
            apply()
        }

        //Создаем Intent для локального broadcast только в пределах нашего приложения
        val updateIntent = Intent("ru.itis.hw_3.REPLY_RECEIVED").apply {
            putExtra("message", message)
        }

        //Отправляем broadcast
        androidx.localbroadcastmanager.content.LocalBroadcastManager
            .getInstance(context)
            .sendBroadcast(updateIntent)

    }

}