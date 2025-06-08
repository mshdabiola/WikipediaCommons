package com.mshdabiola.network

import UploadResponseWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.isSuccess
import kotlinx.io.IOException

// Define if not already present in your project:
// class NetworkDataSourceException(message: String, cause: Throwable? = null) : Exception(message, cause)

internal class UploadDataSource(
    private val client: HttpClient,
    private val baseUrl: String = "https://en.wikipedia.org/w/api.php" // Should be like https://commons.wikimedia.org/w/api.php
) : IUploadDataSource {

    private val commonUploadParams = Parameters.build {
        append("format", "json")
        append("formatversion", "2")
        append("errorformat", "plaintext") // As per .md files
        append("action", "upload")
        append("ignorewarnings", "1") // As per .md files
    }

    override suspend fun uploadFileToStash(
        token: String,
        filename: String,
        fileData: ByteArray,
        fileSize: Long,
        offset: Long?,
        fileKey: String?
    ): UploadResponseWrapper {
        try {
            val urlWithParams = URLBuilder(baseUrl).apply {
                parameters.appendAll(commonUploadParams)
                parameters.append("stash", "1")
            }.buildString()

            val response = client.submitFormWithBinaryData(
                url = urlWithParams, // Corrected URL building
                formData = formData {
                    append("token", token)
                    append("filename", filename)
                    append("filesize", fileSize.toString())
                    offset?.let { append("offset", it.toString()) }
                    fileKey?.let { append("filekey", it) }
                    append("filePart", fileData, Headers.build {
                        append(HttpHeaders.ContentType, "application/octet-stream") // Or detect actual content type
                        append(HttpHeaders.ContentDisposition, "filename=\"$filename\"")
                    })
                }
            )

            if (response.status.isSuccess()) {
                return response.body()
            } else {
                val errorBody: String = response.body()
                // Try to parse error as UploadResponseWrapper as it might contain structured error info
                try {
                    val errorResponse = response.body<UploadResponseWrapper>()
                    if (errorResponse.error != null) return errorResponse
                } catch (_: Exception) { /* Ignore parsing error, throw with raw body */ }
                throw IOException("API request failed for uploadFileToStash with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException("Error during uploadFileToStash for '$filename': ${e.message}", e)
        }
    }

    override suspend fun uploadFromStash(
        token: String,
        fileKey: String,
        filename: String,
        comment: String?,
        text: String?
    ): UploadResponseWrapper {
        try {
            // This is a POST request with URL-encoded parameters
            val response = client.post(baseUrl) {
                url {
                    parameters.appendAll(commonUploadParams)
                    parameters.append("stash", "1") // As per UploadFromStash.md URL (keeps it stashed on warning)
                                                    // To publish directly (not stash on warning), omit stash or set to 0.
                                                    // But the .md has stash=1.
                }
                setBody(Parameters.build {
                    append("token", token)
                    append("filekey", fileKey)
                    append("filename", filename)
                    comment?.let { append("comment", it) }
                    text?.let { append("text", it) }
                    // Other parameters from UploadFromStash.md are already in commonUploadParams or URL
                })
            }

            if (response.status.isSuccess()) {
                return response.body()
            } else {
                val errorBody: String = response.body()
                 try {
                    val errorResponse = response.body<UploadResponseWrapper>()
                    if (errorResponse.error != null) return errorResponse
                } catch (_: Exception) { /* Ignore parsing error, throw with raw body */ }
                throw IOException("API request failed for uploadFromStash with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException("Error during uploadFromStash for filekey '$fileKey', filename '$filename': ${e.message}", e)
        }
    }
}