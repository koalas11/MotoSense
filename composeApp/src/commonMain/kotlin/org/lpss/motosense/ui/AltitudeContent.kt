package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AirplanemodeActive
import androidx.compose.material.icons.outlined.Crop
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.lpss.motosense.platform
import kotlin.math.roundToInt

@Composable
fun AltitudeContent(
    modifier: Modifier = Modifier,
    altitude: Float,
) {
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
                "${altitude.roundToInt()} m"
            else
                "Altitude: ${altitude.roundToInt()} m"
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
