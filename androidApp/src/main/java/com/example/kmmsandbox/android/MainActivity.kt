package com.example.kmmsandbox.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val myViewModel = MyViewModel()
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column {
                        val tickValue = myViewModel.tickerFow.collectAsState().value
                        TickerView(
                            tickValue = tickValue,
                            onStart = myViewModel::startTicker,
                            onCancel = myViewModel::cancelTicker,
                        )
                        Divider()
                        val progress = myViewModel.progressFlow.collectAsState(null).value
                        GreetingWithProgressView(
                            progress = progress,
                            greetingText = myViewModel.greetingText.value,
                            onLoad = myViewModel::loadGreeting,
                            onCancel = myViewModel::cancelGreeting,
                        )
                    }
                }
            }
        }
    }
}