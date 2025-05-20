package com.mshdabiola.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllImageReponse(
    @SerialName("batchcomplete")
    val batchcomplete: Boolean,
    @SerialName("continue")
    val continueX: Continue,
    @SerialName("query")
    val query: Query
)