package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class WikibaseApiError(
    val code: String? = null,
    val info: String? = null,
    @SerialName("*")
    val starInfo: String? = null,
    val messages: List<WikibaseMessage>? = null,
)

@Serializable
data class WikibaseMessage(
    val name: String? = null,
    val parameters: List<String>? = null,
    val html: WikibaseHtmlContent? = null,
)

@Serializable
data class WikibaseHtmlContent(
    @SerialName("*")
    val content: String? = null,
)

@Serializable
data class WikibaseEditEntityResponse(
    val entity: WikibaseEntityInfo? = null,
    val success: Int? = null, // Typically 1 on success
    val error: WikibaseApiError? = null,
)

@Serializable
data class WikibaseEntityInfo(
    val type: String? = null,
    val id: String? = null,
    val lastrevid: Long? = null,
    val title: String? = null, // For entities that are also pages, like MediaInfo
)

@Serializable
data class WikibaseQueryFileEntityResponse(
    val batchcomplete: String? = null,
    val query: QueryPages<FileEntityPageDetails>? = null,
    val error: WikibaseApiError? = null,
)

@Serializable
data class FileEntityPageDetails(
    val pageid: Long? = null,
    val ns: Int? = null,
    val title: String? = null,
    val lastrevid: Long? = null,
    val length: Long? = null,
    val contentmodel: String? = null,
    val pagelanguage: String? = null,
    val protection: List<ProtectionInfo>? = null,
    val missing: String? = null, // Present (empty string) if page is missing
    val invalid: String? = null, // Present (empty string) if title is invalid
    val invalidreason: String? = null,
    // Note: The original .md for FileEntityById.md requests prop=info.
    // It doesn't request imageinfo specifically.
    // If imageinfo is needed, prop=imageinfo should be added to the request.
)

@Serializable
data class ProtectionInfo(
    val type: String? = null,
    val level: String? = null,
    val expiry: String? = null,
)

@Serializable
data class WikibaseSetLabelResponse(
    val entity: WikibaseLabelEntityInfo? = null,
    val success: Int? = null,
    val error: WikibaseApiError? = null,
)

@Serializable
data class WikibaseLabelEntityInfo(
    val id: String? = null,
    val type: String? = null,
    val lastrevid: Long? = null,
    val labels: Map<String, LabelDetail>? = null,
)

@Serializable
data class LabelDetail(
    val language: String? = null,
    val value: String? = null,
    val removed: String? = null, // Present if the label was removed
)

@Serializable
data class WikibaseGetClaimsResponse(
    val claims: Map<String, List<Claim>>? = null, // Property ID -> List of Claims
    val success: Int? = null, // Typically 1 if request is processed, even if no claims
    val error: WikibaseApiError? = null,
)

@Serializable
data class Claim(
    val id: String,
    val mainsnak: Snak,
    val type: String, // "statement"
    val rank: String, // "preferred", "normal", "deprecated"
    val qualifiers: Map<String, List<Snak>>? = null,
    @SerialName("qualifiers-order")
    val qualifiersOrder: List<String>? = null,
    val references: List<Reference>? = null,
)

@Serializable
data class Snak(
    val snaktype: String,
    val property: String,
    val hash: String? = null,
    val datavalue: JsonElement? = null, // Use JsonElement for flexibility
    val datatype: String? = null,
)

@Serializable
data class Reference(
    val hash: String,
    val snaks: Map<String, List<Snak>>,
    @SerialName("snaks-order")
    val snaksOrder: List<String>,
)