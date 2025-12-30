package ru.itis.hw5.presentation.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.itis.hw5.constants.StringResources
import ru.itis.hw5.data.database.entities.User
import ru.itis.hw5.data.repository.MovieRepository
import ru.itis.hw5.data.repository.RegistrationResult
import ru.itis.hw5.data.repository.UserRepository

class RegistrationViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    var registeredUser by mutableStateOf<User?>(null)
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isSuccess by mutableStateOf(false)


    fun register() {
        if (!validate()) return

        viewModelScope.launch {
            isLoading = true
            val result = userRepository.register(name, email, password, confirmPassword)
            when (result) {
                is RegistrationResult.Success -> {
                    isSuccess = true
                    errorMessage = null
                    registeredUser = result.user
                }
                is RegistrationResult.Error -> {
                    errorMessage = result.message
                }
            }
            isLoading = false
        }
    }

    private fun validate(): Boolean {
        if (name.isBlank()) {
            errorMessage = StringResources.ERROR_NAME_EMPTY
            return false
        }

        if (name.length < 5) {
            errorMessage = StringResources.ERROR_NAME_TOO_SHORT
            return false
        }

        if (email.isBlank()) {
            errorMessage = StringResources.ERROR_EMAIL_EMPTY
            return false
        }

        if (password.isBlank()) {
            errorMessage = StringResources.ERROR_PASSWORD_EMPTY
            return false
        }

        if (password.length < 6) {
            errorMessage = StringResources.ERROR_PASSWORD_TOO_SHORT
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errorMessage = StringResources.ERROR_EMAIL_INVALID
            return false
        }

        if (password != confirmPassword || confirmPassword.isBlank()) {
            errorMessage = StringResources.ERROR_PASSWORDS_NOT_MATCH
            return false
        }

        return true
    }

}