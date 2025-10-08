package org.lpss.motosense

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import platform.UIKit.UIDevice

object IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion

    override val version: Int
        get() = TODO("Not yet implemented")

    @Composable
    override fun isSystemInDarkTheme(): Boolean {
        TODO("Not yet implemented")
    }

    @Composable
    override fun getDynamicColor(darkTheme: Boolean): ColorScheme? {
        TODO("Not yet implemented")
    }
}

actual fun getPlatform(): Platform = IOSPlatform