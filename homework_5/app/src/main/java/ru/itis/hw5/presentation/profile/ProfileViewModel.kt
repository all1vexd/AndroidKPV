package ru.itis.hw5.presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.itis.hw5.data.database.entities.User
import ru.itis.hw5.data.repository.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val userId: Int
): ViewModel() {

    var user by mutableStateOf<User?>(null)
    var username by mutableStateOf("")

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            user = userRepository.getUserById(userId)
            username = user?.userName ?: ""
        }
    }

    fun deleteAccount(onComplete: () -> Unit) {
        viewModelScope.launch {
            userRepository.deleteAccount(user?.id ?: 0)
            onComplete()
        }
    }
}