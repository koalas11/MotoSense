package org.lpss.motosense

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.lpss.motosense.ui.navigation.NavHandler
import org.lpss.motosense.ui.theme.MotoSenseTheme
import org.lpss.motosense.viewmodel.AppState
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.motoSenseViewModel

val AppContainerKey = object : CreationExtras.Key<PlatformAppContainer> {}

val LocalMotoSenseExtras = staticCompositionLocalOf<CreationExtras> {
    error("No CreationExtras provided")
}

val LocalPlatformContext = staticCompositionLocalOf<PlatformContext> {
    error("No PlatformContext provided")
}

@Composable
fun App(
    modifier: Modifier = Modifier,
    appContainer: PlatformAppContainer,
) {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    val oldCreationExtras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }

    val creationExtras = MutableCreationExtras(oldCreationExtras).apply {
        this[AppContainerKey] = appContainer
    }

    CompositionLocalProvider(LocalMotoSenseExtras provides creationExtras) {
        Main(
            modifier = modifier,
        )
    }
}

@Composable
fun Main(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel = motoSenseViewModel(factory = AppViewModel.Factory),
) {
    LaunchedEffect(Unit) {
        appViewModel.initialize()
    }

    val settingsState by appViewModel.settingsState.collectAsStateWithLifecycle()

    when (settingsState) {
        is AppState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(
                    modifier = modifier,
                )
                Text(
                    modifier = modifier,
                    text = "Loading..."
                )
            }
        }
        is AppState.Error -> {
            val errorMsg = (settingsState as AppState.Error).error.message
            Text(
                modifier = modifier,
                text = "Error: $errorMsg"
            )
        }
        is AppState.Success -> {
            MotoSenseTheme(
                appViewModel = appViewModel,
            ) {
                NavHandler(
                    modifier = modifier,
                    appViewModel = appViewModel,
                )
            }
        }
    }
}
