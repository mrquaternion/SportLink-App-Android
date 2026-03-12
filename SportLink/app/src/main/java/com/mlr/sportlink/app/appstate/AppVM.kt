package com.mlr.sportlink.app.appstate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mlr.sportlink.app.views.RootTab

enum class BookingsTab {
    HOSTING,
    GOING,
    BOOKMARKED,
}

enum class DisplayMode {
    LIST,
    MAP,
}

class AppVM {
    var selectedTab by mutableStateOf(RootTab.DISCOVER)
    var selectedBookingsTab by mutableStateOf(BookingsTab.HOSTING)
    var selectedDiscoverMode by mutableStateOf(DisplayMode.LIST)
    var isCreatePresented by mutableStateOf(false)
}
