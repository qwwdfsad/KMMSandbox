package com.example.kmmsandbox

import com.example.kmmsandbox.util.ClassFlow
import com.example.kmmsandbox.util.SuspendWrapper
import com.example.kmmsandbox.util.asClassFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration.Companion.seconds

// A similar to flow story goes for suspend functions:
// currently, they are not cancellable from iOS
// Manual solution is to provide some sort of wrapper
// Kmp-native-coroutines & Skie generate such wrappers automatically

class GreetingService : MyService() {
    private val platform: Platform = getPlatform()

    suspend fun greet(): String {
        for (i in 1..10) {
            if (i != 1) {
                delay(0.3.seconds)
            }
            failInErrorModeOn { i == 6 }
            _progressFlow.value = "$i/10"
        }
        return "Hello, ${platform.name}!"
    }

    // Extra method to be used from iOS
    fun greetWrapper() = SuspendWrapper { greet() }

    // To observe cancellation of the suspend function above:
    private val _progressFlow = MutableStateFlow<String?>(null)

    val progressFlow: ClassFlow<String?> get() = _progressFlow.asClassFlow()
}