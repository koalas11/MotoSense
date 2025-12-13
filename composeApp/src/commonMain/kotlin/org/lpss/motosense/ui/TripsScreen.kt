package org.lpss.motosense.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.ui.navigation.ScreenRoute
import org.lpss.motosense.util.roundToDecimals
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.TripsUiState
import org.lpss.motosense.viewmodel.TripsViewModel
import org.lpss.motosense.viewmodel.motoSenseViewModel

@Composable
fun TripsScreen(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    tripsViewModel: TripsViewModel = motoSenseViewModel(factory = TripsViewModel.Factory)
) {
    LaunchedEffect(Unit) {
        tripsViewModel.initialize()
    }
    val tripsUiState by tripsViewModel.tripsUiState.collectAsStateWithLifecycle()
    if (tripsUiState is TripsUiState.Loading) {
        CircularProgressIndicator(
            modifier = modifier
        )
        return
    }
    if (tripsUiState is TripsUiState.Error) {
        Text(
            text = "Error loading trips",
            modifier = modifier
        )
        return
    }
    val trips = (tripsUiState as TripsUiState.Success).trips
    if (trips.isEmpty()) {
        Text(
            text = "No trips available",
            modifier = modifier
                .padding(16.dp)
        )
        return
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Trips:",
                modifier = modifier
                    .padding(8.dp)
            )
        }
        items(trips) { trip ->
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                onClick = {
                    appViewModel.navigateTo(ScreenRoute.TripDetails(trip.id))
                }
            ) {
                val rounded = roundToDecimals(trip.distanceTravelledAvgKm, 2)
                Text(
                    text = "${trip.title} - Distance Travelled: $rounded km",
                    modifier = modifier
                        .padding(16.dp)
                )
            }
        }
    }
}
