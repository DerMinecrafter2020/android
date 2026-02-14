package com.android.swingmusic.player.domain.manager

import com.android.swingmusic.core.domain.model.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages favorites playlist operations and automatic track additions.
 * This is a central place for handling favorites logic that can be used by
 * both the Player module and the Playlist module.
 */
@Singleton
class FavoritesManager @Inject constructor() {
    
    private val _favoritesTracks = MutableStateFlow<List<Track>>(emptyList())
    val favoritesTracks: StateFlow<List<Track>> = _favoritesTracks.asStateFlow()
    
    /**
     * Called when a track is marked as favorite.
     * Automatically adds it to the favorites playlist.
     */
    fun addTrackToFavorites(track: Track) {
        val currentFavorites = _favoritesTracks.value
        if (!currentFavorites.any { it.trackHash == track.trackHash }) {
            _favoritesTracks.value = currentFavorites + track
            Timber.d("Track auto-added to favorites: ${track.name}")
        }
    }
    
    /**
     * Called when a track is unmarked as favorite.
     * Automatically removes it from the favorites playlist.
     */
    fun removeTrackFromFavorites(trackHash: String) {
        _favoritesTracks.value = _favoritesTracks.value.filter { it.trackHash != trackHash }
        Timber.d("Track auto-removed from favorites: $trackHash")
    }
    
    /**
     * Gets the current list of favorite tracks.
     */
    fun getFavoriteTracks(): List<Track> = _favoritesTracks.value
    
    /**
     * Checks if a track is in favorites.
     */
    fun isFavorite(trackHash: String): Boolean {
        return _favoritesTracks.value.any { it.trackHash == trackHash }
    }
    
    /**
     * Syncs favorites list with external source (e.g., from API or Playlist module).
     */
    fun syncFavorites(tracks: List<Track>) {
        _favoritesTracks.value = tracks
        Timber.d("Favorites synced with ${tracks.size} tracks")
    }
    
    /**
     * Clears all favorites (mainly for testing or reset scenarios).
     */
    fun clearFavorites() {
        _favoritesTracks.value = emptyList()
        Timber.d("Favorites cleared")
    }
}
