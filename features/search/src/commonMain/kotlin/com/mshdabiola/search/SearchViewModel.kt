/*
 *abiola 2022
 */

package com.mshdabiola.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.IMediaRepository
import com.mshdabiola.model.MainImage
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val mediaRepository: IMediaRepository,
) : ViewModel() {
    val searchQuery = TextFieldState()
    private val _searchState = MutableStateFlow<SearchState>(SearchState.Loading)
    val searchState = _searchState.asStateFlow()

    init {
        viewModelScope.launch {
            val sh = mediaRepository.searchHistory.firstOrNull() ?: emptyList()
            _searchState.update { SearchState.History(sh) }
        }
        viewModelScope.launch {
            snapshotFlow { searchQuery.text }
                .debounce(500)
                .collectLatest { text ->
                    if (text.isEmpty()) {
                        viewModelScope.launch {
                            val sh = mediaRepository.searchHistory.firstOrNull() ?: emptyList()
                            _searchState.update { SearchState.History(sh) }
                        }
                    }
                }
        }
    }

    private fun load(
        paginationState: PaginationState<Int, MainImage>,
        page: Int,
    ) {
        viewModelScope.launch {
            val search = mediaRepository.search(searchQuery.text.toString(), page, 8)
            paginationState.appendPage(search, page + 1)
        }
    }

    fun onSearchHistory(query: String) {
        searchQuery.clearText()
        searchQuery.edit {
            append(query)
        }
        onSearchSubmit()
    }

    fun onSearchSubmit() {
        _searchState.update {
            SearchState.Results(
                paginationState =
                    PaginationState(
                        initialPageKey = 1,
                        onRequestPage = { page ->
                            load(this, page)
                        },
                    ),
            )
        }
        viewModelScope.launch {
            mediaRepository.addSearchHistory(searchQuery.text.toString())
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            mediaRepository.clearSearchHistory()
            _searchState.update {
                SearchState.History(emptyList())
            }
        }
    }
}
