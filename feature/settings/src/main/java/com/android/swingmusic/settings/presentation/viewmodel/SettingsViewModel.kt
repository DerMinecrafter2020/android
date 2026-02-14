package com.android.swingmusic.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.swingmusic.settings.domain.model.StartPage
import com.android.swingmusic.settings.domain.repository.AppSettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val autoUpdateEnabled: Boolean = false,
    val startPage: StartPage = StartPage.FOLDERS,
    val showLyrics: Boolean = false
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: AppSettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.autoUpdateEnabled.collect { enabled ->
                _uiState.value = _uiState.value.copy(autoUpdateEnabled = enabled)
            }
        }
        viewModelScope.launch {
            settingsRepository.startPage.collect { page ->
                _uiState.value = _uiState.value.copy(startPage = page)
            }
        }
        viewModelScope.launch {
            settingsRepository.showLyrics.collect { show ->
                _uiState.value = _uiState.value.copy(showLyrics = show)
            }
        }
    }

    fun setAutoUpdateEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setAutoUpdateEnabled(enabled)
        }
    }

    fun setStartPage(page: StartPage) {
        viewModelScope.launch {
            settingsRepository.setStartPage(page)
        }
    }

    fun setShowLyrics(show: Boolean) {
        viewModelScope.launch {
            settingsRepository.setShowLyrics(show)
        }
    }
}
