package com.android.swingmusic.playlist.di

import com.android.swingmusic.playlist.data.repository.DataPlaylistRepository
import com.android.swingmusic.playlist.domain.repository.PlaylistRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlaylistModule {
    
    @Binds
    @Singleton
    abstract fun bindPlaylistRepository(
        dataPlaylistRepository: DataPlaylistRepository
    ): PlaylistRepository
}
