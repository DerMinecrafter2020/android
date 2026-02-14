package com.android.swingmusic.presentation.util

import com.android.swingmusic.album.presentation.screen.destinations.AllAlbumScreenDestination
import com.android.swingmusic.artist.presentation.screen.destinations.AllArtistsScreenDestination
import com.android.swingmusic.folder.presentation.screen.destinations.FoldersAndTracksScreenDestination
import com.android.swingmusic.search.presentation.screen.destinations.SearchScreenDestination
import com.android.swingmusic.settings.domain.model.StartPage
import com.ramcosta.composedestinations.spec.DestinationSpec

fun StartPage.toDestination(): DestinationSpec<*> {
    return when (this) {
        StartPage.FOLDERS -> FoldersAndTracksScreenDestination
        StartPage.ALBUMS -> AllAlbumScreenDestination
        StartPage.ARTISTS -> AllArtistsScreenDestination
        StartPage.SEARCH -> SearchScreenDestination
    }
}
