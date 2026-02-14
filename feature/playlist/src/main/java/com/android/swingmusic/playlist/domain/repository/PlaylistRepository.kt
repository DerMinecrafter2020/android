package com.android.swingmusic.playlist.domain.repository

import androidx.paging.PagingData
import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.playlist.domain.model.Playlist
import com.android.swingmusic.playlist.domain.model.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    
    suspend fun getAllPlaylists(): Flow<Resource<List<Playlist>>>
    
    suspend fun getPlaylistWithTracks(playlistId: String): Flow<Resource<PlaylistWithTracks>>
    
    suspend fun getFavoritesPlaylist(): Flow<Resource<Playlist?>>
    
    suspend fun getFavoritesTracks(): Flow<Resource<List<Track>>>
    
    suspend fun createPlaylist(name: String, description: String = ""): Flow<Resource<Playlist>>
    
    suspend fun createFavoritesPlaylistIfNotExists(): Flow<Resource<Playlist>>
    
    suspend fun addTracksToPlaylist(playlistId: String, trackHashes: List<String>): Flow<Resource<Boolean>>
    
    suspend fun removeTrackFromPlaylist(playlistId: String, trackHash: String): Flow<Resource<Boolean>>
    
    suspend fun deletePlaylist(playlistId: String): Flow<Resource<Boolean>>
    
    suspend fun updatePlaylist(playlist: Playlist): Flow<Resource<Boolean>>
    
    fun getPagingPlaylists(): Flow<PagingData<Playlist>>
}
