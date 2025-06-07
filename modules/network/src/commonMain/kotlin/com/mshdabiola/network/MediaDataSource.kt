package com.mshdabiola.network

import com.mshdabiola.model.MainImage
import com.mshdabiola.network.model.AllImageResponse
import com.mshdabiola.network.model.AllSearchResponse
import com.mshdabiola.network.model.FileExistenceShaResponse
import com.mshdabiola.network.model.PageExistenceResponse
import com.mshdabiola.network.model.toMainImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import io.ktor.http.parametersOf
import io.ktor.http.plus
import kotlinx.io.IOException

internal class MediaDataSource(
    private val client: HttpClient,
) : IMediaDataSource {
    var cont = ""

    override suspend fun getAllImages(
        limit: Int,
        continuation: String,
    ): List<MainImage> {
        try {
            var parameter =
                parametersOf(
                    "action" to listOf("query"),
                    "format" to listOf("json"),
                    "formatversion" to listOf("2"),
                    "generator" to listOf("random"),
                    "prop" to listOf("imageinfo"),
                    "iiprop" to listOf("user|url|mime|canonicaltitle|sha1"),
                    "iilimit" to listOf("6"),
                    "grnlimit" to listOf(limit.toString()),
                    "continue" to listOf("grncontinue||"),
                )
            if (continuation.isNotBlank()) {
                parameter =
                    parameter.plus(
                        parametersOf(
                            Pair("grncontinue", listOf(cont)),
                        ),
                    )
                println(parameter)
            }
            val response = client.get(getUrl(parameter))

            if (response.status.isSuccess()) {
                val allImageResponse: AllImageResponse = response.body()
                cont = allImageResponse.continueX.grncontinue
                return allImageResponse
                    .query
                    .pages
                    .map { it.imageinfo }
                    .flatten()
                    .filter {
                        it.mime.endsWith("jpeg") ||
                            it.mime.endsWith("jpg") ||
                            it.mime.endsWith("png")
                    }
                    .map { it.toMainImage() }
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) { // Catching generic Exception to include SerializationException
            throw NetworkDataSourceException("An unexpected error occurred during getAllImages: ${e.message}", e)
        }
    }

    override suspend fun search(
        title: String,
        page: Int,
        limit: Int,
    ): List<MainImage> {
        try {
            val parameter =
                parametersOf(
                    "action" to listOf("query"),
                    "format" to listOf("json"),
                    "formatversion" to listOf("2"),
                    "prop" to listOf("imageinfo"),
                    "iiprop" to listOf("user|url|mime|canonicaltitle|sha1"),
                    "generator" to listOf("search"),
                    "gsrwhat" to listOf("text"),
                    "gsrnamespace" to listOf("6"),
                )
            val dynamicParameters =
                parametersOf(
                    "gsrsearch" to listOf(title),
                    "gsrlimit" to listOf(limit.toString()),
                    "gsroffset" to listOf(((page - 1) * limit).toString()),
                )
            val response = client.get(getUrl(parameter + dynamicParameters))

            if (response.status.isSuccess()) {
                // Note: This uses AllSearchResponse, which might have a different Imageinfo structure
                // than AllImageResponse used by other functions if iiprop/prop differ significantly.
                // For GetMediaListFormSearch.md, we need AllImageResponse for richer data.
                val searchImages: AllSearchResponse = response.body()
                return searchImages
                    .query
                    .pages
                    .map { it.imageinfo }
                    .flatten()
                    .filter {
                        it.mime.endsWith("jpeg") ||
                            it.mime.endsWith("jpg") ||
                            it.mime.endsWith("png")
                    }
                    .map { it.toMainImage() }
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) { // Catching generic Exception
            throw NetworkDataSourceException("An unexpected error occurred during media search: ${e.message}", e)
        }
    }

    override suspend fun checkPageExists(title: String): Boolean {
        try {
            val parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "titles" to listOf(title)
            )
            val response = client.get(getUrl(parameters))

            if (response.status.isSuccess()) {
                val pageExistenceResponse: PageExistenceResponse = response.body()
                val pageInfo = pageExistenceResponse.query?.pages?.firstOrNull()
                if (pageInfo == null || pageInfo.missing == true || pageInfo.invalid == true) {
                    return false
                }
                return pageInfo.pageid != null && pageInfo.pageid > 0
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status} while checking page existence: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error occurred while checking page existence for title '$title': ${e.message}", e)
        }
    }

    override suspend fun checkFileExistsBySha(sha1: String): Boolean {
        try {
            val parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "list" to listOf("allimages"),
                "aisha1" to listOf(sha1)
            )
            val response = client.get(getUrl(parameters))

            if (response.status.isSuccess()) {
                val fileExistenceResponse: FileExistenceShaResponse = response.body()
                return fileExistenceResponse.query?.allimages?.isNotEmpty() ?: false
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status} while checking file existence by SHA1: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error occurred while checking file existence for SHA1 '$sha1': ${e.message}", e)
        }
    }

    override suspend fun getMediaListFromCategory(
        category: String,
        limit: Int,
        continuation: String?,
    ): List<MainImage> {
        try {
            var parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "generator" to listOf("categorymembers"),
                "gcmtype" to listOf("file"),
                "gcmsort" to listOf("timestamp"),
                "gcmdir" to listOf("desc"),
                "prop" to listOf("imageinfo", "coordinates"),
                "iiprop" to listOf("url", "extmetadata", "user"),
                "iiurlwidth" to listOf("640"),
                "iiextmetadatafilter" to listOf("DateTime|Categories|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"),
                "gcmtitle" to listOf("Category:$category"),
                "gcmlimit" to listOf(limit.toString()),
            )
            continuation?.takeIf { it.isNotBlank() }?.let {
                parameters = parameters.plus(parametersOf("gcmcontinue" to listOf(it)))
            }

            val response = client.get(getUrl(parameters))

            if (response.status.isSuccess()) {
                val allImageResponse: AllImageResponse = response.body()
                val images = allImageResponse.query?.pages
                    ?.flatMap { page ->
                        val imageInfoList = page.imageinfo ?: emptyList()
                        imageInfoList.mapNotNull { imageinfo ->
                            val mainImage = imageinfo.toMainImage().copy(
                                // latitude = page.coordinates?.firstOrNull()?.lat,
                                // longitude = page.coordinates?.firstOrNull()?.lon
                            )
                            if (mainImage.url.isNotBlank() && 
                                (mainImage.url.endsWith("jpeg", ignoreCase = true) ||
                                mainImage.url.endsWith("jpg", ignoreCase = true) ||
                                mainImage.url.endsWith("png", ignoreCase = true))) {
                                mainImage
                            } else {
                                null
                            }
                        }
                    } ?: emptyList()
                // Note: GetMediaListFromCategory.md doesn't specify how to handle continuation for List<MainImage>.
                // If pagination is needed, this function should return MediaListResult or similar.
                return images
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error occurred during getMediaListFromCategory for category '$category': ${e.message}", e)
        }
    }

    override suspend fun getMediaListBySearchTerm(searchTerm: String, limit: Int, offset: Int): List<MainImage> {
        try {
            val parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "prop" to listOf("imageinfo", "coordinates"),
                "iiprop" to listOf("url", "extmetadata", "user"),
                "iiurlwidth" to listOf("640"),
                "iiextmetadatafilter" to listOf("DateTime|Categories|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"),
                "generator" to listOf("search"),
                "gsrwhat" to listOf("text"),
                "gsrnamespace" to listOf("6"),
                "gsrsearch" to listOf(searchTerm),
                "gsrlimit" to listOf(limit.toString()),
                "gsroffset" to listOf(offset.toString())
            )

            val response = client.get(getUrl(parameters))

            if (response.status.isSuccess()) {
                val allImageResponse: AllImageResponse = response.body() // Using AllImageResponse for richer data
                return allImageResponse.query?.pages
                    ?.flatMap { page ->
                        val imageInfoList = page.imageinfo ?: emptyList()
                        imageInfoList.mapNotNull { imageinfo ->
                            val mainImage = imageinfo.toMainImage().copy(
                                // latitude = page.coordinates?.firstOrNull()?.lat,
                                // longitude = page.coordinates?.firstOrNull()?.lon
                            )
                            // Ensure only valid image types are processed
                            if (mainImage.url.isNotBlank() &&
                                (mainImage.url.endsWith("jpeg", ignoreCase = true) ||
                                    mainImage.url.endsWith("jpg", ignoreCase = true) ||
                                    mainImage.url.endsWith("png", ignoreCase = true))) {
                                mainImage
                            } else {
                                null
                            }
                        }
                    } ?: emptyList()
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error occurred during getMediaListBySearchTerm for '$searchTerm': ${e.message}", e)
        }
    }
}
