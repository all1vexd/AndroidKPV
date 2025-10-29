package ru.itis.hw_2.utils

import android.content.Context
import android.util.Patterns
import ru.itis.hw_2.R


fun validateEmail(context: Context, email: String): String? {
    return when {
        email.isBlank() -> context.getString(R.string.email_empty_error)
        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> context.getString(R.string.email_invalid_error)
        else -> null
    }
}

fun validatePassword(context: Context, password: String): String? {
    return when {
        password.isBlank() -> context.getString(R.string.password_empty_error)
        password.length < 8 -> context.getString(R.string.password_length_error)
        else -> null
    }
}