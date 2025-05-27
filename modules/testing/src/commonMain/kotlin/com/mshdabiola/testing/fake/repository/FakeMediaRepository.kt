package com.mshdabiola.testing.fake.repository

import com.mshdabiola.data.repository.IMediaRepository
import com.mshdabiola.model.MainImage
import kotlinx.coroutines.flow.Flow

class FakeMediaRepository : IMediaRepository {
    override val bookmarkSet: Flow<Set<String>>
        get() = TODO("Not yet implemented")

    override suspend fun getAllMedia(
        page: Int,
        limit: Int,
    ): List<MainImage> {
        TODO("Not yet implemented")
    }

    override suspend fun toggleBookmark(id: String) {
        TODO("Not yet implemented")
    }
}
