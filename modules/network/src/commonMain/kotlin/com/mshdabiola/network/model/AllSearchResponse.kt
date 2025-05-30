package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AllSearchResponse(
    @SerialName("batchcomplete")
    val batchcomplete: Boolean?,
    @SerialName("continue")
    val continueX: ContinueX?,
    @SerialName("query")
    val query: Query,
)
