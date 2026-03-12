package com.mlr.sportlink.core.models.firestore

// Mechanical first-pass port generated from Core/Models/Firestore/AppNotification.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

import java.util.Date

data class AppNotification(
    var id: String? = null,
    val userId: String,
    val type: NotificationType,
    val activityId: String? = null,
    var status: NotificationStatus,
    val createdAt: Date,
) {
    val title: String
        get() = when (type) {
            NotificationType.ACTIVITY_FINISHED_CONFIRM_ORGANIZER -> "review this activity"
            NotificationType.ACTIVITY_INVITATION -> "someone invited you"
        }

    companion object {
        val mockSample = AppNotification(
            id = "",
            userId = "",
            type = NotificationType.ACTIVITY_FINISHED_CONFIRM_ORGANIZER,
            activityId = null,
            status = NotificationStatus.UNREAD,
            createdAt = Date(),
        )
    }
}

data class AppNotificationDTO(
    var id: String? = null,
    val userId: String,
    val type: String,
    val activityId: String? = null,
    var status: String,
    val createdAt: Date? = null,
) {
    fun toAppNotification(): AppNotification = AppNotification(
        id = id,
        userId = userId,
        type = NotificationType.fromRawValue(type),
        activityId = activityId,
        status = NotificationStatus.fromRawValue(status),
        createdAt = createdAt ?: Date(),
    )
}

enum class NotificationStatus(val rawValue: String) {
    UNREAD("unread"),
    READ("read"),
    CONFIRMED("confirmed");

    companion object {
        fun fromRawValue(value: String): NotificationStatus =
            entries.firstOrNull { it.rawValue == value } ?: UNREAD
    }
}

enum class NotificationType(val rawValue: String) {
    ACTIVITY_FINISHED_CONFIRM_ORGANIZER("ACTIVITY_FINISHED_CONFIRM_ORGANIZER"),
    ACTIVITY_INVITATION("ACTIVITY_INVITATION");

    val rowAction: NotificationRowAction
        get() = when (this) {
            ACTIVITY_FINISHED_CONFIRM_ORGANIZER -> NotificationRowAction.SHEET
            ACTIVITY_INVITATION -> NotificationRowAction.ACTIVITY_DETAILS
        }

    val totalPages: Int
        get() = when (this) {
            ACTIVITY_FINISHED_CONFIRM_ORGANIZER -> 3
            ACTIVITY_INVITATION -> 0
        }

    companion object {
        fun fromRawValue(value: String): NotificationType =
            entries.firstOrNull { it.rawValue == value } ?: ACTIVITY_FINISHED_CONFIRM_ORGANIZER
    }
}

enum class NotificationRowAction {
    SHEET,
    ACTIVITY_DETAILS,
}
