package com.example.kmmsandbox

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration.Companion.seconds

class GreetingService {
    private val platform: Platform = getPlatform()

    suspend fun greet(): String {
        for (i in 1..10) {
            if (i != 1) {
                delay(0.3.seconds)
            }
            _progressFlow.value = "$i/10"
        }
        return "Hello, ${platform.name}!"
    }

    // To observe cancellation of the suspend function above:
    private val _progressFlow = MutableStateFlow<String?>(null)

    val progressFlow: Flow<String?> get() = _progressFlow
}