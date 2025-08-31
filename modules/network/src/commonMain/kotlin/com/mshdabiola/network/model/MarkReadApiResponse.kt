package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
data class MarkReadApiResponse(
    val batchcomplete: String? = null,
    val echomarkread: EchoMarkReadResult? = null,
)

@Serializable
data class EchoMarkReadResult(
    val result: String? = null,
    val list: List<String>? = null,
    val count: Int? = null,
    val error: EchoMarkReadError? = null,
)

@Serializable
data class EchoMarkReadError(
    val code: String? = null,
    val info: String? = null,
    val docs: String? = null,
)
