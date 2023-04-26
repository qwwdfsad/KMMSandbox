package com.example.kmmsandbox.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

// This is a demonstration of solutions used by the community
// The similar classes should be added to different types of Flow (StateFlow, MutableStateFlow, etc)

class ClassFlow<T>(
    private val delegate: Flow<T>
) : Flow<T> by delegate {

    // One option to "hide" 'subscribe' in Android is to use expect/actual classes
    // and add 'subscribe' in iOS version
    fun subscribe(onEach: (T) -> Unit, onComplete: (cause: Throwable?) -> Unit): Cancellable {
        // you can find different versions of which scope is used, might be a parameter
        // if we use Dispatchers.Default here,
        // we should run logic that updates the UI from the main thread on the call site
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        val job = delegate
            .onEach { onEach(it) }
            .catch { onComplete(it) }
            .onCompletion { onComplete(null) }
            .launchIn(scope)

        return object: Cancellable {
            override fun cancel() {
                job.cancel()
            }
        }
    }
}

fun <T> Flow<T>.asClassFlow(): ClassFlow<T> = ClassFlow(this)