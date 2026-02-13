package com.android.swingmusic.player.presentation.state

import com.android.swingmusic.core.domain.model.LyricLine

sealed class LyricsState {
    object Idle : LyricsState()
    object Loading : LyricsState()
    data class Success(
        val syncedLyrics: List<LyricLine>?,
        val plainLyrics: String?
    ) : LyricsState()
    object NotFound : LyricsState()
    data class Error(val message: String) : LyricsState()
}
