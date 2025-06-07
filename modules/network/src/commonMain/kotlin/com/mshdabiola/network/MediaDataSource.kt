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
                // If query is null or allimages is null or empty, the file doesn't exist.
                return fileExistenceResponse.query?.allimages?.isNotEmpty() ?: false
            } else {
                val errorBody: String = response.body()
                // Consider if specific error codes from the API mean "not found" vs. a general error.
                // For now, any non-success status is treated as an error and will rethrow.
                throw IOException("API request failed with status ${response.status} while checking file existence by SHA1: $errorBody")
            }
        } catch (e: Exception) { // Catching generic Exception (includes SerializationException, IOException, etc.)
            // Log or handle more specifically if needed.
            // Depending on requirements, you might want to return false if an error occurs rather than rethrowing.
            throw NetworkDataSourceException("An unexpected error occurred while checking file existence for SHA1 '$sha1': ${e.message}", e)
        }
    }
}
