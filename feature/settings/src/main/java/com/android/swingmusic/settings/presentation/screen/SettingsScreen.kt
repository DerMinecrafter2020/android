package com.android.swingmusic.settings.presentation.screen

import android.os.Build
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.android.swingmusic.settings.domain.model.StartPage
import com.android.swingmusic.settings.presentation.viewmodel.SettingsViewModel
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    val versionInfo = remember {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                packageInfo.versionCode.toLong()
            }
            "Version ${packageInfo.versionName} ($versionCode)"
        } catch (e: Exception) {
            "Version unbekannt"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                text = "Allgemein",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Auto-Update",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Prüft automatisch auf neue App-Versionen",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Switch(
                    checked = uiState.autoUpdateEnabled,
                    onCheckedChange = { viewModel.setAutoUpdateEnabled(it) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Lyrics anzeigen",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Zeigt die aktuelle Lyrics-Zeile im Player an",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Switch(
                    checked = uiState.showLyrics,
                    onCheckedChange = { viewModel.setShowLyrics(it) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            var showStartPageMenu by remember { mutableStateOf(false) }

            Column {
                Text(
                    text = "Startseite",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "Wähle welche Seite beim Start der App geöffnet wird",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showStartPageMenu = true }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (uiState.startPage) {
                                StartPage.FOLDERS -> "Folders"
                                StartPage.ALBUMS -> "Albums"
                                StartPage.ARTISTS -> "Artists"
                                StartPage.SEARCH -> "Search"
                            },
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null
                        )
                    }
                }

                DropdownMenu(
                    expanded = showStartPageMenu,
                    onDismissRequest = { showStartPageMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Folders") },
                        onClick = {
                            viewModel.setStartPage(StartPage.FOLDERS)
                            showStartPageMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Albums") },
                        onClick = {
                            viewModel.setStartPage(StartPage.ALBUMS)
                            showStartPageMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Artists") },
                        onClick = {
                            viewModel.setStartPage(StartPage.ARTISTS)
                            showStartPageMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Search") },
                        onClick = {
                            viewModel.setStartPage(StartPage.SEARCH)
                            showStartPageMenu = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Info",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "App-Version",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = versionInfo,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }        }
    }
}
