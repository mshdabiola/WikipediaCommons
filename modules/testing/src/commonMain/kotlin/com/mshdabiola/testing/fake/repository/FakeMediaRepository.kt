package com.mshdabiola.testing.fake.repository

import com.mshdabiola.data.repository.IMediaRepository
import com.mshdabiola.model.MainImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeMediaRepository : IMediaRepository {
    override val bookmarkSet: Flow<Set<String>>
        get() = flowOf(emptySet())
    override val searchHistory: Flow<List<String>>
        get() = flowOf(emptyList())

    override suspend fun getAllMedia(
        page: Int,
        limit: Int,
    ): List<MainImage> {
        return emptyList()
    }

    override suspend fun toggleBookmark(id: String) {
        print(id)
    }

    override suspend fun search(
        title: String,
        page: Int,
        limit: Int,
    ): List<MainImage> {
        return emptyList()
    }

    override suspend fun addSearchHistory(search: String) {
        print(search)
    }

    override suspend fun clearSearchHistory() {
        println("clear search")
    }
}
