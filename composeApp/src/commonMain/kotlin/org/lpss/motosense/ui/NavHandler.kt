package org.lpss.motosense.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import org.lpss.motosense.ui.navigation.ScreenRoute
import org.lpss.motosense.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavHandler(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
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
                    )
                }

                entry<ScreenRoute.Debug> {
                    DebugScreen(
                        modifier = modifier,
                        appViewModel = appViewModel,
                    )
                }
            }
        )
    }
}
