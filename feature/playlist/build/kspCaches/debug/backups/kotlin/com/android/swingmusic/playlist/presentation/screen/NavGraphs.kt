package com.android.swingmusic.playlist.presentation.screen

import com.android.swingmusic.playlist.presentation.screen.destinations.*
import com.ramcosta.composedestinations.spec.*

/**
 * Class generated if any Composable is annotated with `@Destination`.
 * It aggregates all [TypedDestination]s in their [NavGraph]s.
 */
public object NavGraphs {

    public val root: NavGraph = NavGraph(
        route = "root",
        startRoute = PlaylistsScreenDestination,
        destinations = listOf(
            PlaylistsScreenDestination
        )
    )
}