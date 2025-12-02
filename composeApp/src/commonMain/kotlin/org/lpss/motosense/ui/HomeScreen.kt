package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.DeviceState
import org.lpss.motosense.viewmodel.DeviceViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    deviceViewModel: DeviceViewModel,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val deviceUiState by deviceViewModel.deviceState.collectAsStateWithLifecycle()
        if (deviceUiState is DeviceState.Idle) {
            Button(
                onClick = {
                    deviceViewModel.startScanning()
                },
            ) {
                Text(text = "Scan for Devices")
            }
            return@Column
        }
        if (deviceUiState is DeviceState.Scanning || deviceUiState is DeviceState.Ready) {
            Button(
                onClick = {
                    deviceViewModel.stopScanning()
                },
            ) {
                Text(text = "Stop Scanning")
            }
        }
        if (deviceUiState is DeviceState.Scanning) {
            Text(text = "Scanning for devices...")
            return@Column
        }
        if (deviceUiState is DeviceState.Ready) {
            val deviceNames = (deviceUiState as DeviceState.Ready).deviceNames
            LazyColumn(
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(deviceNames) { name ->
                    Button(
                        modifier = modifier,
                        onClick = {
                            deviceViewModel.startDataReadings(name)
                        },
                    ) {
                        Text(text = "Found Device: $name, Tap to Connect")
                    }
                }
            }
            return@Column
        }
    }
}
