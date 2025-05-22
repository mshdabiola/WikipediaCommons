package com.mshdabiola.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContinueX(
    @SerialName("continue")
    val continueX: String?,
    @SerialName("grncontinue")
    val grncontinue: String?
)