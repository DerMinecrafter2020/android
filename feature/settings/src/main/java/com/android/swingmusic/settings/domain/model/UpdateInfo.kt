package com.android.swingmusic.settings.domain.model

data class UpdateInfo(
    val versionName: String,
    val versionCode: Int,
    val downloadUrl: String,
    val releaseNotes: String,
    val isNewer: Boolean
)
