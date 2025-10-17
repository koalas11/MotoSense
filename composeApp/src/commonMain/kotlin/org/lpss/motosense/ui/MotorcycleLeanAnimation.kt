package org.lpss.motosense.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lpss.motosense.resources.Res
import org.lpss.motosense.resources.motorbike_svg


@Composable
fun MotorcycleLeanAnimation(
    modifier: Modifier = Modifier,
    leanAngle: Float,
) {
    val imageVector = painterResource(Res.drawable.motorbike_svg)
    val animatedAngle by animateFloatAsState(
        targetValue = leanAngle,
        animationSpec = tween(
        )
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
        text = "$leanAngle°",
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )
}

@Preview
@Composable
fun MotoTest(
    modifier: Modifier = Modifier,
) {
    var sliderPosition by remember { mutableStateOf(0f) }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Slider(
            modifier = modifier.padding(8.dp),
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
            },
            valueRange = (-90f).rangeTo(90f),

        )
        MotorcycleLeanAnimation(
            modifier = modifier.padding(8.dp),
            leanAngle = sliderPosition,
        )
    }
}
