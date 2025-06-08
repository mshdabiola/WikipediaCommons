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
import io.ktor.client.request.headers
import io.ktor.http.isSuccess
import io.ktor.http.parametersOf
import io.ktor.http.plus
import kotlinx.io.IOException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
                "titles" to listOf(title),
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
                "aisha1" to listOf(sha1),
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
                "gsroffset" to listOf(offset.toString()),
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


    override suspend fun getMediaDetails(title: String): MainImage? {
        try {
            val parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "prop" to listOf("categories", "imageinfo"),
                "clprop" to listOf("hidden"),
                "iiprop" to listOf("url", "extmetadata", "user"),
                "iiurlwidth" to listOf("640"),
                "iiextmetadatafilter" to listOf("DateTime|Categories|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"),
                "titles" to listOf(title),
            )

            val response = client.get(getUrl(parameters))

            if (response.status.isSuccess()) {
                val allImageResponse: AllImageResponse = response.body()
                val page = allImageResponse.query?.pages?.firstOrNull()
                val imageInfo = page?.imageinfo?.firstOrNull()

                // The 'categories' are in page?.categories
                // You might want to pass page?.categories to toMainImage() or handle them here

                return if (imageInfo != null) {
                    val mainImage = imageInfo.toMainImage() // Potentially pass page.categories if needed by toMainImage
                    if (mainImage.url.isNotBlank() &&
                        (mainImage.url.endsWith("jpeg", ignoreCase = true) ||
                                mainImage.url.endsWith("jpg", ignoreCase = true) ||
                                mainImage.url.endsWith("png", ignoreCase = true))) {
                        mainImage
                    } else {
                        null
                    }
                } else {
                    null
                }
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${'$'}{response.status}: ${'$'}errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException("An unexpected error occurred during getMediaDetails for title '${'$'}title': ${'$'}{e.message}", e)
        }
    }


    override suspend fun getMediaListFromGeoSearch(
        latitude: Double,
        longitude: Double,
        limit: Int,
        radius: Int,
        continuation: String?,
    ): List<MainImage> { // Or a more specific return type like MediaListResult if you need continuation support
        try {
            var parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "prop" to listOf("imageinfo", "coordinates"),
                "iiprop" to listOf("url", "extmetadata", "user"),
                "iiurlwidth" to listOf("640"),
                "iiextmetadatafilter" to listOf("DateTime|Categories|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"),
                "generator" to listOf("geosearch"),
                "ggsnamespace" to listOf("6"),
                "ggscoord" to listOf("$latitude|$longitude"),
                "ggslimit" to listOf(limit.toString()),
                "ggsradius" to listOf(radius.toString()),
            )
            continuation?.takeIf { it.isNotBlank() }?.let {
                parameters =
                    parameters.plus(parametersOf("continuation" to listOf(it))) // Or the specific continuation param like "ggscontinue"
            }

            val response = client.get(getUrl(parameters))

            if (response.status.isSuccess()) {
                val allImageResponse: AllImageResponse =
                    response.body() // Assuming AllImageResponse is appropriate
                // Process the response to extract MainImage list
                // This will be similar to getMediaListFromCategory or getMediaListBySearchTerm
                // You'll need to map pages to MainImage, potentially using coordinates
                // and filtering by mime type.
                val images = allImageResponse.query?.pages
                    ?.flatMap { page ->
                        val imageInfoList = page.imageinfo ?: emptyList()
                        imageInfoList.mapNotNull { imageinfo ->
                            val mainImage = imageinfo.toMainImage().copy(
                            )
                            if (mainImage.url.isNotBlank() &&
                                (mainImage.url.endsWith("jpeg", ignoreCase = true) ||
                                        mainImage.url.endsWith("jpg", ignoreCase = true) ||
                                        mainImage.url.endsWith("png", ignoreCase = true))
                            ) {
                                mainImage
                            } else {
                                null
                            }
                        }
                    } ?: emptyList()
                // If you need to return a continuation token for pagination,
                // you'll need to extract it from `allImageResponse.continueX` (e.g., allImageResponse.continueX?.ggscontinue)
                // and return it as part of a custom result object instead of just List<MainImage>.
                return images
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during getMediaListFromGeoSearch: ${e.message}",
                e,
            )
        }
    }

    // In /home/mshdabiola/StudioProjects/WikipediaCommons/modules/network/src/commonMain/kotlin/com/mshdabiola/network/MediaDataSource.kt
