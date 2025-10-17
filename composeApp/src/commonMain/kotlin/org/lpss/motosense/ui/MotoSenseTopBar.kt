package org.lpss.motosense.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.lpss.motosense.ui.navigation.ScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotoSenseTopBar(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    currentDestination: ScreenRoute,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        title = {
            Text(
                modifier = modifier,
                text = "",
            )
        },
        actions = {
            if (currentDestination is ScreenRoute.Home) {
                IconButton(
                    modifier = modifier,
                    onClick = {
                        navController.navigate(ScreenRoute.Debug)
                    }
                ) {
                    Icon(
                        modifier = modifier,
                        imageVector = Icons.Filled.Settings,
                        contentDescription = null,
                    )
                }
            }
        },
        navigationIcon = {
            if (currentDestination is ScreenRoute.Debug) {
                IconButton(
                    modifier = modifier,
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        modifier = modifier,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        }
    )
}
