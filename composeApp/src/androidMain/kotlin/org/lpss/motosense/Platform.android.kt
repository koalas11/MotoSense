package org.lpss.motosense

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"

    override val version: Int = Build.VERSION.SDK_INT

    @Composable
    override fun isSystemInDarkTheme(): Boolean {
        return androidx.compose.foundation.isSystemInDarkTheme()
    }

    @Composable
    override fun getDynamicColor(darkTheme: Boolean): ColorScheme? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            return if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
                context
            )
        }
        return null
    }
}

actual fun getPlatform(): Platform = AndroidPlatform
