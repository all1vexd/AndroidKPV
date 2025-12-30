import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.itis.hw5.R
import ru.itis.hw5.data.database.AppDatabase
import ru.itis.hw5.data.repository.UserRepository
import ru.itis.hw5.presentation.registration.RegistrationViewModel

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit,
) {
    val context = LocalContext.current
    val viewModel = remember {
        val database = AppDatabase.getInstance(context)
        val repository = UserRepository(database, context)
        RegistrationViewModel(repository)
    }

    LaunchedEffect(viewModel.isSuccess) {
        if (viewModel.isSuccess) {
            viewModel.registeredUser?.let {
                onNavigateToLogin()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.registration_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = viewModel.name,
            onValueChange = {
                viewModel.name = it
            },
            label = {
                Text(
                    text = stringResource(R.string.name_hint)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = {
                viewModel.email = it
            },
            label = {
                Text(
                    text = stringResource(R.string.email_hint)
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

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
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = viewModel.confirmPassword,
            onValueChange = {
                viewModel.confirmPassword = it
            },
            label = {
                Text(
                    text = stringResource(R.string.confirm_password_hint)
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        viewModel.errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.register()
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = stringResource(R.string.register_button)
                )
            }
        }

        TextButton(
            onClick = onNavigateToLogin
        ) {
            Text(
                text = stringResource(R.string.login_prompt)
            )
        }
    }
}