package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileExistenceShaResponse(
    @SerialName("batchcomplete") val batchcomplete: Boolean? = null,
    @SerialName("query") val query: FileExistenceShaQuery? = null,
)
