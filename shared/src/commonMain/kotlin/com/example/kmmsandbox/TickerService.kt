package com.example.kmmsandbox

import com.example.kmmsandbox.util.Cancellable
import com.example.kmmsandbox.util.ClassFlow
import com.example.kmmsandbox.util.asClassFlow
import com.example.kmmsandbox.util.subscribe
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class TickerService {
    // Is unusable in this way from iOS:
    fun launchTickFlow() = flow {
        for (i in 0..100) {
            delay(0.4.seconds)
            emit(i)
        }
    }

    // Exposing 'tick' flow to iOS:

    // Solution# 1. Explicit, changing the type of resulting Flow to ClassFlow
    // Problems:
    // - you need to replace the type everywhere, and have it from Android (some folks don't like it)
    // - generic type becomes nullable in Swift
    // - for primitive generics, the usual Kotlin->Swift type conversion doesn't work

    fun launchTickClassFlow(): ClassFlow<Int> = launchTickFlow().asClassFlow()

    // Solution# 2. Explicit, providing additionally a separate function with callbacks
    // Solves the issue with primitive arguments with ClassFlow
    // Problems:
    // - you need to do it for each property of Flow type (some people even create IJ live templates for that)
    // => much more verbose

    fun launchTicks(
        onEach: (Int) -> Unit,
        onComplete: (Throwable?) -> Unit
    ): Cancellable =
        launchTickFlow().subscribe(onEach, onComplete)
}