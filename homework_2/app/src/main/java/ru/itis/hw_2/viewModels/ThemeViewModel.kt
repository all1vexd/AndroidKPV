package ru.itis.hw_2.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.itis.hw_2.ui.theme.AppTheme
import androidx.compose.runtime.State

class ThemeViewModel: ViewModel() {

    private val _currentTheme = mutableStateOf(AppTheme.BLUE)
    val currentTheme: State<AppTheme> = _currentTheme

    fun setTheme(theme: AppTheme) {
        _currentTheme.value = theme
    }
}