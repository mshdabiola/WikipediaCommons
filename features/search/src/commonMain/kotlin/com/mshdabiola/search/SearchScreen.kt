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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
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
import io.github.ahmad_hamwi.compose.pagination.PaginationState
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
import wikipediacommons.features.search.generated.resources.search_history_empty
import wikipediacommons.features.search.generated.resources.search_history_title

@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class)
@Composable
internal fun SearchRoute(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    navigateToDetail: (String) -> Unit, // This is executed when a search is submitted
    onBackClick: () -> Unit,
    back: () -> Unit,
) {
    val viewModel: SearchViewModel = koinViewModel()
    val searchHistory by viewModel.searchHistory.collectAsStateWithLifecycle()

    SearchScreen(
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        modifier = modifier,
        paginationState = viewModel.paginationState,
        searchState = viewModel.search, // Assuming your ViewModel holds TextFieldState
        searchHistory = searchHistory,
        onSearchSubmit = { query ->
            if (query.isNotBlank()) {
                viewModel.addToSearchHistory(query) // Add to history before navigating
                navigateToDetail(query)
            }
        },
        onClearSearch = { viewModel.search.clearText() },
        onHistoryItemClick = { historyQuery ->
            viewModel.search.edit {
                this.replace(
                    0,
                    this.length,
                    historyQuery,
                )
            } // Update TextFieldState
            // Optionally, you could also immediately trigger search:
            // viewModel.addToSearchHistory(historyQuery)
            // navigateToDetail(historyQuery)
        },
        onClearHistory = viewModel::clearSearchHistory, // Assuming ViewModel has this
        onBackClick = onBackClick,
        back = back,
    )
}

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class, // Needed for ListItem, etc.
)
@Composable
internal fun SearchScreen(
    modifier: Modifier = Modifier,
    paginationState: PaginationState<Int, MainImage>,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    searchState: TextFieldState,
    searchHistory: List<String>,
    onSearchSubmit: (String) -> Unit,
    onClearSearch: () -> Unit,
    onHistoryItemClick: (String) -> Unit,
    onClearHistory: () -> Unit,
    onBackClick: () -> Unit,
    back: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var isSearchActive by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }



    Surface(modifier = modifier.fillMaxSize()) {
        with(sharedTransitionScope) {
            Column(
                modifier = Modifier.fillMaxSize()
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState("search_bar_bounds"),
                        animatedVisibilityScope = animatedContentScope,
                    ),
            ) {
                WcsTextField(
                    state = searchState,
                    modifier = Modifier
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
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(
                                    Res.string.abc_action_bar_up_description,
                                ),
                            ) // Use a common back arrow description
                        }

                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = searchState.text.isNotEmpty() && isSearchActive,
                            enter = fadeIn() + slideInHorizontally { it / 2 },
                            exit = fadeOut() + slideOutHorizontally { it / 2 },
                        ) {
                            IconButton(onClick = onClearSearch) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription =
                                        stringResource(
                                            Res.string.abc_clear_search_api_title,
                                        ),
                                ) // Common clear description
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search,
                        showKeyboardOnFocus = true,
                    ),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    ),
                    keyboardActions = { onSearchSubmit(searchState.text.toString()) },
                )

                HorizontalDivider()

                PaginatedLazyColumn(
                    modifier = Modifier.weight(1f),
                    paginationState = paginationState,
                    firstPageEmptyIndicator = {
                        Column(modifier = Modifier.fillMaxSize()) {

                            if (searchHistory.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    Text(
                                        text = stringResource(Res.string.search_history_title), // Add "Search History" to resources
                                        style = MaterialTheme.typography.titleSmall,
                                    )
                                    Text(
                                        text = stringResource(Res.string.search_clear_history),
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.clickable { onClearHistory() },
                                    )
                                }
                            }


                            LazyColumn(
                                modifier = Modifier.weight(1f),
                            )
                            {
                                if (searchHistory.isEmpty()) {
                                    item {
                                        FullEmptyState(

                                            modifier = Modifier.padding(top = 32.dp),
                                        )
                                    }
                                } else {
                                    items(items = searchHistory, key = { it }) { historyItem ->
                                        ListItem(
                                            headlineContent = { Text(historyItem) },
                                            leadingContent = {
                                                Icon(
                                                    Icons.Default.History,
                                                    contentDescription = null,
                                                )
                                            },
                                            modifier = Modifier
                                                .clickable {
                                                    onHistoryItemClick(historyItem)
                                                    isSearchActive = true // Keep search active
                                                    keyboardController?.show() // Optionally show keyboard
                                                    //   focusRequester.requestFocus()
                                                }
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp),
                                        )
                                    }
                                }
                            }
                        }

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
                                    paginationState.retryLastFailedRequest()
                                },
                            ) {
                                Text("Retry")
                            }
                        }
                    },
                    newPageErrorIndicator = { e ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterVertically,
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                e.message ?: "Error occur",
                                maxLines = 1,
                            )
                            WcsButton(onClick = { paginationState.retryLastFailedRequest() }) {
                                Text("Retry")
                            }
                        }
                    },

                    ) {
                    item {
                        Text("Search Results")
                    }
                    items(items = paginationState.allItems!!) { image ->
                        with(sharedTransitionScope) {
                            ListItem(
                                modifier = Modifier.fillMaxWidth().sharedBounds(
                                    sharedContentState = rememberSharedContentState(image.sha1),
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
//                                        placeholder =
//                                            painterResource(
//                                                wikipediacommons.features.main.generated.resources.Res.drawable.features_main_photo,
//                                            ),

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

@Composable
fun FullLoadingState(modifier: Modifier = Modifier) { // Renamed to avoid conflict
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
@Preview
@Composable
internal fun SearchScreenPreview2() {
    WcsTheme {
        SharedContentPreview { sharedTransitionScope, animatedVisibilityScope ->
            SearchScreen(
                modifier = Modifier,
                paginationState = rememberPaginationState(
                    initialPageKey = 1,
                    onRequestPage = {
                        appendPage(listOf(
                            MainImage(
                                title = "Image 1",
                                mime = "image/jpeg",
                                sha1 = "sha1_1",
                                url = "https://example.com/image1.jpg",
                                user = "User 1"
                            ),
                            MainImage(
                                title = "Image 2",
                                mime = "image/png",
                                sha1 = "sha1_2",
                                url = "https://example.com/image2.png",
                                user = "User 2"
                            )
                        ), 2)
                    }
                ),
                sharedTransitionScope = sharedTransitionScope,
                animatedContentScope = animatedVisibilityScope,
                searchState = rememberTextFieldState(initialText = "Search Query"),
                searchHistory = listOf("History 1", "History 2"),
                onSearchSubmit = {},
                onClearSearch = {},
                onHistoryItemClick = {},
                onClearHistory = {},
                onBackClick = {},
                back = {}
            )
        }
    }
}

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
            painter = painterResource(
                Res.drawable.features_main_img_empty_bookmarks,
            ),
            colorFilter = if (iconTint != Color.Unspecified)
                ColorFilter.tint(iconTint) else null,
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
    val history = remember { mutableStateOf(listOf("Kotlin", "Compose", "Android Development")) }

    val pagerState = rememberPaginationState<Int, MainImage>(
        initialPageKey = 1,
        onRequestPage = {
            appendPage(emptyList(), 2)
        },
    )
    WcsTheme(darkTheme = false) {
        Surface {
            SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                SearchScreen(
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    searchState = searchState,
                    paginationState = pagerState,
                    searchHistory = history.value,
                    onSearchSubmit = { query -> println("Search submitted: $query") },
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
                    onClearHistory = { history.value = emptyList() },
                    onBackClick = { println("Back clicked") },
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SearchScreenEmptyHistoryPreview() {
    val searchState = rememberTextFieldState()
    val history = remember { mutableStateOf<List<String>>(emptyList()) }
    val pagerState = rememberPaginationState<Int, MainImage>(
        initialPageKey = 1,
        onRequestPage = {},
    )
    WcsTheme(darkTheme = false) {
        Surface {
            SharedContentPreview { sharedTransitionScope, animatedContentScope ->
                SearchScreen(
                    sharedTransitionScope = sharedTransitionScope,
                    animatedContentScope = animatedContentScope,
                    searchState = searchState,
                    paginationState = pagerState,
                    searchHistory = history.value,
                    onSearchSubmit = { query -> println("Search submitted: $query") },
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
                    onClearHistory = { history.value = emptyList() },
                    onBackClick = { println("Back clicked") },
                )
            }
        }
    }
}