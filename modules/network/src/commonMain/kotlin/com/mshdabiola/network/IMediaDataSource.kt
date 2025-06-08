package com.mshdabiola.network

import com.mshdabiola.model.MainImage

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

        suspend fun getMediaListFromGeoSearch(
            latitude: Double,
            longitude: Double,
            limit: Int,
            radius: Int,
            continuation: String?, // Or ggscontinue: String?
        ): List<MainImage> // Or your GeoSearchResult type
    suspend fun getMedia(title: String): MainImage?
    suspend fun getMediaSuppressErrors(title: String): MainImage?
    suspend fun getMediaById(pageId: Long): MainImage?
    suspend fun fetchImageForDepicted(
        searchTerm: String,
        limit: Int,
        offset: Int
    ): List<MainImage>

    suspend fun getEntity(ids: String): WikiDataEntityResponse? // Or a more specific, domain-mapped type
    suspend fun getWikiText(titles: String): String? // Or WikiTextRevision?



}
