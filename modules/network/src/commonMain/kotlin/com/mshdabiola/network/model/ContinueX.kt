package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ContinueX(
    @SerialName("continue")
    val continueX: String?,
    @SerialName("gsroffset")
    val gsroffset: Int?,
)
