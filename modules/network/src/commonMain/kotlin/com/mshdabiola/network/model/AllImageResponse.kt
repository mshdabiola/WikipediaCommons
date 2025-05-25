package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AllImageResponse(
    @SerialName("batchcomplete")
    var batchcomplete: Boolean = false,
    @SerialName("continue")
    var continueX: Continue = Continue(),
    @SerialName("query")
    var query: Query = Query(),
)
