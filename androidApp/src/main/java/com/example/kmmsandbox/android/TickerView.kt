package com.example.kmmsandbox.android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TickerView(
    tickValue: Int?,
    onStart: () -> Unit,
    onCancel: () -> Unit,
) {
    Column {
        val withPadding = Modifier.padding(5.dp)
        Text(modifier = withPadding, text = "Tick: ${tickValue ?: "-"}")
        Row {
            Button(modifier = withPadding, onClick = onStart) {
                Text("Start")
            }
            Button(modifier = withPadding, onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}

@Preview
@Composable
fun TickerPreview() {
    MyApplicationTheme {
        TickerView(
            tickValue = 10,
            onStart = {},
            onCancel = {},
        )
    }
}
