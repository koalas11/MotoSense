package org.lpss.motosense.model

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    val dynamicColorMode: DynamicColorMode = DynamicColorMode.ENABLED,
    val themeMode: ThemeMode = ThemeMode.SYSTEM_DEFAULT,
    val biggerText: Boolean = true,
    val immersiveMode: Boolean = false,
    val motorcycleIcon: MotorcycleIcon = MotorcycleIcon.DEFAULT,
)
