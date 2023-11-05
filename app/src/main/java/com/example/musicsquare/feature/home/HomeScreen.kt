package com.example.musicsquare.feature.home

import MusicViewModel
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicsquare.core.data.music.Music
import com.example.musicsquare.core.designsystem.theme.MusicSquareTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(name: String) {
    val musicList = MusicViewModel().musicList
    var isAllButtonSelected by remember { mutableStateOf(true) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = name) })
        },
        content = {
            Column {
                Spacer(modifier = Modifier.padding(top = 48.dp))
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
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
                        onClick = { /*TODO*/ },
                        label = { Text(text = "Favorite") },
                    )
                }
                LazyColumn {
                    items(musicList) { music ->
                        if (isAllButtonSelected || music.isFavorite) {
                            MusicItem(music)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun MusicItem(music: Music) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .background(MaterialTheme.colorScheme.inverseOnSurface),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.MusicNote,
                contentDescription = "details",
            )
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = music.title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = music.author,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.outline
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = "actions",
            )
        }
    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    MusicSquareTheme {
        Home("Music Square")
    }
}