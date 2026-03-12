package com.mlr.sportlink.core.models.local

// Mechanical first-pass port generated from Core/Models/Local/User.kt.
// Platform-specific SwiftUI and Apple-framework behavior still needs Android-specific refinement.

data class UserStatistics(
    val sport: Sport,
    val totalScore: Int,
    val totalGames: Int,
    val won: Int,
    val lost: Int,
    val trend: List<String>,
) {
    companion object {
        fun fromMap(data: Map<String, Any?>): UserStatistics? {
            val sportRaw = data["sport"] as? String ?: return null
            return UserStatistics(
                sport = runCatching { Sport.valueOf(sportRaw.uppercase()) }.getOrElse { Sport.fromJsonName(sportRaw) },
                totalScore = (data["totalScore"] as? Number)?.toInt() ?: 0,
                totalGames = (data["totalGames"] as? Number)?.toInt() ?: 0,
                won = (data["won"] as? Number)?.toInt() ?: 0,
                lost = (data["lost"] as? Number)?.toInt() ?: 0,
                trend = (data["trend"] as? List<*>)?.filterIsInstance<String>().orEmpty(),
            )
        }
    }
}

data class UserID(val value: String)

data class User(
    var id: String? = null,
    val firstname: String,
    val lastname: String,
    val email: String,
    var photoURL: String,
    var city: String? = null,
    var bio: String? = null,
    var availabilities: Map<Int, List<String>> = emptyMap(),
    var favouriteSports: List<Sport> = emptyList(),
    var bookmarkedActivities: List<ActivityId> = emptyList(),
    var recentTeammates: Map<String, Int> = emptyMap(),
    var statistics: List<UserStatistics> = emptyList(),
    var lastSportPlayed: Sport? = null,
    var isOnboardingCompleted: Boolean = false,
    var hasSignedUpWithEmail: Boolean = false,
) {
    fun isFavourite(activityId: ActivityId): Boolean =
        bookmarkedActivities.any { it.value == activityId.value }
}

val String.toUserID: UserID
    get() = UserID(this)
