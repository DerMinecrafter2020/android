package com.android.swingmusic.playlist.domain.model

import com.android.swingmusic.core.domain.model.Track

data class PlaylistWithTracks(
    val playlist: Playlist,
    val tracks: List<Track>
)
