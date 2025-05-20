package com.mshdabiola.network.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Imageinfo(
    @SerialName("canonicaltitle")
    val canonicaltitle: String,
    @SerialName("descriptionshorturl")
    val descriptionshorturl: String,
    @SerialName("descriptionurl")
    val descriptionurl: String,
    @SerialName("mime")
    val mime: String,
    @SerialName("url")
    val url: String,
    @SerialName("user")
    val user: String
)