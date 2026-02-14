package com.android.swingmusic.playlist.presentation.viewmodel;

import com.android.swingmusic.playlist.domain.repository.PlaylistRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class PlaylistViewModel_Factory implements Factory<PlaylistViewModel> {
  private final Provider<PlaylistRepository> playlistRepositoryProvider;

  public PlaylistViewModel_Factory(Provider<PlaylistRepository> playlistRepositoryProvider) {
    this.playlistRepositoryProvider = playlistRepositoryProvider;
  }

  @Override
  public PlaylistViewModel get() {
    return newInstance(playlistRepositoryProvider.get());
  }

  public static PlaylistViewModel_Factory create(
      Provider<PlaylistRepository> playlistRepositoryProvider) {
    return new PlaylistViewModel_Factory(playlistRepositoryProvider);
  }

  public static PlaylistViewModel newInstance(PlaylistRepository playlistRepository) {
    return new PlaylistViewModel(playlistRepository);
  }
}