// ... (other imports and class definition)
    override suspend fun getMedia(title: String): MainImage? { // Assuming you want to return a single MainImage or null if not found/error
        try {
            val parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "clprop" to listOf("hidden"),
                "prop" to listOf("categories", "imageinfo"),
                "iiprop" to listOf("url", "extmetadata", "user"),
                "iiurlwidth" to listOf("640"),
                "iiextmetadatafilter" to listOf("DateTime|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"),
                "titles" to listOf(title), // Use the function parameter for the title
            )

            val response = client.get(getUrl(parameters))

            if (response.status.isSuccess()) {
                val imageResponse: AllImageResponse =
                    response.body() // Assuming AllImageResponse can parse this structure

                // The response for a single media item will likely have one page.
                // We need to extract the imageinfo from that page.
                val page = imageResponse.query?.pages?.firstOrNull()
                val imageInfo = page?.imageinfo?.firstOrNull()

                return if (imageInfo != null) {
                    imageInfo.toMainImage().copy(
                        // categories = page.categories?.map { it.title } // Example if you parse categories
                        // latitude = imageInfo.extmetadata?.gPSLatitude?.value, // Example for GPS
                        // longitude = imageInfo.extmetadata?.gPSLongitude?.value // Example for GPS
                        // Extract other relevant details from extmetadata or page.categories as needed
                    )
                } else {
                    // Could indicate the media title was not found or the response was unexpected
                    null
                }
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during getMedia for title '$title': ${e.message}",
                e,
            )
        }
    }

    // In /home/mshdabiola/StudioProjects/WikipediaCommons/modules/network/src/commonMain/kotlin/com/mshdabiola/network/MediaDataSource.kt
// ... (other imports and class definition)
    override suspend fun getMediaSuppressErrors(title: String): MainImage? {
        try {
            val parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "clprop" to listOf("hidden"),
                "prop" to listOf("categories", "imageinfo"),
                "iiprop" to listOf("url", "extmetadata", "user"),
                "iiurlwidth" to listOf("640"),
                "iiextmetadatafilter" to listOf("DateTime|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"),
                "titles" to listOf(title),
            )
            val response = client.get(getUrl(parameters)){
                headers {
                    append("x-commons-suppress-error-log", "true")
                }
            }




            if (response.status.isSuccess()) {
                val imageResponse: AllImageResponse = response.body()

                val page = imageResponse.query?.pages?.firstOrNull()
                val imageInfo = page?.imageinfo?.firstOrNull()

                return if (imageInfo != null) {
                    imageInfo.toMainImage().copy(
                        // Extract categories and extmetadata as in the getMedia function
                        // e.g., categories = page.categories?.map { it.title }
                        // latitude = imageInfo.extmetadata?.gPSLatitude?.value,
                        // longitude = imageInfo.extmetadata?.gPSLongitude?.value
                    )
                } else {
                    null
                }
            } else {
                // Even with suppress-error-log, network errors or severe API errors can still occur
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            // Log this error, as it might be unexpected if x-commons-suppress-error-log is true
            println("Error in getMediaSuppressErrors for title '$title': ${e.message}")
            // Depending on desired behavior, you might return null or rethrow a specific exception
            // For now, let's rethrow as NetworkDataSourceException for consistency
            throw NetworkDataSourceException(
                "An unexpected error occurred during getMediaSuppressErrors for title '$title': ${e.message}",
                e,
            )
        }
    }

    // In /home/mshdabiola/StudioProjects/WikipediaCommons/modules/network/src/commonMain/kotlin/com/mshdabiola/network/MediaDataSource.kt
// ... (other imports and class definition)
    override suspend fun getMediaById(pageId: Long): MainImage? { // Assuming pageids are typically Long
        try {
            val parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"),
                "prop" to listOf("imageinfo", "coordinates"),
                "iiprop" to listOf("url", "extmetadata", "user"),
                "iiurlwidth" to listOf("640"),
                "iiextmetadatafilter" to listOf("DateTime|Categories|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"),
                "pageids" to listOf(pageId.toString()), // Use the function parameter for the pageid
            )

            // Assuming your getUrl function or a similar utility constructs the full URL with parameters
            // Or, construct it explicitly with Ktor as shown in previous examples:
            // val response = client.get(BASE_URL) {
            //     url {
            //         appendPathSegments("w/api.php") // Your API endpoint
            //         parameters.forEach { key, values ->
            //             values.forEach { value -> this.parameters.append(key, value) }
            //         }
            //     }
            // }
            val response = client.get(getUrl(parameters))


            if (response.status.isSuccess()) {
                val imageResponse: AllImageResponse =
                    response.body() // Assuming AllImageResponse can parse this

                // The response for a single media item by pageid will likely have one page.
                val page = imageResponse.query?.pages?.firstOrNull()
                val imageInfo = page?.imageinfo?.firstOrNull()

                return if (imageInfo != null) {
                    imageInfo.toMainImage().copy( )
                } else {
                    // Could indicate the media pageid was not found or the response was unexpected
                    null
                }
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during getMediaById for pageId '$pageId': ${e.message}",
                e,
            )
        }
    }

    // In /home/mshdabiola/StudioProjects/WikipediaCommons/modules/network/src/commonMain/kotlin/com/mshdabiola/network/MediaDataSource.kt
