package com.mshdabiola.network

import com.mshdabiola.network.model.EditResponseWrapper
import com.mshdabiola.network.model.SetLabelResponseWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import kotlinx.io.IOException

internal class EditDataSource(
    private val client: HttpClient,
    private val baseUrl: String = "https://en.wikipedia.org/w/api.php",
) : IEditDataSource {
    override suspend fun postEdit(
        title: String,
        summary: String,
        text: String,
        token: String,
    ): EditResponseWrapper {
        val response =
            client.submitForm(
                url = "$baseUrl?action=edit&format=json&formatversion=2",
                formParameters =
                    Parameters.build {
                        append("title", title)
                        append("summary", summary)
                        append("text", text)
                        append("token", token)
                    },
            )

        if (response.status.isSuccess()) {
            return response.body<EditResponseWrapper>()
        } else {
            val errorBody: String = response.body()
//            try {
//                val errorResponse = response.body<EditResponseWrapper>()
//                if (errorResponse.error != null) return errorResponse
//            } catch (e: Exception) {
//                // If parsing the error response fails, throw with the raw body.
//            }
            throw IOException("API request failed for postEdit with status ${response.status}: $errorBody")
        }
    }

    override suspend fun postCreatePage(
        title: String,
        summary: String,
        text: String,
        contentFormat: String,
        contentModel: String,
        isMinorEdit: Boolean?,
        recreatePage: Boolean?,
        token: String,
    ): EditResponseWrapper {
        val response =
            client.submitForm(
                url = "$baseUrl?action=edit&format=json&formatversion=2",
                formParameters =
                    Parameters.build {
                        append("title", title)
                        append("summary", summary)
                        append("text", text)
                        append("contentformat", contentFormat)
                        append("contentmodel", contentModel)
                        isMinorEdit?.let { if (it) append("minor", "true") }
                        recreatePage?.let { if (it) append("recreate", "true") }
                        append("token", token)
                    },
            )

        if (response.status.isSuccess()) {
            return response.body<EditResponseWrapper>()
        } else {
            val errorBody: String = response.body()
//            try {
//                val errorResponse = response.body<EditResponseWrapper>()
//                if (errorResponse.error != null) return errorResponse
//            } catch (_: Exception) {
//                // If parsing the error response fails, throw with the raw body.
//            }
            throw IOException("API request failed for postCreatePage with status ${response.status}: $errorBody")
        }
    }

    override suspend fun postAppendText(
        title: String,
        summary: String,
        appendText: String,
        token: String,
    ): EditResponseWrapper {
        val response =
            client.submitForm(
                url = "$baseUrl?action=edit&format=json&formatversion=2",
                formParameters =
                    Parameters.build {
                        append("title", title)
                        append("summary", summary)
                        append("appendtext", appendText)
                        append("token", token)
                    },
            )
        if (response.status.isSuccess()) {
            return response.body<EditResponseWrapper>()
        } else {
            val errorBody: String = response.body()
            try {
                val errorResponse = response.body<EditResponseWrapper>()
                if (errorResponse.error != null) return errorResponse
            } catch (_: Exception) {
            }
            throw IOException("API request failed for postAppendText with status ${response.status}: $errorBody")
        }
    }

    override suspend fun postPrependText(
        title: String,
        summary: String,
        prependText: String,
        token: String,
    ): EditResponseWrapper {
        val response =
            client.submitForm(
                url = "$baseUrl?action=edit&format=json&formatversion=2",
                formParameters =
                    Parameters.build {
                        append("title", title)
                        append("summary", summary)
                        append("prependtext", prependText)
                        append("token", token)
                    },
            )
        if (response.status.isSuccess()) {
            return response.body<EditResponseWrapper>()
        } else {
            val errorBody: String = response.body()
            try {
                val errorResponse = response.body<EditResponseWrapper>()
                if (errorResponse.error != null) return errorResponse
            } catch (_: Exception) {
            }
            throw IOException("API request failed for postPrependText with status ${response.status}: $errorBody")
        }
    }

    override suspend fun postNewSection(
        title: String,
        summary: String,
        sectionTitle: String,
        text: String,
        token: String,
    ): EditResponseWrapper {
        val response =
            client.submitForm(
                url = "$baseUrl?action=edit&section=new&format=json&formatversion=2",
                formParameters =
                    Parameters.build {
                        append("title", title)
                        append("summary", summary)
                        append("sectiontitle", sectionTitle)
                        append("text", text)
                        append("token", token)
                    },
            )
        if (response.status.isSuccess()) {
            return response.body<EditResponseWrapper>()
        } else {
            val errorBody: String = response.body()
            try {
                val errorResponse = response.body<EditResponseWrapper>()
                if (errorResponse.error != null) return errorResponse
            } catch (_: Exception) {
            }
            throw IOException("API request failed for postNewSection with status ${response.status}: $errorBody")
        }
    }

    override suspend fun postCaption(
        title: String,
        language: String,
        value: String,
        summary: String?,
        token: String,
    ): SetLabelResponseWrapper {
        val effectiveUrl = "$baseUrl?action=wbsetlabel&format=json&formatversion=2&site=commonswiki"

        val actualResponse =
            client.submitForm(
                url = effectiveUrl,
                formParameters =
                    Parameters.build {
                        append("title", title)
                        append("summary", summary ?: "")
                        append("language", language)
                        append("value", value)
                        append("token", token)
                    },
            )

        if (actualResponse.status.isSuccess()) {
            return actualResponse.body<SetLabelResponseWrapper>()
        } else {
            val errorBody: String = actualResponse.body()
            try {
                val errorResponse = actualResponse.body<SetLabelResponseWrapper>()
                if (errorResponse.error != null) return errorResponse
            } catch (_: Exception) {
            }
            throw IOException("API request failed for postCaption with status ${actualResponse.status}: $errorBody")
        }
    }
}
