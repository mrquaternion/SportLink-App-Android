package com.mlr.sportlink.core.models.local

// Mechanical first-pass port generated from Core/Models/Local/Park.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

import java.util.UUID

data class Park(
    val id: String = UUID.randomUUID().toString(),
    val index: String,
    val name: String?,
    val polygon: List<GeoCoordinate>,
    val infrastructuresIDs: List<String>,
    var totalEventsCount: Int = 0,
)
