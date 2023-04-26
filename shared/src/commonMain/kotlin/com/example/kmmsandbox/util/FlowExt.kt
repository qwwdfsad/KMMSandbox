package com.example.kmmsandbox.util

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

// This is a demonstration of solutions used by the community

@OptIn(DelicateCoroutinesApi::class)
fun <T> Flow<T>.subscribe(onEach: (T) -> Unit, onComplete: (cause: Throwable?) -> Unit): Cancellable {
    // you can find different versions of which scope is used
//    val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    val job = this
        .onEach { onEach(it) }
        .catch { onComplete(it) }
        .onCompletion { onComplete(null) }
        .launchIn(GlobalScope)

    return object : Cancellable {
        override fun cancel() {
            job.cancel()
        }
    }
}