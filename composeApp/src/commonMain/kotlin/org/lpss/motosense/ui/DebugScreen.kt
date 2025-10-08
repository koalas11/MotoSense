package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.viewmodel.AppViewModel

@Composable
fun DebugScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = modifier,
            text = "Altitude:",
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
            text = "Lean Angle:",
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
            text = "Speed:",
        )
        val speedValue by appViewModel.speedValue.collectAsStateWithLifecycle()
        Slider(
            value = speedValue,
            onValueChange = { it ->
                appViewModel.setSpeed(it)
            },
            valueRange = 0f.rangeTo(300f),
        )
    }
}