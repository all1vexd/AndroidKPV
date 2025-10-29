package ru.itis.hw_2.model

import androidx.compose.material3.ColorScheme

data class ThemeSet(
    val labelResId: Int,
    val light: ColorScheme,
    val dark: ColorScheme
)