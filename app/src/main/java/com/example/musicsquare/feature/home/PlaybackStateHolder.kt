package com.example.musicsquare.feature.home

import com.example.musicsquare.core.data.music.Music
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object PlaybackStateHolder {
    private val _isPlaying = MutableStateFlow(false)
    private val _progress = MutableStateFlow(0.0f)
    private val _currentMusic = MutableStateFlow(Music.mock)

    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    val progress: StateFlow<Float> = _progress.asStateFlow()
    val currentMusic: StateFlow<Music> = _currentMusic.asStateFlow()

    fun updatePlaybackState(isPlaying: Boolean) {
        _isPlaying.value = isPlaying
    }

    fun updateProgress(progress: Float) {
        _progress.value = progress
    }

    fun setCurrentMusic(music: Music) {
        _currentMusic.value = music
    }
}
