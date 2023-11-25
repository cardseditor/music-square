package com.example.musicsquare.core.data.music

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri

data class Music(
    val title: String,
    val author: String,
    val totalSeconds: Int,
    val isFavorite: Boolean,
    val path: Uri,
    val albumArt: Bitmap? = null
) {
    companion object {
        private fun fromUri(path: Uri): Music {
            val retriever = MediaMetadataRetriever().apply {
                setDataSource(path.toString())
            }

            val title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
                ?: "Unknown Title"
            val author = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                ?: "Unknown Artist"
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                ?.toIntOrNull() ?: 0
            val albumArtBytes = retriever.embeddedPicture
            val albumArtBitmap =
                albumArtBytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

            retriever.release()

            return Music(
                title = title,
                author = author,
                totalSeconds = duration / 1000,
                isFavorite = false,
                path = path,
                albumArt = albumArtBitmap
            )
        }

        val mock = Music.fromUri(Uri.parse("file:///storage/emulated/0/Music/CultureCode.mp3"))
    }
}
