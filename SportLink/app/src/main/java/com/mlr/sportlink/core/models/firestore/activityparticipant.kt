package com.mlr.sportlink.core.models.firestore

// Mechanical first-pass port generated from Core/Models/Firestore/ActivityParticipant.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

import com.mlr.sportlink.core.models.local.UserID
import java.util.Date

data class ActivityParticipant(
    var id: String? = null,
    val userId: UserID,
    var unreadCount: Int,
    var lastReadAt: Date,
)
