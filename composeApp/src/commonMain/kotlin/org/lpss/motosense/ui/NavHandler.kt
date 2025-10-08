package org.lpss.motosense.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.lpss.motosense.ui.navigation.ScreenRoute
import org.lpss.motosense.viewmodel.AppViewModel

@Composable
fun NavHandler(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = viewModel(factory = AppViewModel.Factory)
) {
    var currentRoute: ScreenRoute by remember { mutableStateOf(ScreenRoute.Home) }
    Scaffold(
        modifier = modifier,
        topBar = {
            MotoSenseTopBar(
                modifier = modifier,
                navController = navController,
                currentDestination = currentRoute,
            )
        },
    ) { innerPadding ->
        NavHost(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            navController = navController,
            startDestination = ScreenRoute.Home,
        ) {
            composable<ScreenRoute.Home> {
                currentRoute = ScreenRoute.Home
                HomeScreen(
                    modifier = modifier,
                    appViewModel = appViewModel,
                )
            }
            composable<ScreenRoute.Debug> {
                currentRoute = ScreenRoute.Debug
                DebugScreen(
                    modifier = modifier,
                    appViewModel = appViewModel,
                )
            }
        }
    }
}
