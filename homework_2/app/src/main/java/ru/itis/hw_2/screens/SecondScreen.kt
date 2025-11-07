package ru.itis.hw_2.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ru.itis.hw_2.R
import ru.itis.hw_2.navigation.NavParams
import ru.itis.hw_2.ui.theme.AppTheme
import ru.itis.hw_2.viewModels.SecondScreenViewModel
import ru.itis.hw_2.viewModels.ThemeViewModel
import kotlin.math.exp


@Composable
fun SecondScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    email: String,
    viewModel: SecondScreenViewModel = viewModel(),
    themeViewModel: ThemeViewModel
) {

    var expanded by remember {
        mutableStateOf(false)
    }

    val selectedTheme = themeViewModel.currentTheme.value
    val themeLabel = stringResource(id = selectedTheme.scheme.labelResId)
    val posts = viewModel.posts
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle

    LaunchedEffect(savedStateHandle) {
        val newPostTitle = savedStateHandle?.get<String>(NavParams.NEW_POST_TITLE_KEY)
        val newPostContent = savedStateHandle?.get<String>(NavParams.NEW_POST_CONTENT_KEY)

        if (newPostTitle != null) {
            viewModel.addPost(newPostTitle, newPostContent)

            savedStateHandle.remove<String>(NavParams.NEW_POST_TITLE_KEY)
            savedStateHandle.remove<String>(NavParams.NEW_POST_CONTENT_KEY)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = email,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Тема: ${themeLabel}",
                fontSize = 18.sp,
                modifier = Modifier
                    .clickable {
                        expanded = true
                    }
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(12.dp),
                textAlign = TextAlign.Center
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                AppTheme.entries.forEach { theme ->
                    val menuItemText = stringResource(id = theme.scheme.labelResId)
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = menuItemText
                            )
                        },
                        onClick = {
                            themeViewModel.setTheme(theme = theme)
                            expanded = false
                        }
                    )
                }
            }
        }


        LazyColumn (
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(posts) { post ->
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                            shape = MaterialTheme.shapes.small
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(12.dp)
                ) {
                    Text(
                        text = post.title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = post.content,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Button(
            onClick = {
                navController.navigate(NavParams.THIRD_SCREEN)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            Text(
                text = stringResource(R.string.create_post_button),
                fontSize = 16.sp
            )
        }
    }
}