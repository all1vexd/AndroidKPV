package ru.itis.hw_3.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.itis.hw_3.viewmodel.ThirdScreenViewModel

@Composable
fun ThirdScreen(
    modifier: Modifier = Modifier,
    thirdScreenViewModel: ThirdScreenViewModel = viewModel()
) {

    val context = LocalContext.current
    val messages = thirdScreenViewModel.messages

    var message by remember {
        mutableStateOf("")
    }


    DisposableEffect(Unit) {  // Unit - означает что эффект не зависит от параметров

        // Создаем BroadcastReceiver для приема сообщений
        val broadcastReceiver = object : BroadcastReceiver() {

            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d("ThirdScreen", "Broadcast received: ${intent?.action}")

                when (intent?.action) {

                    "ru.itis.hw_3.REPLY_RECEIVED" -> {

                        val replyMessage = intent.getStringExtra("message")
                        Log.d("ThirdScreen", "Reply message: $replyMessage")

                        replyMessage?.let {

                            thirdScreenViewModel.addMessage(it)
                            Log.d("ThirdScreen", "Message added to list")
                        }
                    }
                }
            }
        }

        val filter = IntentFilter("ru.itis.hw_3.REPLY_RECEIVED")

        // Регистрируем receiver в LocalBroadcastManager
        // (он работает только в пределах нашего приложения)
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, filter)

        // onDispose - выполняется когда Composable уходит с экрана
        onDispose {
            // ОБЯЗАТЕЛЬНО отписываемся от receiver чтобы избежать утечек памяти
            LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Сообщения пользователя",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = message,
                onValueChange = { it->
                    message = it
                },
                label = {
                    Text(
                        text = "Новое сообщение"
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn (
                modifier = Modifier.weight(1f)
            ) {
                items(messages) { msg ->
                    Text(
                        text = msg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 12.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Button(
            onClick = {
                if (message.isNotBlank()) {
                    thirdScreenViewModel.addMessage(message)
                    message = ""
                }

            },
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = "Создать сообщение"
            )
        }
    }
}