/*
 *abiola 2022
 */

package com.mshdabiola.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.data.model.Result
import com.mshdabiola.data.model.asResult
import com.mshdabiola.data.repository.IMediaRepository
import com.mshdabiola.data.repository.NoteRepository
import com.mshdabiola.model.MainImage
import com.mshdabiola.model.Note
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    modelRepository: NoteRepository,
    private val mediaRepository: IMediaRepository,
) : ViewModel() {
    val bookmarkSet: StateFlow<Set<String>> =
        mediaRepository
            .bookmarkSet
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet(),
            )

    val paginationState =
        PaginationState<Int, MainImage>(
            initialPageKey = 1,
            onRequestPage = { loadPage(it) },
        )

    fun loadPage(pageKey: Int) {
        viewModelScope.launch {
            try {
                // val page = DataSource.getPage(pageNumber = pageKey, pageSize = 10)

//                paginationState.appendPage(
//                    items = page.items,
//                    isLastPage = page.isLastPage
//                )

                // delay(2000)
                paginationState.appendPage(mediaRepository.getAllMedia(pageKey, 10), pageKey + 1)

                paginationState.appendPage(emptyList(), pageKey + 1)
                // paginationState.appendPage(List(20){ Note(title = "Title ${pageKey.times(it)}")},pageKey+1)
            } catch (e: Exception) {
                paginationState.setError(e)
            }
        }
    }

    fun toggleBookmark(id: String) {
        viewModelScope.launch {
            mediaRepository.toggleBookmark(id)
        }
    }

    val notes: StateFlow<Result<List<Note>>> =
        modelRepository.getAll()
            .asResult()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = Result.Loading,
            )
}
