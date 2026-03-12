package com.mlr.sportlink.shared.views

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mlr.sportlink.core.models.local.Activity
import com.mlr.sportlink.core.models.local.ActivityState
import com.mlr.sportlink.core.models.local.InfrastructureImageAsset
import com.mlr.sportlink.core.models.local.Sport
import com.mlr.sportlink.core.models.local.TimeWindow
import com.mlr.sportlink.core.models.local.UserID
import com.mlr.sportlink.ui.theme.SportLinkTheme
import java.util.Date
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.mlr.sportlink.R

enum class ActivitySource {
    DISCOVER,
    HOSTING,
    JOINED,
}

val LocalHideJoinButton = compositionLocalOf { false }
val LocalExtendedDate = compositionLocalOf { false }
val LocalActivitySource = compositionLocalOf { ActivitySource.DISCOVER }

@Composable
fun ActivityCardRow(
    modifier: Modifier = Modifier,
    activity: Activity,
    parkName: String,
    distanceLabel: String,
    imageAssets: List<InfrastructureImageAsset> = emptyList(),
    isFavorite: Boolean = false,
    isJoining: Boolean = false,
    onSeeMore: () -> Unit = {},
    onJoin: () -> Unit = {},
    onBookmarkToggle: () -> Unit = {},
) {
    val hideJoinButton = LocalHideJoinButton.current
    val extendedDate = LocalExtendedDate.current

    val displayedStatus = when {
        activity.status == ActivityState.CANCELLED -> ActivityState.CANCELLED
        activity.numberOfPlayers - activity.players.size <= 0 -> ActivityState.FULL
        else -> ActivityState.OPEN
    }

    val spotsLeft = maxOf(activity.numberOfPlayers - activity.players.size, 0)
    val spotsLabel = if (spotsLeft == 0) {
        "No spot left"
    } else {
        "$spotsLeft ${if (spotsLeft == 1) "spot" else "spots"} left"
    }

    val cannotJoin = activity.status == ActivityState.FULL || spotsLeft == 0
    val (date, startTime, endTime) = activity.date.displayFormat
    val dateLabel = if (extendedDate) "$date, $startTime - $endTime" else "$startTime - $endTime"
    val currentAsset = imageAssets.firstOrNull()

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSeeMore),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp,
        shadowElevation = 2.dp,
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = 20.dp,
                        )
                    ),
            ) {
                when (currentAsset) {
                    is InfrastructureImageAsset.Remote -> {
                        Image(
                            bitmap = currentAsset.image.asImageBitmap(),
                            contentDescription = activity.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    is InfrastructureImageAsset.Placeholder, null -> {
                        PlaceholderSportHero(sportJsonName = activity.sport)
                    }
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.92f))
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "📍 $distanceLabel",
                        color = Color.White,
                        fontSize = 12.sp,
                    )
                }

                if (!hideJoinButton) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
                            .clickable(onClick = onBookmarkToggle),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = if (isFavorite) {
                                painterResource(id = R.drawable.ic_bookmark_filled)
                            } else {
                                painterResource(id = R.drawable.ic_bookmark_outline)
                            },
                            contentDescription = if (isFavorite) "Remove bookmark" else "Bookmark",
                            tint = if (isFavorite) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.92f),
                            modifier = Modifier.size(22.dp),
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp, bottom = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(displayedStatus.displayColor),
                    )
                    Text(
                        text = spotsLabel,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light,
                        color = displayedStatus.displayColor,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = Sport.fromJsonName(activity.sport).emoji,
                        fontSize = 24.sp,
                        modifier = Modifier.padding(top = 8.dp),
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(
                            text = activity.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                        )

                        Text(
                            text = parkName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            maxLines = 1,
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = dateLabel,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    onClick = onSeeMore,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = "See more",
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                if (!hideJoinButton && !cannotJoin) {
                    VerticalDivider(
                        modifier = Modifier.height(50.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.85f),
                    )

                    TextButton(
                        onClick = onJoin,
                        enabled = !isJoining,
                        modifier = Modifier.weight(1f),
                    ) {
                        if (isJoining) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text(
                                text = "Join",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }
        }
    }
}

private val ActivityState.displayColor: Color
    get() = when (this) {
        ActivityState.OPEN -> Color(0xFF4CAF50)
        ActivityState.FULL -> Color(0xFFF44336)
        ActivityState.CANCELLED -> Color(0xFFF44336)
    }

@Composable
private fun PlaceholderSportHero(sportJsonName: String) {
    val sport = Sport.fromJsonName(sportJsonName)
    val backgroundRes = sportBackgroundRes(sport)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = backgroundRes),
            contentDescription = "${sport.displayName} background",
            modifier = Modifier
                .fillMaxSize()
                .blur(1.dp),
            contentScale = ContentScale.Crop,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.18f)),
        )
    }
}

