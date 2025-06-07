package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiSubCategoryQuery(
    val pages: List<ApiSubCategoryPage>? = null
)