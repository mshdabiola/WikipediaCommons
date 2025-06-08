package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
data class MarkReadApiResponse(
    val batchcomplete: String? = null,
    val echomarkread: EchoMarkReadResult? = null
)

@Serializable
data class EchoMarkReadResult(
    val result: String? = null, // "success" or "failure"
    val list: List<String>? = null, // List of notification IDs that were marked as read
    val count: Int? = null, // Number of notifications marked as read
    val error: EchoMarkReadError? = null // Present on failure
)

@Serializable
data class EchoMarkReadError(
    val code: String? = null,
    val info: String? = null,
    val docs: String? = null
)
