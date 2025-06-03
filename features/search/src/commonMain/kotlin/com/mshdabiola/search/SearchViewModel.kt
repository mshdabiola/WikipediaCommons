/*
 *abiola 2022
 */

package com.mshdabiola.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.IMediaRepository
import com.mshdabiola.model.MainImage
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    mediaRepository: IMediaRepository,
) : ViewModel() {


    val paginationState = PaginationState<Int, MainImage>(
        initialPageKey = 1,
        onRequestPage = {}
    )
    fun addToSearchHistory(query: String) {

    }

    fun clearSearchHistory(){

    }

    val search = TextFieldState()
    val searchHistory =
        mediaRepository.searchHistory
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
}
