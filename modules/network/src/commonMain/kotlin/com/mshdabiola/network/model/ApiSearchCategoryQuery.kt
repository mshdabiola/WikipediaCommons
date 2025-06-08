package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiSearchCategoryQuery(
    val pages: List<ApiSearchCategoryPage>? = null,
)
