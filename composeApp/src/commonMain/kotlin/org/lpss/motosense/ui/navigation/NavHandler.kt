package org.lpss.motosense.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.lpss.motosense.ui.DebugScreen
import org.lpss.motosense.ui.HomeScreen
import org.lpss.motosense.ui.MotoSenseTopBar
import org.lpss.motosense.ui.TravelScreen
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.DeviceState
import org.lpss.motosense.viewmodel.DeviceViewModel
import org.lpss.motosense.viewmodel.motoSenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHandler(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    deviceViewModel: DeviceViewModel = motoSenseViewModel(factory = DeviceViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MotoSenseTopBar(
                modifier = modifier,
                appViewModel = appViewModel,
                scrollBehavior = scrollBehavior,
            )
        },
    ) { paddingValues ->
        val backStack by appViewModel.backStack.collectAsStateWithLifecycle()
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

                entry<ScreenRoute.Debug> {
                    DebugScreen(
                        modifier = modifier,
                    )
                }
            }
        )
    }
}
