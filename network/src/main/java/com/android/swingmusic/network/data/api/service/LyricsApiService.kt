package com.android.swingmusic.network.data.api.service

import com.android.swingmusic.network.data.dto.LyricsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface LyricsApiService {
    @GET("api/get")
    suspend fun getLyrics(
        @Query("track_name") trackName: String,
        @Query("artist_name") artistName: String,
        @Query("album_name") albumName: String,
        @Query("duration") duration: Int
    ): LyricsDto
}
