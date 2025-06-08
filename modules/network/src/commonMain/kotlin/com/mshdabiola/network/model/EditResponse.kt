package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
data class EditResponseWrapper( // Renaming to avoid conflict if EditResponse is used elsewhere
    val edit: EditResult? = null,
    val error: EditError? = null // For potential API errors
)

@Serializable
data class EditResult(
    val result: String? = null, // e.g., "Success", "Failure"
    val pageid: Int? = null,
    val title: String? = null,
    val contentmodel: String? = null,
    val oldrevid: Long? = null,
    val newrevid: Long? = null,
    val newtimestamp: String? = null,
    val nochange: String? = null // Present if no change was made
)

@Serializable
data class EditError(
    val code: String? = null,
    val info: String? = null,
//    @Serializable(with = kotlinx.serialization.json.JsonElementSerializer::class)
//    val more_info: kotlinx.serialization.json.JsonElement? = null, // Can be complex
    val warning: String? = null
)


@Serializable
data class SetLabelResponseWrapper(
    val entity: EntityLabelInfo? = null,
    val success: Int? = null, // Typically 1 on success for wbset* actions
    val error: EditError? = null, // Reusing EditError for common error structure
)

@Serializable
data class EntityLabelInfo(
    val id: String? = null,
    val type: String? = null,
    val lastrevid: Long? = null,
    val labels: Map<String, LabelValue>? = null, // Language code -> LabelValue
)

@Serializable
data class LabelValue(
    val language: String? = null,
    val value: String? = null,
)

