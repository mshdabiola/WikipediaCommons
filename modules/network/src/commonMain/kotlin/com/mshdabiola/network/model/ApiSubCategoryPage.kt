package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiSubCategoryPage(
    val pageid: Long,
    val ns: Int,
    val title: String
    // Add other fields from 'prop=info' if needed, e.g., touched, lastrevid
)