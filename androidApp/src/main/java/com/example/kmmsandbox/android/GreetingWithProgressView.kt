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
fun GreetingWithProgressView(
    progress: String?,
    greetingText: String,
    onLoad: () -> Unit,
    onCancel: () -> Unit,
) {
    Column {
        val withPadding = Modifier.padding(5.dp)
        Text(modifier = withPadding, text = greetingText)

        val loadingText = if (progress == null)
            "No loading in progress"
        else
            "Loading progress: $progress"
        Text(modifier = withPadding, text = loadingText)
        Row {
            Button(modifier = withPadding, onClick = onLoad) {
                Text("Load")
            }
            Button(modifier = withPadding, onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}

@Preview
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        GreetingWithProgressView(
            progress = "1/10",
            greetingText = "Hello, Android!",
            onLoad = {},
            onCancel = {},
        )
    }
}
