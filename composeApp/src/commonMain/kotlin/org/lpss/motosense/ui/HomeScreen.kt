package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.viewmodel.AppViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
) {
    Column(
        modifier = modifier,
    ) {
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
                    .weight(0.45f)
                    .padding(vertical = 8.dp)
                    .padding(start = 8.dp, end = 8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                AltitudeContent(
                    modifier = modifier,
                    altitude = altitudeValue,
                )
            }
            val speedValue by appViewModel.speedValue.collectAsStateWithLifecycle()
            Text(
                modifier = modifier
                    .fillMaxHeight()
                    .weight(0.45f)
                    .padding(vertical = 8.dp)
                    .padding(start = 8.dp, end = 8.dp),
                text = "$speedValue",
            )
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
            Text(
                modifier = modifier,
                text = "12",
            )
            Text(
                modifier = modifier,
                text = "12",
            )
        }
    }
}
