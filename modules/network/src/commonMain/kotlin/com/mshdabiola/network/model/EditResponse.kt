package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
data class EditResponseWrapper(
    val edit: EditResult? = null,
    val error: EditError? = null,
)

@Serializable
data class EditResult(
    val result: String? = null,
    val pageid: Int? = null,
    val title: String? = null,
    val contentmodel: String? = null,
    val oldrevid: Long? = null,
    val newrevid: Long? = null,
    val newtimestamp: String? = null,
    val nochange: String? = null,
)

@Serializable
data class EditError(
    val code: String? = null,
    val info: String? = null,
//    @Serializable(with = kotlinx.serialization.json.JsonElementSerializer::class)
//    val more_info: kotlinx.serialization.json.JsonElement? = null, // Can be complex
    val warning: String? = null,
)

@Serializable
data class SetLabelResponseWrapper(
    val entity: EntityLabelInfo? = null,
    val success: Int? = null,
    val error: EditError? = null,
)

@Serializable
data class EntityLabelInfo(
    val id: String? = null,
    val type: String? = null,
    val lastrevid: Long? = null,
    val labels: Map<String, LabelValue>? = null,
)

@Serializable
data class LabelValue(
    val language: String? = null,
    val value: String? = null,
)
