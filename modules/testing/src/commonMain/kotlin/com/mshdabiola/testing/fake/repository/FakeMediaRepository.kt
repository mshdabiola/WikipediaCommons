package com.mshdabiola.testing.fake.repository

import com.mshdabiola.data.repository.IMediaRepository
import com.mshdabiola.model.MainImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMediaRepository : IMediaRepository {
    override val bookmarkSet: Flow<Set<String>>
        get() = flowOf(emptySet())

    override suspend fun getAllMedia(
        page: Int,
        limit: Int,
    ): List<MainImage> {
        return emptyList()
    }

    override suspend fun toggleBookmark(id: String) {
    }
}
