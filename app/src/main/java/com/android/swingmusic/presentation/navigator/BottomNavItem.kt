package com.android.swingmusic.presentation.navigator

import androidx.annotation.DrawableRes
import com.android.swingmusic.album.presentation.screen.destinations.AllAlbumScreenDestination
import com.android.swingmusic.artist.presentation.screen.destinations.AllArtistsScreenDestination
import com.android.swingmusic.folder.presentation.screen.destinations.FoldersAndTracksScreenDestination
import com.android.swingmusic.playlist.presentation.screen.destinations.PlaylistsScreenDestination
import com.android.swingmusic.search.presentation.screen.destinations.SearchScreenDestination
import com.android.swingmusic.settings.presentation.screen.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.android.swingmusic.uicomponent.R as UiComponent

sealed class BottomNavItem(
    var title: String,
    @param:DrawableRes var icon: Int,
    var destination: DestinationSpec<*>
) {
    data object Folder : BottomNavItem(
        title = "Folders",
        icon = UiComponent.drawable.folder_filled,
        destination = FoldersAndTracksScreenDestination
    )

    data object Album : BottomNavItem(
        title = "Albums",
        icon = UiComponent.drawable.ic_album,
        destination = AllAlbumScreenDestination
    )

    data object Artist : BottomNavItem(
        title = "Artists",
        icon = UiComponent.drawable.ic_artist,
        destination = AllArtistsScreenDestination
    )

    data object Search : BottomNavItem(
        title = "Search",
        icon = UiComponent.drawable.ic_search,
        destination = SearchScreenDestination
    )

    data object Playlist : BottomNavItem(
        title = "Playlists",
        icon = UiComponent.drawable.play_list,
        destination = PlaylistsScreenDestination
    )

    data object Settings : BottomNavItem(
        title = "Settings",
        icon = UiComponent.drawable.ic_settings,
        destination = SettingsScreenDestination
    )
}
