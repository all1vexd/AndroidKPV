import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.itis.hw5.R
import ru.itis.hw5.constants.SortOrder
import ru.itis.hw5.data.database.AppDatabase
import ru.itis.hw5.data.repository.MovieRepository
import ru.itis.hw5.presentation.movie.MovieViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(
    modifier: Modifier = Modifier,
    userId: Int,
    onNavigateToAddendum: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onMovieClick: (Int) -> Unit
) {
    val context = LocalContext.current
    val viewModel = remember(userId) {
        val repo = MovieRepository(AppDatabase.getInstance(context))
        MovieViewModel(repo, userId)
    }

    val movies by viewModel.movies.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.my_movies_title)
                        )
                    },
                    actions = {

                        Button(
                            onClick = {
                                onNavigateToProfile()
                            },
                        ) {
                            Text(
                                text = stringResource(R.string.profile_button)
                            )
                        }

                        IconButton(
                            onClick = {
                                showBottomSheet = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = stringResource(R.string.sort_action)
                            )
                        }
                    }
                )

                OutlinedTextField(
                    value = viewModel.searchQuery,
                    onValueChange = { newValue ->
                        viewModel.search(newValue)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_placeholder)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        bottomBar = {
            Button(
                onClick = {
                    onNavigateToAddendum()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_movie_button)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            if (movies.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.empty_list_message)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(movies) { movie ->
                        MovieItem(
                            movie = movie,
                            onDelete = {
                                viewModel.deleteMovie(movie)
                            },
                            onFavoriteClick = {
                                viewModel.toggleFavorite(movie)
                            },
                            onMovieClick = {
                                onMovieClick(movie.id)
                            }
                        )
                    }
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                SortOptionsContent(
                    onSortSelected = { order ->
                        viewModel.setSortOrder(order)
                        showBottomSheet = false
                    }
                )
            }
        }
    }
}

@Composable
fun SortOptionsContent(
    onSortSelected: (SortOrder) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 40.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.sort_by_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.sort_by_rating)
                )
            },
            modifier = Modifier.clickable {
                onSortSelected(SortOrder.RATING)
            }
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.sort_by_date)
                )
            },
            modifier = Modifier.clickable {
                onSortSelected(SortOrder.DATE)
            }
        )
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.sort_by_title_alphabetical)
                )
            },
            modifier = Modifier.clickable {
                onSortSelected(SortOrder.TITLE)
            }
        )
    }
}