package com.mshdabiola.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryX(
    @SerialName("pages")
    val pages: List<PageX>?
)