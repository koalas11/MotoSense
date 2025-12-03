package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.lpss.motosense.model.DynamicColorMode
import org.lpss.motosense.model.MotorcycleIcon
import org.lpss.motosense.model.ThemeMode
import org.lpss.motosense.ui.util.handleOperationState
import org.lpss.motosense.viewmodel.AppState
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.SettingsViewModel
import org.lpss.motosense.viewmodel.motoSenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    settingsViewModel: SettingsViewModel = motoSenseViewModel(factory = SettingsViewModel.Factory),
) {
    val scrollState = rememberScrollState()
    Card(
        modifier = modifier
            .padding(16.dp),
    ) {
        Column(
            modifier = modifier
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val enabled = handleOperationState(
                viewModel = settingsViewModel,
            )
            Text(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                text = "General Settings",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
            val appState by appViewModel.settingsState.collectAsStateWithLifecycle()
            val settings = (appState as AppState.Success).settings

            Text(
                modifier = modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Dynamic Color Mode:",
            )
            SingleChoiceSegmentedButtonRow(
                modifier = modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                DynamicColorMode.entries.forEachIndexed { index, entry ->
                    SegmentedButton(
                        modifier = modifier,
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = DynamicColorMode.entries.size
                        ),
                        onClick = {
                            settingsViewModel.setDynamicColorMode(entry)
                        },
                        selected = settings.dynamicColorMode == entry,
                        label = {
                            Text(
                                modifier = modifier,
                                text = stringResource(entry.label),
                            )
                        },
                        enabled = enabled,
                    )
                }
            }

            Text(
                modifier = modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Theme Mode:",
            )
            SingleChoiceSegmentedButtonRow(
                modifier = modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
            ) {
                ThemeMode.entries.forEachIndexed { index, entry ->
                    SegmentedButton(
                        modifier = modifier,
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = ThemeMode.entries.size
                        ),
                        onClick = {
                            settingsViewModel.setThemeMode(entry)
                        },
                        selected = settings.themeMode == entry,
                        label = {
                            Text(
                                modifier = modifier,
                                text = stringResource(entry.label),
                            )
                        },
                        enabled = enabled,
                    )
                }
            }
            Text(
                modifier = modifier
                    .padding(8.dp)
                    .align(Alignment.CenterHorizontally),
                text = "Motorcycle Icon:",
            )

            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                IconButton(
                    modifier = modifier,
                    onClick = {
                        val idx = MotorcycleIcon.entries.indexOf(settings.motorcycleIcon)
                        settingsViewModel.setMotorcycleIcon(
                            MotorcycleIcon.entries[
                                if (idx - 1 < 0) MotorcycleIcon.entries.size - 1 else idx - 1
                            ]
                        )
                    },
                    enabled = enabled,
                ) {
                    Icon(
                        modifier = modifier.size(48.dp),
                        painter = rememberVectorPainter(
                            image = Icons.AutoMirrored.Default.ArrowLeft,
                        ),
                        contentDescription = "Change Motorcycle Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    modifier = modifier,
                    text = settings.motorcycleIcon.label,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    modifier = modifier
                        .padding(start = 8.dp)
                        .size(48.dp),
                    painter = painterResource(settings.motorcycleIcon.icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                IconButton(
                    modifier = modifier,
                    onClick = {
                        val idx = MotorcycleIcon.entries.indexOf(settings.motorcycleIcon)
                        settingsViewModel.setMotorcycleIcon(
                            MotorcycleIcon.entries[
                                (idx + 1) % MotorcycleIcon.entries.size
                            ]
                        )
                    },
                    enabled = enabled,
                ) {
                    Icon(
                        modifier = modifier.size(48.dp),
                        painter = rememberVectorPainter(
                            image = Icons.AutoMirrored.Default.ArrowRight,
                        ),
                        contentDescription = "Change Motorcycle Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
