package ru.itis.hw5

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.hw5.data.database.AppDatabase
import ru.itis.hw5.data.database.entities.User
import ru.itis.hw5.data.repository.UserRepository
import ru.itis.hw5.presentation.login.LoginViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateBackToRegister: () -> Unit,
    onLoginSuccess: (User) -> Unit,
    onNavigateToRestore: (User) -> Unit
) {
    val context = LocalContext.current

    val viewModel = remember {

        val database = AppDatabase.getInstance(context)
        val repository = UserRepository(database, context)

        LoginViewModel(repository)
    }

    LaunchedEffect(viewModel.isLoginSuccessful) {
        if (viewModel.isLoginSuccessful) {
            viewModel.loggedInUser?.let { user ->
                onLoginSuccess(user)
            }
        }
    }

    LaunchedEffect(viewModel.pendingUserEmail) {
        if (viewModel.pendingUserEmail.isNotEmpty()) {
            viewModel.loggedInUser?.let { user ->
                onNavigateToRestore(user)
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.login_title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6200EE)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.login_subtitle),
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.email = it },
            label = {
                Text(
                    text = stringResource(R.string.email_hint)
                )
            },
            isError = viewModel.errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = {
                viewModel.password = it
            },
            label = {
                Text(
                    text = stringResource(R.string.password_hint)
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            isError = viewModel.errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        viewModel.errorMessage?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.login()
            },
            enabled = !viewModel.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text(
                    text = stringResource(R.string.login_button),
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onNavigateBackToRegister
        ) {
            Text(
                text = stringResource(R.string.register_prompt)
            )
        }
    }
}