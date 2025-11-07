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
import ru.itis.hw_3.domain.model.NotificationPriority
import ru.itis.hw_3.domain.service.NotificationService

@Composable
fun FirstScreen(
    modifier: Modifier = Modifier,
    notificationService: NotificationService
) {

    val context = LocalContext.current

    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedPriority: NotificationPriority by remember { mutableStateOf(NotificationPriority.Medium) }
    var isExpandable by remember { mutableStateOf(false) }
    var shouldOpenApp by remember { mutableStateOf(false) }
    var hasReplyAction by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Настройки уведомления",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = title,
            onValueChange = { it->
                title = it
                titleError = null
            },
            label = {
                Text(
                    text = "Заголовок уведомления"
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            isError = titleError != null,
            singleLine = true,
            supportingText = {
                val error = titleError
                if (error != null) {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = message,
            onValueChange = { it->
                message = it
            },
            label = {
                Text(
                    text = "Текст уведомления"
                )
            },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = false,
            maxLines = 3
        )

        Spacer(modifier = Modifier.height(24.dp))

        PriorityDropDown(
            selectedPriority = selectedPriority,
            onPrioritySelected = {
                selectedPriority = it
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SwitchSetting(
            checked = isExpandable,
            onCheckedChange = { isExpandable = it },
            text = "Раскрывать длинный текст",
            enabled = message.isNotBlank()
        )

        SwitchSetting(
            checked = shouldOpenApp,
            onCheckedChange = { shouldOpenApp = it },
            text = "Открывать приложение при нажатии"
        )

        SwitchSetting(
            checked = hasReplyAction,
            onCheckedChange = { hasReplyAction = it },
            text = "Добавить кнопку ответа",
            enabled = true
        )

        Button(
            onClick = {
                titleError = validateTitle(title)
                if (titleError == null) {
                    val id = notificationService.createNotification(
                        title = title,
                        message = message,
                        priority = selectedPriority,
                        isExpandable = isExpandable,
                        shouldOpenApp = shouldOpenApp,
                        hasReplyAction = hasReplyAction
                    )
                    Toast.makeText(context, "Уведомление создано с ID: ${id}", Toast.LENGTH_SHORT).show()
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Тест уведомления")
        }

    }
}

private fun validateTitle(title: String): String? {
    return when {
        title.isBlank() -> "Заголовок не может быть пустым"
        else -> null
    }
}