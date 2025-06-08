package com.mshdabiola.network

import com.mshdabiola.network.model.WikibaseEditEntityResponse
import com.mshdabiola.network.model.WikibaseGetClaimsResponse
import com.mshdabiola.network.model.WikibaseQueryFileEntityResponse
import com.mshdabiola.network.model.WikibaseSetLabelResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import kotlinx.io.IOException

// Assuming NetworkDataSourceException is defined elsewhere
// class NetworkDataSourceException(message: String, cause: Throwable? = null) : Exception(message, cause)

internal class WikibaseDataSource(
    private val client: HttpClient,
    private val wikibaseApiUrl: String = "https://www.wikidata.org/w/api.php", // Default to Wikidata
) : IWikibaseDataSource {

    private fun commonParamsBuilder(): Parameters {
        return Parameters.build {
            append("format", "json")
            append("formatversion", "2")
            append("errorformat", "plaintext")
        }
    }

    override suspend fun editEntityById(
        id: String,
        token: String,
        data: String,
    ): WikibaseEditEntityResponse {
        val response = client.submitForm(
            url = wikibaseApiUrl,
            formParameters = Parameters.build {
                appendAll(commonParamsBuilder())
                append("action", "wbeditentity")
                append("id", id)
                append("token", token)
                append("data", data)
            },
        )
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody: String = response.body()
            try {
                return response.body<WikibaseEditEntityResponse>()
            } catch (_: Exception) {
            }
            throw IOException("API error for editEntityById ($id): ${response.status} - $errorBody")
        }
    }

    override suspend fun editEntityByFilename(
        title: String,
        token: String,
        data: String,
    ): WikibaseEditEntityResponse {
        val response = client.submitForm(
            url = wikibaseApiUrl, // Assuming wikibaseApiUrl is set to Commons for this.
            formParameters = Parameters.build {
                appendAll(commonParamsBuilder())
                append("action", "wbeditentity")
                append("site", "commonswiki") // As per EditEntityByFilename.md
                append("title", title)
                append("token", token)
                append("data", data)
            },
        )
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody: String = response.body()
            try {
                return response.body<WikibaseEditEntityResponse>()
            } catch (_: Exception) {
            }
            throw IOException("API error for editEntityByFilename ($title): ${response.status} - $errorBody")
        }
    }

    override suspend fun getFileEntityInfoByTitle(
        title: String,
    ): WikibaseQueryFileEntityResponse {
        // This uses action=query, so it's a standard MediaWiki API call, not specific Wikibase.
        // The wikibaseApiUrl should point to the correct wiki (e.g., Commons).
        val response = client.get(wikibaseApiUrl) {
            url {
                parameters.appendAll(commonParamsBuilder())
                parameters.append("action", "query")
                parameters.append("prop", "info") // As per FileEntityById.md
                parameters.append("titles", title)
            }
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody: String = response.body()
            try {
                return response.body<WikibaseQueryFileEntityResponse>()
            } catch (_: Exception) {
            }
            throw IOException("API error for getFileEntityInfoByTitle ($title): ${response.status} - $errorBody")
        }
    }

    override suspend fun setWikidataLabel(
        id: String,
        token: String,
        language: String,
        value: String,
    ): WikibaseSetLabelResponse {
        // This is for Wikidata, so wikibaseApiUrl should be Wikidata's API.
        val response = client.submitForm(
            url = wikibaseApiUrl,
            formParameters = Parameters.build {
                appendAll(commonParamsBuilder())
                append("action", "wbsetlabel")
                append("id", id)
                append("token", token)
                append("language", language)
                append("value", value)
            },
        )
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody: String = response.body()
            try {
                return response.body<WikibaseSetLabelResponse>()
            } catch (_: Exception) {
            }
            throw IOException("API error for setWikidataLabel ($id, $language): ${response.status} - $errorBody")
        }
    }

    override suspend fun getClaimsByProperty(
        entityId: String,
        propertyId: String,
    ): WikibaseGetClaimsResponse {
        val response = client.get(wikibaseApiUrl) {
            url {
                parameters.appendAll(commonParamsBuilder())
                parameters.append("action", "wbgetclaims")
                parameters.append("entity", entityId)
                parameters.append("property", propertyId)
            }
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody: String = response.body()
            try {
                return response.body<WikibaseGetClaimsResponse>()
            } catch (_: Exception) {
            }
            throw IOException("API error for getClaimsByProperty ($entityId, $propertyId): ${response.status} - $errorBody")
        }
    }

    override suspend fun deleteClaims(
        id: String,
        token: String,
        data: String,
    ): WikibaseEditEntityResponse {
        // This is specified as GET in PostDeleteClaims.md, which is unusual for an edit.
        val response = client.get(wikibaseApiUrl) {
            url {
                parameters.appendAll(commonParamsBuilder())
                parameters.append("action", "wbeditentity") // As per .md file
                parameters.append("id", id)
                parameters.append("token", token)
                parameters.append("data", data)
            }
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody: String = response.body()
            try {
                return response.body<WikibaseEditEntityResponse>()
            } catch (_: Exception) {
            }
            throw IOException("API error for deleteClaims ($id): ${response.status} - $errorBody")
        }
    }
}
