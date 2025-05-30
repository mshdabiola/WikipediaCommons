/*
 *abiola 2022
 */

package com.mshdabiola.search

import androidx.compose.foundation.text.input.TextFieldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.repository.IMediaRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    mediaRepository: IMediaRepository,
) : ViewModel() {
    val search = TextFieldState()
    val searchHistory =
        mediaRepository.searchHistory
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
}
