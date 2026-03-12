package com.mlr.sportlink.app.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlr.sportlink.R
import com.mlr.sportlink.ui.theme.SportLinkTheme

@Composable
fun CustomTabBar(
    selectedTab: RootTab,
    onTabSelected: (RootTab) -> Unit,
    onCreateRequested: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TabBarButton(
                tab = RootTab.HOME,
                title = "Home",
                isSelected = selectedTab == RootTab.HOME,
                onClick = { onTabSelected(RootTab.HOME) },
            )
            TabBarButton(
                tab = RootTab.DISCOVER,
                title = "Discover",
                isSelected = selectedTab == RootTab.DISCOVER,
                onClick = { onTabSelected(RootTab.DISCOVER) },
            )
            TabBarButton(
                tab = RootTab.CREATE,
                title = "Create",
                isSelected = false,
                onClick = onCreateRequested,
            )
            TabBarButton(
                tab = RootTab.BOOKINGS,
                title = "Bookings",
                isSelected = selectedTab == RootTab.BOOKINGS,
                onClick = { onTabSelected(RootTab.BOOKINGS) },
            )
            TabBarButton(
                tab = RootTab.PROFILE,
                title = "Profile",
                isSelected = selectedTab == RootTab.PROFILE,
                onClick = { onTabSelected(RootTab.PROFILE) },
            )
        }
    }
}

@Composable
private fun RowScope.TabBarButton(
    tab: RootTab,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val inactiveColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Gray
    }

    val tint = if (isSelected) MaterialTheme.colorScheme.primary else inactiveColor

    Column(
        modifier = Modifier
            .weight(1f)
            .clickable(onClick = onClick)
            .padding(horizontal = 2.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Icon(
            painter = painterResource(id = tabIconRes(tab, isSelected)),
            contentDescription = title,
            tint = tint,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = title,
            color = tint,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

private fun tabIconRes(tab: RootTab, isSelected: Boolean): Int = when (tab) {
    RootTab.HOME -> if (isSelected) R.drawable.ic_tab_home_fill else R.drawable.ic_tab_home
    RootTab.DISCOVER -> if (isSelected) R.drawable.ic_tab_discover_fill else R.drawable.ic_tab_discover
    RootTab.CREATE -> if (isSelected) R.drawable.ic_tab_create_fill else R.drawable.ic_tab_create
    RootTab.BOOKINGS -> if (isSelected) R.drawable.ic_tab_dashboard_fill else R.drawable.ic_tab_dashboard
    RootTab.PROFILE -> if (isSelected) R.drawable.ic_tab_profile_fill else R.drawable.ic_tab_profile
}

@Preview(showBackground = true)
@Composable
private fun CustomTabBarPreview() {
    SportLinkTheme(dynamicColor = false) {
        var selectedTab by remember { mutableStateOf(RootTab.HOME) }
        CustomTabBar(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it },
            onCreateRequested = {},
        )
    }
}
