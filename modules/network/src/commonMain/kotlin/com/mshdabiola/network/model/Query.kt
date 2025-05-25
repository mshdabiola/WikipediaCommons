package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Query(
    @SerialName("pages")
    var pages: List<Page> = listOf(),
)
