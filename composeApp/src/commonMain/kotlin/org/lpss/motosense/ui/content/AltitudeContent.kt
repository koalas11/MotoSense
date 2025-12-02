package org.lpss.motosense.ui.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AirplanemodeActive
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.platform
import org.lpss.motosense.viewmodel.DeviceViewModel

@Composable
fun RowScope.AltitudeContent(
    modifier: Modifier = Modifier,
    deviceViewModel: DeviceViewModel,
) {
    val altitude by deviceViewModel.altitudeState.collectAsStateWithLifecycle()
    val elevationType = when {
        altitude >= 2500 -> "Mountain"
        altitude >= 300 -> "Hill"
        else -> "Lowland"
    }

    val icon = when {
        altitude >= 2500 -> Icons.Outlined.AirplanemodeActive
        altitude >= 300 -> Icons.Outlined.Crop
        else -> Icons.Outlined.LocationCity
    }

    val weightValue = if (platform.isPortrait()) 0.45f else 0.35f
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
        Box(
            modifier = modifier
                .fillMaxSize(),
        ) {
            Icon(
                modifier = modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(0.9f)
                    .aspectRatio(1f),
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.18f)
            )

            if (platform.isPortrait()) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp)
                        .align(Alignment.TopStart),
                    text = "Altitude:",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )
            }

            Column(
                modifier = modifier
                    .align(Alignment.CenterStart)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val text = if (platform.isPortrait())
                    "$altitude m"
                else
                    "Altitude: $altitude m"
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    text = text,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp),
                    text = elevationType,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}
