package com.mlr.sportlink.core.models.local

// Mechanical first-pass port generated from Utilities/Extensions/Sport+Extension.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

enum class Sport(
    val jsonDecodingName: String,
    val displayName: String,
    val icon: String,
    val emoji: String,
    val placeholderAsset: String,
) {
    SOCCER("soccer", "soccer", "soccerball", "⚽️", "soccer_placeholder"),
    BASKETBALL("basketball", "basketball", "basketball.fill", "🏀", "basketball_placeholder"),
    TENNIS("tennis", "tennis", "tennisball.fill", "🎾", "tennis_placeholder"),
    FOOTBALL("football", "football", "american.football.fill", "🏈", "football_placeholder"),
    VOLLEYBALL("volleyball", "volleyball", "volleyball.fill", "🏐", "volleyball_placeholder"),
    RUGBY("rugby", "rugby", "rugbyball.fill", "🏉", "rugby_placeholder"),
    BASEBALL("balle", "baseball", "baseball.fill", "⚾️", "baseball_placeholder"),
    PINGPONG("ping-pong", "ping-pong", "figure.table.tennis", "🏓", "pingpong_placeholder"),
    PETANQUE("pétanque", "pétanque", "target", "🎯", "petanque_placeholder");

    companion object {
        fun fromJsonName(value: String): Sport =
            entries.firstOrNull { it.jsonDecodingName.equals(value, ignoreCase = true) } ?: SOCCER
    }
}
