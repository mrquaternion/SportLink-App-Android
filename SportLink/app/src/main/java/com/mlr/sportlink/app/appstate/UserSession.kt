package com.mlr.sportlink.app.appstate

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mlr.sportlink.core.models.local.Activity
import com.mlr.sportlink.core.models.local.Sport
import com.mlr.sportlink.core.models.local.User

data class UserBestStats(
    val level: Int = 1,
    val points: Double = 0.0,
    val levelThreshold: Double = 100.0,
    val sport: Sport = Sport.SOCCER,
)

data class TeammateProfileData(
    val id: String,
    val name: String,
)

class UserSession {
    var avatar by mutableStateOf<Bitmap?>(null)
    var user by mutableStateOf<User?>(null)
    var activities by mutableStateOf<List<Activity>>(emptyList())
    var bestStats by mutableStateOf<UserBestStats?>(null)
    var teammatesInfo by mutableStateOf<List<TeammateProfileData>>(emptyList())
    var isReady by mutableStateOf(false)

    private var hasStarted = false

    fun start() {
        if (hasStarted) return
        hasStarted = true
        bestStats = UserBestStats()
        isReady = true
    }

    fun logout() {
        avatar = null
        user = null
        activities = emptyList()
        bestStats = null
        teammatesInfo = emptyList()
        isReady = false
        hasStarted = false
    }
}
