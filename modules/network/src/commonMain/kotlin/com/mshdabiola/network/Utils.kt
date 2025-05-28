package com.mshdabiola.network

import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url

expect val BASE_URL_HOST: String

fun getUrl(parameters: Parameters): Url {
    return URLBuilder(
        protocol = URLProtocol.HTTPS,
        host = BASE_URL_HOST,
        parameters = parameters,
        pathSegments = listOf("w", "api.php"),
    ).build()
}
