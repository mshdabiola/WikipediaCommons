package com.mshdabiola.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Page(
    @SerialName("imageinfo")
    val imageinfo: List<Imageinfo>,
    @SerialName("imagerepository")
    val imagerepository: String,
    @SerialName("ns")
    val ns: Int,
    @SerialName("pageid")
    val pageid: Int,
    @SerialName("title")
    val title: String
)