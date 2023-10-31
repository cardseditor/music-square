package com.example.musicsquare.feature.greeting

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicsquare.core.designsystem.theme.MusicSquareTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = name) })
        },
        content = {
        Column {
            Row {
                Text(
                    text = "All"
                )
                Text(
                    text = "Pop"
                )
                Text(
                    text = "Popular"
                )
                Text(
                    text = "Library"
                )
            }
            LazyColumn(content = {
                items(100) {
                    Text(text = "Item $it")
                }
            }, modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 48.dp, bottom = 16.dp))
        }
    })
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MusicSquareTheme {
        Greeting("Music Square")
    }
}