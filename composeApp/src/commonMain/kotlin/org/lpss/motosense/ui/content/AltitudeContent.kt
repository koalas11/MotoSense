package org.lpss.motosense.ui.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.LocalContentTextStyle
import org.lpss.motosense.ui.util.iconMaxHeight
import org.lpss.motosense.ui.util.iconPadding
import org.lpss.motosense.ui.util.textAlign
import org.lpss.motosense.viewmodel.DeviceViewModel

@Composable
fun ColumnScope.AltitudeContent(
    modifier: Modifier = Modifier,
    deviceViewModel: DeviceViewModel,
) {
    val altitude by deviceViewModel.altitudeState.collectAsStateWithLifecycle()


    val icon = if (altitude != null) {
        when {
            altitude!! >= 2500 -> Icons.Outlined.AirplanemodeActive
            altitude!! >= 300 -> Icons.Outlined.Crop
            else -> Icons.Outlined.LocationCity
        }
    } else {
        Icons.Outlined.LocationCity
    }

    Card(
        modifier = modifier
            .weight(0.65f)
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
                    .align(Alignment.BottomStart)
                    .padding(iconPadding)
                    .fillMaxHeight(iconMaxHeight)
                    .aspectRatio(1f),
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.18f)
            )

            Column(
                modifier = modifier
                    .fillMaxSize(),
            ) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp)
                        .align(Alignment.Start),
                    text = "Altitude:",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = LocalContentTextStyle.current.fontSize)) {
                            append(altitude?.toString() ?: "-")
                        }
                        withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.headlineSmall.fontSize)) {
                            append(" m")
                        }
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = textAlign,
                )
            }
        }
    }
}
