package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.platform
import org.lpss.motosense.viewmodel.AppViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
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
            val altitudeValue by appViewModel.altitudeValue.collectAsStateWithLifecycle()
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
                AltitudeContent(
                    modifier = modifier,
                    altitude = altitudeValue,
                )
            }
            Spacer(
                modifier = modifier
                    .weight(1f - 2 * weightValue)
            )
            val speedValue by appViewModel.speedValue.collectAsStateWithLifecycle()
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
                SpeedContent(
                    modifier = modifier,
                    speed = speedValue,
                )
            }
        }
        val leanAngleValue by appViewModel.leanAngleValue.collectAsStateWithLifecycle()
        Column(
            modifier
                .fillMaxWidth()
                .weight(0.5f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MotorcycleLeanAnimation(
                modifier = modifier,
                leanAngle = leanAngleValue,
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .weight(0.25f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            val slopeValue by appViewModel.slopeValue.collectAsStateWithLifecycle()
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
                SlopeContent(
                    modifier = modifier,
                    slope = slopeValue,
                )
            }
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
