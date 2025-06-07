package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PageCheckPageInfo(
    @SerialName("pageid")
    val pageid: Int? = null, // Typically positive if the page exists
    @SerialName("ns")
    val ns: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("missing")
    val missing: Boolean? = null, // True if the page does not exist
    @SerialName("invalid")
    val invalid: Boolean? = null // True if the title itself is invalid
)
