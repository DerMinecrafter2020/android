package com.android.swingmusic.player.data.repository

import com.android.swingmusic.auth.data.baseurlholder.BaseUrlHolder
import com.android.swingmusic.auth.data.tokenholder.AuthTokenHolder
import com.android.swingmusic.auth.domain.repository.AuthRepository
import com.android.swingmusic.core.data.dto.FoldersAndTracksRequestDto
import com.android.swingmusic.core.data.mapper.Map.toModel
import com.android.swingmusic.core.data.mapper.Map.toTrack
import com.android.swingmusic.core.data.util.Resource
import com.android.swingmusic.core.domain.model.LyricLine
import com.android.swingmusic.core.domain.model.Track
import com.android.swingmusic.core.domain.util.QueueSource
import com.android.swingmusic.database.data.dao.LastPlayedTrackDao
import com.android.swingmusic.database.data.dao.QueueDao
import com.android.swingmusic.database.data.mapper.toEntity
import com.android.swingmusic.database.data.mapper.toModel
import com.android.swingmusic.database.domain.model.LastPlayedTrack
import com.android.swingmusic.network.data.api.service.LyricsApiService
import com.android.swingmusic.network.data.api.service.NetworkApiService
import com.android.swingmusic.network.data.dto.ToggleFavoriteRequest
import com.android.swingmusic.network.data.mapper.toDto
import com.android.swingmusic.network.domain.model.LogTrackRequest
import com.android.swingmusic.player.domain.repository.PLayerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.sql.Timestamp
import java.time.Instant
import javax.inject.Inject

