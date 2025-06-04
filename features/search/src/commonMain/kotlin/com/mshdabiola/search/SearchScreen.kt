/*
 *abiola 2022
 */

package com.mshdabiola.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.onClick
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mshdabiola.designsystem.component.WcsButton
import com.mshdabiola.designsystem.component.WcsLoadingWheel
import com.mshdabiola.designsystem.component.WcsTextField
import com.mshdabiola.designsystem.icon.WcsIcons
import com.mshdabiola.designsystem.theme.LocalTintTheme
import com.mshdabiola.designsystem.theme.WcsTheme
import com.mshdabiola.model.MainImage
import com.mshdabiola.ui.SharedContentPreview
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.rememberPaginationState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import wikipediacommons.features.search.generated.resources.Res
import wikipediacommons.features.search.generated.resources.abc_action_bar_up_description
import wikipediacommons.features.search.generated.resources.abc_clear_search_api_title
import wikipediacommons.features.search.generated.resources.features_main_empty_description
import wikipediacommons.features.search.generated.resources.features_main_empty_error
import wikipediacommons.features.search.generated.resources.features_main_img_empty_bookmarks
import wikipediacommons.features.search.generated.resources.features_main_loading
import wikipediacommons.features.search.generated.resources.search_clear_history
import wikipediacommons.features.search.generated.resources.search_hint
import wikipediacommons.features.search.generated.resources.search_history_title

@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class)
@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    navigateToDetail: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val viewModel: SearchViewModel = koinViewModel()
    val state = viewModel.searchState.collectAsStateWithLifecycle()

    SearchScreen(
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        modifier = modifier,
        searchState = state.value,
        searchQuery = viewModel.searchQuery,
        onSearchSubmit = viewModel::onSearchSubmit,
        onClearSearch = { viewModel.searchQuery.clearText() },
        onHistoryItemClick = viewModel::onSearchHistory,
        onClearHistory = viewModel::clearSearchHistory,
        onBackClick = onBackClick,
        onSearchItem = navigateToDetail,
    )
}

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    searchState: SearchState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    searchQuery: TextFieldState,
    onSearchSubmit: () -> Unit = {},
    onClearSearch: () -> Unit = {},
    onHistoryItemClick: (String) -> Unit = {},
    onClearHistory: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onSearchItem: (String) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
