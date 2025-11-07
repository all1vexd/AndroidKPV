package ru.itis.hw_3.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ThirdScreenViewModel: ViewModel() {

    val messages = mutableStateListOf<String>();

    fun addMessage(message: String) {
        messages.add(message)
    }
}