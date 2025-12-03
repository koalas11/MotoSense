package org.lpss.motosense.ui.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.lpss.motosense.util.roundToDecimals

@Composable
fun F1GForceRingsComposable(
    modifier: Modifier = Modifier,
    activeRing: Int = -1,
    gForceValue: Float = 0f,
) {
    val textMeasurer = rememberTextMeasurer(0)
    val segmentCount = 8
    val ringColors = List(segmentCount) { index ->
        if (index == activeRing) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onTertiary
    }
    val outLinesColor = MaterialTheme.colorScheme.onBackground
    val middleCircleColor = MaterialTheme.colorScheme.background
    val textStyle = MaterialTheme.typography.headlineSmall.copy(
        color = MaterialTheme.colorScheme.onBackground,
    )
    Canvas(modifier = modifier.fillMaxSize()) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        val sweepAngle = 360f / segmentCount

        // Outer ring
        val outerStroke = 25.dp.toPx()
        val outerRadius = radius - outerStroke / 2   // center of stroke

        for (i in 0 until segmentCount) {
            drawArc(
                color = ringColors[i],
                startAngle = i * sweepAngle + 22.5f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                size = Size(outerRadius * 2, outerRadius * 2),
                style = Stroke(width = outerStroke)
            )
        }

        // Middle ring (subtract full stroke, not half)
        val middleStroke = 25.dp.toPx()
        val middleRadius = outerRadius - outerStroke / 2 - middleStroke / 2
        for (i in 0 until segmentCount) {
            drawArc(
                color = ringColors[i],
                startAngle = i * sweepAngle + 22.5f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - middleRadius, center.y - middleRadius),
                size = Size(middleRadius * 2, middleRadius * 2),
                style = Stroke(width = middleStroke)
            )
        }

        // Inner circle
        val innerRadius = middleRadius - middleStroke / 2
        drawCircle(
            color = middleCircleColor,
            radius = innerRadius,
            center = center
        )

        // Inner circle border
        drawCircle(
            color = outLinesColor,
            radius = innerRadius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Center Text
        val rounded = roundToDecimals(gForceValue.toDouble(), 1)
        val text = "$rounded g"
        drawText(
            textMeasurer = textMeasurer,
            text = text,
            topLeft = Offset(
                center.x - textMeasurer.measure(text, textStyle).size.width / 2,
                center.y - textMeasurer.measure(text, textStyle).size.height / 2
            ),
            style = textStyle,
        )

        // Outer - Middle divider
        drawCircle(
            color = outLinesColor,
            radius = middleRadius + middleStroke / 2,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Outer circle border
        drawCircle(
            color = outLinesColor,
            radius = outerRadius + outerStroke / 2,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Radial dividers
        for (i in 0 until segmentCount) {
            val angleRad = (i * sweepAngle  + 22.5f).toDouble() * (kotlin.math.PI / 180.0)
            val xOuter = center.x + (outerRadius + outerStroke / 2) * kotlin.math.cos(angleRad).toFloat()
            val yOuter = center.y + (outerRadius + outerStroke / 2) * kotlin.math.sin(angleRad).toFloat()

            val xInner = center.x + innerRadius * kotlin.math.cos(angleRad).toFloat()
            val yInner = center.y + innerRadius * kotlin.math.sin(angleRad).toFloat()

            drawLine(
                color = outLinesColor,
                start = Offset(xInner, yInner),
                end = Offset(xOuter, yOuter),
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}

@Preview
@Composable
fun F1GForceRingsComposableWithDividersPreview() {
    Column(
        modifier = Modifier
            .size(600.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        F1GForceRingsComposable(
            activeRing = 3,
            gForceValue = 1.76f,
        )
    }
}
