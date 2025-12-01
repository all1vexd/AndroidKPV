package ru.itis.hw4

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CoroutineTracker {

    private var _completedCount by mutableStateOf(0)

    val totalCount: Int get() = _completedCount

    fun jobIncrement() {
        _completedCount++
    }

    fun reset() {
        _completedCount = 0
    }
}