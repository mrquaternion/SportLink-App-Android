package com.mlr.sportlink.core.models.firestore

// Mechanical first-pass port generated from Core/Models/Firestore/ActivityDTO.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

import com.mlr.sportlink.core.models.local.Activity
import com.mlr.sportlink.core.models.local.ActivityFinishedState
import com.mlr.sportlink.core.models.local.ActivityState
import com.mlr.sportlink.core.models.local.Sport
import com.mlr.sportlink.core.models.local.TimeWindow
import com.mlr.sportlink.core.models.local.toUserID
import java.util.Date

data class ActivityDTO(
    var id: String? = null,
    var title: String,
    var organizerId: String,
    var infrastructureId: String,
    var sport: String,
    var date: TimeWindow,
    var numberOfPlayers: Int,
    var playerIds: List<String>,
    var description: String,
    var status: Int,
    var areInvitationsOpen: Boolean,
    var lastMessageText: String? = null,
    var lastMessageAt: Date? = null,
    var finishedStatus: String,
) {
    fun toActivity(): Activity = Activity(
        id = id,
        title = title,
        organizerID = organizerId.toUserID,
        infrastructureID = infrastructureId,
        sport = Sport.fromJsonName(sport).jsonDecodingName,
        date = date,
        numberOfPlayers = numberOfPlayers,
        players = playerIds.map { it.toUserID },
        description = description,
        areInvitationsOpen = areInvitationsOpen,
        lastMessageText = lastMessageText,
        lastMessageAt = lastMessageAt,
        status = ActivityState.fromRawValue(status),
        finishedStatus = ActivityFinishedState.fromRawValue(finishedStatus),
    )
}
