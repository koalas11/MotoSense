package org.lpss.motosense.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.lpss.motosense.ui.HomeScreen
import org.lpss.motosense.ui.MotoSenseTopBar
import org.lpss.motosense.ui.SettingsScreen
import org.lpss.motosense.ui.TravelScreen
import org.lpss.motosense.ui.TripDetailsScreen
import org.lpss.motosense.ui.TripsScreen
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.DeviceState
import org.lpss.motosense.viewmodel.DeviceViewModel
import org.lpss.motosense.viewmodel.motoSenseViewModel

@Composable
fun NavHandler(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    deviceViewModel: DeviceViewModel = motoSenseViewModel(factory = DeviceViewModel.Factory)
) {
    val backStack by appViewModel.backStack.collectAsStateWithLifecycle()
    Scaffold(
        modifier = modifier,
        topBar = {
            if (backStack.last() is ScreenRoute.Home) {
                MotoSenseTopBar(
                    modifier = modifier,
                    appViewModel = appViewModel,
                )
            }
        },
        bottomBar = {
            if (backStack.last() !is ScreenRoute.Travel) {
                NavigationBar(
                    modifier = modifier,
                ) {
                    MainRoute.entries.forEach { entry ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = entry.icon,
                                    contentDescription = null,
                                )
                            },
                            label = {
                                Text(
                                    text = entry.label,
                                )
                            },
                            selected = backStack.last() == entry.route,
                            onClick = {
                                appViewModel.navigateTo(entry.route)
                            }
                        )
                    }
                }
            }
        },
    ) { paddingValues ->
        val deviceUiState by deviceViewModel.deviceState.collectAsStateWithLifecycle()
        LaunchedEffect(deviceUiState) {
            if (deviceUiState is DeviceState.Running && backStack.last() !is ScreenRoute.Travel) {
                appViewModel.navigateTo(ScreenRoute.Travel)
            }
            if (deviceUiState is DeviceState.Idle && backStack.last() is ScreenRoute.Travel) {
                appViewModel.navigateTo(ScreenRoute.Home)
            }
        }

        NavDisplay(
            backStack = backStack,
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            onBack = {
                appViewModel.navigateBack()
            },
            entryProvider = entryProvider {
                entry<ScreenRoute.Home> {
                    HomeScreen(
                        modifier = modifier,
                        appViewModel = appViewModel,
                        deviceViewModel = deviceViewModel,
                    )
                }
                entry<ScreenRoute.Travel> {
                    TravelScreen(
                        modifier = modifier,
                        appViewModel = appViewModel,
                        deviceViewModel = deviceViewModel,
                    )
                }

                entry<ScreenRoute.Settings> {
                    SettingsScreen(
                        modifier = modifier,
                        appViewModel = appViewModel,
                    )
                }

                entry<ScreenRoute.Trips> {
                    TripsScreen(
                        modifier = modifier,
                        appViewModel = appViewModel,
                    )
                }

                entry<ScreenRoute.TripDetails> { tripDetails ->
                    TripDetailsScreen(
                        modifier = modifier,
                        tripId = tripDetails.tripId,
                        appViewModel = appViewModel,
                    )
                }
            }
        )
    }
}