//    var isSearchActive by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }

    Surface(modifier = modifier.fillMaxSize()) {
        with(sharedTransitionScope) {
            Column(
                modifier =
                    Modifier.fillMaxSize()
                        .sharedBounds(
                            sharedContentState =
                                rememberSharedContentState("search_bar_bounds"),
                            animatedVisibilityScope = animatedContentScope,
                        ),
            ) {
                WcsTextField(
                    state = searchQuery,
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    interactionSource = interactionSource,
                    placeholder = {
                        Text(
                            stringResource(
                                Res.string.search_hint,
                            ),
                        )
                    },
                    leadingIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                WcsIcons.ArrowBack,
                                contentDescription =
                                    stringResource(
                                        Res.string.abc_action_bar_up_description,
                                    ),
                            )
                        }
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = searchQuery.text.isNotEmpty(),
                            enter = fadeIn() + slideInHorizontally { it / 2 },
                            exit = fadeOut() + slideOutHorizontally { it / 2 },
                        ) {
                            IconButton(onClick = onClearSearch) {
                                Icon(
                                    WcsIcons.Close,
                                    contentDescription =
                                        stringResource(
                                            Res.string.abc_clear_search_api_title,
                                        ),
                                ) // Common clear description
                            }
                        }
                    },
                    keyboardOptions =
                        KeyboardOptions(
                            imeAction = ImeAction.Search,
                            showKeyboardOnFocus = true,
                        ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    colors =
                        TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                        ),
                    keyboardActions = { onSearchSubmit() },
                )

                HorizontalDivider()

                when (searchState) {
                    is SearchState.Loading -> {
                        FullLoadingState()
                    }
                    is SearchState.History -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            if (searchState.searchHistory.isNotEmpty()) {
                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text =
                                            stringResource(
                                                Res.string.search_history_title,
                                            ),
                                        style = MaterialTheme.typography.titleSmall,
                                    )
                                    Text(
                                        text =
                                            stringResource(
                                                Res.string.search_clear_history,
                                            ),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.clickable { onClearHistory() },
                                    )
                                }
                            }

                            LazyColumn(
                                modifier = Modifier.weight(1f),
                            ) {
                                if (searchState.searchHistory.isEmpty()) {
                                    item {
                                        FullEmptyState(
                                            modifier = Modifier.padding(top = 32.dp),
                                        )
                                    }
                                } else {
                                    items(
                                        items = searchState.searchHistory,
                                        key = { it },
                                    ) { historyItem ->
                                        ListItem(
                                            headlineContent = { Text(historyItem) },
                                            leadingContent = {
                                                Icon(
                                                    WcsIcons.History,
                                                    contentDescription = null,
                                                )
                                            },
                                            modifier =
                                                Modifier
                                                    .clickable {
                                                        onHistoryItemClick(historyItem)
                                                        keyboardController?.show()
                                                    }
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 16.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                    is SearchState.Results -> {
                        PaginatedLazyColumn(
                            modifier = Modifier.weight(1f),
                            paginationState = searchState.paginationState,
                            firstPageEmptyIndicator = {
                            },
                            firstPageProgressIndicator = {
                                FullLoadingState()
                            },
                            newPageProgressIndicator = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                ) {
                                    WcsLoadingWheel(
                                        contentDesc = "Loading",
                                    )
                                }
                            },
                            firstPageErrorIndicator = { e ->
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement =
                                        Arrangement.spacedBy(
                                            16.dp,
                                            Alignment.CenterVertically,
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Icon(WcsIcons.Info, contentDescription = null)
                                    Text(e.message ?: "Error occur")
                                    WcsButton(
                                        onClick = {
                                            searchState.paginationState.retryLastFailedRequest()
                                        },
                                    ) {
                                        Text("Retry")
                                    }
                                }
                            },
                            newPageErrorIndicator = { e ->
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement =
                                        Arrangement.spacedBy(
                                            8.dp,
                                            Alignment.CenterVertically,
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                ) {
                                    Text(
                                        e.message ?: "Error occur",
                                        maxLines = 1,
                                    )
                                    WcsButton(onClick = {
                                        searchState
                                            .paginationState.retryLastFailedRequest()
                                    }) {
                                        Text("Retry")
                                    }
                                }
                            },
                        ) {
                            items(items = searchState.paginationState.allItems!!) { image ->
                                with(sharedTransitionScope) {
                                    ListItem(
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .clickable { onSearchItem(image.sha1) }
                                                .sharedBounds(
                                                    sharedContentState =
                                                        rememberSharedContentState(image.sha1),
                                                    animatedVisibilityScope = animatedContentScope,
                                                ),
                                        leadingContent = {
                                            AsyncImage(
                                                model = image.url,
                                                contentDescription = image.title,
                                                modifier =
                                                    Modifier
                                                        .size(64.dp),
                                                // Adjust height as needed
                                                contentScale = ContentScale.Crop,
                                            )
                                        },
                                        headlineContent = {
                                            Text(image.title)
                                        },
                                        supportingContent = {
                                            Text(image.user)
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FullLoadingState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().testTag("main:loading"),
        contentAlignment = Alignment.Center,
    ) {
        WcsLoadingWheel(
            contentDesc = stringResource(Res.string.features_main_loading),
        )
    }
}

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
fun FullEmptyState(modifier: Modifier = Modifier) {
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
            painter =
                painterResource(
                    Res.drawable.features_main_img_empty_bookmarks,
                ),
            colorFilter =
                if (iconTint != Color.Unspecified) {
                    ColorFilter.tint(iconTint)
                } else {
                    null
                },
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

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchScreenPreview() {
    val searchState = rememberTextFieldState("Preview Query")

    val state =
        SearchState.History(
            listOf("Kotlin", "Compose", "Android Development"),
        )
    WcsTheme(darkTheme = false) {
        Surface {
            SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                SearchScreen(
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    searchState = state,
                    searchQuery = searchState,
                    onSearchSubmit = { },
                    onClearSearch = { searchState.clearText() },
                    onHistoryItemClick = { query ->
                        searchState.edit {
                            this.replace(
                                0,
                                this.length,
                                query,
                            )
                        }
                    },
                    onClearHistory = {},
                    onBackClick = { println("Back clicked") },
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Preview()
@Composable
internal fun SearchScreenEmptyHistoryPreview() {
    val searchState = rememberTextFieldState(initialText = "")
    // Pagination state that will show its firstPageEmptyIndicator
    // This indicator in your SearchScreen then checks searchHistory.
    val paginationState =
        rememberPaginationState<Int, MainImage>(
            initialPageKey = 1,
            onRequestPage = {
            },
        )

    WcsTheme {
        Surface {
            SharedContentPreview { sharedTransitionScope, animatedVisibilityScope ->
                SearchScreen(
                    modifier = Modifier,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedVisibilityScope,
                    searchState = SearchState.History(emptyList()),
                    searchQuery = searchState,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Preview()
@Composable
internal fun SearchScreenWithHistoryListPreview() {
    val searchState = rememberTextFieldState(initialText = "")

    WcsTheme {
        Surface {
            SharedContentPreview { sharedTransitionScope, animatedVisibilityScope ->
                SearchScreen(
                    modifier = Modifier,
                    searchState =
                        SearchState.History(
                            listOf(
                                "Nature",
                                "Technology",
                                "Art",
                                "Compose Multiplatform",
                            ),
                        ),
                    searchQuery = searchState,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedVisibilityScope,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Preview()
@Composable
internal fun SearchScreenEmptyMainImagePreview() {
    val searchState = rememberTextFieldState(initialText = "QueryThatYieldsNoResults")

    WcsTheme {
        Surface {
            SharedContentPreview { sharedTransitionScope, animatedVisibilityScope ->
                SearchScreen(
                    modifier = Modifier,
                    searchState =
                        SearchState.Results(
                            paginationState =
                                rememberPaginationState(
                                    initialPageKey = 1,
                                    onRequestPage = {
                                        appendPage(emptyList(), 1)
                                    },
                                ),
                        ),
                    searchQuery = searchState,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedVisibilityScope,
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Preview()
@Composable
internal fun SearchScreenWithMainImageListPreview() {
    val searchState = rememberTextFieldState(initialText = "Photos")
    val sampleImages =
        listOf(
            MainImage(
                title = "Beautiful Landscape",
                mime = "image/jpeg",
                sha1 = "abc",
                url = "url1",
                user = "UserA",
            ),
            MainImage(
                title = "City Skyline",
                mime = "image/png",
                sha1 = "def",
                url = "url2",
                user = "UserB",
            ),
            MainImage(
                title = "Abstract Art",
                mime = "image/jpeg",
                sha1 = "ghi",
                url = "url3",
                user = "UserC",
            ),
        )
    val paginationState =
        rememberPaginationState(
            initialPageKey = 1,
            onRequestPage = { currentPage ->
                appendPage(sampleImages, 1)
            },
        )

    WcsTheme {
        Surface {
            SharedContentPreview { sharedTransitionScope, animatedVisibilityScope ->
                SearchScreen(
                    modifier = Modifier,
                    searchState =
                        SearchState.Results(
                            paginationState = paginationState,
                        ),
                    searchQuery = searchState,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedVisibilityScope,
                )
            }
        }
    }
}
