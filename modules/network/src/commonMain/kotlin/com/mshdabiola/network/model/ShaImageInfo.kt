package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShaImageInfo(
    @SerialName("name") val name: String?,
    @SerialName("timestamp") val timestamp: String?,
    @SerialName("sha1") val sha1: String?,
    // Add other fields if you need them from the allimages list items
)
