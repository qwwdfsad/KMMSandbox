package com.example.kmmsandbox

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

class TickerService : MyService() {
    @NativeCoroutines
    fun launchTickFlow() = flow {
        for (i in 0..100) {
            delay(0.3.seconds)
            failInErrorModeOn { i == 6 }
            emit(i)
        }
    }
}