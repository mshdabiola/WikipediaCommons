package com.mshdabiola.network

import com.mshdabiola.model.MainImage

interface IMediaDataSource {
    suspend fun getAllImages(
        limit: Int,
        continuation: String = "",
    ): List<MainImage>
}
