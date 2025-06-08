import com.mshdabiola.network.model.FileUsageResponse
import com.mshdabiola.network.model.FirstRevisionResponse
import com.mshdabiola.network.model.RecentChangesResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import kotlinx.io.IOException

internal class ReviewSource(
    private val client: HttpClient,
    private val baseUrl: String = "https://en.wikipedia.org/w/api.php", // Configure properly
) : IReviewSource {

    override suspend fun getRecentChanges(
        category: String,
        limit: Int,
        continuation: String?,
    ): RecentChangesResponse {
        val response = client.get(baseUrl) {
            url {
                parameters.append("action", "query")
                parameters.append("format", "json")
                parameters.append("formatversion", "2")
                parameters.append("generator", "categorymembers")
                parameters.append("gcmtype", "file")
                parameters.append("gcmsort", "timestamp")
                parameters.append("gcmdir", "desc")
                parameters.append("gcmtitle", "Category:$category")
                parameters.append("gcmlimit", limit.toString())
                continuation?.let { parameters.append("gcmcontinue", it) }
            }
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed for getRecentChanges with status ${response.status}: $errorBody")
        }
    }

    override suspend fun getFirstRevisionOfFile(
        fileTitle: String,
    ): FirstRevisionResponse {
        val response = client.get(baseUrl) {
            url {
                parameters.append("action", "query")
                parameters.append("format", "json")
                parameters.append("formatversion", "2")
                parameters.append("prop", "revisions")
                parameters.append("rvprop", "timestamp|ids|user")
                parameters.append("rvdir", "newer")
                parameters.append("rvlimit", "1")
                parameters.append("titles", fileTitle)
            }
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed for getFirstRevisionOfFile with status ${response.status}: $errorBody")
        }
    }

    override suspend fun getFileUsage(
        fileTitle: String,
        fuContinue: String?,
        guContinue: String?,
    ): FileUsageResponse {
        val response = client.get(baseUrl) {
            url {
                parameters.append("action", "query")
                parameters.append("format", "json")
                parameters.append("formatversion", "2")
                parameters.append(
                    "prop",
                    "fileusage|globalusage",
                ) 
                parameters.append("titles", fileTitle)
                fuContinue?.let { parameters.append("fucontinue", it) }
                guContinue?.let { parameters.append("gucontinue", it) }
            }
        }
        if (response.status.isSuccess()) {
            return response.body()
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed for getFileUsage with status ${response.status}: $errorBody")
        }
    }
}