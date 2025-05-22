package com.mshdabiola.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewImage(
    @SerialName("batchcomplete")
    val batchcomplete: Boolean?,
    @SerialName("continue")
    val continueX: ContinueX?,
    @SerialName("query")
    val query: QueryX?
)