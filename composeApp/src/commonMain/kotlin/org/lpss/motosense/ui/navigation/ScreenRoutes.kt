package org.lpss.motosense.ui.navigation

import kotlinx.serialization.Serializable

interface ScreenRoute {
    @Serializable
    object Home: ScreenRoute
    @Serializable
    object Debug: ScreenRoute
}
