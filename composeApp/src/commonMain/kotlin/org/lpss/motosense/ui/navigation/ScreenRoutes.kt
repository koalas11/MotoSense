package org.lpss.motosense.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TravelExplore
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface ScreenRoute: NavKey {
    @Serializable
    data object Home: ScreenRoute
    @Serializable
    data object Travel: ScreenRoute
    @Serializable
    data object Settings: ScreenRoute
    @Serializable
    data object Trips: ScreenRoute
    @Serializable
    data class TripDetails(val tripId: Long): ScreenRoute
}

enum class MainRoute(
    val route: ScreenRoute,
    val label: String,
    val icon: ImageVector,
) {
    HOME(ScreenRoute.Home, "Home", Icons.Default.Home),
    TRIPS(ScreenRoute.Trips, "Trips", Icons.Default.TravelExplore),
}
