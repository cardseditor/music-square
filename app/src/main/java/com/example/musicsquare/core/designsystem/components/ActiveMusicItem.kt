package com.example.musicsquare.core.designsystem.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.musicsquare.core.service.MusicPlaybackService
import com.example.musicsquare.feature.home.PlaybackStateHolder

@Composable
fun ActiveMusicItem(
    navigateToListening: () -> Unit = { },
) {
    val context = LocalContext.current
    val currentMusic by PlaybackStateHolder.currentMusic.collectAsState()
    val isPlaying by PlaybackStateHolder.isPlaying.collectAsState()

    fun controlMusicService(action: String, musicUri: Uri) {
        val serviceIntent = Intent(context, MusicPlaybackService::class.java).apply {
            putExtra("ACTION", action)
            putExtra("MUSIC_URI", musicUri.toString())
        }
        context.startService(serviceIntent)
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = navigateToListening,
            )
            .padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 4.dp)
    ) {
        val albumArt = currentMusic.albumArt
        if (albumArt != null) {
            Image(
                bitmap = albumArt.asImageBitmap(),
                contentDescription = "Album Art",
                modifier = Modifier
                    .size(58.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
                    .aspectRatio(1f),
            )
        } else {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(MaterialTheme.shapes.small)
                    .background(MaterialTheme.colorScheme.inverseOnSurface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.MusicNote,
                    contentDescription = "details",
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = currentMusic.title, overflow = TextOverflow.Ellipsis, maxLines = 1
            )
            Text(
                text = currentMusic.author,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                color = MaterialTheme.colorScheme.outline
            )
        }
        IconToggleButton(
            checked = isPlaying,
            onCheckedChange = { shouldBePlaying ->
                if (shouldBePlaying) {
                    controlMusicService("PLAY", currentMusic.path)
                } else {
                    controlMusicService("PAUSE", currentMusic.path)
                }
            }) {
            Icon(
                imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play"
            )
        }
    }
}
