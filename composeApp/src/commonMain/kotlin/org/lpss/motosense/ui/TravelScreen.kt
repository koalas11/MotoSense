package org.lpss.motosense.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import org.lpss.motosense.ui.content.AltitudeContent
import org.lpss.motosense.ui.content.DistanceTravelledContent
import org.lpss.motosense.ui.content.F1GForceRingsComposable
import org.lpss.motosense.ui.content.MotorcycleLeanAnimation
import org.lpss.motosense.ui.content.SlopeContent
import org.lpss.motosense.ui.content.SpeedContent
import org.lpss.motosense.ui.util.fastUIActions
import org.lpss.motosense.util.Log
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.DeviceViewModel
import kotlin.time.Duration.Companion.seconds

@Composable
fun TravelScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    deviceViewModel: DeviceViewModel,
) {
    fastUIActions.ForceScreenOrientation(2)
    var iconVisible by remember { mutableStateOf(false) }
    var iconToggleCounter by remember { mutableStateOf(false) }
    LaunchedEffect(iconToggleCounter) {
        Log.d(
            "TravelScreen",
            "Icon visibility toggled",
        )
        iconVisible = true
        delay(4.seconds)
        iconVisible = false
    }
    Column(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
            ) {
                iconToggleCounter = !iconToggleCounter
            },
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.6f),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = modifier
                    .weight(0.25f)
                    .wrapContentHeight(),
            ) {
                AltitudeContent(
                    modifier = modifier,
                    deviceViewModel = deviceViewModel,
                )
                Spacer(
                    modifier = modifier
                        .weight(0.35f),
                )
            }
            Box(
                modifier = modifier
                    .weight(0.5f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                val perceivedAcceleration by deviceViewModel.perceivedAccelerationState.collectAsStateWithLifecycle()
                val accelerationDirection by deviceViewModel.accelerationDirectionState.collectAsStateWithLifecycle()

                F1GForceRingsComposable(
                    activeRing = accelerationDirection?.toInt(),
                    gForceValue = perceivedAcceleration,
                )
            }
            Column(
                modifier = modifier
                    .weight(0.25f),
            ) {
                SpeedContent(
                    modifier = modifier,
                    deviceViewModel = deviceViewModel,
                )
                if (!iconVisible) {
                    Spacer(
                        modifier = modifier
                            .weight(0.35f),
                    )
                    return@Column
                }
                IconButton(
                    modifier = modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp)
                        .weight(0.35f),
                    onClick = {
                        deviceViewModel.stopDataReadings()
                    },
                ) {
                    Icon(
                        modifier = modifier,
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Stop Readings",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.4f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SlopeContent(
                modifier = modifier,
                deviceViewModel = deviceViewModel,
            )
            Box(
                modifier = modifier
                    .weight(0.5f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                MotorcycleLeanAnimation(
                    modifier = modifier,
                    appViewModel = appViewModel,
                    deviceViewModel = deviceViewModel,
                )
            }
            DistanceTravelledContent(
                modifier = modifier,
                deviceViewModel = deviceViewModel,
            )
        }
    }
}
