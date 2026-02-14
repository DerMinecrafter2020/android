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
                
                // Extract version from tag (e.g., "v1.0.4.2" -> "1.0.4.2")
                val latestVersionName = release.tag_name.removePrefix("v")
                
                // Compare version strings directly
                val isNewer = compareVersions(latestVersionName, currentVersionName) > 0
                
                Timber.d("Update check: current=$currentVersionName (code=$currentVersionCode), latest=$latestVersionName, isNewer=$isNewer")
                
                if (isNewer) {
                    val apkAsset = release.assets.firstOrNull { it.name.endsWith(".apk") }
                    
                    if (apkAsset != null) {
                        val updateInfo = UpdateInfo(
                            versionName = latestVersionName,
                            versionCode = -1, // Not used anymore
                            downloadUrl = apkAsset.browser_download_url,
                            releaseNotes = release.body ?: "Keine Release-Notes verfügbar",
                            isNewer = true
                        )
                        Result.success(updateInfo)
                    } else {
                        Timber.w("No APK asset found in release")
                        Result.success(null)
                    }
                } else {
                    Timber.d("No update available")
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
    
    /**
     * Compare two version strings (e.g., "1.0.4.6" vs "1.0.4.5")
     * Returns: negative if v1 < v2, zero if equal, positive if v1 > v2
     */
    private fun compareVersions(v1: String, v2: String): Int {
        val parts1 = v1.split(".").map { it.toIntOrNull() ?: 0 }
        val parts2 = v2.split(".").map { it.toIntOrNull() ?: 0 }
        
        val maxLength = maxOf(parts1.size, parts2.size)
        
        for (i in 0 until maxLength) {
            val part1 = parts1.getOrNull(i) ?: 0
            val part2 = parts2.getOrNull(i) ?: 0
            
            if (part1 != part2) {
                return part1 - part2
            }
        }
        
        return 0
    }
}
