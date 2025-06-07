package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PageExistenceResponse(
    @SerialName("batchcomplete")
    val batchcomplete: String? = null, // Can be an empty string if complete
    @SerialName("query")
    val query: PageCheckQuery? = null
)
