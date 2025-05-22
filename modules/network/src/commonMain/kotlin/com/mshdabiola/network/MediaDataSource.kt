package com.mshdabiola.network

import com.mshdabiola.network.model.AllImageResponse
import com.mshdabiola.network.model.Page
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import io.ktor.http.parametersOf

internal class MediaDataSource(
    private val client: HttpClient,
) : IMediaDataSource {
    override suspend fun getAllImages(limit: Int, continuation: String): List<Page> {


        try {
            val parameter =
                parametersOf(
                    "action" to listOf("query"),
                    "format" to listOf("json"),
                    "formatversion" to listOf("2"),
                    "generator" to listOf("random"),
                    "prop" to listOf("imageinfo"),
                    "iiprop" to listOf("user|url|mime|canonicaltitle"),
                    "iilimit" to listOf("6"),
                    "grnlimit" to listOf(limit.toString()),
                    "grncontinue" to
                            listOf(
                                continuation
                                    .ifBlank { "0.573993798555|0.57399474331|62056655|0" },
                            ),
                    "continue" to listOf("grncontinue||"),
                )
            val response = client.get(getUrl(parameter))

            // Check the response status and parse the body
            if (response.status.isSuccess()) {
                return response.body<AllImageResponse>().query.pages
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
}