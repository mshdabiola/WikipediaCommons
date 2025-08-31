import com.mshdabiola.network.model.FileUsageResponse
import com.mshdabiola.network.model.FirstRevisionResponse
import com.mshdabiola.network.model.RecentChangesResponse

interface IReviewSource {
    /**
     * Fetches recent changes from a specific category.
     * Corresponds to @RecentChange.md
     */
    suspend fun getRecentChanges(
        category: String,
        // e.g., "Uploaded_with_Mobile/Android"
        limit: Int = 50,
        continuation: String? = null,
    ): RecentChangesResponse

    /**
     * Fetches the first revision details for a given file title.
     * Corresponds to @FirstRevisionOfFile.md
     */
    suspend fun getFirstRevisionOfFile(fileTitle: String): FirstRevisionResponse

    /**
     * Fetches local and global usage for a given file title.
     * Corresponds to @GlobalUsage.md
     */
    suspend fun getFileUsage(
        fileTitle: String,
        fuContinue: String? = null,
        guContinue: String? = null,
    ): FileUsageResponse
}
