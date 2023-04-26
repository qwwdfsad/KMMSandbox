package com.example.kmmsandbox.android

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmmsandbox.GreetingService
import com.example.kmmsandbox.TickerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyViewModel: ViewModel() {

    // flow

    private val tickerService = TickerService()
    private var _tickerFlow = MutableStateFlow<Int?>(null)
    private var tickerJob: Job? = null

    val tickerFow: StateFlow<Int?> = _tickerFlow

    fun startTicker() {
        cancelTicker()
        tickerJob = viewModelScope.launch {
            tickerService.launchTickFlow().collect {
                _tickerFlow.value = it
            }
        }
    }

    fun cancelTicker() {
        tickerJob?.cancel()
        _tickerFlow.value = null
    }


    // suspend function

    private val greetingService = GreetingService()

    private var greetingJob: Job? = null

    val greetingText = mutableStateOf("")

    fun loadGreeting() {
        cancelGreeting()
        greetingJob = viewModelScope.launch {
            greetingText.value = "Loading..."
            greetingText.value = greetingService.greet()
        }
    }

    fun cancelGreeting() {
        if (greetingJob?.isCompleted != true) {
            greetingText.value = "Loading canceled"
        }
        greetingJob?.cancel()
    }

    val progressFlow: Flow<String?>
        get() = greetingService.progressFlow
}