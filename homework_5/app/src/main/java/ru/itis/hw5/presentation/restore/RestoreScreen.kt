package ru.itis.hw5.presentation.restore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.itis.hw5.R
import ru.itis.hw5.data.database.AppDatabase
import ru.itis.hw5.data.repository.UserRepository
import ru.itis.hw5.presentation.login.LoginViewModel

@Composable
fun RestoreScreen(
    modifier: Modifier = Modifier,
    userEmail: String,
    onRestoreComplete: () -> Unit,
    onPermanentlyDeleteComplete: () -> Unit
) {

    val context = LocalContext.current
    val viewModel = remember {
        val database = AppDatabase.getInstance(context)
        val repository = UserRepository(database, context)

        RestoreViewModel(repository)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = stringResource(R.string.restore_access_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.restore_description, userEmail),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    viewModel.restoreAccount(userEmail, onRestoreComplete)
                },
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = stringResource(R.string.restore_account_button)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {
                    viewModel.permanentlyDelete(userEmail, onPermanentlyDeleteComplete)
                },
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
            ) {
                Text(
                    text = stringResource(R.string.permanently_delete_button)
                )
            }
        }

        viewModel.errorMessage?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}