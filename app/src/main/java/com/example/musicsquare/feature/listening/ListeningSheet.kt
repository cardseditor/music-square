import android.annotation.SuppressLint
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
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import com.example.musicsquare.core.data.active_music.ActiveMusic
import com.example.musicsquare.core.data.music.Music
import com.example.musicsquare.core.designsystem.theme.MusicSquareTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ListeningSheet(
) {
    val mediaAudioPermissionState =
        rememberPermissionState(android.Manifest.permission.READ_MEDIA_AUDIO)

    val context = LocalContext.current
    val activeMusic by remember {
        mutableStateOf(ActiveMusic(isPlaying = false, progress = 0.0f, currentMusic = Music.mock))
    }
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(activeMusic.currentMusic.path))
            prepare()
            addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    activeMusic.isPlaying = isPlaying
                }
            })
        }
    }
    val mediaSession = remember {
        MediaSession.Builder(context, player).build()
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaSession.release()
            player.release()
        }
    }

    LaunchedEffect(activeMusic.isPlaying) {
        while (activeMusic.isPlaying) {
            val progress = player.currentPosition.toFloat() / player.duration.toFloat()
            activeMusic.updateProgress(progress)
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(32.dp)
    ) {
        val albumArt = activeMusic.currentMusic.albumArt
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
                text = Music.mock.title,
                style = MaterialTheme.typography.titleLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                text = Music.mock.author,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        if (!mediaAudioPermissionState.status.isGranted) {
            Column {
                val textToShow = if (mediaAudioPermissionState.status.shouldShowRationale) {
                    "The media access is important for this app. Please grant the permission."
                } else {
                    "Media access not available"
                }

                Text(textToShow)
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { mediaAudioPermissionState.launchPermissionRequest() }) {
                    Text("Request permission")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LinearProgressIndicator(
            progress = activeMusic.progress,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatSeconds(
                    (activeMusic.progress * activeMusic.currentMusic.totalSeconds).roundToInt()
                )
            )
            Text(
                text = formatSeconds(
                    activeMusic.currentMusic.totalSeconds
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
            FilledTonalIconToggleButton(checked = activeMusic.isPlaying,
                onCheckedChange = { shouldBePlaying ->
                    activeMusic.isPlaying = shouldBePlaying
                    player.playWhenReady = shouldBePlaying
                }) {
                Icon(
                    imageVector = if (activeMusic.isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                    contentDescription = if (activeMusic.isPlaying) "Pause" else "Play"
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
        ListeningSheet()
    }
}
