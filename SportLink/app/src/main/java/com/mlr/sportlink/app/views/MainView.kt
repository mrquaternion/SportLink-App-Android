package com.mlr.sportlink.app.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlr.sportlink.app.appstate.AppVM
import com.mlr.sportlink.app.appstate.UserSession
import com.mlr.sportlink.ui.theme.SportLinkTheme

enum class RootTab {
    HOME,
    DISCOVER,
    CREATE,
    BOOKINGS,
    PROFILE,
}

@Composable
fun MainView(
    onLogout: () -> Unit = {},
) {
    val appVM = remember { AppVM() }
    val session = remember { UserSession() }

    LaunchedEffect(session) {
        session.start()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        if (session.isReady) {
            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    when (appVM.selectedTab) {
                        RootTab.HOME -> AppPlaceholderScreen(
                            title = "Home",
                            subtitle = "The Home tab is ready for your upcoming Android port.",
                        )
                        RootTab.DISCOVER -> AppPlaceholderScreen(
                            title = "Discover",
                            subtitle = "Map and list discovery will plug into the tab shell here.",
                        )
                        RootTab.CREATE -> Unit
                        RootTab.BOOKINGS -> AppPlaceholderScreen(
                            title = "Bookings",
                            subtitle = "Hosted, going, and bookmarked activities will live here.",
                        )
                        RootTab.PROFILE -> AppPlaceholderScreen(
                            title = "Profile",
                            subtitle = "Profile, settings, and edit flows will surface here.",
                            actionLabel = "Logout",
                            onAction = onLogout,
                        )
                    }
                }

                CustomTabBar(
                    selectedTab = appVM.selectedTab,
                    onTabSelected = { appVM.selectedTab = it },
                    onCreateRequested = { appVM.isCreatePresented = true },
                )
            }

            if (appVM.isCreatePresented) {
                CreateOverlay(
                    onDismiss = { appVM.isCreatePresented = false },
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun AppPlaceholderScreen(
    title: String,
    subtitle: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Black,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
            modifier = Modifier.padding(top = 10.dp),
        )
        if (actionLabel != null && onAction != null) {
            Button(
                onClick = onAction,
                modifier = Modifier.padding(top = 22.dp),
            ) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
private fun CreateOverlay(
    onDismiss: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.92f))
            .padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            tonalElevation = 3.dp,
            shadowElevation = 10.dp,
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    text = "Create Activity",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "The create flow is mounted from the App shell and ready for the full screen port.",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                )
                Button(onClick = onDismiss) {
                    Text("Close")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainViewPreview() {
    SportLinkTheme(dynamicColor = false) {
        MainView()
    }
}
