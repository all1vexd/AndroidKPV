package ru.itis.hw_3.screens.third

import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.itis.hw_3.R
import ru.itis.hw_3.viewmodel.SharedViewModel

@Composable
fun ThirdScreen(
    modifier: Modifier = Modifier,
    sharedViewModel: SharedViewModel
) {

    val context = LocalContext.current
    val messages = sharedViewModel.messages

    var message by remember {
        mutableStateOf("")
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
                text = context.getString(R.string.messages_title),
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
                        text = context.getString(R.string.new_message_label)
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
                    sharedViewModel.addMessage(message)
                    message = ""
                }

            },
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(
                text = context.getString(R.string.create_message)
            )
        }
    }
}