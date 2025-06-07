package com.mshdabiola.network.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CategoryInfo(
    val sortkey: String? = null,
    @SerialName("*")
    val title: String? = null,
    val hidden: Boolean? = null
)