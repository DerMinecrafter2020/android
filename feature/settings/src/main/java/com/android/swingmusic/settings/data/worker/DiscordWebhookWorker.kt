package com.android.swingmusic.settings.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.swingmusic.auth.data.baseurlholder.BaseUrlHolder
import com.android.swingmusic.auth.domain.repository.AuthRepository
import com.android.swingmusic.settings.data.api.DiscordEmbed
import com.android.swingmusic.settings.data.api.DiscordField
import com.android.swingmusic.settings.data.api.DiscordFooter
import com.android.swingmusic.settings.data.api.DiscordThumbnail
import com.android.swingmusic.settings.data.api.DiscordWebhookPayload
import com.android.swingmusic.settings.data.api.DiscordWebhookService
import com.android.swingmusic.settings.domain.repository.AppSettingsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@HiltWorker
class DiscordWebhookWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val discordWebhookService: DiscordWebhookService,
    private val settingsRepository: AppSettingsRepository,
    private val authRepository: AuthRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val enabled = settingsRepository.discordWebhookEnabled.first()
            val webhookUrl = settingsRepository.discordWebhookUrl.first()

            if (!enabled || webhookUrl.isBlank()) {
                Timber.tag("DiscordWebhook").d("Webhook disabled or URL empty")
                return Result.success()
            }

            val trackTitle = inputData.getString("track_title") ?: return Result.success()
            val trackArtist = inputData.getString("track_artist") ?: "Unknown Artist"
            val albumTitle = inputData.getString("album_title") ?: "Unknown Album"
            val trackImage = inputData.getString("track_image")
            val duration = inputData.getInt("duration", 0)

            val baseUrl = BaseUrlHolder.baseUrl ?: authRepository.getBaseUrl()
            val thumbnailUrl = if (!trackImage.isNullOrBlank() && !baseUrl.isNullOrBlank()) {
                "${baseUrl}img/thumbnail/${trackImage}"
            } else null

            val durationFormatted = formatDuration(duration)
            val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            val embed = DiscordEmbed(
                title = "🎵 Now Playing",
                description = "**$trackTitle**\nby $trackArtist",
                color = 0x1DB954, // Spotify green
                fields = listOf(
                    DiscordField("Album", albumTitle, inline = true),
                    DiscordField("Duration", durationFormatted, inline = true)
                ),
                thumbnail = thumbnailUrl?.let { DiscordThumbnail(it) },
                footer = DiscordFooter("SwingMusic • $timestamp")
            )

            val payload = DiscordWebhookPayload(embeds = listOf(embed))

            val response = discordWebhookService.sendWebhook(webhookUrl, payload)

            if (response.isSuccessful) {
                Timber.tag("DiscordWebhook").d("Successfully sent: $trackTitle")
                Result.success()
            } else {
                Timber.tag("DiscordWebhook").e("Failed to send webhook: ${response.code()}")
                Result.retry()
            }
        } catch (e: Exception) {
            Timber.tag("DiscordWebhook").e(e, "Error sending webhook")
            Result.retry()
        }
    }

    private fun formatDuration(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format(Locale.getDefault(), "%d:%02d", minutes, secs)
    }
}
