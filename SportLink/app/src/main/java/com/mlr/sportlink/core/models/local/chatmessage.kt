package com.mlr.sportlink.core.models.local

// Mechanical first-pass port generated from Core/Models/Local/ChatMessage.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

import java.util.Date

data class ChatMessage(
    var id: String? = null,
    val senderId: UserID,
    val content: String,
    val timestamp: Date,
    val imageURL: String? = null,
)

data class MessageID(val value: String)

val String.toMessageID: MessageID
    get() = MessageID(this)
