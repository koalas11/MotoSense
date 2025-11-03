package org.lpss.motosense.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import org.lpss.motosense.AppContainerKey
import org.lpss.motosense.LocalMotoSenseExtras
import org.lpss.motosense.MotoSenseApp
import org.lpss.motosense.PlatformAppContainer

/**
 * Extension function to queries for [MotoSenseApp] object and returns an instance of
 * [PlatformAppContainer].
 */
fun CreationExtras.motoSenseAppContainer(): PlatformAppContainer = this[AppContainerKey]!!

@Composable
inline fun <reified VM : ViewModel> motoSenseViewModel(
    viewModelStoreOwner: ViewModelStoreOwner =
        checkNotNull(LocalViewModelStoreOwner.current) {
            "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
        },
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extras: CreationExtras = LocalMotoSenseExtras.current,
): VM = viewModel(VM::class, viewModelStoreOwner, key, factory, extras)
