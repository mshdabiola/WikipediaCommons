import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadResponseWrapper(
    val upload: UploadResult? = null,
    val error: UploadError? = null,
)

@Serializable
data class UploadResult(
    val result: String? = null,
    val filename: String? = null,
    val imageinfo: UploadImageInfo? = null,
    val filekey: String? = null,
    val sessionkey: String? = null,
    val warnings: Map<String, UploadWarningDetail>? = null,
    val offset: Long? = null,
    val status: String? = null,
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
    @SerialName("*")
    val message: String? = null,
    val code: String? = null,
)

@Serializable
data class UploadError(
    val code: String? = null,
    val info: String? = null,
    @SerialName("*")
    val starInfo: String? = null,
    val details: String? = null,
    val messages: List<UploadErrorMessage>? = null,
    val status: Int? = null,
    val sessionkey: String? = null,
)

@Serializable
data class UploadErrorMessage(
    val name: String? = null,
    val message: String? = null,
    val html: UploadErrorHtmlContent? = null,
    val parameters: List<String>? = null,
)

@Serializable
data class UploadErrorHtmlContent(
    @SerialName("*")
    val content: String? = null,
)
