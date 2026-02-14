package com.android.swingmusic.playlist.data.repository;

import com.android.swingmusic.auth.domain.repository.AuthRepository;
import com.android.swingmusic.network.data.api.service.NetworkApiService;
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
public final class DataPlaylistRepository_Factory implements Factory<DataPlaylistRepository> {
  private final Provider<NetworkApiService> networkApiServiceProvider;

  private final Provider<AuthRepository> authRepositoryProvider;

  public DataPlaylistRepository_Factory(Provider<NetworkApiService> networkApiServiceProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    this.networkApiServiceProvider = networkApiServiceProvider;
    this.authRepositoryProvider = authRepositoryProvider;
  }

  @Override
  public DataPlaylistRepository get() {
    return newInstance(networkApiServiceProvider.get(), authRepositoryProvider.get());
  }

  public static DataPlaylistRepository_Factory create(
      Provider<NetworkApiService> networkApiServiceProvider,
      Provider<AuthRepository> authRepositoryProvider) {
    return new DataPlaylistRepository_Factory(networkApiServiceProvider, authRepositoryProvider);
  }

  public static DataPlaylistRepository newInstance(NetworkApiService networkApiService,
      AuthRepository authRepository) {
    return new DataPlaylistRepository(networkApiService, authRepository);
  }
}
