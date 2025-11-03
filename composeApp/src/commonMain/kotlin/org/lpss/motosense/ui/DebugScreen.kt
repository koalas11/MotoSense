package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.viewmodel.AppState
import org.lpss.motosense.viewmodel.AppViewModel

@Composable
fun DebugScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
) {
    Column(
        modifier = modifier.padding(8.dp),
    ) {
        Text(
            modifier = modifier,
            text = "Altitude: ${appViewModel.altitudeValue.value.toInt()} m",
        )
        val altitudeValue by appViewModel.altitudeValue.collectAsStateWithLifecycle()
        Slider(
            value = altitudeValue,
            onValueChange = { it ->
                appViewModel.setAltitude(it)
            },
            valueRange = 0f.rangeTo(3000f),
        )
        Text(
            modifier = modifier,
            text = "Lean Angle: ${appViewModel.leanAngleValue.value.toInt()} °",
        )
        val leanAngleValue by appViewModel.leanAngleValue.collectAsStateWithLifecycle()
        Slider(
            value = leanAngleValue,
            onValueChange = { it ->
                appViewModel.setLeanAngle(it)
            },
            valueRange = (-90f).rangeTo(90f),
        )
        Text(
            modifier = modifier,
            text = "Speed: ${appViewModel.speedValue.value} km/h",
        )
        val speedValue by appViewModel.speedValue.collectAsStateWithLifecycle()
        Slider(
            value = speedValue,
            onValueChange = { it ->
                appViewModel.setSpeed(it)
            },
            valueRange = 0f.rangeTo(300f),
        )
        Text(
            modifier = modifier,
            text = "Slope: ${appViewModel.slopeValue.value} %",
        )
        val slopeValue by appViewModel.slopeValue.collectAsStateWithLifecycle()
        Slider(
            value = slopeValue,
            onValueChange = { it ->
                appViewModel.setSlope(it)
            },
            valueRange = 0f.rangeTo(100f),
        )
        Button(
            modifier = modifier,
            onClick = {
                appViewModel.startSimulation()
            },
        ) {
            Text("Start Simulation")
        }

        val settingState by appViewModel.settingsStateFlow.collectAsStateWithLifecycle()
        val settings = (settingState as AppState.Success).settings

        Text(
            modifier = modifier,
            text = "Dynamic Theme",
        )
        Checkbox(
            modifier = modifier,
            checked = settings.enableDynamicTheme,
            onCheckedChange = {
                appViewModel.updateSettings(it)
            }
        )
    }
}
