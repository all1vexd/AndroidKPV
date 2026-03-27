package ru.itis.hw4.utils

import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.itis.hw4.CoroutineTracker

suspend fun sequentialCoroutines(
    count: Int,
    selectedDispatcher: CoroutineDispatcher,
    onError: (Exception, Int) -> Unit,
    inCreateTime: Boolean,
    tracker: CoroutineTracker,
    context: Context
) {
    coroutineScope {
        for (i in 1..count) {


            val job = if (inCreateTime) {
                launch(selectedDispatcher, start = CoroutineStart.LAZY) {
                    heavyOperation(onError, i, tracker, context)
                }
            } else {
                launch(selectedDispatcher) {
                    heavyOperation(onError, i, tracker, context)
                }
            }

            if (inCreateTime) {
                job.start()
            }

            job.join()
        }
    }
}

suspend fun parallelCoroutines(
    count: Int,
    selectedDispatcher: CoroutineDispatcher,
    onError: (Exception, Int) -> Unit,
    inCreateTime: Boolean,
    tracker: CoroutineTracker,
    context: Context
) {

    val jobs = mutableListOf<Job>()

    coroutineScope {
        for (i in 1..count) {

            val job = if (inCreateTime) {
                launch(selectedDispatcher, start = CoroutineStart.LAZY) {
                    heavyOperation(onError, i, tracker, context)
                }
            } else {
                launch(selectedDispatcher) {
                    heavyOperation(onError, i, tracker, context)
                }
            }
            jobs.add(job)
        }

        if (inCreateTime) {
            jobs.forEach { it.start() }
        }

        jobs.forEach{ it.join() }
    }
}