// ... (other imports and class definition)
    override suspend fun fetchImageForDepicted(
        searchTerm: String, // To make it dynamic instead of fixed "Nigeria"
        limit: Int,
        offset: Int,
    ): List<MainImage> { // Or a more specific return type like MediaListResult if you need continuation for gsrcontinue
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
                // "gsrnamespace" to listOf("6"), // File namespace is 6
                "gsrsearch" to listOf(searchTerm), // Using the function parameter
                "gsrlimit" to listOf(limit.toString()),
                "gsroffset" to listOf(offset.toString()),
                // Note: The .md file has gsrnamespace=6, which is good for files.
                // It also has gsrsearch=Nigeria. I've made gsrsearch dynamic.
                // The .md file also has gsrlimit=8 and gsroffset=0. I've made these dynamic.
            )

            val response = client.get(getUrl(parameters)) // Assuming getUrl constructs the full URL

            if (response.status.isSuccess()) {
                val allImageResponse: AllImageResponse =
                    response.body() // Assuming AllImageResponse is appropriate

                // The response structure for generator=search is similar to other list queries
                val images = allImageResponse.query?.pages
                    ?.flatMap { page ->
                        val imageInfoList = page.imageinfo ?: emptyList()
                        imageInfoList.mapNotNull { imageinfo ->
                            // Create the MainImage object
                            val mainImage = imageinfo.toMainImage().copy()
                            // Filter for valid image types (jpeg, png)
                            if (mainImage.url.isNotBlank() &&
                                (mainImage.url.endsWith("jpeg", ignoreCase = true) ||
                                        mainImage.url.endsWith("jpg", ignoreCase = true) ||
                                        mainImage.url.endsWith("png", ignoreCase = true))
                            ) {
                                mainImage
                            } else {
                                null
                            }
                        }
                    } ?: emptyList()

                // If you need to handle continuation (e.g., allImageResponse.continueX?.gsrcontinue),
                // you'd return a custom class containing both the list of images and the continuation token.
                return images
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status}: $errorBody")
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during fetchImageForDepicted for term '$searchTerm': ${e.message}",
                e,
            )
        }
    }

    // In /home/mshdabiola/StudioProjects/WikipediaCommons/modules/network/src/commonMain/kotlin/com/mshdabiola/network/MediaDataSource.kt
// ... (other imports)
// ...
// Assuming WIKI_DATA_URL_BASE is defined, e.g., "https://www.wikidata.org/w/api.php"
// You might need to add this constant or get it from your environment/config.
// For now, I'll use a placeholder. You should replace this.
    private val WIKI_DATA_BASE_URL = "https://www.wikidata.org/w/api.php"

    // ... (inside MediaDataSource class)
    override suspend fun getEntity(ids: String): WikiDataEntityResponse? { // ids can be a single ID or multiple IDs pipe-separated (e.g., "Q42|Q1")
        try {
            // The .md file has {{WikiDataUrl}}?format=json&action=wbgetentities&ids
            // We'll construct this using Ktor's URL builder for clarity with the base URL.

            val response = client.get(WIKI_DATA_BASE_URL) {
                url {
                    // protocol = URLProtocol.HTTPS // Ktor usually infers this from the base URL string
                    // host = "www.wikidata.org" // Ktor usually infers this
                    // encodedPath = "/w/api.php" // Ktor usually infers this
                    parameters.append("action", "wbgetentities")
                    parameters.append("format", "json")
                    parameters.append("ids", ids)
                    // Add other necessary parameters from wbgetentities documentation if needed,
                    // e.g., props, languages, etc. The .md file is minimal.
                }
            }

            if (response.status.isSuccess()) {
                val entityResponse: WikiDataEntityResponse = response.body()
                return entityResponse.takeIf { it.success == 1 && it.entities != null }
            } else {
                val errorBody: String = response.body()
                throw IOException("API request failed with status ${response.status} for entity IDs '$ids': $errorBody")
            }
        } catch (e: Exception) {
            // This includes SerializationException if the response doesn't match WikiDataEntityResponse
            throw NetworkDataSourceException(
                "An unexpected error occurred during getEntity for IDs '$ids': ${e.message}",
                e,
            )
        }
    }

    // In /home/mshdabiola/StudioProjects/WikipediaCommons/modules/network/src/commonMain/kotlin/com/mshdabiola/network/MediaDataSource.kt
