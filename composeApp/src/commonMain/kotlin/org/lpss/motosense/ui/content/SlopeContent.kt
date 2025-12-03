package org.lpss.motosense.ui.content

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
import androidx.compose.material.icons.outlined.DownhillSkiing
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import motosense.composeapp.generated.resources.Res
import motosense.composeapp.generated.resources.elevation
import org.jetbrains.compose.resources.painterResource
import org.lpss.motosense.ui.util.iconMaxHeight
import org.lpss.motosense.ui.util.iconPadding
import org.lpss.motosense.ui.util.textAlign
import org.lpss.motosense.util.roundToDecimals
import org.lpss.motosense.viewmodel.DeviceViewModel

@Composable
fun RowScope.SlopeContent(
    modifier: Modifier = Modifier,
    deviceViewModel: DeviceViewModel,
) {
    val slope by deviceViewModel.slopeAngleState.collectAsStateWithLifecycle()

    val icon = when {
        slope >= 0.0f -> painterResource(Res.drawable.elevation)
        else -> rememberVectorPainter(Icons.Outlined.DownhillSkiing)
    }
    Card(
        modifier = modifier
            .weight(0.2f)
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
                painter = icon,
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
                    text = "Slope:",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )
                val rounded = roundToDecimals(slope, 1)
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.headlineMedium.fontSize)) {
                            append(rounded.toString())
                        }
                        withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.headlineSmall.fontSize)) {
                            append(" %")
                        }
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = textAlign,
                )
            }
        }
    }
}
