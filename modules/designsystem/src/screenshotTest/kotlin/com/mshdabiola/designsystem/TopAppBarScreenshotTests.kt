/*
 *abiola 2024
 */

package com.mshdabiola.designsystem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mshdabiola.designsystem.component.WcsTopAppBar
import com.mshdabiola.designsystem.icon.WcsIcons
import com.mshdabiola.testing.util.CaptureMultiTheme

class TopAppBarScreenshotTests() {

    @Preview
    @Composable
    fun TopAppBar() {
        CaptureMultiTheme {
            WcsTopAppBarExample()
        }
    }

    @Preview(fontScale = 2.0f)
    @Composable
    fun TopAppBarHumFontScale2() {
        CaptureMultiTheme {
            WcsTopAppBarExample()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun WcsTopAppBarExample() {
        WcsTopAppBar(
            titleRes = "untitled",
            navigationIcon = WcsIcons.Search,
            navigationIconContentDescription = "Navigation icon",
            actionIcon = WcsIcons.MoreVert,
            actionIconContentDescription = "Action icon",
        )
    }
}
