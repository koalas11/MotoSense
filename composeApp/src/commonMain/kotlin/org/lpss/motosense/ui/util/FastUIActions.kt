package org.lpss.motosense.ui.util

import androidx.compose.runtime.Composable

interface FastUIActions {
    @Composable
    fun ForceScreenOrientation(
        orientation: Int,
    )

    @Composable
    fun DisplayNotification(
        message: String,
    )
}

expect val fastUIActions: FastUIActions
