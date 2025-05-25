package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Continue(
    @SerialName("continue")
    var continueX: String = "",
    @SerialName("grncontinue")
    var grncontinue: String = "",
)
