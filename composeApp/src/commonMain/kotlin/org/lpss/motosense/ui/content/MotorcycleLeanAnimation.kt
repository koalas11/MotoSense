package org.lpss.motosense.ui.content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.lpss.motosense.viewmodel.AppState
import org.lpss.motosense.viewmodel.AppViewModel
import org.lpss.motosense.viewmodel.DeviceViewModel

@Composable
fun MotorcycleLeanAnimation(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    deviceViewModel: DeviceViewModel,
) {
    val rollAngle by deviceViewModel.rollAngleState.collectAsStateWithLifecycle()
    val settingsState by appViewModel.settingsState.collectAsStateWithLifecycle()
    val settings = (settingsState as AppState.Success).settings
    val imageVector = painterResource(settings.motorcycleIcon.icon)
    val animatedAngle by animateFloatAsState(
        targetValue = rollAngle.toFloat(),
        animationSpec = tween()
    )
    Box(
        modifier = modifier
            .padding(top = 16.dp)
            .fillMaxSize(),
    ) {
        Icon(
            modifier = modifier
                .padding(bottom = 8.dp)
                .fillMaxHeight(0.9f)
                .aspectRatio(1f)
                .align(Alignment.Center)
                .graphicsLayer {
                    rotationZ = animatedAngle
                    transformOrigin = TransformOrigin(0.5f, 1f)
                },
            painter = imageVector,
            contentDescription = null,
        )
        Text(
            modifier = modifier
                .padding(end = 32.dp)
                .align(Alignment.CenterEnd)
                .fillMaxWidth(),
            textAlign = TextAlign.End,
            text = "$rollAngle°",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
    }
}
