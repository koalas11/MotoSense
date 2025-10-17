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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.lpss.motosense.platform
import kotlin.math.round

@Composable
fun SpeedContent(
    modifier: Modifier = Modifier,
    speed: Float,
) {
    val elevationType = when {
        speed >= 90 -> "Fast"
        speed >= 60 -> "Normal"
        else -> "Slow"
    }

    val icon = when {
        speed >= 90 -> Icons.Outlined.AirplanemodeActive
        speed >= 60 -> Icons.Outlined.Crop
        else -> Icons.Outlined.LocationCity
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Icon(
            modifier = modifier
                .align(Alignment.CenterStart)
                .fillMaxHeight(0.9f)
                .aspectRatio(1f)
                .alpha(0.5f),
            imageVector = icon,
            contentDescription = null,
        )

        if (platform.isPortrait()) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp, top = 8.dp)
                    .align(Alignment.TopEnd),
                text = "Speed:",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
            )
        }

        Column(
            modifier = modifier
                .align(Alignment.CenterEnd)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val text = if (platform.isPortrait())
                "${round(speed)} km/h"
            else
                "Speed: ${round(speed)} km/h"
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                text = text,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
            )
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                text = elevationType,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.End,
            )
        }
    }
}
