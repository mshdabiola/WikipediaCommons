package com.mshdabiola.network

import com.mshdabiola.network.model.ApiSubCategoryResponse
import com.mshdabiola.network.model.SubCategoryInfo
import com.mshdabiola.network.model.SubCategoryListResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.ParametersBuilder
import io.ktor.http.isSuccess
import io.ktor.http.parametersOf
import kotlinx.io.IOException

internal class CategoryDataSource(
    private val client: HttpClient,
) : ICategoryDataSource {

    // Base URL should ideally come from a central config or be injectable
    private fun getUrl(parameters: ParametersBuilder): String {
        // Assuming a base URL similar to MediaDataSource
        return "https://en.wikipedia.org/w/api.php?"
    }

    override suspend fun getSubCategories(
        parentCategoryTitle: String,
        limit: Int,
        continuation: String?
    ): SubCategoryListResult {
        try {
            var params = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "generator" to listOf("categorymembers"),
                "gcmtype" to listOf("subcat"),
                "prop" to listOf("info"), // As per SubCategoryList.md
                "gcmtitle" to listOf(parentCategoryTitle), // Dynamic based on input
                "gcmlimit" to listOf(limit.toString())
            )
            continuation?.takeIf { it.isNotBlank() }?.let {
                // The .md file uses 'gcmtitle=Category:Gallery pages of sovereign states'
                // but continuation for categorymembers is typically gcmcontinue
                // Need to ensure Continue.kt has gcmcontinue
                params = params + parametersOf("gcmcontinue" to listOf(it))
            }

            val response = client.get(getUrl(params))

            if (response.status.isSuccess()) {
                val apiResponse: ApiSubCategoryResponse = response.body()
                val subCategories = apiResponse.query?.pages?.map {
                    SubCategoryInfo(pageid = it.pageid, ns = it.ns, title = it.title)
                } ?: emptyList()

                // Assuming Continue.kt is updated to include gcmcontinue
                val nextContinuation = apiResponse.continueResponse?.gcmcontinue 

                return SubCategoryListResult(
                    subCategories = subCategories,
                    continuationToken = nextContinuation
                )
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${'$'}{response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during getSubCategories for '$parentCategoryTitle': ${'$'}{e.message}",
                e
            )
        }
    }
}