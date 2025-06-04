package com.mshdabiola.search

import com.mshdabiola.model.MainImage
import io.github.ahmad_hamwi.compose.pagination.PaginationState

sealed class SearchState {
    data class History(
        val searchHistory: List<String>,
    ) : SearchState()

    data class Results(
        val paginationState: PaginationState<Int, MainImage>,
    ) : SearchState()

    data object Loading : SearchState()
}
