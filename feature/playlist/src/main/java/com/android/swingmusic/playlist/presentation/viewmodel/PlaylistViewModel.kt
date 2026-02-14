package com.android.swingmusic.playlist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.playlist.domain.model.Playlist
import com.android.swingmusic.playlist.domain.model.PlaylistWithTracks
import com.android.swingmusic.playlist.domain.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

data class PlaylistUiState(
    val playlists: Resource<List<Playlist>> = Resource.Loading(),
    val currentPlaylist: Resource<PlaylistWithTracks>? = null,
    val favoritesTracks: Resource<List<Track>> = Resource.Loading()
)

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistUiState())
    val uiState: StateFlow<PlaylistUiState> = _uiState.asStateFlow()

    init {
        loadPlaylists()
        loadFavoritesTracks()
        ensureFavoritesPlaylistExists()
    }

    private fun loadPlaylists() {
        viewModelScope.launch {
            playlistRepository.getAllPlaylists().collectLatest { resource ->
                _uiState.value = _uiState.value.copy(playlists = resource)
            }
        }
    }

    private fun loadFavoritesTracks() {
        viewModelScope.launch {
            playlistRepository.getFavoritesTracks().collectLatest { resource ->
                _uiState.value = _uiState.value.copy(favoritesTracks = resource)
            }
        }
    }

    private fun ensureFavoritesPlaylistExists() {
        viewModelScope.launch {
            playlistRepository.createFavoritesPlaylistIfNotExists().collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Timber.d("Favorites playlist ensured")
                    }
                    is Resource.Error -> {
                        Timber.e("Failed to create favorites playlist: ${resource.message}")
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    fun createPlaylist(name: String, description: String = "") {
        viewModelScope.launch {
            playlistRepository.createPlaylist(name, description).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Timber.d("Playlist created: ${resource.data?.name}")
                        loadPlaylists()
                    }
                    is Resource.Error -> {
                        Timber.e("Failed to create playlist: ${resource.message}")
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    fun loadPlaylistWithTracks(playlistId: String) {
        viewModelScope.launch {
            playlistRepository.getPlaylistWithTracks(playlistId).collectLatest { resource ->
                _uiState.value = _uiState.value.copy(currentPlaylist = resource)
            }
        }
    }

    fun addTrackToFavoritesPlaylist(trackHash: String) {
        viewModelScope.launch {
            playlistRepository.addTracksToPlaylist("favorites", listOf(trackHash)).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Timber.d("Track added to favorites playlist")
                        loadFavoritesTracks()
                    }
                    is Resource.Error -> {
                        Timber.e("Failed to add track: ${resource.message}")
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    fun removeTrackFromFavoritesPlaylist(trackHash: String) {
        viewModelScope.launch {
            playlistRepository.removeTrackFromPlaylist("favorites", trackHash).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Timber.d("Track removed from favorites playlist")
                        loadFavoritesTracks()
                    }
                    is Resource.Error -> {
                        Timber.e("Failed to remove track: ${resource.message}")
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }

    fun deletePlaylist(playlistId: String) {
        viewModelScope.launch {
            playlistRepository.deletePlaylist(playlistId).collectLatest { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Timber.d("Playlist deleted")
                        loadPlaylists()
                    }
                    is Resource.Error -> {
                        Timber.e("Failed to delete playlist: ${resource.message}")
                    }
                    is Resource.Loading -> {}
                }
            }
        }
    }
}
