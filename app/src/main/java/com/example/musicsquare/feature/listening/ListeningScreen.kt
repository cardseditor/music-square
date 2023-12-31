package com.example.musicsquare.feature.listening

import MultiThemePreviews
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musicsquare.core.designsystem.theme.MusicSquareTheme
import com.example.musicsquare.core.service.MusicPlaybackService
import com.example.musicsquare.feature.home.PlaybackStateHolder
import kotlin.math.roundToInt

object ListeningDestination {
    const val ROUTE = "listening"
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Listening(
    navController: NavController = NavController(LocalContext.current),
    navigateBack: () -> Unit = { navController.popBackStack() },
    onFavoriteClicked: () -> Unit = { },
    onMoreOptionsClicked: () -> Unit = { },
) {
//    val mediaAudioPermissionState =
//        rememberPermissionState(android.Manifest.permission.READ_MEDIA_AUDIO)
    val context = LocalContext.current
    val isPlaying by PlaybackStateHolder.isPlaying.collectAsState()
    val progress by PlaybackStateHolder.progress.collectAsState()
    val activeMusic by PlaybackStateHolder.currentMusic.collectAsState()

    fun controlMusicService(action: String, musicUri: Uri) {
        val serviceIntent = Intent(context, MusicPlaybackService::class.java).apply {
            putExtra("ACTION", action)
            putExtra("MUSIC_URI", musicUri.toString())
        }
        context.startService(serviceIntent)
    }

    Scaffold(topBar = {
        TopAppBar(title = {}, navigationIcon = {
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack, contentDescription = "Go back"
                )
            }
        }, actions = {
            IconButton(onClick = onFavoriteClicked) {
                Icon(
                    imageVector = Icons.Rounded.FavoriteBorder,
                    contentDescription = "Add to favorites"
                )
            }
            IconButton(onClick = onMoreOptionsClicked) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert, contentDescription = "View more options"
                )
            }
        })
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(32.dp)
        ) {
            val albumArt = activeMusic.albumArt
            if (albumArt != null) {
                Image(
                    bitmap = albumArt.asImageBitmap(),
                    contentDescription = "Album Art",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.inverseOnSurface)
                )
            } else {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.inverseOnSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MusicNote,
                        contentDescription = "Music note",
                        modifier = Modifier.size(64.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = activeMusic.title,
                    style = MaterialTheme.typography.titleLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(
                    text = activeMusic.author,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            }
//            if (!mediaAudioPermissionState.status.isGranted) {
//                Column {
//                    val textToShow = if (mediaAudioPermissionState.status.shouldShowRationale) {
//                        "The media access is important for this app. Please grant the permission."
//                    } else {
//                        "Media access not available"
//                    }
//
//                    Text(textToShow)
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Button(onClick = { mediaAudioPermissionState.launchPermissionRequest() }) {
//                        Text("Request permission")
//                    }
//                }
//            }
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = progress,
                strokeCap = StrokeCap.Round,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = formatSeconds(
                        (progress * activeMusic.totalSeconds).roundToInt()
                    )
                )
                Text(
                    text = formatSeconds(
                        activeMusic.totalSeconds
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Rounded.SkipPrevious, contentDescription = "Replay song"
                    )
                }
                Spacer(modifier = Modifier.padding(16.dp))
                FilledTonalIconToggleButton(checked = isPlaying,
                    onCheckedChange = { shouldBePlaying ->
                        if (shouldBePlaying) {
                            controlMusicService("PLAY", activeMusic.path)
                        } else {
                            controlMusicService("PAUSE", activeMusic.path)
                        }
                    }) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
                Spacer(modifier = Modifier.padding(16.dp))
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Rounded.SkipNext, contentDescription = "More options"
                    )
                }
            }
        }
    }
}

fun formatSeconds(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@MultiThemePreviews
@Composable
fun ListeningPreview() {
    MusicSquareTheme {
        Listening()
    }
}
