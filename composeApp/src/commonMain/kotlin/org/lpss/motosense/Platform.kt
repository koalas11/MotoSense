package org.lpss.motosense

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

interface Platform {
    val name: String

    val version: Int

    @Composable
    fun isSystemInDarkTheme(): Boolean

    @Composable
    fun getDynamicColor(darkTheme: Boolean): ColorScheme?

    @Composable
    fun isPortrait(): Boolean

    @Composable
    fun isLandscape(): Boolean
}

expect val platform: Platform
