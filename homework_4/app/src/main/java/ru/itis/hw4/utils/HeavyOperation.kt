package ru.itis.hw4.utils

import android.content.Context
import androidx.activity.contextaware.ContextAware
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.itis.hw4.CoroutineTracker
import ru.itis.hw4.R
import kotlin.random.Random

suspend fun heavyOperation(
    onError: (Exception, Int) -> Unit,
    i: Int,
    tracker: CoroutineTracker,
    context: Context
) {

    try {
        val delayTime = Random.nextLong(from = 1000, until = 10000)

        delay(delayTime)

        if (delayTime >= 7000L && Random.nextFloat() < 0.3f) {
            throwRandomException(i, context)
        }
        withContext(Dispatchers.Main) {
            tracker.jobIncrement()
        }


    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        onError(e, i)
        withContext(Dispatchers.Main) {
            tracker.jobIncrement()
        }
    }
}

private fun throwRandomException(
    index: Int,
    context: Context
) {
    val exceptions = listOf(
        RuntimeException(context.getString(R.string.error_timeout, index)),
        IllegalArgumentException(context.getString(R.string.error_invalid_argument, index)),
        IllegalStateException(context.getString(R.string.settings_reset))
    )
    throw exceptions.random()
}
