package com.mshdabiola.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Page(
    @SerialName("imageinfo")
    var imageinfo: List<Imageinfo> = listOf(),
    @SerialName("imagerepository")
    var imagerepository: String = "",
    @SerialName("ns")
    var ns: Int = 0,
    @SerialName("pageid")
    var pageid: Int = 0,
    @SerialName("title")
    var title: String = ""
)