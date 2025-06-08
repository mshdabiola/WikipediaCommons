import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadResponseWrapper(
    val upload: UploadResult? = null,
    val error: UploadError? = null, // General API error structure
)

@Serializable
data class UploadResult(
    val result: String? = null, // e.g., "Success", "Warning", "Continue"
    val filename: String? = null,
    val imageinfo: UploadImageInfo? = null, // Populated on successful final upload of an image
    val filekey: String? = null,    // Key for stashed/chunked uploads
    val sessionkey: String? = null, // Older name for filekey
    val warnings: Map<String, UploadWarningDetail>? = null,
    val offset: Long? = null,       // For chunked uploads, the new offset if result is "Continue"
    val status: String? = null,      // e.g. "200"
)

// Simplified ImageInfo specific to upload response, can be expanded
@Serializable
data class UploadImageInfo(
    val timestamp: String? = null,
    val user: String? = null,
    val userid: Long? = null,
    val url: String? = null,
    val descriptionurl: String? = null,
    val size: Long? = null,
    val width: Int? = null,
    val height: Int? = null,
    val canonicaltitle: String? = null,
    val sha1: String? = null,
    val mime: String? = null,
    val mediatype: String? = null,
    val bitdepth: Int? = null,
)

@Serializable
data class UploadWarningDetail(
    @SerialName("*") // MediaWiki often uses '*' for the main message
    val message: String? = null,
    val code: String? = null, // e.g. 'exists'
)


@Serializable
data class UploadError(
    val code: String? = null,
    val info: String? = null,
    @SerialName("*")
    val starInfo: String? = null,
    val details: String? = null,
    val messages: List<UploadErrorMessage>? = null,
    val status: Int? = null, // HTTP status if available in error payload
    val sessionkey: String? = null, // filekey might also be returned in error if a chunk failed
)

@Serializable
data class UploadErrorMessage(
    val name: String? = null,
    val message: String? = null, // This is the main error text for the message
    val html: UploadErrorHtmlContent? = null, // If error provides HTML content
    val parameters: List<String>? = null,
)

@Serializable
data class UploadErrorHtmlContent(
    @SerialName("*")
    val content: String? = null,
)