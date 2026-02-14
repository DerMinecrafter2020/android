package com.android.swingmusic.playlist.domain.model

data class Playlist(
    val id: String,
    val name: String,
    val description: String = "",
    val trackCount: Int = 0,
    val image: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isFavoritesPlaylist: Boolean = false
)
