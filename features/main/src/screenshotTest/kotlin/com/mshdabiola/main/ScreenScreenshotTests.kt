/*
 *abiola 2023
 */

package com.mshdabiola.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mshdabiola.data.model.Result
import com.mshdabiola.designsystem.DevicePreviews
import com.mshdabiola.designsystem.theme.darkDefaultScheme
import com.mshdabiola.designsystem.theme.lightDefaultScheme
import com.mshdabiola.model.MainImage
import com.mshdabiola.testing.fake.notes
import io.github.ahmad_hamwi.compose.pagination.rememberPaginationState

import com.mshdabiola.ui.SharedContentPreview

class ScreenScreenshotTests {

    @OptIn(ExperimentalSharedTransitionApi::class)
    @DevicePreviews
    @Composable
    fun LoadingLight() {
        val paginationState =
            rememberPaginationState<Int, MainImage>(
                initialPageKey = 1,
                onRequestPage = {
                    this.appendPage(sampleImages, it + 1)
                },
            )
        MaterialTheme(colorScheme = lightDefaultScheme) {
            Surface {
                SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                    MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        paginationState = paginationState,
                        onImageClick = {},
                        onBookmarkClick = {},
                        onSearchClick = {},
                        onMenuClick = { },
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @DevicePreviews
    @Composable
    fun LoadingDark() {
        val paginationState =
            rememberPaginationState<Int, MainImage>(
                initialPageKey = 1,
                onRequestPage = {
                    this.appendPage(sampleImages, it + 1)
                },
            )
        MaterialTheme(colorScheme = darkDefaultScheme) {
            Surface {
                SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                    MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        paginationState = paginationState,
                        onImageClick = {},
                        onBookmarkClick = {},
                        onSearchClick = {},
                        onMenuClick = { },
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @DevicePreviews
    @Composable
    fun MainLight() {
        val paginationState =
            rememberPaginationState<Int, MainImage>(
                initialPageKey = 1,
                onRequestPage = {
                    this.appendPage(sampleImages, it + 1)
                },
            )
        MaterialTheme(colorScheme = lightDefaultScheme) {
            Surface {
                SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                    MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        paginationState = paginationState,
                        onImageClick = {},
                        onBookmarkClick = {},
                        onSearchClick = {},
                        onMenuClick = { },
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @DevicePreviews
    @Composable
    fun MainDark() {
        val paginationState =
            rememberPaginationState<Int, MainImage>(
                initialPageKey = 1,
                onRequestPage = {
                    this.appendPage(sampleImages, it + 1)
                },
            )
        MaterialTheme(colorScheme = darkDefaultScheme) {
            Surface {
                SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                    MainScreen(
                        modifier = Modifier.fillMaxSize(),
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        paginationState = paginationState,
                        onImageClick = {},
                        onBookmarkClick = {},
                        onSearchClick = {},
                        onMenuClick = { },
                    )
                }
            }
        }
    }
}
