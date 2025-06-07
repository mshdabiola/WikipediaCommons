package com.mshdabiola.network

import com.mshdabiola.model.MainImage
import com.mshdabiola.network.model.MediaListResult // Added import for MediaListResult

interface IMediaDataSource {
    suspend fun getAllImages(
        limit: Int,
        continuation: String = "",
    ): List<MainImage> // This might also need MediaListResult if it supports true pagination

    suspend fun search(
        title: String,
        page: Int,
        limit: Int,
    ): List<MainImage>

    suspend fun checkPageExists(title: String): Boolean

    suspend fun checkFileExistsBySha(sha1: String): Boolean

    suspend fun getMediaListFromCategory(category: String, limit: Int, continuation: String?): List<MainImage>

    suspend fun getMediaListBySearchTerm(searchTerm: String, limit: Int, offset: Int): List<MainImage>

    suspend fun getMediaDetails(title: String): MainImage? // Added this line

}
