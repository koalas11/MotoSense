package org.lpss.motosense.model

import kotlinx.serialization.Serializable
import motosense.composeapp.generated.resources.Res
import motosense.composeapp.generated.resources.classic_motorcycle
import motosense.composeapp.generated.resources.default_motorcycle
import motosense.composeapp.generated.resources.sport_motorcycle
import org.jetbrains.compose.resources.DrawableResource

@Serializable
enum class MotorcycleIcon(
    val label: String,
    val icon: DrawableResource,
) {
    DEFAULT("Default", Res.drawable.default_motorcycle),
    CLASSIC("Classic", Res.drawable.classic_motorcycle),
    SPORT("Sport", Res.drawable.sport_motorcycle),
}
