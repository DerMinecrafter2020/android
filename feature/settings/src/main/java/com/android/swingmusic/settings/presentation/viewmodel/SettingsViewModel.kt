package com.android.swingmusic.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.swingmusic.settings.data.worker.DiscordWebhookManager
import com.android.swingmusic.settings.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val discordWebhookEnabled: Boolean = false,
    val discordWebhookUrl: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: AppSettingsRepository,
    private val webhookManager: DiscordWebhookManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.discordWebhookEnabled.collect { enabled ->
                _uiState.value = _uiState.value.copy(discordWebhookEnabled = enabled)
            }
        }
        viewModelScope.launch {
            settingsRepository.discordWebhookUrl.collect { url ->
                _uiState.value = _uiState.value.copy(discordWebhookUrl = url)
            }
        }
    }

    fun setDiscordWebhookEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDiscordWebhookEnabled(enabled)
            if (enabled) {
                webhookManager.schedulePeriodicWebhook()
            } else {
                webhookManager.cancelPeriodicWebhook()
            }
        }
    }

    fun setDiscordWebhookUrl(url: String) {
        viewModelScope.launch {
            settingsRepository.setDiscordWebhookUrl(url)
        }
    }
}
