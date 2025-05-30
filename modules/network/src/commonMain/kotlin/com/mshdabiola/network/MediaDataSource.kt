package com.mshdabiola.network

import com.mshdabiola.model.MainImage
import com.mshdabiola.network.model.AllImageResponse
import com.mshdabiola.network.model.AllSearchResponse
import com.mshdabiola.network.model.toMainImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import io.ktor.http.parametersOf
import io.ktor.http.plus

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
//                    "grncontinue" to
//                            listOf(
//                                continuation
//                                    .ifBlank { "0.573993798555|0.57399474331|62056655|0" },
//                            ),
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

            // Check the response status and parse the body
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
                // Handle error responses (e.g., throw an exception, return a default value)
                val errorBody: String = response.body() // Get the error message body
                throw Exception("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            // Handle network errors or other exceptions during the request
            e.printStackTrace()
            throw e // Re-throw the exception or handle it as needed
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

            // Check the response status and parse the body
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
                // Handle error responses (e.g., throw an exception, return a default value)
                val errorBody: String = response.body()
                throw Exception("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            // Handle network errors or other exceptions during the request
            e.printStackTrace()
            throw e // Re-throw the exception or handle it as needed
        }
    }
}
