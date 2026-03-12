package com.mlr.sportlink.core.models.local

// Mechanical first-pass port generated from Core/Models/Local/Infrastructure.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

import android.graphics.Bitmap

data class GeoCoordinate(
    val latitude: Double,
    val longitude: Double,
)

data class Infrastructure(
    val id: String,
    val parkIndex: String,
    val coordinates: GeoCoordinate,
    val sports: List<Sport>,
    var eventsCount: Int = 0,
)

sealed class InfrastructureImageAsset {
    abstract val id: String

    data class Remote(
        override val id: String,
        val image: Bitmap,
    ) : InfrastructureImageAsset()

    data class Placeholder(
        val sport: Sport?,
    ) : InfrastructureImageAsset() {
        override val id: String = "placeholder"
    }
}
