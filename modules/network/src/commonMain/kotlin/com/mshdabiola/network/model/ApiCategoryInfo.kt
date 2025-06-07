package com.mshdabiola.network.model
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiCategoryInfo(
    val size: Int? = null,
    val pages: Int? = null,
    val files: Int? = null,
    val subcats: Int? = null
)
