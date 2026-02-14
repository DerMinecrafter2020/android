package com.android.swingmusic.settings.domain.repository

import com.android.swingmusic.settings.domain.model.UpdateInfo

interface UpdateRepository {
    suspend fun checkForUpdates(
        currentVersionCode: Int,
        currentVersionName: String
    ): Result<UpdateInfo?>
}
