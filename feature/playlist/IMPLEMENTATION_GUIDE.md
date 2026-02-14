# Playlist Feature - Implementation Guide

## Overview
Das Playlist-Feature ermöglicht es Benutzern, eigene Playlists zu erstellen und ihre Lieblingsliedfer automatisch in eine "Favorites"-Playlist zu speichern.

## Architektur

### Module Structure
```
feature/playlist/
├── domain/
│   ├── model/
│   │   ├── Playlist.kt (Playlist Datenmodell)
│   │   └── PlaylistWithTracks.kt
│   └── repository/
│       └── PlaylistRepository.kt (Interface)
├── data/
│   └── repository/
│       └── DataPlaylistRepository.kt (Implementierung)
├── presentation/
│   ├── viewmodel/
│   │   └── PlaylistViewModel.kt
│   └── screen/
│       └── PlaylistsScreen.kt (Compose UI)
└── di/
    └── PlaylistModule.kt (Hilt Injection)
```

### Key Models
- **Playlist**: Repräsentiert eine Playlist mit ID, Namen, Beschreibung, Tracking
- **PlaylistWithTracks**: Playlist mit zugehörigen Tracks

## Integration Steps

### 1. Automatic Favorites Playlist Creation
Wenn ein Track als Favorite hinzugefügt wird:
```kotlin
// In MediaControllerViewModel.kt - OnToggleFavorite handler:
if (!isFavorite) {
    playlistViewModel.addTrackToFavoritesPlaylist(trackHash)
}
```

### 2. Add Playlist Tab to Navigation
In [MainActivity.kt](../../app/src/main/java/com/android/swingmusic/presentation/activity/MainActivity.kt):
```kotlin
val bottomNavItems: List<BottomNavItem> = listOf(
    BottomNavItem.Folder,
    BottomNavItem.Album,
    BottomNavItem.Artist,
    BottomNavItem.Search,
    BottomNavItem.Playlist,  // NEW
    BottomNavItem.Settings,
)
```

### 3. Update Navigation Destinations
In [NavGraphs.kt](../../app/src/main/java/com/android/swingmusic/presentation/navigator/NavGraphs.kt):
```kotlin
val pastAuthDestSpec = listOf(
    HomeDestination,
    FoldersAndTracksScreenDestination,
    AllAlbumScreenDestination,
    AllArtistsScreenDestination,
    SearchScreenDestination,
    PlaylistsScreenDestination,  // NEW
    // ... rest
)
```

### 4. Update BottomNavItem
In BottomNavItem.kt:
```kotlin
object Playlist : BottomNavItem(
    title = "Playlists",
    icon = R.drawable.ic_playlist,  // Add icon
    destination = PlaylistsScreenDestination
)
```

## TODO Items for Complete Implementation

### API Integration
- [ ] Implement API calls in DataPlaylistRepository for:
  - Create Playlist
  - Get All Playlists
  - Add Track to Playlist
  - Remove Track from Playlist
  - Delete Playlist
  - Get Favorite Tracks

### UI Components
- [ ] Add TextField to CreatePlaylistDialog
- [ ] Create Playlist Detail Screen
- [ ] Add Playlist editing functionality
- [ ] Create CreatePlaylistDialog as separate Composable

### Data Persistence
- [ ] Add Room Entities for local caching (if needed)
- [ ] Implement DAO interfaces

### Integration with Existing Code
- [ ] Hook up addTrackToFavorite in MediaControllerViewModel
- [ ] Hook up addTrackToFavorite in ArtistInfoViewModel
- [ ] Hook up addTrackToFavorite in AlbumWithInfoViewModel
- [ ] Hook up addTrackToFavorite in SearchViewModel

### Testing
- [ ] Unit tests for PlaylistViewModel
- [ ] Integration tests for PlaylistRepository
- [ ] UI tests for PlaylistsScreen

## Files Created
- [Playlist.kt](src/main/java/com/android/swingmusic/playlist/domain/model/Playlist.kt)
- [PlaylistWithTracks.kt](src/main/java/com/android/swingmusic/playlist/domain/model/PlaylistWithTracks.kt)
- [PlaylistRepository.kt](src/main/java/com/android/swingmusic/playlist/domain/repository/PlaylistRepository.kt)
- [DataPlaylistRepository.kt](src/main/java/com/android/swingmusic/playlist/data/repository/DataPlaylistRepository.kt)
- [PlaylistViewModel.kt](src/main/java/com/android/swingmusic/playlist/presentation/viewmodel/PlaylistViewModel.kt)
- [PlaylistsScreen.kt](src/main/java/com/android/swingmusic/playlist/presentation/screen/PlaylistsScreen.kt)
- [PlaylistModule.kt](src/main/java/com/android/swingmusic/playlist/di/PlaylistModule.kt)
- [build.gradle.kts](build.gradle.kts)

## Configuration Updates
- ✅ Added `:feature:playlist` to settings.gradle.kts
- ✅ Added playlist dependency to app/build.gradle.kts

## Next Steps
1. Test compilation: `./gradlew build`
2. Implement API integration in DataPlaylistRepository
3. Add BottomNavItem.Playlist and update MainActivity navigation
4. Complete UI components (TextFields in dialog, Detail screen)
5. Integrate with existing ViewModels to auto-add favorites
6. Add tests
