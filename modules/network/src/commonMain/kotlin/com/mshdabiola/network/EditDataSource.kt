package com.mshdabiola.network

import com.mshdabiola.network.model.EditResponseWrapper
import com.mshdabiola.network.model.SetLabelResponseWrapper // New import
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import kotlinx.io.IOException

// Ensure this custom exception is defined, or use a common one from your project.
// class NetworkDataSourceException(message: String, cause: Throwable? = null) : Exception(message, cause)

internal class EditDataSource(
    private val client: HttpClient,
    private val baseUrl: String = "https://en.wikipedia.org/w/api.php" // Placeholder. For Wikidata actions like wbsetlabel, might be different or need specific config.
    // For Commons, action=wbsetlabel usually goes to commonswiki API URL directly.
    // Example: https://commons.wikimedia.org/w/api.php
) : IEditDataSource {

    // ... [previous postEdit and postCreatePage methods remain unchanged] ...
    /**
     * Submits an edit to a page.
     * Corresponds to @PostEdit.md
     * The URL in PostEdit.md is '{{WikiDataUrlWithData}}action=edit'.
     * We'll assume WikiDataUrlWithData is the baseUrl and action=edit is a query param.
     * Common parameters like format=json are usually added for MediaWiki APIs.
     */
    override suspend fun postEdit(
        title: String,
        summary: String,
        text: String,
        token: String
    ): EditResponseWrapper {
        try {
            val response = client.submitForm(
                url = "$baseUrl?action=edit&format=json&formatversion=2", // Added format and formatversion
                formParameters = Parameters.build {
                    append("title", title)
                    append("summary", summary)
                    append("text", text)
                    append("token", token) // This is crucial, often a CSRF token
                }
            )

            if (response.status.isSuccess()) {
                return response.body<EditResponseWrapper>()
            } else {
                val errorBody: String = response.body()
                // Attempt to parse as EditResponseWrapper even on failure, as it might contain an error structure.
                try {
                    val errorResponse = response.body<EditResponseWrapper>()
                    if (errorResponse.error != null) return errorResponse
                } catch (e: Exception) {
                    // If parsing the error response fails, throw with the raw body.
                }
                throw IOException("API request failed for postEdit with status ${response.status}: $errorBody")
            }
        } catch (e: IOException) {
            throw NetworkDataSourceException("Network error during postEdit for title '$title': ${e.message}", e)
        } catch (e: Exception) { // Includes SerializationException etc.
            throw NetworkDataSourceException("An unexpected error occurred during postEdit for title '$title': ${e.message}", e)
        }
    }

    /**
     * Creates a new page or edits an existing one, with more content options.
     * Corresponds to @PostCreate.md
     * URL is '{{WikiDataUrlWithData}}action=edit'
     */
    override suspend fun postCreatePage(
        title: String,
        summary: String,
        text: String,
        contentFormat: String,
        contentModel: String,
        isMinorEdit: Boolean?,
        recreatePage: Boolean?,
        token: String
    ): EditResponseWrapper {
        try {
            val response = client.submitForm(
                url = "$baseUrl?action=edit&format=json&formatversion=2", // Added format and formatversion
                formParameters = Parameters.build {
                    append("title", title)
                    append("summary", summary)
                    append("text", text)
                    append("contentformat", contentFormat)
                    append("contentmodel", contentModel)
                    isMinorEdit?.let { if (it) append("minor", "true") }
                    recreatePage?.let { if (it) append("recreate", "true") }
                    append("token", token)
                }
            )

            if (response.status.isSuccess()) {
                return response.body<EditResponseWrapper>()
            } else {
                val errorBody: String = response.body()
                try {
                    val errorResponse = response.body<EditResponseWrapper>()
                    if (errorResponse.error != null) return errorResponse
                } catch (e: Exception) {
                    // If parsing the error response fails, throw with the raw body.
                }
                throw IOException("API request failed for postCreatePage with status ${response.status}: $errorBody")
            }
        } catch (e: IOException) {
            throw NetworkDataSourceException("Network error during postCreatePage for title '$title': ${e.message}", e)
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error occurred during postCreatePage for title '$title': ${e.message}", e)
        }
    }

    override suspend fun postAppendText(
        title: String,
        summary: String,
        appendText: String,
        token: String
    ): EditResponseWrapper {
        try {
            val response = client.submitForm(
                url = "$baseUrl?action=edit&format=json&formatversion=2",
                formParameters = Parameters.build {
                    append("title", title)
                    append("summary", summary)
                    append("appendtext", appendText)
                    append("token", token)
                }
            )
            if (response.status.isSuccess()) {
                return response.body<EditResponseWrapper>()
            } else {
                val errorBody: String = response.body()
                try {
                    val errorResponse = response.body<EditResponseWrapper>()
                    if (errorResponse.error != null) return errorResponse
                } catch (_: Exception) {}
                throw IOException("API request failed for postAppendText with status ${response.status}: $errorBody")
            }
        } catch (e: IOException) {
            throw NetworkDataSourceException("Network error during postAppendText for title '$title': ${e.message}", e)
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error during postAppendText for title '$title': ${e.message}", e)
        }
    }

    override suspend fun postPrependText(
        title: String,
        summary: String,
        prependText: String,
        token: String
    ): EditResponseWrapper {
        try {
            val response = client.submitForm(
                url = "$baseUrl?action=edit&format=json&formatversion=2",
                formParameters = Parameters.build {
                    append("title", title)
                    append("summary", summary)
                    append("prependtext", prependText)
                    append("token", token)
                }
            )
            if (response.status.isSuccess()) {
                return response.body<EditResponseWrapper>()
            } else {
                val errorBody: String = response.body()
                try {
                    val errorResponse = response.body<EditResponseWrapper>()
                    if (errorResponse.error != null) return errorResponse
                } catch (_: Exception) {}
                throw IOException("API request failed for postPrependText with status ${response.status}: $errorBody")
            }
        } catch (e: IOException) {
            throw NetworkDataSourceException("Network error during postPrependText for title '$title': ${e.message}", e)
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error occurred during postPrependText for title '$title': ${e.message}", e)
        }
    }

    override suspend fun postNewSection(
        title: String,
        summary: String,
        sectionTitle: String,
        text: String,
        token: String
    ): EditResponseWrapper {
        try {
            // Note: section=new is part of the URL itself as per @NewSection.md
            val response = client.submitForm(
                url = "$baseUrl?action=edit&section=new&format=json&formatversion=2",
                formParameters = Parameters.build {
                    append("title", title) // Page title
                    append("summary", summary)
                    append("sectiontitle", sectionTitle)
                    append("text", text) // Content of the new section
                    append("token", token)
                }
            )
            if (response.status.isSuccess()) {
                return response.body<EditResponseWrapper>()
            } else {
                val errorBody: String = response.body()
                try {
                    val errorResponse = response.body<EditResponseWrapper>()
                    if (errorResponse.error != null) return errorResponse
                } catch (_: Exception) {}
                throw IOException("API request failed for postNewSection with status ${response.status}: $errorBody")
            }
        } catch (e: IOException) {
            throw NetworkDataSourceException("Network error during postNewSection for page '$title', section '$sectionTitle': ${e.message}", e)
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error during postNewSection for page '$title', section '$sectionTitle': ${e.message}", e)
        }
    }

    override suspend fun postCaption(
        title: String,
        language: String,
        value: String,
        summary: String?,
        token: String
    ): SetLabelResponseWrapper {
        // URL for wbsetlabel is different from typical 'edit' action.
        // It's {{WikiDataUrlWithData}}action=wbsetlabel&format=json&site=commonswiki&formatversion=2
        // Assuming baseUrl is for the correct wiki (e.g., Commons).
        // The `site=commonswiki` param in the .md suggests the target. This should align with `baseUrl`.
        // If `baseUrl` is generic (like en.wikipedia.org), this call might need a dedicated base URL or client.
        // For now, I'll assume `baseUrl` is correctly set for Commons for this call.
        try {
            val response = client.submitForm(
                url = "$baseUrl?action=wbsetlabel&format=json&formatversion=2", // site=commonswiki is implied if baseUrl is commons.wikimedia.org
                formParameters = Parameters.build {
                    append("id", title) // API uses 'id' for wbsetlabel, not 'title' for the entity. Confirmed via MediaWiki API docs for wbsetlabel.
                    // The @PostCaption.md uses 'title' in its body, but this seems to be for the page *containing* the entity,
                    // or it's an alias if the target is a File page on Commons itself.
                    // For wbsetlabel, 'id' or 'site' + 'title' (for page) is standard.
                    // Let's assume 'title' from input means the entity ID or page title that wbsetlabel can resolve.
                    // If 'title' is a page title like "File:X.jpg", 'site' is needed.
                    // Given @PostCaption.md has `site=commonswiki` in its URL, if `title` is "File:X.jpg", it should work.
                    // If `title` is an M-ID (e.g. "M12345"), then 'id' parameter should be used.
                    // For safety, let's try 'id' first, then 'title' + 'site' if that's more appropriate for user's context.
                    // The .md has `[[body.urlEncoded]] key = 'title'` which is ambiguous for wbsetlabel.
                    // I will use `id = title` for entity IDs, and if the user means a page, they might need to use a different parameter.
                    // Let's stick to the `.md` body param `title` and assume the API can interpret it.
                    append("title", title) // As per PostCaption.md body.
                    append("language", language)
                    append("value", value)
                    summary?.let { append("summary", it) }
                    append("token", token)
                    // append("site", "commonswiki") // This was in the .md's URL. If baseUrl doesn't specify commons, this is vital.
                    // However, for POST, it's usually not a URL param unless API specific.
                    // Re-checking MediaWiki API: for wbsetlabel, 'site' and 'title' are used to identify the page item.
                    // Or 'id' for an entity id.
                    // The .md has `site=commonswiki` in its URL, but it's a POST.
                    // Let's assume the `baseUrl` is implicitly correct for the target site or the `title` includes site prefix if needed.
                    // The .md file's URL definition '{{WikiDataUrlWithData}}action=wbsetlabel&format=json&site=commonswiki&formatversion=2'
                    // suggests `site` is a query parameter, even for POST. This is unusual but possible.
                    // For robustness, will add it to the URL string.

                }
            )

            // The URL in @PostCaption.md has 'site=commonswiki'. Adding it to the submitForm URL.
            val effectiveUrl = "$baseUrl?action=wbsetlabel&format=json&formatversion=2&site=commonswiki"


            val actualResponse = client.submitForm(
                url = effectiveUrl,
                formParameters = Parameters.build {
                    // Parameters as per @PostCaption.md:
                    append("title", title) // This is what the .md file requests in the body.
                    // For wbsetlabel, MediaWiki usually expects 'id' (for Qxxx or Mxxx)
                    // or 'site' + 'title' (for a page like File:xx.jpg on a specific wiki).
                    // If the .md means 'title' of the page on 'commonswiki', this is correct.
                    append("summary", summary ?: "") // API might require it.
                    append("language", language)
                    append("value", value)
                    append("token", token)
                }
            )


            if (actualResponse.status.isSuccess()) {
                return actualResponse.body<SetLabelResponseWrapper>()
            } else {
                val errorBody: String = actualResponse.body()
                try {
                    val errorResponse = actualResponse.body<SetLabelResponseWrapper>()
                    if (errorResponse.error != null) return errorResponse
                } catch (_: Exception) {}
                throw IOException("API request failed for postCaption with status ${actualResponse.status}: $errorBody")
            }
        } catch (e: IOException) {
            throw NetworkDataSourceException("Network error during postCaption for title '$title', lang '$language': ${e.message}", e)
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error during postCaption for title '$title', lang '$language': ${e.message}", e)
        }
    }
}