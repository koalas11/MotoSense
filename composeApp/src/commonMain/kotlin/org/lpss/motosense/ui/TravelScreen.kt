package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.lpss.motosense.platform
import org.lpss.motosense.ui.content.AltitudeContent
import org.lpss.motosense.ui.content.MotorcycleLeanAnimation
import org.lpss.motosense.ui.content.SlopeContent
import org.lpss.motosense.ui.content.SpeedContent
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.DeviceViewModel

@Composable
fun TravelScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    deviceViewModel: DeviceViewModel,
) {
    Column(
        modifier = modifier,
    ) {
        val weightValue = if (platform.isPortrait()) 0.45f else 0.35f
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.25f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            AltitudeContent(
                modifier = modifier,
                deviceViewModel = deviceViewModel,
            )
            Spacer(
                modifier = modifier
                    .weight(1f - 2 * weightValue)
            )
            SpeedContent(
                modifier = modifier,
                deviceViewModel = deviceViewModel,
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.05f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = {
                    deviceViewModel.stopDataReadings()
                },
            ) {
                Text(
                    text = "Stop Readings",
                )
            }
            MotorcycleLeanAnimation(
                modifier = modifier,
                deviceViewModel = deviceViewModel,
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.25f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            SlopeContent(
                modifier = modifier,
                deviceViewModel = deviceViewModel,
            )
            Spacer(
                modifier = modifier
                    .weight(1f - 2 * weightValue)
            )
            Card(
                modifier = modifier
                    .fillMaxHeight()
                    .weight(weightValue)
                    .padding(vertical = 8.dp)
                    .padding(start = 8.dp, end = 8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
            ) {
            }
        }
    }
}
