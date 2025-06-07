package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
data class SubCategoryInfo(
    val pageid: Long,
    val ns: Int,
    val title: String
)