package com.android.swingmusic.settings.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

data class DiscordWebhookPayload(
    val content: String? = null,
    val embeds: List<DiscordEmbed>? = null
)

data class DiscordEmbed(
    val title: String? = null,
    val description: String? = null,
    val color: Int? = null,
    val fields: List<DiscordField>? = null,
    val thumbnail: DiscordThumbnail? = null,
    val footer: DiscordFooter? = null
)

data class DiscordField(
    val name: String,
    val value: String,
    val inline: Boolean = false
)

data class DiscordThumbnail(
    val url: String
)

data class DiscordFooter(
    val text: String
)

interface DiscordWebhookService {
    @POST
    suspend fun sendWebhook(
        @Url webhookUrl: String,
        @Body payload: DiscordWebhookPayload
    ): Response<Unit>
}
