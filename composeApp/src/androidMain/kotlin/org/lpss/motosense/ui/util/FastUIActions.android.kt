package org.lpss.motosense.ui.util

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

object AndroidFastUIActions : FastUIActions {
    @Composable
    override fun ForceScreenOrientation(orientation: Int) {
        val activity = LocalActivity.current

        DisposableEffect(orientation) {
            val originalOrientation = activity!!.requestedOrientation
            activity.requestedOrientation = orientation

            onDispose {
                activity.requestedOrientation = originalOrientation
            }
        }
    }

    @Composable
    override fun DisplayNotification(message: String) {
        val context = LocalContext.current
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

actual val fastUIActions: FastUIActions = AndroidFastUIActions
