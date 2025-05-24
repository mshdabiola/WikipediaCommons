package com.mshdabiola.network

import com.mshdabiola.model.MainImage
import com.mshdabiola.network.model.Page

interface IMediaDataSource {

    suspend fun getAllImages(
        limit: Int,
        continuation: String="",
    ): List<MainImage>
}