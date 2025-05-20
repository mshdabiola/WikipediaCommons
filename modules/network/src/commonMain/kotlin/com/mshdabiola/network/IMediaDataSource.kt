package com.mshdabiola.network

import com.mshdabiola.network.model.AllImageReponse
import com.mshdabiola.network.model.Page

interface IMediaDataSource {

    suspend fun getAllImages(
        limit: Int,
        continuation: String="",
    ): List<Page>
}