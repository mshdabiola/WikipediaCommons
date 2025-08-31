package com.mshdabiola.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllNotificationsResponse(
    val batchcomplete: String? = null,
    val query: QueryNotifications? = null,
)

@Serializable
data class QueryNotifications(
    val notifications: NotificationsResult? = null,
)

@Serializable
data class NotificationsResult(
    val list: List<NotificationModelApi>? = null,
    @SerialName("continue")
    val continueN: String? = null,
)

@Serializable
data class NotificationModelApi(
    val wiki: String? = null,
    val id: String? = null,
    val type: String? = null,
    val category: String? = null,
    val section: String? = null,
    @SerialName("*")
    val contentPayload: NotificationContentPayload? = null,
    val timestamp: NotificationTimestamp? = null,
    val agent: NotificationAgent? = null,
    val read: String? = null,
    val reverted: String? = null,
)

@Serializable
data class NotificationContentPayload(
    val header: String? = null,
    val compactHeader: String? = null,
    val body: String? = null,
    val icon: String? = null,
    val links: NotificationLinks? = null,
    val title: NotificationTitle? = null,
)

@Serializable
data class NotificationTitle(
    val full: String? = null,
    val text: String? = null,
    val url: String? = null,
    val namespace: Int? = null,
    val exists: Boolean? = null,
)

@Serializable
data class NotificationTimestamp(
    val mw: String? = null,
    val iso8601: String? = null,
    val unix: Long? = null,
    val date: String? = null,
    val time: String? = null,
)

@Serializable
data class NotificationAgent(
    val id: Int? = null,
    val name: String? = null,
)

@Serializable
data class NotificationLinks(
    val primary: NotificationLink? = null,
    val secondary: List<NotificationLink>? = null,
)

@Serializable
data class NotificationLink(
    val url: String,
    val label: String,
    val description: String? = null,
    val icon: String? = null,
    val type: String? = null,
)
