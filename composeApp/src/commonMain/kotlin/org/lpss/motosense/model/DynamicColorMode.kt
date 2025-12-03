package org.lpss.motosense.model

import kotlinx.serialization.Serializable
import motosense.composeapp.generated.resources.Res
import motosense.composeapp.generated.resources.disabled
import motosense.composeapp.generated.resources.enabled
import org.jetbrains.compose.resources.StringResource

@Serializable
enum class DynamicColorMode(
    val label: StringResource,
) {
    ENABLED(Res.string.enabled),
    DISABLED(Res.string.disabled),
}
