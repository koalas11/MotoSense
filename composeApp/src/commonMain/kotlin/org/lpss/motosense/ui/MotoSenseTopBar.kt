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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.lpss.motosense.ui.navigation.ScreenRoute
import org.lpss.motosense.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotoSenseTopBar(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val backStack by appViewModel.backStack.collectAsStateWithLifecycle()
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
            if (backStack.last() !is ScreenRoute.Settings) {
                IconButton(
                    modifier = modifier,
                    onClick = {
                        appViewModel.navigateTo(ScreenRoute.Settings)
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
            if (backStack.last() is ScreenRoute.Settings) {
                IconButton(
                    modifier = modifier,
                    onClick = {
                        appViewModel.navigateBack()
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
