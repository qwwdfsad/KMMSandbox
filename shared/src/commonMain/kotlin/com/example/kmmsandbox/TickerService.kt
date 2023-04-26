package com.example.kmmsandbox

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class TickerService {
    @NativeCoroutines
    fun launchTickFlow() = flow {
        for (i in 0..100) {
            delay(0.4.seconds)
            emit(i)
        }
    }
}