private fun sportBackgroundRes(sport: Sport): Int = when (sport) {
    Sport.SOCCER -> R.drawable.bg_soccer_min
    Sport.BASKETBALL -> R.drawable.bg_basketball_min
    Sport.TENNIS -> R.drawable.bg_tennis_min
    Sport.FOOTBALL -> R.drawable.bg_football_min
    Sport.VOLLEYBALL -> R.drawable.bg_volleyball_min
    Sport.RUGBY -> R.drawable.bg_football_min
    Sport.BASEBALL -> R.drawable.bg_baseball_min
    Sport.PINGPONG -> R.drawable.bg_pingpong_min
    Sport.PETANQUE -> R.drawable.bg_petanque_min
}

private val mockActivity = Activity(
    id               = "preview-1",
    title            = "5v5 Football",
    sport            = "soccer",
    infrastructureID = "infra-1",
    organizerID      = UserID("organizer-1"),
    numberOfPlayers  = 10,
    players          = listOf(UserID("uid1"), UserID("uid2"), UserID("uid3")),
    status           = ActivityState.OPEN,
    description      = "",
    areInvitationsOpen = true,
    date             = TimeWindow(
        start = Date(),                              // now
        end   = Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000), // +2 hours
    ),
)

private val mockActivityFull = mockActivity.copy(
    title   = "Basketball pickup",
    sport   = "basketball",
    players = List(10) { UserID("uid$it") },
    status  = ActivityState.FULL,
)

private val mockActivityNotFull = mockActivity.copy(
    title   = "Basketball pickup",
    sport   = "basketball",
    players = List(5) { UserID("uid$it") },
    status  = ActivityState.OPEN,
)

// ─── Previews ────────────────────────────────────────────────────────────────
@Preview(
    name = "Full – no Join button",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewActivityCardFull() {
    SportLinkTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        CompositionLocalProvider(
            LocalHideJoinButton provides false,
            LocalExtendedDate   provides false,
            LocalActivitySource provides ActivitySource.DISCOVER,
        ) {
            ActivityCardRow(
                modifier      = Modifier.padding(16.dp),
                activity      = mockActivityFull,
                parkName      = "Complexe sportif Claude-Robillard",
                distanceLabel = "3.4 km away",
            )
        }
    }
}

@Preview(
    name = "Not Full – Join button",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewActivityCardNotFull() {
    SportLinkTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        var isFavorite by remember { mutableStateOf(false) }

        CompositionLocalProvider(
            LocalHideJoinButton provides false,
            LocalExtendedDate   provides false,
            LocalActivitySource provides ActivitySource.DISCOVER,
        ) {
            ActivityCardRow(
                modifier      = Modifier.padding(16.dp),
                activity      = mockActivityNotFull,
                parkName      = "Complexe sportif Claude-Robillard",
                distanceLabel = "3.4 km away",
                isFavorite = isFavorite,
                onBookmarkToggle = { isFavorite = !isFavorite },
            )
        }
    }
}
