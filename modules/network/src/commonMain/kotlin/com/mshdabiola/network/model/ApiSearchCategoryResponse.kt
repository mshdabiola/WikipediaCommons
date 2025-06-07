package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiSearchCategoryResponse(
    val batchcomplete: Boolean? = null,
    // Search with offset typically doesn't use the 'continue' block
    val query: ApiSearchCategoryQuery? = null
)
