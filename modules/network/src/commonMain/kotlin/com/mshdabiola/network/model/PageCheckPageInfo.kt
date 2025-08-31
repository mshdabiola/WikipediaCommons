package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class PageCheckPageInfo(
    @SerialName("pageid")
    val pageid: Int? = null,
    @SerialName("ns")
    val ns: Int? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("missing")
    val missing: Boolean? = null,
    @SerialName("invalid")
    val invalid: Boolean? = null,
)
