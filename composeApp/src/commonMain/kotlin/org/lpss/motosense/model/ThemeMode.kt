package org.lpss.motosense.model

import kotlinx.serialization.Serializable
import motosense.composeapp.generated.resources.Res
import motosense.composeapp.generated.resources.dark_mode
import motosense.composeapp.generated.resources.light_mode
import motosense.composeapp.generated.resources.system_default
import org.jetbrains.compose.resources.StringResource

@Serializable
enum class ThemeMode(
    val label: StringResource,
) {
    LIGHT(Res.string.light_mode),
    DARK(Res.string.dark_mode),
    SYSTEM_DEFAULT(Res.string.system_default),
}
