package com.android.swingmusic.settings.presentation.screen

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
                                StartPage.FOLDERS -> "Ordner"
                                StartPage.ALBUMS -> "Alben"
                                StartPage.ARTISTS -> "Künstler"
                                StartPage.SEARCH -> "Suche"
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
                        text = { Text("Ordner") },
                        onClick = {
                            viewModel.setStartPage(StartPage.FOLDERS)
                            showStartPageMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Alben") },
                        onClick = {
                            viewModel.setStartPage(StartPage.ALBUMS)
                            showStartPageMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Künstler") },
                        onClick = {
                            viewModel.setStartPage(StartPage.ARTISTS)
                            showStartPageMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Suche") },
                        onClick = {
                            viewModel.setStartPage(StartPage.SEARCH)
                            showStartPageMenu = false
                        }
                    )
                }
            }
        }
    }
}
