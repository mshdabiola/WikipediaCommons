package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiSubCategoryResponse(
    val batchcomplete: Boolean? = null,
    @SerialName("continue")
    val continueResponse: Continue? = null,
    val query: ApiSubCategoryQuery? = null,
)
