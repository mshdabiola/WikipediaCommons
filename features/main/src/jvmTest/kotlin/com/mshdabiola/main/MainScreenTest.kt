package com.mshdabiola.main

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import com.mshdabiola.data.model.Result
import com.mshdabiola.model.MainImage
import com.mshdabiola.testing.fake.notes
import com.mshdabiola.ui.SharedContentPreview
import io.github.ahmad_hamwi.compose.pagination.rememberPaginationState
import org.junit.Rule
import kotlin.test.Test

class MainScreenTest {
    @get:Rule
    val composeRule = createComposeRule()

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Test
    fun main() {
        composeRule.setContent {
            val paginationState =
                rememberPaginationState<Int, MainImage>(
                    initialPageKey = 1,
                    onRequestPage = {
                        this.appendPage(sampleImages, it + 1)
                    },
                )

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

//        composeRule.onNodeWithTag("main:screen").assertExists()
//        composeRule.onNodeWithTag("main:list").assertExists()
    }
}
