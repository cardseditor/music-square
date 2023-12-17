package com.example.musicsquare.feature.home

import MultiThemePreviews
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musicsquare.core.data.music.Music
import com.example.musicsquare.core.data.music.MusicViewModel
import com.example.musicsquare.core.designsystem.components.MusicItem
import com.example.musicsquare.core.designsystem.theme.MusicSquareTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

object HomeDestination {
    const val ROUTE = "home"
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun Home(
    navigateToListening: () -> Unit = { },
    musicViewModel: MusicViewModel = viewModel(),
) {
    var isAllButtonSelected by remember { mutableStateOf(true) }
    val mediaAudioPermissionState =
        rememberPermissionState(android.Manifest.permission.READ_MEDIA_AUDIO)
//    val externalStoragePermissionState =
//        rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    val musicFiles by musicViewModel.musicFiles.observeAsState(
        listOf(
            "file:///storage/emulated/0/Music/CultureCode.mp3",
            "file:///storage/emulated/0/Music/Cartoon.mp3",
        )
    )
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Music Square") },
        )
    }) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            item {
                if (!mediaAudioPermissionState.status.isGranted) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val textToShow = if (mediaAudioPermissionState.status.shouldShowRationale) {
                            "このアプリには音楽アクセスの権限が必要です。権限を付与してください。"
                        } else {
                            "音楽アクセスの権限がありません。"
                        }

                        Text(textToShow)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { mediaAudioPermissionState.launchPermissionRequest() }) {
                            Text("アクセスを許可")
                        }
                    }
                }
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
//                if (!externalStoragePermissionState.status.isGranted) {
//                    Column(modifier = Modifier.padding(16.dp)) {
//                        val textToShow = if (externalStoragePermissionState.status.shouldShowRationale) {
//                            "このアプリには外部ストレージアクセスの権限が必要です。権限を付与してください。"
//                        } else {
//                            "外部ストレージアクセスの権限がありません。"
//                        }
//
//                        Text(textToShow)
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Button(onClick = { externalStoragePermissionState.launchPermissionRequest() }) {
//                            Text("アクセスを許可")
//                        }
//                    }
//                } else {
//                    Button(onClick = onPickFolder) {
//                        Text("フォルダーを選択")
//                    }
//                }
            }
            items(musicFiles) { path ->
                val music = Music.fromUri(Uri.parse(path.toString()))
                MusicItem(
                    music = music,
                    onMusicSelected = { selectedMusic ->
                        musicViewModel.setCurrentMusic(selectedMusic)
                    }
                )
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@MultiThemePreviews
@Composable
fun HomePreview() {
    MusicSquareTheme {
        Home()
    }
}
