package org.lpss.motosense.model

import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    var enableDynamicTheme: Boolean = true,
)
