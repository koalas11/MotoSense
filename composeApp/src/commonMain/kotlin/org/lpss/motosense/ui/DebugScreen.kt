package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.lpss.motosense.viewmodel.DebugViewModel
import org.lpss.motosense.viewmodel.motoSenseViewModel

@Composable
fun DebugScreen(
    modifier: Modifier = Modifier,
    debugViewModel: DebugViewModel = motoSenseViewModel(factory = DebugViewModel.Factory),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                debugViewModel.createDebugDevice()
            }
        ) {
            Text("Create Debug Device")
        }
        Button(
            onClick = {
                debugViewModel.destroyDebugDevice()
            }
        ) {
            Text("Destroy Debug Device")
        }
        Button(
            onClick = {
                debugViewModel.startProducingData()
            }
        ) {
            Text("Start Producing Data")
        }
        Button(
            onClick = {
                debugViewModel.stopProducingData()
            }
        ) {
            Text("Stop Producing Data")
        }
    }
}
