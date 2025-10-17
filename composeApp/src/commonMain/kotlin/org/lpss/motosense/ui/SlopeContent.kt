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
import org.lpss.motosense.getPlatform
import kotlin.math.round

@Composable
fun SlopeContent(
    modifier: Modifier = Modifier,
    slope: Float,
) {
    val elevationType = when {
        slope >= 60 -> "Mountain"
        slope >= 30 -> "Hill"
        else -> "Lowland"
    }

    val icon = when {
        slope >= 60 -> Icons.Outlined.AirplanemodeActive
        slope >= 30 -> Icons.Outlined.Crop
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
                .aspectRatio(1f)
                .alpha(0.5f),
            imageVector = icon,
            contentDescription = null,
        )

        if (getPlatform().isPortrait()) {
            Text(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, top = 8.dp)
                    .align(Alignment.TopStart),
                text = "Slope Angle:",
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
            val text = if (getPlatform().isPortrait())
                "${round(slope)} %"
            else
                "Slope Angle: ${round(slope)} %"
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
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Start,
            )
        }
    }
}
