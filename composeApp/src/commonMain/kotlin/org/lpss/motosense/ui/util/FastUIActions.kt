package org.lpss.motosense.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface FastUIActions {
    @Composable
    fun ForceScreenOrientation(
        orientation: Int,
    )

    @Composable
    fun DisplayNotification(
        message: String,
    )

    @Composable
    fun RequestNecessaryPermissions(
        modifier: Modifier = Modifier,
        onDismiss: () -> Unit,
        onGranted: () -> Unit,
    )
}

expect val fastUIActions: FastUIActions
