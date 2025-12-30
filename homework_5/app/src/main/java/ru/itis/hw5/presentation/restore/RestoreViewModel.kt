package ru.itis.hw5.presentation.restore

import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.itis.hw5.constants.StringResources
import ru.itis.hw5.data.repository.UserRepository

class RestoreViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun restoreAccount(email: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val success = userRepository.restoreAccount(email)
            isLoading = false

            if (success) {
                onComplete()
            } else {
                errorMessage = StringResources.RESTORE_ERROR
            }
        }
    }

    fun permanentlyDelete(email: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            isLoading = true
            val success = userRepository.permanentlyDeleteAccount(email)
            isLoading = false
            if (success) {
                onComplete()
            } else {
                errorMessage = StringResources.DELETE_ERROR
            }
        }
    }
}