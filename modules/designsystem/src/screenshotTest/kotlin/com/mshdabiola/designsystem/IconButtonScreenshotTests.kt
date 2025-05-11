/*
 *abiola 2023
 */

package com.mshdabiola.designsystem

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.mshdabiola.designsystem.component.WcsIconToggleButton
import com.mshdabiola.designsystem.icon.WcsIcons
import com.mshdabiola.testing.util.CaptureMultiTheme

class IconButtonScreenshotTests {

    @Preview
    @Composable
    fun ToggleButton() {
        CaptureMultiTheme {
            WcsIconToggleButton(
                checked = true,
                onCheckedChange = { },
                icon = {
                    Icon(
                        imageVector = WcsIcons.BookmarkBorder,
                        contentDescription = null,
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = WcsIcons.Bookmark,
                        contentDescription = null,
                    )
                },
            )
        }
    }

    @Preview
    @Composable
    fun UnToggleButton() {
        CaptureMultiTheme {
            WcsIconToggleButton(
                checked = false,
                onCheckedChange = { },
                icon = {
                    Icon(
                        imageVector = WcsIcons.BookmarkBorder,
                        contentDescription = null,
                    )
                },
                checkedIcon = {
                    Icon(
                        imageVector = WcsIcons.Bookmark,
                        contentDescription = null,
                    )
                },
            )
        }
    }
}
