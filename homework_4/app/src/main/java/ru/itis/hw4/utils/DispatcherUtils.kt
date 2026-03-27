package ru.itis.hw4.utils

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.itis.hw4.R

fun getDispatcherName(dispatcher: CoroutineDispatcher, context: Context): String {
    return when (dispatcher) {
        Dispatchers.Main -> context.getString(R.string.dispatchers_main)
        Dispatchers.IO -> context.getString(R.string.dispatchers_io)
        Dispatchers.Default -> context.getString(R.string.dispatchers_default)
        Dispatchers.Unconfined -> context.getString(R.string.dispatchers_unconfined)
        else -> context.getString(R.string.dispatcher_custom)
    }
}