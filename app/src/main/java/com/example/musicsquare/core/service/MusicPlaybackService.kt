package com.example.musicsquare.core.service

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicsquare.R
import com.example.musicsquare.app.MainActivity
import com.example.musicsquare.core.data.music.Music
import com.example.musicsquare.feature.home.PlaybackStateHolder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPlaybackService : Service() {
    private lateinit var player: ExoPlayer
    private val serviceScope = CoroutineScope(Dispatchers.IO)
    private var currentMusicUri: Uri? = null
    private val notificationId = 1
    private val channelId = "MusicPlaybackChannel"

    override fun onCreate() {
        super.onCreate()
        startForeground(notificationId, createNotification(Music.mock))

        player = ExoPlayer.Builder(this).build()
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_READY -> {
                        if (player.playWhenReady) {
                            PlaybackStateHolder.updatePlaybackState(true)
                        } else {
                            PlaybackStateHolder.updatePlaybackState(false)
                        }
                    }

                    Player.STATE_ENDED -> {
                        PlaybackStateHolder.updatePlaybackState(false)
                    }
                }
            }
        })
    }

    private fun createNotification(music: Music): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(music.title)
            .setContentText(music.author)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
    }

    private fun updateNotification(music: Music) {
        val notification = createNotification(music)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, notification)
    }

    private fun playMusic(music: Music) {
        if (currentMusicUri != music.path || player.playbackState == Player.STATE_ENDED) {
            val mediaItem = MediaItem.fromUri(music.path)
            player.setMediaItem(mediaItem)
            player.prepare()
            currentMusicUri = music.path
        }

        player.play()
        PlaybackStateHolder.setCurrentMusic(music)
        startProgressUpdater()
        updateNotification(music)
    }

    private fun startProgressUpdater() {
        serviceScope.launch {
            while (isActive) {
                withContext(Dispatchers.Main) {
                    if (player.isPlaying) {
                        val progress = player.currentPosition.toFloat() / player.duration.toFloat()
                        PlaybackStateHolder.updateProgress(progress)
                    }
                }
                delay(1000)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_STICKY

        val action = intent.getStringExtra("ACTION")
        when (action) {
            "PLAY" -> {
                val musicUri = intent.getStringExtra("MUSIC_URI")
                if (musicUri != null && Uri.parse(musicUri) != currentMusicUri) {
                    val music = Music.fromUri(Uri.parse(musicUri))
                    playMusic(music)
                } else {
                    player.play()
                }
                PlaybackStateHolder.updatePlaybackState(true)
            }

            "PAUSE" -> {
                player.pause()
                PlaybackStateHolder.updatePlaybackState(false)
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder(this)
    }

    override fun onDestroy() {
        serviceScope.cancel()
        player.release()
        super.onDestroy()
    }

    class LocalBinder(private val service: MusicPlaybackService) : Binder() {
        fun getService(): MusicPlaybackService = service
    }
}
