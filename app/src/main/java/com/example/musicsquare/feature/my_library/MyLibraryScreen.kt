package com.example.musicsquare.feature.my_library

import MultiThemePreviews
import MusicItem
import MyLibraryViewModel
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musicsquare.core.data.music.Music
import com.example.musicsquare.core.designsystem.theme.MusicSquareTheme

object MyLibraryDestination {
    const val ROUTE = "my_library"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLibrary(
    navigateToListening: () -> Unit = { },
) {
    val musicList = MyLibraryViewModel().musicList
    var isAllButtonSelected by remember { mutableStateOf(true) }
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = TopLevelDestination.MY_LIBRARY.title) })
    }) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            item {
                Row(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                ) {
                    ElevatedSuggestionChip(
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (isAllButtonSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                            labelColor = if (isAllButtonSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        ),
                        onClick = { isAllButtonSelected = !isAllButtonSelected },
                        label = { Text(text = "All") },
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    ElevatedSuggestionChip(
                        onClick = navigateToListening,
                        label = { Text(text = "Favorite") },
                    )
                }
            }
            items(50) {
                if (isAllButtonSelected || Music.mock.isFavorite) {
                    MusicItem(
                        Music.mock,
                        navigateToListening = navigateToListening,
                    )
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .safeContentPadding()
                        .padding(bottom = 80.dp)
                )
            }
        }
    }
}

@MultiThemePreviews
@Composable
fun MyLibraryPreview() {
    MusicSquareTheme {
        MyLibrary()
    }
}
