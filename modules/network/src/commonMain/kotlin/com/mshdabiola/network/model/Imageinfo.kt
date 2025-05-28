package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Imageinfo(
    @SerialName("canonicaltitle")
    var canonicaltitle: String = "",
    @SerialName("descriptionshorturl")
    var descriptionshorturl: String = "",
    @SerialName("descriptionurl")
    var descriptionurl: String = "",
    @SerialName("mime")
    var mime: String = "",
    @SerialName("sha1")
    var sha1: String = "",
    @SerialName("url")
    var url: String = "",
    @SerialName("user")
    var user: String = "",
)
