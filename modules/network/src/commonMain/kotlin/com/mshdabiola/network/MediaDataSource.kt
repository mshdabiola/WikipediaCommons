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
    }

    override suspend fun search(
        title: String,
        page: Int,
        limit: Int,
    ): List<MainImage> {
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
    }

    override suspend fun checkPageExists(title: String): Boolean {
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
    }

    override suspend fun checkFileExistsBySha(sha1: String): Boolean {
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
    }

    override suspend fun getMediaListFromCategory(
        category: String,
        limit: Int,
        continuation: String?,
    ): List<MainImage> {
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
            return images
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed with status ${response.status}: $errorBody")
        }
    }

    override suspend fun getMediaListBySearchTerm(searchTerm: String, limit: Int, offset: Int): List<MainImage> {
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
            val allImageResponse: AllImageResponse = response.body()
            return allImageResponse.query?.pages
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
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed with status ${response.status}: $errorBody")
        }
    }


    override suspend fun getMediaDetails(title: String): MainImage? {
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

            return if (imageInfo != null) {
                val mainImage = imageInfo.toMainImage() 
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
            throw IOException("API request failed with status ${response.status}: $errorBody")
        }
    }


    override suspend fun getMediaListFromGeoSearch(
        latitude: Double,
        longitude: Double,
        limit: Int,
        radius: Int,
        continuation: String?,
    ): List<MainImage> { 
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
                parameters.plus(parametersOf("continuation" to listOf(it))) 
        }

        val response = client.get(getUrl(parameters))

        if (response.status.isSuccess()) {
            val allImageResponse: AllImageResponse = response.body()
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
            return images
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed with status ${response.status}: $errorBody")
        }
    }

    override suspend fun getMedia(title: String): MainImage? { 
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

        val response = client.get(getUrl(parameters))

        if (response.status.isSuccess()) {
            val imageResponse: AllImageResponse = response.body()
            val page = imageResponse.query?.pages?.firstOrNull()
            val imageInfo = page?.imageinfo?.firstOrNull()

            return if (imageInfo != null) {
                imageInfo.toMainImage().copy(
                )
            } else {
                null
            }
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed with status ${response.status}: $errorBody")
        }
    }

    override suspend fun getMediaSuppressErrors(title: String): MainImage? {
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
                )
            } else {
                null
            }
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed with status ${response.status}: $errorBody")
        }
    }

    override suspend fun getMediaById(pageId: Long): MainImage? { 
        val parameters = parametersOf(
            "action" to listOf("query"),
            "format" to listOf("json"),
            "formatversion" to listOf("2"),
            "prop" to listOf("imageinfo", "coordinates"),
            "iiprop" to listOf("url", "extmetadata", "user"),
            "iiurlwidth" to listOf("640"),
            "iiextmetadatafilter" to listOf("DateTime|Categories|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"),
            "pageids" to listOf(pageId.toString()), 
        )
        
        val response = client.get(getUrl(parameters))

        if (response.status.isSuccess()) {
            val imageResponse: AllImageResponse = response.body() 
            val page = imageResponse.query?.pages?.firstOrNull()
            val imageInfo = page?.imageinfo?.firstOrNull()

            return if (imageInfo != null) {
                imageInfo.toMainImage().copy( )
            } else {
                null
            }
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed with status ${response.status}: $errorBody")
        }
    }

    override suspend fun fetchImageForDepicted(
        searchTerm: String, 
        limit: Int,
        offset: Int,
    ): List<MainImage> { 
        val parameters = parametersOf(
            "action" to listOf("query"),
            "format" to listOf("json"),
            "formatversion" to listOf("2"),
            "prop" to listOf("imageinfo", "coordinates"),
            "iiprop" to listOf("url", "extmetadata", "user"),
            "iiurlwidth" to listOf("640"),
            "iiextmetadatafilter" to listOf("DateTime|Categories|GPSLatitude|GPSLongitude|ImageDescription|DateTimeOriginal|Artist|LicenseShortName|LicenseUrl"),
            "generator" to listOf("search"),
            "gsrsearch" to listOf(searchTerm), 
            "gsrlimit" to listOf(limit.toString()),
            "gsroffset" to listOf(offset.toString()),
        )

        val response = client.get(getUrl(parameters)) 

        if (response.status.isSuccess()) {
            val allImageResponse: AllImageResponse = response.body() 
            val images = allImageResponse.query?.pages
                ?.flatMap { page ->
                    val imageInfoList = page.imageinfo ?: emptyList()
                    imageInfoList.mapNotNull { imageinfo ->
                        val mainImage = imageinfo.toMainImage().copy()
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
            return images
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed with status ${response.status}: $errorBody")
        }
    }
    
    private val WIKI_DATA_BASE_URL = "https://www.wikidata.org/w/api.php"

    override suspend fun getEntity(ids: String): WikiDataEntityResponse? { 
        val response = client.get(WIKI_DATA_BASE_URL) {
            url {
                parameters.append("action", "wbgetentities")
                parameters.append("format", "json")
                parameters.append("ids", ids)
            }
        }

        if (response.status.isSuccess()) {
            val entityResponse: WikiDataEntityResponse = response.body()
            return entityResponse.takeIf { it.success == 1 && it.entities != null }
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed with status ${response.status} for entity IDs '$ids': $errorBody")
        }
    }

    override suspend fun getWikiText(titles: String): String? { 
        val parameters = parametersOf(
            "action" to listOf("query"),
            "format" to listOf("json"),
            "formatversion" to listOf("2"), 
            "errorformat" to listOf("plaintext"), 
            "prop" to listOf("revisions"),
            "rvprop" to listOf("content|timestamp"), 
            "rvlimit" to listOf("1"), 
            "converttitles" to listOf(""), 
            "titles" to listOf(titles), 
        )

        val response = client.get(getUrl(parameters))

        if (response.status.isSuccess()) {
            val queryResponse: WikiTextQueryResponse = response.body()
            val page = queryResponse.query?.pages?.firstOrNull { it.missing != true }
            val revisionContent = page?.revisions?.firstOrNull()?.content
            return revisionContent
        } else {
            val errorBody: String = response.body()
            // Log the error or handle it more gracefully depending on requirements
            println("API request for getWikiText failed with status ${response.status}: $errorBody")
            // Optionally, you could try to parse a MediaWiki error response if errorformat=plaintext isn't enough
            // For now, return null on error status after success check
            // To strictly follow "no try-catch" and propagate errors, we should throw here.
            // However, the original code returned null. For now, I'll keep it as throwing IOException.
            throw IOException("API request for getWikiText failed with status ${response.status}: $errorBody")
        }
    }

}

@Serializable
data class WikiDataEntityResponse(
    val entities: Map<String, WikiDataEntity>? = null, 
    val success: Int? = null,
)

@Serializable
data class WikiDataEntity(
    val id: String,
    val type: String? = null,
    val labels: Map<String, WikiDataLanguageValue>? = null, 
    val descriptions: Map<String, WikiDataLanguageValue>? = null,
    val aliases: Map<String, List<WikiDataLanguageValue>>? = null,
    val claims: Map<String, List<WikiDataClaim>>? = null, 
    @SerialName("sitelinks")
    val siteLinks: Map<String, WikiDataSiteLink>? = null, 
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
)

@Serializable
data class WikiDataSnak(
    val snaktype: String, 
    val property: String? = null, 
    val datatype: String? = null,
    @SerialName("datavalue")
    val dataValue: WikiDataDataValue? = null,
)

@Serializable
data class WikiDataDataValue(
    val value: String? = null, 
    val type: String, 
)

@Serializable
data class WikiDataSiteLink(
    val site: String, 
    val title: String, 
    val badges: List<String>? = emptyList(),
)

@Serializable
data class WikiTextQueryResponse(
    val batchcomplete: Boolean? = null,
    val query: WikiTextQuery? = null,
)

@Serializable
data class WikiTextQuery(
    val pages: List<WikiTextPage>? = null, 
)

@Serializable
data class WikiTextPage(
    val pageid: Long? = null,
    val ns: Int? = null,
    val title: String? = null,
    val revisions: List<WikiTextRevision>? = null,
    val missing: Boolean? = null, 
)

@Serializable
data class WikiTextRevision(
    val contentformat: String? = null, 
    val contentmodel: String? = null, 
    val content: String? = null, 
    val timestamp: String? = null, 
)
