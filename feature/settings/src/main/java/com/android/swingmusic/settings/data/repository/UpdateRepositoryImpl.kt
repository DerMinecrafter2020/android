package com.android.swingmusic.settings.data.repository

import com.android.swingmusic.settings.data.api.GitHubApiService
import com.android.swingmusic.settings.domain.model.UpdateInfo
import com.android.swingmusic.settings.domain.repository.UpdateRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UpdateRepositoryImpl @Inject constructor(
    private val gitHubApiService: GitHubApiService
) : UpdateRepository {

    override suspend fun checkForUpdates(
        currentVersionCode: Int,
        currentVersionName: String
    ): Result<UpdateInfo?> {
        return try {
            val response = gitHubApiService.getLatestRelease("DerMinecrafter2020", "android")
            
            if (response.isSuccessful && response.body() != null) {
                val release = response.body()!!
                
                // Extract version code from tag (e.g., "v1.0.4.2" -> 14)
                val tagName = release.tag_name.removePrefix("v")
                val versionParts = tagName.split(".")
                
                // Calculate version code: major * 1000 + minor * 100 + patch * 10 + build
                val latestVersionCode = try {
                    if (versionParts.size >= 4) {
                        versionParts[0].toInt() * 1000 + 
                        versionParts[1].toInt() * 100 + 
                        versionParts[2].toInt() * 10 + 
                        versionParts[3].toInt()
                    } else {
                        currentVersionCode // Fallback to current if parsing fails
                    }
                } catch (e: Exception) {
                    Timber.e(e, "Failed to parse version code")
                    currentVersionCode
                }
                
                val isNewer = latestVersionCode > currentVersionCode
                
                if (isNewer) {
                    val apkAsset = release.assets.firstOrNull { it.name.endsWith(".apk") }
                    
                    if (apkAsset != null) {
                        val updateInfo = UpdateInfo(
                            versionName = tagName,
                            versionCode = latestVersionCode,
                            downloadUrl = apkAsset.browser_download_url,
                            releaseNotes = release.body ?: "Keine Release-Notes verfügbar",
                            isNewer = true
                        )
                        Result.success(updateInfo)
                    } else {
                        Result.success(null)
                    }
                } else {
                    Result.success(null)
                }
            } else {
                Result.failure(Exception("Failed to fetch release: ${response.code()}"))
            }
        } catch (e: Exception) {
            Timber.e(e, "Error checking for updates")
            Result.failure(e)
        }
    }
}