class DataPLayerRepository @Inject constructor(
    private val queueDao: QueueDao,
    private val lastPlayedTrackDao: LastPlayedTrackDao,
    private val networkApiService: NetworkApiService,
    private val lyricsApiService: LyricsApiService,
    private val authRepository: AuthRepository
) : PLayerRepository {

    override suspend fun insertQueue(track: List<Track>) {
        val trackEntities = track.map { it.toEntity() }
        queueDao.insertQueueInTransaction(trackEntities)
    }

    override suspend fun getSavedQueue(): List<Track> {
        return queueDao.getSavedQueue().map { it.toModel() }
    }

    override suspend fun clearQueue() {
        queueDao.clearQueue()
    }

    override suspend fun updateLastPlayedTrack(
        trackHash: String,
        indexInQueue: Int,
        source: QueueSource,
        lastPlayPositionMs: Long
    ) {
        lastPlayedTrackDao.insertLastPlayedTrack(
            LastPlayedTrack(
                trackHash = trackHash,
                indexInQueue = indexInQueue,
                source = source,
                lastPlayPositionMs = lastPlayPositionMs
            ).toEntity()
        )
    }

    override suspend fun getLastPlayedTrack(): LastPlayedTrack? {
        return lastPlayedTrackDao.getLastPlayedTrack()?.toModel()
    }

    override suspend fun logLastPlayedTrackToServer(
        track: Track,
        playDuration: Int,
        source: String
    ) {
        try {
            val accessToken = AuthTokenHolder.accessToken ?: authRepository.getAccessToken()
            val baseUrl = BaseUrlHolder.baseUrl ?: authRepository.getBaseUrl()

            val timeStamp = Timestamp.from(Instant.now()).toInstant().epochSecond
            val logRequest = LogTrackRequest(
                trackHash = track.trackHash,
                duration = playDuration,
                timestamp = timeStamp,
                source = source
            ).toDto()

            networkApiService.logLastPlayedTrackToServer(
                logTrackRequest = logRequest,
                url = "${baseUrl}logger/track/log",
                bearerToken = "Bearer $accessToken"
            )

        } catch (e: HttpException) {
            Timber.e("NETWORK ERROR LOGGING TRACK TO SERVER")
        } catch (e: Exception) {
            Timber.e("ERROR LOGGING TRACK TO SERVER: CAUSED BY -> ${e.message}")
        }
    }

    override suspend fun addTrackToFavorite(trackHash: String): Flow<Resource<Boolean>> {
        return flow {
            try {
                emit(Resource.Loading())

                val accessToken = AuthTokenHolder.accessToken ?: authRepository.getAccessToken()
                val baseUrl = BaseUrlHolder.baseUrl ?: authRepository.getBaseUrl()

                networkApiService.addFavorite(
                    url = "${baseUrl}favorites/add",
                    toggleFavoriteRequest = ToggleFavoriteRequest(hash = trackHash, type = "track"),
                    bearerToken = "Bearer $accessToken"
                )

                emit(Resource.Success(data = true))

            } catch (e: HttpException) {
                emit(Resource.Error(message = "FAILED TO ADD TRACK TO FAVORITE"))

            } catch (e: Exception) {
                emit(Resource.Error(message = "FAILED TO ADD TRACK TO FAVORITE"))
            }
        }
    }

    override suspend fun removeTrackFromFavorite(trackHash: String): Flow<Resource<Boolean>> {
        return flow {
            try {
                emit(Resource.Loading())

                val accessToken = AuthTokenHolder.accessToken ?: authRepository.getAccessToken()
                val baseUrl = BaseUrlHolder.baseUrl ?: authRepository.getBaseUrl()

                networkApiService.removeFavorite(
                    url = "${baseUrl}favorites/remove",
                    toggleFavoriteRequest = ToggleFavoriteRequest(hash = trackHash, type = "track"),
                    bearerToken = "Bearer $accessToken"
                )
                emit(Resource.Success(data = false))

            } catch (e: HttpException) {
                emit(Resource.Error(message = "FAILED TO REMOVE TRACK FROM FAVORITE"))

            } catch (e: Exception) {
                emit(Resource.Error(message = "FAILED TO REMOVE TRACK FROM FAVORITE"))
            }
        }
    }

    override suspend fun getTracksChunk(folderPath: String, start: Int, limit: Int): List<Track> {
        return try {
            val accessToken = AuthTokenHolder.accessToken ?: authRepository.getAccessToken()
            val baseUrl = BaseUrlHolder.baseUrl ?: authRepository.getBaseUrl()

            val requestDto = FoldersAndTracksRequestDto(
                folder = folderPath,
                tracksOnly = true,
                start = start,
                limit = limit
            )

            val response = networkApiService.getFoldersAndTracks(
                requestData = requestDto,
                url = "${baseUrl}folder",
                bearerToken = "Bearer $accessToken"
            )

            response.tracksDto?.map { it.toTrack() } ?: emptyList()

        } catch (e: HttpException) {
            Timber.e("Network error fetching tracks chunk: $e")
            emptyList()
        } catch (e: Exception) {
            Timber.e("Error fetching tracks chunk: $e")
            emptyList()
        }
    }

    override suspend fun getLyrics(
        title: String,
        artist: String,
        album: String,
        duration: Int
    ): Flow<Resource<Pair<List<LyricLine>?, String?>>> {
        return flow {
            try {
                emit(Resource.Loading())

                val response = lyricsApiService.getLyrics(
                    trackName = title,
                    artistName = artist,
                    albumName = album,
                    duration = duration
                )

                val syncedLyrics = response.syncedLyrics?.let { parseLrc(it) }
                val plainLyrics = response.plainLyrics

                emit(Resource.Success(data = Pair(syncedLyrics, plainLyrics)))

            } catch (e: HttpException) {
                if (e.code() == 404) {
                    emit(Resource.Error(message = "NOT_FOUND"))
                } else {
                    emit(Resource.Error(message = "Network error fetching lyrics"))
                }
            } catch (e: Exception) {
                emit(Resource.Error(message = "Error fetching lyrics: ${e.message}"))
            }
        }
    }

    private fun parseLrc(lrc: String): List<LyricLine> {
        val regex = Regex("""^\[(\d{2}):(\d{2})\.(\d{2,3})](.*)$""")
        return lrc.lines()
            .mapNotNull { line ->
                regex.matchEntire(line.trim())?.let { match ->
                    val minutes = match.groupValues[1].toLong()
                    val seconds = match.groupValues[2].toLong()
                    val centisStr = match.groupValues[3]
                    val centis = centisStr.toLong()
                    val millis = if (centisStr.length == 2) centis * 10 else centis
                    val timeMs = minutes * 60_000L + seconds * 1_000L + millis
                    val text = match.groupValues[4].trim()
                    LyricLine(timeMs = timeMs, text = text)
                }
            }
            .sortedBy { it.timeMs }
    }
}
