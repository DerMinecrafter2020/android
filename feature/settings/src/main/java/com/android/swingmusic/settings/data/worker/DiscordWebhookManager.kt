package com.android.swingmusic.settings.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.android.swingmusic.core.domain.model.Track
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscordWebhookManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val WORK_NAME = "discord_webhook_periodic"
    }

    fun schedulePeriodicWebhook() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<DiscordWebhookWorker>(
            3, TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

        Timber.tag("DiscordWebhook").d("Scheduled periodic webhook work")
    }

    fun cancelPeriodicWebhook() {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        Timber.tag("DiscordWebhook").d("Cancelled periodic webhook work")
    }

    fun sendNowPlayingWebhook(track: Track?) {
        if (track == null) return

        val inputData = Data.Builder()
            .putString("track_title", track.title)
            .putString("track_artist", track.trackArtists.joinToString(", ") { it.name })
            .putString("album_title", track.album)
            .putString("track_image", track.image)
            .putInt("duration", track.duration)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val oneTimeWorkRequest = androidx.work.OneTimeWorkRequestBuilder<DiscordWebhookWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(context).enqueue(oneTimeWorkRequest)
    }
}
