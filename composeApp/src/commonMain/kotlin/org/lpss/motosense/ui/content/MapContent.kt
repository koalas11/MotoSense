package org.lpss.motosense.ui.content

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.json.JsonObject
import org.lpss.motosense.viewmodel.DeviceViewModel
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.spatialk.geojson.Feature
import org.maplibre.spatialk.geojson.FeatureCollection
import org.maplibre.spatialk.geojson.Point
import org.maplibre.spatialk.geojson.Position
import org.maplibre.spatialk.geojson.dsl.featureCollectionOf
import org.maplibre.spatialk.geojson.toJson

@Composable
fun MapContent(
    modifier: Modifier = Modifier,
    deviceViewModel: DeviceViewModel,
) {
    val gpsLatitude by deviceViewModel.latitudeState.collectAsStateWithLifecycle()
    val gpsLongitude by deviceViewModel.longitudeState.collectAsStateWithLifecycle()
    val cameraState = rememberCameraState(
        CameraPosition(
            target = Position(
                gpsLongitude?.toDouble() ?: 0.0,
                gpsLatitude?.toDouble() ?: 0.0,
            ),
            zoom = 16.0,
            tilt = 45.0,
        )
    )
    val styleState = rememberStyleState()

    Box(
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
            .clip(CutCornerShape(10.dp)),
    ) {
        MaplibreMap(
            baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty"),
            cameraState = cameraState,
            styleState = styleState,
            options = MapOptions(
                ornamentOptions = OrnamentOptions.OnlyLogo,
                gestureOptions = GestureOptions.AllDisabled
            ),
        ) {
            var data by remember { mutableStateOf(featureCollectionOf().toJson()) }
            val altitude by deviceViewModel.altitudeState.collectAsStateWithLifecycle()
            val accelerationDirection by deviceViewModel.accelerationDirectionState.collectAsStateWithLifecycle()

            val dirSector = ((accelerationDirection?.toInt() ?: 0) % 8 + 8) % 8
            val dirDeg = dirSector * 45f

            LaunchedEffect(gpsLatitude, gpsLongitude, altitude, accelerationDirection) {
                if (gpsLatitude == null || gpsLongitude == null || altitude == null) {
                    return@LaunchedEffect
                }
                cameraState.animateTo(
                    CameraPosition(
                        bearing = dirDeg.toDouble(),
                        target = Position(
                            gpsLongitude!!.toDouble(),
                            gpsLatitude!!.toDouble(),
                        ),
                        zoom = 16.0,
                        tilt = 45.0,
                    )
                )
                data = FeatureCollection(
                    Feature(
                        geometry = Point(
                            gpsLongitude!!.toDouble(),
                            gpsLatitude!!.toDouble() ,
                            altitude!!.toDouble(),
                        ),
                        properties = JsonObject(emptyMap()),
                    )
                ).toJson()
            }

            val gpsSource = rememberGeoJsonSource(
                GeoJsonData.JsonString(data),
            )

            val marker = rememberVectorPainter(Icons.Filled.Navigation)

            SymbolLayer(
                id = "gps_symbols",
                source = gpsSource,
                iconImage = image(marker),
            )
        }
    }
}
