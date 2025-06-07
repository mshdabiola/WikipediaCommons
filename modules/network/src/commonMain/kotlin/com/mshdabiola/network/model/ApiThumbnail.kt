package com.mshdabiola.network.model

import kotlinx.serialization.Serializable

@Serializable
internal data class ApiThumbnail(
    val source: String,
    val width: Int,
    val height: Int
)
