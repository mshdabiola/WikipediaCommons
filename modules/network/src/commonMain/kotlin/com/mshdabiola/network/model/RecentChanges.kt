package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecentChangesResponse(
    val batchcomplete: Boolean? = null,
    @SerialName("continue")
    val continueData: ContinueData? = null,
    val query: QueryPages<RecentChangePageInfo>? = null,
)

// Generic QueryPages structure for formatversion=2 where 'pages' is an array
@Serializable
data class QueryPages<T>(
    val pages: List<T>? = null,
)

@Serializable
data class RecentChangePageInfo(
    val pageid: Long,
    val ns: Int,
    val title: String,
    // Minimal info based on generator=categorymembers without additional props for this specific call
    // If other props were added to the API call, they would be here.
)

// Common ContinueData, specific continue tokens might vary by generator/prop
@Serializable
data class ContinueData(
    val gcmcontinue: String? = null, // For categorymembers generator
    val rvcontinue: String? = null, // For revision queries
    @SerialName("continue")
    val continueValue: String? = null, // General continue parameter
)

@Serializable
data class FirstRevisionResponse(
    val batchcomplete: Boolean? = null,
    val query: QueryPages<PageWithRevisions>? = null, // QueryPages is reused
)

@Serializable
data class PageWithRevisions(
    val pageid: Long? = null, // Can be null if page is missing
    val ns: Int? = null,
    val title: String? = null,
    val revisions: List<RevisionInfo>? = null,
    val missing: String? = null, // Present (empty string) if page is missing
    val invalid: String? = null,  // Present (empty string) if title is invalid
    val invalidreason: String? = null, // Reason if title is invalid
)

@Serializable
data class RevisionInfo(
    val revid: Long,
    val parentid: Long = 0,
    val user: String,
    val timestamp: String, // ISO 8601 format
    // ids is also requested by rvprop=ids, revid is part of that.
)

@Serializable
data class FileUsageResponse(
    val batchcomplete: Boolean? = null,
    val query: QueryPages<PageWithUsageInfo>? = null, // QueryPages is reused
    @SerialName("continue")
    val continueData: FileUsageContinueData? = null,
)

@Serializable
data class FileUsageContinueData(
    val fucontinue: String? = null, // For fileusage
    val gucontinue: String? = null, // For globalusage
    @SerialName("continue")
    val generalContinue: String? = null,
)

@Serializable
data class PageWithUsageInfo(
    val pageid: Long? = null, // Can be null if page is missing
    val ns: Int? = null,
    val title: String? = null,
    val fileusage: List<FileUsageEntry>? = null,
    val globalusage: List<GlobalUsageEntry>? = null,
    val missing: String? = null, // Present if page is missing
    val invalid: String? = null,  // Present if title is invalid
    val invalidreason: String? = null, // Reason if title is invalid
    val imagerepository: String? = null, // "local" or "shared" (e.g. "commons")
)

@Serializable
data class FileUsageEntry(
    // Local usage
    val pageid: Long? = null,
    val ns: Int,
    val title: String,
)

@Serializable
data class GlobalUsageEntry(
    val wiki: String,
    val pageid: Long? = null,
    val ns: Int,
    val title: String,
    val url: String? = null,
)