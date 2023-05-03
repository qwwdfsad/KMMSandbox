package com.example.kmmsandbox.util

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class SuspendWrapper<T>(private val block: suspend () -> T) {
    fun subscribe(
        onSuccess: (item: T) -> Unit,
        onThrow: (error: Throwable) -> Unit
    ): Cancellable {
        val job = GlobalScope.launch(Dispatchers.Main) {
            try {
                onSuccess(block())
            } catch (error: Throwable) {
                onThrow(error)
            }
        }
        return object: Cancellable {
            override fun cancel() {
                job.cancel()
            }
        }
    }
}