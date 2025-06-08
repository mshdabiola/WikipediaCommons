package com.mshdabiola.network

import com.mshdabiola.network.model.AllNotificationsResponse
import com.mshdabiola.network.model.MarkReadApiResponse

interface INotificationDataSource {
    /**
     * Fetches all notifications based on the provided filters.
     * Corresponds to @AllNoti.md
     */
    suspend fun getAllNotifications(
        wikis: String?, // e.g., "wikidatawiki|commonswiki|enwiki"
        filter: String?, // e.g., "!read" or "read"
        limit: String?, // e.g., "max" or a number
        continueToken: String?
    ): AllNotificationsResponse

    /**
     * Marks a list of notification IDs as read.
     * Corresponds to @MarkRead.md
     * Note: @MarkRead.md specifies this as a GET request in its metadata,
     * but has `body.urlEncoded` parameters, which typically implies a POST request.
     * Ktor's `submitForm` will be used for POST with form parameters.
     * If it truly must be GET with body params, Ktor might require custom setup.
     * Assuming standard POST for form data.
     */
    suspend fun markNotificationsAsRead(
        token: String,
        unreadList: String // Comma or pipe separated list of notification IDs
    ): MarkReadApiResponse
}
