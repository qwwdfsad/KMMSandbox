package com.example.kmmsandbox.android

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmmsandbox.GreetingService
import com.example.kmmsandbox.MyException
import com.example.kmmsandbox.TickerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {
    // flow

    private val tickerService = TickerService()
    private var _tickerFlow = MutableStateFlow<Int?>(null)
    private var tickerJob: Job? = null

    val tickerFow: StateFlow<Int?> = _tickerFlow
    private val _tickerErrorMessage = mutableStateOf("")
    val tickerErrorMessage: State<String> get() = _tickerErrorMessage

    fun startTicker() {
        _tickerErrorMessage.value = ""
        cancelTicker()
        tickerJob = viewModelScope.launch {
            tickerService.launchTickFlow()
                .catch { throwable: Throwable ->
                    if (throwable is MyException) {
                        _tickerErrorMessage.value = "'tickerFlow' error: ${throwable.localizedMessage}"
                    }
                }
                .collect {
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

    private val _greetingErrorMessage = mutableStateOf("")
    val greetingErrorMessage: State<String> get() = _greetingErrorMessage

    fun loadGreeting() {
        _greetingErrorMessage.value = ""
        cancelGreeting()
        greetingJob = viewModelScope.launch {
            greetingText.value = "Loading..."
            try {
                greetingText.value = greetingService.greet()
            } catch (e: MyException) {
                _greetingErrorMessage.value = "'greet' call error: ${e.localizedMessage}"
            }
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

    // error mode
    private val _errorMode = mutableStateOf(false)
    val errorMode: State<Boolean> get() = _errorMode
    fun changeErrorMode(value: Boolean) {
        _errorMode.value = value
        tickerService.changeErrorMode(value)
        greetingService.changeErrorMode(value)
    }
}