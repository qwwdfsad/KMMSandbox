package com.example.kmmsandbox.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
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
                            tickerErrorMessage = myViewModel.tickerErrorMessage.value,
                            onStart = myViewModel::startTicker,
                            onCancel = myViewModel::cancelTicker,
                        )
                        Divider()
                        val progress = myViewModel.progressFlow.collectAsState(null).value
                        GreetingWithProgressView(
                            progress = progress,
                            greetingText = myViewModel.greetingText.value,
                            greetingErrorMessage = myViewModel.greetingErrorMessage.value,
                            onLoad = myViewModel::loadGreeting,
                            onCancel = myViewModel::cancelGreeting,
                        )
                        Divider()
                        ErrorModeView(
                            checked = myViewModel.errorMode.value,
                            onCheckedChange = myViewModel::changeErrorMode
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun ErrorModeView(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
            Text("Error mode")
        }
    }
}