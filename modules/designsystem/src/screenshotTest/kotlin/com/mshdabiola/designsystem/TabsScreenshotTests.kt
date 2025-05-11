/*
 *abiola 2023
 */

package com.mshdabiola.designsystem

import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mshdabiola.designsystem.component.WcsTab
import com.mshdabiola.designsystem.component.WcsTabRow
import com.mshdabiola.testing.util.CaptureMultiTheme

class TabsScreenshotTests {

    @Preview
    @Composable
    fun Tabs() {
        CaptureMultiTheme {
            WcsTabsExample()
        }
    }

    @Preview
    @Preview(fontScale = 2.0f)
    @Composable
    fun TabsHumFontScale2() {
        CaptureMultiTheme {
            WcsTabsExample("Looooong item")
        }
    }

    @Composable
    private fun WcsTabsExample(label: String = "Topics") {
        Surface {
            val titles = listOf(label, "People")
            WcsTabRow(selectedTabIndex = 0) {
                titles.forEachIndexed { index, title ->
                    WcsTab(
                        selected = index == 0,
                        onClick = { },
                        text = { Text(text = title) },
                    )
                }
            }
        }
    }
}
