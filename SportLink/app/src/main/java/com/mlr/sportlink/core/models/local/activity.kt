package com.mlr.sportlink.core.models.local

// Mechanical first-pass port generated from Core/Models/Local/Activity.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

import com.mlr.sportlink.core.models.firestore.ActivityDTO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

enum class ActivityState(val rawValue: Int, val strVal: String) {
    OPEN(0, "open"),
    FULL(1, "full"),
    CANCELLED(2, "cancelled");

    companion object {
        fun fromRawValue(value: Int): ActivityState = entries.firstOrNull { it.rawValue == value } ?: OPEN
    }
}

enum class ActivityFinishedState(val rawValue: String) {
    ONGOING("ONGOING"),
    PENDING_CONFIRMATION("PENDING_CONFIRMATION"),
    CONFIRMED("HAS_BEEN_CONFIRMED"),
    PROCESSED("IS_PROCESSED");

    companion object {
        fun fromRawValue(value: String): ActivityFinishedState =
            entries.firstOrNull { it.rawValue == value } ?: ONGOING
    }
}

class ActivityError(message: String) : IllegalArgumentException(message) {
    companion object {
        val TitleTooLong = ActivityError("titleTooLong")
    }
}

data class ActivityId(val value: String)

data class TimeWindow(
    val start: Date,
    val end: Date,
) {
    val interval: ClosedRange<Date>
        get() {
            val realStart = if (start.before(end)) start else end
            val realEnd = if (start.after(end)) start else end
            return realStart..realEnd
        }

    val displayFormat: Triple<String, String, String>
        get() {
            val dateFormatter = SimpleDateFormat("MMM d", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("America/Toronto")
            }
            val timeFormatter = SimpleDateFormat("h:mm a", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("America/Toronto")
            }
            return Triple(
                dateFormatter.format(start),
                timeFormatter.format(start),
                timeFormatter.format(end),
            )
        }
}

data class Activity(
    var id: String? = null,
    var title: String,
    val organizerID: UserID,
    val infrastructureID: String,
    val sport: String,
    var date: TimeWindow,
    var numberOfPlayers: Int,
    var players: List<UserID>,
    var description: String,
    var areInvitationsOpen: Boolean,
    var lastMessageText: String? = null,
    var lastMessageAt: Date? = null,
    var status: ActivityState,
    var finishedStatus: ActivityFinishedState = ActivityFinishedState.ONGOING,
) {
    constructor(
        title: String,
        organizerId: UserID,
        infrastructureId: String,
        sport: Sport,
        date: ClosedRange<Date>,
        numberOfPlayers: Int,
        players: List<UserID>,
        description: String,
        status: ActivityState,
        areInvitationsOpen: Boolean,
        lastMessageText: String? = null,
        lastMessageAt: Date? = null,
        finishedStatus: ActivityFinishedState = ActivityFinishedState.ONGOING,
    ) : this(
        title = title,
        organizerID = organizerId,
        infrastructureID = infrastructureId,
        sport = sport.jsonDecodingName,
        date = TimeWindow(date.start, date.endInclusive),
        numberOfPlayers = numberOfPlayers,
        players = players,
        description = description,
        areInvitationsOpen = areInvitationsOpen,
        lastMessageText = lastMessageText,
        lastMessageAt = lastMessageAt,
        status = status,
        finishedStatus = finishedStatus,
    )

    fun toDTO(): ActivityDTO = ActivityDTO(
        id = id,
        title = title,
        organizerId = organizerID.value,
        infrastructureId = infrastructureID,
        sport = sport,
        date = date,
        numberOfPlayers = numberOfPlayers,
        playerIds = players.map { it.value },
        description = description,
        status = status.rawValue,
        areInvitationsOpen = areInvitationsOpen,
        lastMessageText = lastMessageText,
        lastMessageAt = lastMessageAt,
        finishedStatus = finishedStatus.rawValue,
    )

    fun formattedTimeWindow(): Pair<String, String> {
        val formatter = SimpleDateFormat("h:mm a", Locale.US)
        return formatter.format(date.start) to formatter.format(date.end)
    }
}

val String.toActivityID: ActivityId
    get() = ActivityId(this)
