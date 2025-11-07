package ru.itis.hw_3.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ru.itis.hw_3.domain.service.NotificationService

@Composable
fun SecondScreen(
    modifier: Modifier = Modifier,
    notificationService: NotificationService
) {

    var notificationId by remember{
        mutableStateOf("")
    }
    var newMessage by remember{
        mutableStateOf("")
    }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Редактирование уведомлений",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = notificationId,
            onValueChange = { it->
                notificationId = it
            },
            label = {
                Text(
                    text = "ID уведомления"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Введите число..."
                )
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = newMessage,
            onValueChange = { it->
                newMessage = it
            },
            label = {
                Text(
                    text = "Новый текст уведомления"
                )
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val id = notificationId.toIntOrNull()
                if (id == null) {
                    Toast.makeText(context, "Ошибка: ID должен быть числом", Toast.LENGTH_SHORT).show()
                } else {
                    val success = notificationService.updateNotification(notificationId = id, newContent = newMessage)
                    if (success) {
                        Toast.makeText(context, "Уведомление обновлено!", Toast.LENGTH_SHORT).show()
                        newMessage = ""
                        notificationId = ""
                    } else {
                        Toast.makeText(context, "Ошибка: уведомление с ID $id не найдено", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Обновить уведомление"
            )
        }

        Button(
            onClick = {
                notificationService.cancelAllNotifications()
                Toast.makeText(context, "Все уведомления удалены", Toast.LENGTH_SHORT).show()
                notificationId = ""
                newMessage = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Удалить все уведомления"
            )
        }

    }

}