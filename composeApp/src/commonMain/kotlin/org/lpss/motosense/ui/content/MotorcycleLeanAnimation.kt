package org.lpss.motosense.ui.content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import motosense.composeapp.generated.resources.Res
import motosense.composeapp.generated.resources.motorbike_svg
import org.jetbrains.compose.resources.painterResource
import org.lpss.motosense.viewmodel.DeviceViewModel

@Composable
fun MotorcycleLeanAnimation(
    modifier: Modifier = Modifier,
    deviceViewModel: DeviceViewModel,
) {
    val rollAngle by deviceViewModel.rollAngleState.collectAsStateWithLifecycle()
    val imageVector = painterResource(Res.drawable.motorbike_svg)
    val animatedAngle by animateFloatAsState(
        targetValue = rollAngle.toFloat(),
        animationSpec = tween()
    )
    Icon(
        modifier = modifier
            .padding(bottom = 8.dp)
            .fillMaxWidth(0.45f)
            .graphicsLayer {
                rotationZ = animatedAngle
                transformOrigin = TransformOrigin(0.5f, 1f)
            },
        painter = imageVector,
        contentDescription = null,
    )
    Text(
        modifier = modifier
            .padding(bottom = 16.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = "$rollAngle°",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )
}
