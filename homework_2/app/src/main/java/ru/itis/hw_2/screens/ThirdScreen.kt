package ru.itis.hw_2.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.itis.hw_2.R
import ru.itis.hw_2.navigation.NavParams

@Composable
fun ThirdScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var title by remember {
        mutableStateOf("")
    }
    var content by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { newText ->
                title = newText
            },
            modifier = Modifier
                .fillMaxWidth(),
            singleLine = true,
            label = {
                Text(
                    text = stringResource(R.string.title_label)
                )
            }
        )

        OutlinedTextField(
            value = content,
            onValueChange = { newText ->
                content = newText
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            singleLine = false,
            label = {
                Text(
                    text = stringResource(R.string.content_label)
                )
            }
        )

        Button(
            onClick = {
                if (title.isNotBlank()) {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(NavParams.NEW_POST_TITLE_KEY, title)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(NavParams.NEW_POST_CONTENT_KEY, content)
                    navController.popBackStack()
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Text(
                text = stringResource(R.string.create_post_button)
            )
        }
    }
}