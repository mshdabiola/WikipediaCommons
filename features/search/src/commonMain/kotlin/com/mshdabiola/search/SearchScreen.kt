/*
 *abiola 2022
 */

package com.mshdabiola.search

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mshdabiola.designsystem.component.WcsLoadingWheel
import com.mshdabiola.designsystem.theme.LocalTintTheme
import com.mshdabiola.designsystem.theme.WcsTheme
import com.mshdabiola.ui.SharedContentPreview
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import wikipediacommons.features.search.generated.resources.Res
import wikipediacommons.features.search.generated.resources.features_main_empty_description
import wikipediacommons.features.search.generated.resources.features_main_empty_error
import wikipediacommons.features.search.generated.resources.features_main_img_empty_bookmarks
import wikipediacommons.features.search.generated.resources.features_main_loading

@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class)
@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    navigateToDetail: (Long) -> Unit,
    showSnackbar: suspend (String, String?) -> Boolean,
) {
    val viewModel: SearchViewModel = koinViewModel()

    SearchScreen(
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        modifier = modifier,
    )
}

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
) {
}

@Composable
private fun LoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().testTag("main:loading"),
        contentAlignment = Alignment.Center,
    ) {
        WcsLoadingWheel(
            contentDesc = stringResource(Res.string.features_main_loading),
        )
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .padding(16.dp)
                .fillMaxSize()
                .testTag("main:empty"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val iconTint = LocalTintTheme.current.iconTint
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(Res.drawable.features_main_img_empty_bookmarks),
            colorFilter = if (iconTint != Color.Unspecified) ColorFilter.tint(iconTint) else null,
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = stringResource(Res.string.features_main_empty_error),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(Res.string.features_main_empty_description),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun SearchLight() {
    WcsTheme(darkTheme = false) {
        Surface {
            SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                SearchScreen(
                    modifier = Modifier.fillMaxSize(),
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                )
            }
        }
    }
}
