package com.example.musicsquare.core.data.active_music

import com.example.musicsquare.core.data.music.Music
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ActiveMusic(
    isPlaying: Boolean = false,
    progress: Float = 0.0f,
    val currentMusic: Music = Music.mock
) {
    var isPlaying by mutableStateOf(isPlaying)
    var progress by mutableFloatStateOf(progress)

    fun updateProgress(newProgress: Float) {
        progress = newProgress
    }
}
