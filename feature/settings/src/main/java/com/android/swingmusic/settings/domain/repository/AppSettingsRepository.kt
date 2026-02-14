package com.android.swingmusic.settings.domain.repository

import com.android.swingmusic.core.domain.util.SortBy
import com.android.swingmusic.core.domain.util.SortOrder
import com.android.swingmusic.settings.domain.model.StartPage
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    val albumGridCount: Flow<Int>
    val albumSortBy: Flow<SortBy>
    val albumSortOrder: Flow<SortOrder>

    val artistGridCount: Flow<Int>
    val artistSortBy: Flow<SortBy>
    val artistSortOrder: Flow<SortOrder>

    val autoUpdateEnabled: Flow<Boolean>
    val startPage: Flow<StartPage>
    val showLyrics: Flow<Boolean>

    suspend fun setAlbumGridCount(count: Int)
    suspend fun setAlbumSortBy(sortBy: SortBy)
    suspend fun setAlbumSortOrder(order: SortOrder)

    suspend fun setArtistGridCount(count: Int)
    suspend fun setArtistSortBy(sortBy: SortBy)
    suspend fun setArtistSortOrder(order: SortOrder)

    suspend fun setAutoUpdateEnabled(enabled: Boolean)
    suspend fun setStartPage(page: StartPage)
    suspend fun setShowLyrics(show: Boolean)
}
