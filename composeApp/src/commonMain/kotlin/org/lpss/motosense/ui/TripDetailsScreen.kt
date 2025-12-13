package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowLeft
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.lineSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import org.lpss.motosense.ui.util.handleOperationState
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.TripDetailsViewModel
import org.lpss.motosense.viewmodel.TripsDetailsUiState
import org.lpss.motosense.viewmodel.motoSenseViewModel

@Composable
fun TripDetailsScreen(
    modifier: Modifier = Modifier,
    tripId: Long,
    appViewModel: AppViewModel,
    tripDetailsViewModel: TripDetailsViewModel = motoSenseViewModel(factory = TripDetailsViewModel.Factory),
) {
    LaunchedEffect(tripId) {
        tripDetailsViewModel.initialize(tripId)
    }
    val tripDetailsState by tripDetailsViewModel.tripDetailsUiState.collectAsStateWithLifecycle()
    if (tripDetailsState is TripsDetailsUiState.Loading) {
        CircularProgressIndicator(
            modifier = modifier,
        )
        return
    }
    if (tripDetailsState is TripsDetailsUiState.Error) {
        Text(
            modifier = modifier,
            text = "Error loading trip details.",
        )
        return
    }
    val trip = (tripDetailsState as TripsDetailsUiState.Success).trip
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {
        val enabled = handleOperationState(
            viewModel = tripDetailsViewModel,
            onSuccess = {
                tripDetailsViewModel.resetOperationState()
                appViewModel.navigateBack()
            }
        )
        Row(
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = modifier,
                text = trip.title,
            )
            IconButton(
                modifier = modifier,
                onClick = {
                    tripDetailsViewModel.deleteTripById(trip.id)
                },
                enabled = enabled,
            ) {
                Icon(
                    modifier = modifier,
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Trip",
                )
            }
        }

        var selectedIdx by remember { mutableStateOf(0) }
        val options = listOf(
            "Speed",
            "Altitude",
            "Percent Slope",
            "Perceived Acceleration",
        )

        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            IconButton(
                modifier = modifier,
                onClick = {
                    selectedIdx = (if (selectedIdx - 1 < 0) options.size - 1 else selectedIdx - 1) % options.size
                },
            ) {
                Icon(
                    modifier = modifier.size(48.dp),
                    imageVector = Icons.AutoMirrored.Default.ArrowLeft,
                    contentDescription = "Change Motorcycle Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                modifier = modifier,
                text = options[selectedIdx],
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(
                modifier = modifier,
                onClick = {
                    selectedIdx = (selectedIdx + 1) % options.size
                },
            ) {
                Icon(
                    modifier = modifier.size(48.dp),
                    imageVector = Icons.AutoMirrored.Default.ArrowRight,
                    contentDescription = "Change Motorcycle Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        val modelProducer = remember { CartesianChartModelProducer() }

        LaunchedEffect(selectedIdx) {
            val chartData = when (options[selectedIdx]) {
                "Speed" -> trip.history.map { it.speed?.toDouble() ?: Double.NaN }
                "Altitude" -> trip.history.map { it.altitude?.toDouble() ?: Double.NaN }
                "Percent Slope" -> trip.history.map { it.slopeAngle ?: Double.NaN }
                "Perceived Acceleration" -> trip.history.map { it.perceivedAcceleration?.toDouble() ?: Double.NaN }
                else -> throw IllegalArgumentException("Unknown idx: $selectedIdx")
            }
            modelProducer.runTransaction { lineSeries { series(chartData) } }
        }

        CartesianChartHost(
            chart =
                rememberCartesianChart(
                    rememberLineCartesianLayer(),
                    startAxis = VerticalAxis.rememberStart(),
                    bottomAxis = HorizontalAxis.rememberBottom(),
                ),
            modelProducer = modelProducer,
            modifier = modifier,
        )
    }
}
