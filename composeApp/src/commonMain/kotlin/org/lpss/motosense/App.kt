package org.lpss.motosense

import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.lpss.motosense.ui.NavHandler
import org.lpss.motosense.ui.theme.MotoSenseTheme

@Composable
@Preview
fun App() {
    MotoSenseTheme {
        NavHandler()
    }
}
