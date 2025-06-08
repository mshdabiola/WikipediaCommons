package com.mshdabiola.network

import com.mshdabiola.network.model.AllNotificationsResponse
import com.mshdabiola.network.model.MarkReadApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.get
import io.ktor.client.request.forms.submitForm
import io.ktor.http.Parameters
import io.ktor.http.isSuccess
import kotlinx.io.IOException

internal class NotificationDataSource(
    private val client: HttpClient,
    private val baseUrl: String = "https://en.wikipedia.org/w/api.php", // Placeholder - Configure properly in your DI or build config
) : INotificationDataSource {

    override suspend fun getAllNotifications(
        wikis: String?,
        filter: String?,
        limit: String?,
        continueToken: String?,
    ): AllNotificationsResponse {
        val response = client.get(baseUrl) {
            url {
                // Common parameters for most MediaWiki API calls
                parameters.append("action", "query")
                parameters.append("format", "json")
                parameters.append("formatversion", "2")
                parameters.append("errorformat", "plaintext") // Consistent error handling

                // Specific parameters for fetching notifications
                parameters.append("meta", "notifications")
                parameters.append("notformat", "model") // Requests structured notification data

                // Optional parameters based on function arguments
                wikis?.takeIf { it.isNotBlank() }?.let { parameters.append("notwikis", it) }
                filter?.takeIf { it.isNotBlank() }?.let { parameters.append("notfilter", it) }
                limit?.takeIf { it.isNotBlank() }?.let { parameters.append("notlimit", it) }
                continueToken?.takeIf { it.isNotBlank() }
                    ?.let { parameters.append("notcontinue", it) }
            }
        }

        if (response.status.isSuccess()) {
            return response.body<AllNotificationsResponse>()
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed for getAllNotifications with status ${response.status}: $errorBody")
        }
    }

    override suspend fun markNotificationsAsRead(
        token: String,
        unreadList: String, // Comma or pipe separated list of notification IDs
    ): MarkReadApiResponse {
        // This request uses POST with form parameters as per @MarkRead.md's body.urlEncoded section.
        // Query parameters for action, format etc., are part of the URL.
        val response = client.submitForm(
            url = "$baseUrl?action=echomarkread&format=json&formatversion=2&errorformat=plaintext",
            formParameters = Parameters.build {
                append("token", token)
                append("unreadlist", unreadList)
                // The 'csrf_token' or a similar token is often required for state-changing operations.
                // The 'token' parameter here likely refers to that.
            },
        )

        if (response.status.isSuccess()) {
            return response.body<MarkReadApiResponse>()
        } else {
            val errorBody: String = response.body()
            throw IOException("API request failed for markNotificationsAsRead with status ${response.status}: $errorBody")
        }
    }
}