package com.example.musicsquare.core.data.music

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

//"file:///storage/emulated/0/Music/CultureCode.mp3",
//"file:///storage/emulated/0/Music/Cartoon.mp3",

class MusicViewModel(application: Application) : AndroidViewModel(application) {
    private val _musicFiles = MutableLiveData<List<Uri>>()
    val musicFiles: LiveData<List<Uri>> = _musicFiles

    private val _currentMusic = MutableStateFlow(Music.mock)
    val currentMusic: StateFlow<Music> = _currentMusic.asStateFlow()

    fun setCurrentMusic(music: Music) {
        _currentMusic.value = music
    }
}
