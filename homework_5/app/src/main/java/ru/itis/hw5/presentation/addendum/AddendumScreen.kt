package ru.itis.hw5.presentation.addendum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import getStatusInfo
import ru.itis.hw5.R
import ru.itis.hw5.constants.StatusConstant
import ru.itis.hw5.data.database.AppDatabase
import ru.itis.hw5.data.repository.MovieRepository
import ru.itis.hw5.presentation.movie.MovieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddendumScreen(
    modifier: Modifier = Modifier,
    userId: Int,
    movieId: Int?,
    onNavigateBackToMain: () -> Unit
) {

    val context = LocalContext.current
    val viewModel = remember(userId) {
        val repo = MovieRepository(AppDatabase.getInstance(context))
        AddendumViewModel(repo, userId, movieId)
    }

    val statuses = listOf(
        StatusConstant.STATUS_PLANNED,
        StatusConstant.STATUS_WATCHING,
        StatusConstant.STATUS_WATCHED,
        StatusConstant.STATUS_DROPPED
    )
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBackToMain) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
            Text(
                text = if (movieId == null) stringResource(R.string.add_movie_title) else stringResource(R.string.edit_movie_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        OutlinedTextField(
            value = viewModel.name,
            onValueChange = {
                viewModel.name = it
                viewModel.error = null
            },
            label = {
                Text(
                    text = stringResource(R.string.movie_title_hint)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError =  viewModel.error?.contains(stringResource(R.string.title_keyword)) == true
        )


        OutlinedTextField(
            value = viewModel.creator,
            onValueChange = {
                viewModel.creator = it
                viewModel.error = null
            },
            label = {
                Text(
                    text = stringResource(R.string.director_hint)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.error?.contains(stringResource(R.string.director_keyword)) == true
        )
        OutlinedTextField(
            value = viewModel.rating,
            onValueChange = {
                viewModel.rating = it
                viewModel.error = null
            },
            label = {
                Text(
                    text = stringResource(R.string.rating_hint)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = viewModel.error?.contains(stringResource(R.string.rating_keyword)) == true
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = getStatusInfo(viewModel.selectedStatus).first,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(R.string.status_hint)
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                statuses.forEach { status ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = getStatusInfo(status).first
                            )
                        },
                        onClick = {
                            viewModel.selectedStatus = status
                            expanded = false
                        }
                    )
                }
            }
        }
        if (viewModel.error != null) {
            Text(
                text = viewModel.error!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.saveMovie(onNavigateBackToMain)
            },
            enabled = !viewModel.isSaving,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(
                text = if (movieId == null) stringResource(R.string.add_button) else stringResource(R.string.save_changes_button)
            )
        }

    }

}