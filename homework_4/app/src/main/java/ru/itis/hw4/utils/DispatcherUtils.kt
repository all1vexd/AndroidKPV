package ru.itis.hw4.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

fun getDispatcherName(dispatcher: CoroutineDispatcher): String {
    return when (dispatcher) {
        Dispatchers.Main -> "Dispatchers.Main"
        Dispatchers.IO -> "Dispatchers.IO"
        Dispatchers.Default -> "Dispatchers.Default"
        Dispatchers.Unconfined -> "Dispatchers.Unconfined"
        else -> "Custom"
    }
}