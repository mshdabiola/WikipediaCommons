package com.mshdabiola.data.repository

import com.mshdabiola.model.MainImage
import kotlinx.coroutines.flow.Flow

interface IMediaRepository {
    val bookmarkSet: Flow<Set<String>>

    suspend fun getAllMedia(
        page: Int,
        limit: Int,
    ): List<MainImage>

    suspend fun toggleBookmark(id: String)
}
