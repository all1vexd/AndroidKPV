package ru.itis.hw4.utils

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import ru.itis.hw4.CoroutineTracker
import kotlin.random.Random

suspend fun heavyOperation(
    onError: (Exception, Int) -> Unit,
    i: Int,
    tracker: CoroutineTracker
) {

    try {
        val delayTime = Random.nextLong(from = 1000, until = 10000)

        delay(delayTime)

        if (delayTime >= 7000L && Random.nextFloat() < 0.3f) {
            throwRandomException(i)
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
    index: Int
) {
    val exceptions = listOf(
        RuntimeException("Timeout in coroutine $index"),
        IllegalArgumentException("Invalid argument in coroutine $index"),
        IllegalStateException("Illegal state in coroutine $index")
    )
    throw exceptions.random()
}
