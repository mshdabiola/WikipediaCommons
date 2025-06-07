package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PageCheckQuery(
    @SerialName("pages")
    val pages: List<PageCheckPageInfo>? = null
)
