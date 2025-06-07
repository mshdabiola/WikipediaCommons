package com.mshdabiola.network

import com.mshdabiola.network.model.ApiSearchCategoryResponse
import com.mshdabiola.network.model.ApiSubCategoryResponse
import com.mshdabiola.network.model.SearchedCategoryInfo
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
                params = params + parametersOf("gcmcontinue" to listOf(it))
            }

            val response = client.get(getUrl(params))

            if (response.status.isSuccess()) {
                val apiResponse: ApiSubCategoryResponse = response.body()
                val subCategories = apiResponse.query?.pages?.map {
                    SubCategoryInfo(pageid = it.pageid, ns = it.ns, title = it.title)
                } ?: emptyList()

                val nextContinuation = apiResponse.continueResponse?.gcmcontinue

                return SubCategoryListResult(
                    subCategories = subCategories,
                    continuationToken = nextContinuation
                )
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during getSubCategories for '$parentCategoryTitle': ${e.message}",
                e
            )
        }
    }

    override suspend fun searchCategories(
        searchTerm: String,
        limit: Int,
        offset: Int
    ): List<SearchedCategoryInfo> {
        try {
            val params = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "generator" to listOf("search"),
                "prop" to listOf("description", "pageimages"),
                "piprop" to listOf("thumbnail"),
                "pithumbsize" to listOf("70"), // As per SearchCategories.md, can be parameterized
                "gsrnamespace" to listOf("14"), // Namespace 14 is for Categories
                "gsrsearch" to listOf(searchTerm),
                "gsrlimit" to listOf(limit.toString()),
                "gsroffset" to listOf(offset.toString())
            )

            val response = client.get(getUrl(params))

            if (response.status.isSuccess()) {
                val apiResponse: ApiSearchCategoryResponse = response.body()
                return apiResponse.query?.pages?.mapNotNull {
                    SearchedCategoryInfo(
                        title = it.title,
                        description = it.description,
                        thumbnailUrl = it.thumbnail?.source
                    )
                } ?: emptyList()
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during searchCategories for '$searchTerm': ${e.message}",
                e
            )
        }
    }

    override suspend fun searchCategoriesByPrefix(
        prefix: String,
        limit: Int,
        offset: Int
    ): List<SearchedCategoryInfo> {
        try {
            val params = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "generator" to listOf("allcategories"),
                "prop" to listOf("categoryinfo", "description", "pageimages"),
                "piprop" to listOf("thumbnail"),
                "pithumbsize" to listOf("70"), // As per SearchCategoriesForPrefix.md
                "gacprefix" to listOf(prefix),
                "gaclimit" to listOf(limit.toString()),
                "gacoffset" to listOf(offset.toString())
            )

            val response = client.get(getUrl(params))

            if (response.status.isSuccess()) {
                val apiResponse: ApiSearchCategoryResponse = response.body()
                return apiResponse.query?.pages?.mapNotNull {
                    // The 'categoryinfo' is available via it.categoryinfo if needed in the future
                    SearchedCategoryInfo(
                        title = it.title,
                        description = it.description,
                        thumbnailUrl = it.thumbnail?.source
                    )
                } ?: emptyList()
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during searchCategoriesByPrefix for '$prefix': ${e.message}",
                e
            )
        }
    }

    override suspend fun searchCategoriesByName(
        from: String,
        to: String?,
        limit: Int,
        offset: Int
    ): List<SearchedCategoryInfo> {
        try {
            var params = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "generator" to listOf("allcategories"),
                "prop" to listOf("categoryinfo", "description", "pageimages"),
                "piprop" to listOf("thumbnail"),
                "pithumbsize" to listOf("70"), // As per SearchCategoriesByName.md (copied from SearchCategoriesForPrefix)
                "gacfrom" to listOf(from),
                "gaclimit" to listOf(limit.toString()),
                "gacoffset" to listOf(offset.toString())
            )
//            to?.takeIf { it.isNotBlank() }?.let {
//                params = params + parametersOf("gacto" to listOf(it))
//            }

            val response = client.get(getUrl(params))

            if (response.status.isSuccess()) {
                val apiResponse: ApiSearchCategoryResponse = response.body()
                return apiResponse.query?.pages?.mapNotNull {
                    // The 'categoryinfo' is available via it.categoryinfo if needed in the future
                    SearchedCategoryInfo(
                        title = it.title,
                        description = it.description,
                        thumbnailUrl = it.thumbnail?.source
                    )
                } ?: emptyList()
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during searchCategoriesByName for from:'$from', to:'$to': ${e.message}",
                e
            )
        }
    }
}
