package com.mshdabiola.data.repository

import com.mshdabiola.model.MainImage
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    val bookmarkSet: Flow<Set<String>>
    val searchHistory: Flow<List<String>>

    suspend fun getAllMedia(
        page: Int,
        limit: Int,
    ): List<MainImage>

    suspend fun toggleBookmark(id: String)

    suspend fun search(
        title: String,
        page: Int,
        limit: Int,
    ): List<MainImage>

    suspend fun addSearchHistory(search: String)
}
