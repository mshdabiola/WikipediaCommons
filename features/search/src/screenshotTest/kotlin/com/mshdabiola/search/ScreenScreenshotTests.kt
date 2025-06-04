/*
 *abiola 2023
 */

package com.mshdabiola.search

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mshdabiola.designsystem.DevicePreviews
import com.mshdabiola.designsystem.theme.darkDefaultScheme
import com.mshdabiola.designsystem.theme.lightDefaultScheme
import com.mshdabiola.ui.SharedContentPreview

class ScreenScreenshotTests {

    @OptIn(ExperimentalSharedTransitionApi::class)
    @DevicePreviews
    @Composable
    fun LoadingLight() {

        MaterialTheme(colorScheme = lightDefaultScheme) {
            Surface {
                SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                    SearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        searchState = SearchState.Loading,
                        searchQuery = rememberTextFieldState(),
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @DevicePreviews
    @Composable
    fun LoadingDark() {

        MaterialTheme(colorScheme = darkDefaultScheme) {
            Surface {
                SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                    SearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        searchState = SearchState.Loading,
                        searchQuery = rememberTextFieldState(),
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @DevicePreviews
    @Composable
    fun MainLight() {

        MaterialTheme(colorScheme = lightDefaultScheme) {
            Surface {
                SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                    SearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        searchState = SearchState.Loading,
                        searchQuery = rememberTextFieldState(),
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @DevicePreviews
    @Composable
    fun MainDark() {

        MaterialTheme(colorScheme = darkDefaultScheme) {
            Surface {
                SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                    SearchScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        searchState = SearchState.Loading,
                        searchQuery = rememberTextFieldState(),
                    )
                }
            }
        }
    }
}
