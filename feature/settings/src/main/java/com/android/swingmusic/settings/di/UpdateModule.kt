package com.android.swingmusic.settings.di

import com.android.swingmusic.settings.data.api.GitHubApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UpdateModule {

    @Provides
    @Singleton
    fun provideGitHubApiService(): GitHubApiService {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUpdateRepository(
        gitHubApiService: GitHubApiService
    ): com.android.swingmusic.settings.domain.repository.UpdateRepository {
        return com.android.swingmusic.settings.data.repository.UpdateRepositoryImpl(gitHubApiService)
    }
}