// ... (other imports)
// ... (inside MediaDataSource class)
    override suspend fun getWikiText(titles: String): String? { // Returning String? for the wikitext content, or you could return WikiTextRevision?
        try {
            val parameters = parametersOf(
                "action" to listOf("query"),
                "format" to listOf("json"),
                "formatversion" to listOf("2"), // As specified in the .md file
                "errorformat" to listOf("plaintext"), // As specified
                "prop" to listOf("revisions"),
                "rvprop" to listOf("content|timestamp"), // content and timestamp for the revision
                "rvlimit" to listOf("1"), // Get only the latest revision
                "converttitles" to listOf(""), // Optional: can be used to normalize titles
                "titles" to listOf(titles), // The page title(s) to fetch
            )

            val response =
                client.get(getUrl(parameters)) // Assuming getUrl constructs the full URL from BaseUrl

            if (response.status.isSuccess()) {
                val queryResponse: WikiTextQueryResponse = response.body()

                // Extract the content from the first page and first revision
                val page = queryResponse.query?.pages?.firstOrNull { it.missing != true }
                val revisionContent = page?.revisions?.firstOrNull()?.content

                return revisionContent
            } else {
                val errorBody: String = response.body()
                // Log the error or handle it more gracefully depending on requirements
                println("API request for getWikiText failed with status ${response.status}: $errorBody")
                // Optionally, you could try to parse a MediaWiki error response if errorformat=plaintext isn't enough
                // For now, return null on error status after success check
                return null
            }
        } catch (e: Exception) {
            throw NetworkDataSourceException(
                "An unexpected error occurred during getWikiText for titles '$titles': ${e.message}",
                e,
            )
        }
    }

}

@Serializable
data class WikiDataEntityResponse(
    val entities: Map<String, WikiDataEntity>? = null, // Key is the entity ID (e.g., "Q42")
    val success: Int? = null,
)

@Serializable
data class WikiDataEntity(
    val id: String,
    val type: String? = null,
    val labels: Map<String, WikiDataLanguageValue>? = null, // e.g., "en" -> { language: "en", value: "Douglas Adams" }
    val descriptions: Map<String, WikiDataLanguageValue>? = null,
    val aliases: Map<String, List<WikiDataLanguageValue>>? = null,
    val claims: Map<String, List<WikiDataClaim>>? = null, // P31 (instance of), P18 (image), etc.
    @SerialName("sitelinks")
    val siteLinks: Map<String, WikiDataSiteLink>? = null, // e.g., "enwiki" -> { site: "enwiki", title: "Douglas Adams" }
)

@Serializable
data class WikiDataLanguageValue(
    val language: String,
    val value: String,
)

@Serializable
data class WikiDataClaim(
    val mainsnak: WikiDataSnak? = null,
    val type: String? = null,
    val rank: String? = null,
    // Qualifiers and references can be added here if needed
)

@Serializable
data class WikiDataSnak(
    val snaktype: String, // "value", "novalue", "somevalue"
    val property: String? = null, // e.g., "P18" for image
    val datatype: String? = null,
    @SerialName("datavalue")
    val dataValue: WikiDataDataValue? = null,
)

@Serializable
data class WikiDataDataValue(
    val value: String? = null, // Can be a string, number, or a more complex object
    val type: String, // "string", "wikibase-entityid", "globecoordinate", "time", "quantity", etc.
    // For entity IDs, 'value' might be an object like: { "entity-type": "item", "numeric-id": 123, "id": "Q123" }
    // For strings, 'value' is just the string.
)

@Serializable
data class WikiDataSiteLink(
    val site: String, // e.g., "enwiki"
    val title: String, // Page title on that site
    val badges: List<String>? = emptyList(),
)

@Serializable
data class WikiTextQueryResponse(
    val batchcomplete: Boolean? = null,
    val query: WikiTextQuery? = null,
)

@Serializable
data class WikiTextQuery(
    val pages: List<WikiTextPage>? = null, // formatversion=2 typically gives a list
    // If not using formatversion=2, pages might be a Map<String, WikiTextPage>
    // where the key is the page ID.
)

@Serializable
data class WikiTextPage(
    val pageid: Long? = null,
    val ns: Int? = null,
    val title: String? = null,
    val revisions: List<WikiTextRevision>? = null,
    val missing: Boolean? = null, // Present if the page does not exist
)

@Serializable
data class WikiTextRevision(
    val contentformat: String? = null, // e.g., "text/x-wiki"
    val contentmodel: String? = null, // e.g., "wikitext"
    val content: String? = null, // This is the actual wikitext
    val timestamp: String? = null, // ISO 8601 format
)
