/*
 *abiola 2023
 */

package com.mshdabiola.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mshdabiola.designsystem.component.WcsLoadingWheel
import com.mshdabiola.designsystem.component.WcsOverlayLoadingWheel
import com.mshdabiola.testing.util.CaptureMultiTheme

class LoadingWheelScreenshotTests {

    @Preview
    @Composable
    fun LoadingWheel() {
        CaptureMultiTheme {
            WcsLoadingWheel(contentDesc = "test")
        }
    }

    @Preview
    @Composable
    fun OverlayLoadingWheel() {
        CaptureMultiTheme {
            WcsOverlayLoadingWheel(contentDesc = "test")
        }
    }
}
