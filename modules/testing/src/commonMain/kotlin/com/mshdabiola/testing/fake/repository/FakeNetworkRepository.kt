package com.mshdabiola.testing.fake.repository

import com.mshdabiola.data.repository.IMediaRepository
import com.mshdabiola.model.MainImage

class FakeNetworkRepository : IMediaRepository {
    override suspend fun getAllMedia(
        page: Int,
        limit: Int,
    ): List<MainImage> {
        TODO("Not yet implemented")
    }
}
