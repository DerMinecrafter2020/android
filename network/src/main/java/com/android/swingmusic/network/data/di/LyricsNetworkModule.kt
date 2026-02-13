package com.android.swingmusic.network.data.di

import com.android.swingmusic.network.data.api.service.LyricsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LyricsRetrofit

@Module
@InstallIn(SingletonComponent::class)
object LyricsNetworkModule {
    private const val LRCLIB_BASE_URL = "https://lrclib.net/"

    @Provides
    @Singleton
    @LyricsRetrofit
    fun providesLyricsRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LRCLIB_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun providesLyricsApiService(@LyricsRetrofit retrofit: Retrofit): LyricsApiService {
        return retrofit.create(LyricsApiService::class.java)
    }
}
