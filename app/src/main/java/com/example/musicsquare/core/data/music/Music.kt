package com.example.musicsquare.core.data.music

data class Music(
    val title: String,
    val author: String,
    val duration: String,
    val isFavorite: Boolean = false,
) {
    companion object {
        val mock =
            Music(
                title = "Music SquareMusic SquareMusic SquareMusic Square",
                author = "musicsquare",
                duration = "5:00",
                isFavorite = false,
            )
    }
}