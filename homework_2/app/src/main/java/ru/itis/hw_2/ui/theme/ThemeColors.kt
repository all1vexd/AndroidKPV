package ru.itis.hw_2.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import ru.itis.hw_2.R
import ru.itis.hw_2.model.ThemeSet

enum class AppTheme(val scheme: ThemeSet) {
    BLUE(
        scheme = ThemeSet(
            labelResId = R.string.blue,
            light = lightColorScheme(
                primary = Color(0xFF1976D2),
                onPrimary = Color(0xFFFFFFFF),
                background = Color(0xFFE3F2FD)
            ),
            dark = darkColorScheme(
                primary = Color(0xFF90CAF9),
                onPrimary = Color(0xFF000000),
                background = Color(0xFF0D1B2A)
            )
        )
    ),
    RED(
        scheme = ThemeSet(
            labelResId = R.string.red,
            light = lightColorScheme(
                primary = Color(0xFFD32F2F),
                onPrimary = Color(0xFFFFFFFF),
                background = Color(0xFFFFEBEE)
            ),
            dark = darkColorScheme(
                primary = Color(0xFFF44336),
                onPrimary = Color(0xFF000000),
                background = Color(0xFF1A0B0C)
            )
        )
    ),
    GREEN(
        scheme = ThemeSet(
            labelResId = R.string.green,
            light = lightColorScheme(
                primary = Color(0xFF388E3C),
                onPrimary = Color(0xFFFFFFFF),
                background = Color(0xFFE8F5E8)
            ),
            dark = darkColorScheme(
                primary = Color(0xFF4CAF50),
                onPrimary = Color(0xFF000000),
                background = Color(0xFF0D1A0E)
            )
        )
    )
}

private fun createdCustomColorScheme(
    appTheme: AppTheme,
    darkTheme: Boolean
): ColorScheme {
    return if (darkTheme) {
        when (appTheme) {
            AppTheme.RED -> AppTheme.RED.scheme.dark
            AppTheme.BLUE -> AppTheme.BLUE.scheme.dark
            AppTheme.GREEN -> AppTheme.GREEN.scheme.dark
        }
    } else {
        when (appTheme) {
            AppTheme.RED -> AppTheme.RED.scheme.light
            AppTheme.BLUE -> AppTheme.BLUE.scheme.light
            AppTheme.GREEN -> AppTheme.GREEN.scheme.light
        }
    }
}

fun getAppColorScheme(theme: AppTheme, darkTheme: Boolean): ColorScheme {
    return when (theme) {
        AppTheme.RED -> createdCustomColorScheme(AppTheme.RED, darkTheme)
        AppTheme.BLUE -> createdCustomColorScheme(AppTheme.BLUE, darkTheme)
        AppTheme.GREEN -> createdCustomColorScheme(AppTheme.GREEN, darkTheme)
    }
}
