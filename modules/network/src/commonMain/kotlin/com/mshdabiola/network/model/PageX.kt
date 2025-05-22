package com.mshdabiola.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageX(
    @SerialName("imageinfo")
    val imageinfo: List<ImageinfoX>?,
    @SerialName("imagerepository")
    val imagerepository: String?,
    @SerialName("ns")
    val ns: Int?,
    @SerialName("pageid")
    val pageid: Int?,
    @SerialName("title")
    val title: String?
)