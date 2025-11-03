package org.lpss.motosense

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val appContainer = (application as MotoSenseApp).appContainer

        setContent {
            val context = AndroidContext(LocalContext.current)
            CompositionLocalProvider(
                LocalPlatformContext provides context
            ) {
                App(appContainer = appContainer)
            }
        }
    }
}
