package com.android.swingmusic.settings.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.android.swingmusic.core.domain.util.SortBy
import com.android.swingmusic.core.domain.util.SortOrder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettings @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    private companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

        private val ALBUM_GRID_COUNT = intPreferencesKey("album_grid_count")
        private val ALBUM_SORT_BY = stringPreferencesKey("album_sort_by")
        private val ALBUM_SORT_ORDER = stringPreferencesKey("album_sort_order")

        private val ARTIST_GRID_COUNT = intPreferencesKey("artist_grid_count")
        private val ARTIST_SORT_BY = stringPreferencesKey("artist_sort_by")
        private val ARTIST_SORT_ORDER = stringPreferencesKey("artist_sort_order")

        private val AUTO_UPDATE_ENABLED = androidx.datastore.preferences.core.booleanPreferencesKey("auto_update_enabled")
        private val START_PAGE = stringPreferencesKey("start_page")
        private val SHOW_LYRICS = androidx.datastore.preferences.core.booleanPreferencesKey("show_lyrics")
        private val IGNORED_UPDATE_VERSION = stringPreferencesKey("ignored_update_version")
    }

    // Album Flows
    val getAlbumGridCount: Flow<Int> = context.dataStore.data.map {
        it[ALBUM_GRID_COUNT] ?: 2
    }
    val getAlbumSortBy: Flow<String> = context.dataStore.data.map {
        it[ALBUM_SORT_BY] ?: SortBy.LAST_PLAYED.name
    }
    val getAlbumSortOrder: Flow<String> = context.dataStore.data.map {
        it[ALBUM_SORT_ORDER] ?: SortOrder.DESCENDING.name
    }

    // Artist Flows
    val getArtistGridCount: Flow<Int> = context.dataStore.data.map {
        it[ARTIST_GRID_COUNT] ?: 2
    }
    val getArtistSortBy: Flow<String> = context.dataStore.data.map {
        it[ARTIST_SORT_BY] ?: SortBy.LAST_PLAYED.name
    }
    val getArtistSortOrder: Flow<String> = context.dataStore.data.map {
        it[ARTIST_SORT_ORDER] ?: SortOrder.DESCENDING.name
    }

    // Album Update methods
    suspend fun updateAlbumGridCount(count: Int) =
        context.dataStore.edit { it[ALBUM_GRID_COUNT] = count }

    suspend fun updateAlbumSortBy(value: String) =
        context.dataStore.edit { it[ALBUM_SORT_BY] = value }

    suspend fun updateAlbumSortOrder(value: String) =
        context.dataStore.edit { it[ALBUM_SORT_ORDER] = value }

    // Artist Update Methods
    suspend fun updateArtistGridCount(count: Int) =
        context.dataStore.edit { it[ARTIST_GRID_COUNT] = count }

    suspend fun updateArtistSortBy(value: String) =
        context.dataStore.edit { it[ARTIST_SORT_BY] = value }

    suspend fun updateArtistSortOrder(value: String) =
        context.dataStore.edit { it[ARTIST_SORT_ORDER] = value }

    // General Settings Flows
    val getAutoUpdateEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[AUTO_UPDATE_ENABLED] ?: false
    }
    
    val getStartPage: Flow<String> = context.dataStore.data.map {
        it[START_PAGE] ?: "HOME"
    }

    val getShowLyrics: Flow<Boolean> = context.dataStore.data.map {
        it[SHOW_LYRICS] ?: false
    }

    val getIgnoredUpdateVersion: Flow<String?> = context.dataStore.data.map {
        it[IGNORED_UPDATE_VERSION]
    }

    // General Settings Update Methods
    suspend fun updateAutoUpdateEnabled(enabled: Boolean) =
        context.dataStore.edit { it[AUTO_UPDATE_ENABLED] = enabled }

    suspend fun updateStartPage(page: String) =
        context.dataStore.edit { it[START_PAGE] = page }

    suspend fun updateShowLyrics(show: Boolean) =
        context.dataStore.edit { it[SHOW_LYRICS] = show }

    suspend fun updateIgnoredUpdateVersion(version: String?) =
        context.dataStore.edit { 
            if (version != null) {
                it[IGNORED_UPDATE_VERSION] = version
            } else {
                it.remove(IGNORED_UPDATE_VERSION)
            }
        }
}
