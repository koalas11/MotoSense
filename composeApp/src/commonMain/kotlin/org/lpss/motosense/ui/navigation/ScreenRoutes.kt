package org.lpss.motosense.ui.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

interface ScreenRoute: NavKey {
    @Serializable
    data object Home: ScreenRoute
    @Serializable
    data object Travel: ScreenRoute
    @Serializable
    data object Settings: ScreenRoute
}
