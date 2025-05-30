/*
 *abiola 2022
 */

package com.mshdabiola.search.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.mshdabiola.search.SearchRoute

fun NavController.navigateToSearch(
    main: Search,
    navOptions: NavOptions,
) = navigate(main, navOptions)

@OptIn(ExperimentalSharedTransitionApi::class)
fun NavGraphBuilder.searchScreen(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    onShowSnack: suspend (String, String?) -> Boolean,
    navigateToDetail: (Long) -> Unit,
) {
    composable<Search> {
        SearchRoute(
            modifier = modifier,
            sharedTransitionScope = sharedTransitionScope,
            animatedContentScope = this,
            showSnackbar = onShowSnack,
            navigateToDetail = navigateToDetail,
        )
    }
}
