package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FileExistenceShaQuery(
    @SerialName("allimages") val allimages: List<ShaImageInfo>,
)
