package com.mshdabiola.network

import com.mshdabiola.model.MainImage

interface IMediaDataSource {
    suspend fun getAllImages(
        limit: Int,
        continuation: String = "",
    ): List<MainImage>

    suspend fun search(
        title: String,
        page: Int,
        limit: Int,
    ): List<MainImage>

    suspend fun checkPageExists(title: String): Boolean
}
