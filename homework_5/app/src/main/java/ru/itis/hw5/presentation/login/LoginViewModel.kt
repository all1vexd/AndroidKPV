package ru.itis.hw5.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.itis.hw5.R
import ru.itis.hw5.constants.StringResources
import ru.itis.hw5.data.database.entities.User
import ru.itis.hw5.data.repository.LoginResult
import ru.itis.hw5.data.repository.UserRepository

class LoginViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    var loggedInUser by mutableStateOf<User?>(null)
    var pendingUserEmail by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isLoginSuccessful by mutableStateOf(false)

    fun validateForm(): Boolean {
        if (email.isBlank()) {
            errorMessage = StringResources.EMAIL_REQUIRED
            return false
        }

        if (password.isBlank()) {
            errorMessage = StringResources.PASSWORD_REQUIRED
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = StringResources.INVALID_EMAIL_FORMAT
            return false
        }

        return true

    }

    fun login() {
        if (!validateForm()) {
            return
        }

        errorMessage = null
        isLoading = true

        viewModelScope.launch {
            try {
                val result = userRepository.loginUser(email, password)

                handleLoginResult(result)
            } catch (e: Exception) {
                errorMessage = StringResources.ERROR + "${e.message}"
                isLoading = false
            }
        }
    }

    fun handleLoginResult(result: LoginResult) {
        when (result) {

            is LoginResult.Success -> {
                isLoginSuccessful = true
                errorMessage = null
                loggedInUser = result.user
            }

            is LoginResult.AccountDeleted -> {
                if (result.canRestore) {
                    pendingUserEmail = email
                    loggedInUser = result.user
                } else {
                    errorMessage = StringResources.ACCOUNT_DELETED_PERMANENTLY
                }
            }

            is LoginResult.InvalidCredentials -> {
                errorMessage = StringResources.INVALID_CREDENTIALS
            }

            LoginResult.UserNotFound -> {
                errorMessage = StringResources.USER_NOT_FOUND
            }

            is LoginResult.Error -> {
                errorMessage = StringResources.ERROR + "${result.message}"
            }
        }

        isLoading = false
    }

    fun clearForm() {
        email = ""
        password = ""
        errorMessage = null
    }

    fun clearError() {
        errorMessage = null
    }
}