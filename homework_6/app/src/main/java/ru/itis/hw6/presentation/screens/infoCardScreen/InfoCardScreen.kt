@file:OptIn(ExperimentalMaterial3Api::class)

package ru.itis.hw6.presentation.screens.infoCardScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.itis.hw6.domain.SongDetails
import ru.itis.hw6.presentation.ui.theme.*

@Composable
fun InfoCardScreen(
    modifier: Modifier = Modifier,
    songId: Long,
    viewModel: InfoCardScreenViewModel = viewModel() {
        InfoCardScreenViewModel(
            songId = songId
        )
    },
    navigateToMainScreen: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        containerColor = Gray100,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Детали песни",
                        color = White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateToMainScreen) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple200,
                    titleContentColor = White,
                    navigationIconContentColor = White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Purple200,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Загрузка...",
                                color = Gray200,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(32.dp)
                        ) {
                            Text(
                                text = "Попробуйте снова",
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = state.error!!,
                                color = Gray200,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { viewModel.retry() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Purple200
                                ),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Text("Повторить", color = White)
                            }
                        }
                    }
                }

                state.songDetails != null -> {
                    SongDetailsContent(state.songDetails!!)
                }
            }
        }
    }
}

@Composable
fun SongDetailsContent(song: SongDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    clip = true
                )
        ) {
            AsyncImage(
                model = song.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(20.dp),
                    clip = true
                ),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Gray300
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = song.author,
                    style = MaterialTheme.typography.titleMedium,
                    color = Gray200
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Альбом: ${song.album}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray200
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Дата релиза: ${song.releaseDate}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray200
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Текст песни",
            style = MaterialTheme.typography.titleLarge,
            color = Purple200
        )

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = White,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Text(
                text = song.lyrics.ifEmpty { "Текст не найден" },
                color = Gray200,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}