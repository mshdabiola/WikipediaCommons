package com.mshdabiola.network

import com.mshdabiola.model.MainImage
import com.mshdabiola.network.model.AllImageResponse
import com.mshdabiola.network.model.AllSearchResponse
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
                
                // A page exists if it's not marked as 'missing' or 'invalid', 
                // and typically has a positive pageid.
                // If pageInfo is null (e.g. invalid API response or malformed title query),
                // or if it explicitly has 'missing' or 'invalid' flags, then it doesn't exist.
                if (pageInfo == null || pageInfo.missing == true || pageInfo.invalid == true) {
                    return false
                }
                // Some APIs might return a negative pageid for non-existent/invalid pages,
                // or pageid might be null. A positive pageid confirms existence.
                return pageInfo.pageid != null && pageInfo.pageid > 0
            } else {
                val errorBody: String = response.body()
                // You might want to return false or throw a more specific error
                // depending on how you want to handle API errors for this check.
                // For now, rethrowing as an IOException similar to other methods.
                throw IOException("API request failed with status ${response.status} while checking page existence: $errorBody")
            }
        } catch (e: Exception) { // Catching generic Exception to include SerializationException, IOException etc.
            // Log or handle more specifically if needed.
            // Consider returning false if an error occurs, or rethrowing.
            throw NetworkDataSourceException("An unexpected error occurred while checking page existence for title '$title': ${e.message}", e)
        }
    }
}
