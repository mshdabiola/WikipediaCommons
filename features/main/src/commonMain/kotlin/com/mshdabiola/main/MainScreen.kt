/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mshdabiola.designsystem.component.WcsButton
import com.mshdabiola.designsystem.component.WcsLoadingWheel
import com.mshdabiola.designsystem.icon.WcsIcons
import com.mshdabiola.designsystem.theme.LocalTintTheme
import com.mshdabiola.designsystem.theme.WcsTheme
import com.mshdabiola.model.MainImage
import com.mshdabiola.ui.SharedContentPreview
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import io.github.ahmad_hamwi.compose.pagination.rememberPaginationState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import wikipediacommons.features.main.generated.resources.Res
import wikipediacommons.features.main.generated.resources.features_main_empty_description
import wikipediacommons.features.main.generated.resources.features_main_empty_error
import wikipediacommons.features.main.generated.resources.features_main_img_empty_bookmarks
import wikipediacommons.features.main.generated.resources.features_main_loading
import wikipediacommons.features.main.generated.resources.features_main_photo

// import org.koin.androidx.compose.koinViewModel

@OptIn(KoinExperimentalAPI::class, ExperimentalSharedTransitionApi::class)
@Composable
internal fun MainRoute(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    navigateToDetail: (String) -> Unit,
    showSnackbar: suspend (String, String?) -> Boolean,
    navigateToSearch: () -> Unit,
//    viewModel: MainViewModel,
) {
    val viewModel: MainViewModel = koinViewModel()
    val coroutine = rememberCoroutineScope()

    val bookmarkedIds = viewModel.bookmarkSet.collectAsStateWithLifecycle()

    MainScreen(
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        modifier = modifier,
        paginationState = viewModel.paginationState,
        bookmarkedIds = bookmarkedIds.value,
        onImageClick = navigateToDetail,
        onBookmarkClick = {
            viewModel.toggleBookmark(it)
            coroutine.launch {
                showSnackbar("Bookmarked", null)
            }
        },
        onSearchClick = navigateToSearch,
        onMenuClick = {},
    )
}

@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun MainScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedVisibilityScope,
    paginationState: PaginationState<Int, MainImage>,
    bookmarkedIds: Set<String> = emptySet(),
    onImageClick: (String) -> Unit = {},
    onBookmarkClick: (String) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .testTag("main:root"),
    ) {
        TopAppBar(
            title = { Text("Wikipedia Commons") },
            navigationIcon = {
                IconButton(onClick = onMenuClick) {
                    Icon(WcsIcons.Menu, contentDescription = "Menu")
                }
            },
            actions = {
                with(sharedTransitionScope) {
                    IconButton(
                        modifier =
                            Modifier.sharedBounds(
                                sharedContentState = rememberSharedContentState("search"),
                                animatedVisibilityScope = animatedContentScope,
                            ),
                        onClick = onSearchClick,
                    ) {
                        Icon(WcsIcons.Search, contentDescription = "Search")
                    }
                }
            },
        )
        PaginatedLazyColumn(
            modifier =
                Modifier
                    .testTag("main:list")
                    .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            paginationState = paginationState,
            firstPageProgressIndicator = {
                LoadingState()
            },
            newPageProgressIndicator = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    WcsLoadingWheel(
                        contentDesc = stringResource(Res.string.features_main_loading),
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
                    WcsButton(onClick = {
                        paginationState.retryLastFailedRequest()
                    }) {
                        Text("Retry")
                    }
                }
            },
            newPageErrorIndicator = { e ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
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
            firstPageEmptyIndicator = {
                EmptyState()
            },
            newPageEmptyIndicator = {
            },
        ) {
            items(items = paginationState.allItems!!) { image ->
                with(sharedTransitionScope) {
                    ImageCard(
                        modifier =
                            Modifier.sharedBounds(
                                sharedContentState = rememberSharedContentState(image.sha1),
                                animatedVisibilityScope = animatedContentScope,
                            ),
                        image = image,
                        isBookmarked = image.sha1 in bookmarkedIds,
                        onClick = { onImageClick(image.sha1) },
                        onBookmarkClick = { onBookmarkClick(image.sha1) },
                    )
                }
            }
        }
    }
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
fun MainLight() {
    val paginationState =
        rememberPaginationState<Int, MainImage>(
            initialPageKey = 1,
            onRequestPage = {
                this.appendPage(sampleImages, it + 1)
            },
        )
    WcsTheme(darkTheme = false) {
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

// Dummy data for preview and example
val sampleImages =
    listOf(
        MainImage(
            mime = "image/png",
            sha1 = "1",
            title = "Image 1",
            url = "https://example.com/image1.jpg",
            user = "User 1",
        ),
        MainImage(
            mime = "image/png",
            sha1 = "2",
            title = "Image 2",
            url = "https://example.com/image2.jpg",
            user = "User 2",
        ),
        MainImage(
            mime = "image/png",
            sha1 = "3",
            title = "Image 3",
            url = "https://example.com/image3.jpg",
            user = "User 3",
        ),
        MainImage(
            mime = "image/png",
            sha1 = "4",
            title = "Image 4",
            url = "https://example.com/image4.jpg",
            user = "User 4",
        ),
        MainImage(
            mime = "image/png",
            sha1 = "5",
            title = "Image 5",
            url = "https://example.com/image5.jpg",
            user = "User 5",
        ),
        MainImage(
            mime = "image/png",
            sha1 = "6",
            title = "Image 6",
            url = "https://example.com/image6.jpg",
            user = "User 6",
        ),
        MainImage(
            mime = "image/png",
            sha1 = "7",
            title = "Image 7",
            url = "https://example.com/image7.jpg",
            user = "User 7",
        ),
        MainImage(
            mime = "image/png",
            sha1 = "8",
            title = "Image 8",
            url = "https://example.com/image8.jpg",
            user = "User 8",
        ),
        MainImage(
            mime = "image/png",
            sha1 = "9",
            title = "Image 9",
            url = "https://example.com/image9.jpg",
            user = "User 9",
        ),
        MainImage(
            mime = "image/png",
            sha1 = "1",
            title = "Image 10",
            url = "https://example.com/image10.jpg",
            user = "User 10",
        ),
    )

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCard(
    image: MainImage,
    isBookmarked: Boolean,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column {
            AsyncImage(
                model = image.url,
                contentDescription = image.title,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                // Adjust height as needed
                contentScale = ContentScale.Crop,
                placeholder =
                    painterResource(
                        Res.drawable.features_main_photo,
                    ),
                //                error = painterResource(id = R.drawable.ic_launcher_foreground),
                // Replace with your error image
            )
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = image.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = image.user,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onBookmarkClick) {
                    Icon(
                        imageVector =
                            if (isBookmarked) {
                                WcsIcons.Bookmark
                            } else {
                                WcsIcons.BookmarkBorder
                            },
                        contentDescription = "Bookmark",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun ImageCardPreview() {
    WcsTheme {
        ImageCard(
            image = sampleImages.first(),
            isBookmarked = false,
            onClick = { /*TODO*/ },
            onBookmarkClick = { /*TODO*/ },
            modifier = Modifier.padding(16.dp),
        )
    }
}
