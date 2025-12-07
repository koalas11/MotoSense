package org.lpss.motosense.ui.util

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import org.lpss.motosense.util.Log

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

    @Composable
    override fun RequestNecessaryPermissions(
        modifier: Modifier,
        onDismiss: () -> Unit,
        onGranted: () -> Unit
    ) {
        val context = LocalContext.current
        val perms = mutableListOf<String>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            perms += Manifest.permission.BLUETOOTH_SCAN
            perms += Manifest.permission.BLUETOOTH_CONNECT
        } else {
            perms += Manifest.permission.ACCESS_FINE_LOCATION
        }

        val missingPerms = perms.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPerms.isEmpty()) {
            onGranted()
            return
        }

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { results ->
            val allGranted = results.values.all { it }
            if (allGranted) {
                Log.d("PermissionRequest", "All permissions granted")
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
                onGranted()
            } else {
                Log.d("PermissionRequest", "Some permissions denied")
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                onDismiss()
            }
        }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    modifier = modifier,
                    text = "Permission Required",
                )
            },
            text = {
                val text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    "The App require the bluetooth permissions to connect to the device."
                } else {
                    "The App require the location permission to scan for bluetooth devices."
                }
                Text(
                    modifier = modifier,
                    text = text,
                )
            },
            confirmButton = {
                Button(
                    modifier = modifier,
                    onClick = {
                        launcher.launch(perms.toTypedArray())
                    },
                ) {
                    Text(
                        modifier = modifier,
                        text = "Grant it",
                    )
                }
            },
            dismissButton = {
                Button(
                    modifier = modifier,
                    onClick = {
                        onDismiss()
                    },
                ) {
                    Text(
                        modifier = modifier,
                        text = "Cancel",
                    )
                }
            }
        )
    }
}

actual val fastUIActions: FastUIActions = AndroidFastUIActions
