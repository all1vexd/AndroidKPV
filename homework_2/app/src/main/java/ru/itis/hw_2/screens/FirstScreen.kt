package ru.itis.hw_2.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import ru.itis.hw_2.R
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.itis.hw_2.navigation.NavParams
import androidx.compose.ui.platform.LocalContext
import ru.itis.hw_2.utils.validateEmail
import ru.itis.hw_2.utils.validatePassword


@Composable
fun FirstScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var emailError by remember {
        mutableStateOf<String?>(null)
    }
    var passwordError by remember {
        mutableStateOf<String?>(null)
    }
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column (
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { it ->
                    email = it
                    emailError = null
                },
                label = {
                    Text(
                    stringResource(id = R.string.email_label)
                    )
                },
                isError = emailError != null,
                modifier = Modifier
                    .fillMaxWidth(),
                singleLine = true,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.email_placeholder)
                    )
                },
                supportingText = {
                    val error = emailError
                    if (error != null) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            OutlinedTextField(
                value = password,
                onValueChange = { it ->
                    password = it
                    passwordError = null
                },
                isError = passwordError != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(
                        onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }
                    ) {
                        Text(
                            text = if (isPasswordVisible) stringResource(id = R.string.hide_password) else stringResource(id = R.string.show_password),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.password_placeholder)
                    )
                },
                supportingText = {
                    val error = passwordError
                    if (error != null) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
            )
        }

        Button(
            onClick = {
                emailError = validateEmail(context = context, email = email)
                passwordError = validatePassword(context = context, password = password)
                if (emailError == null && passwordError == null) {
                    navController.navigate(NavParams.secondScreenBuilder(email))
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.ime)
                .padding(bottom = 32.dp)
                .height(96.dp)
        ) {
            Text(
                text = stringResource(R.string.confirm)
            )
        }
    }
}