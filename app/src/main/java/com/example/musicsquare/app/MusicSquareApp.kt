package com.example.musicsquare.app

import MultiThemePreviews
import MusicItem
import MusicSquareNavHost
import TopLevelDestination
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.musicsquare.core.data.music.Music
import com.example.musicsquare.feature.listening.ListeningDestination

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MusicSquareApp(appState: MusicSquareAppState = rememberMusicSquareAppState()) {
    Scaffold(bottomBar = {
        Box {
            if (appState.currentDestination?.route.toString() in TopLevelDestination.routes) {
                Column {
                    Box(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        MusicItem(
                            Music.mock,
                            navigateToListening = {
                                appState.navController.navigate(
                                    ListeningDestination.ROUTE
                                )
                            },
                        )
                    }
                    MusicSquareBottomBar(
                        destinations = appState.topLevelDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.testTag("MusicSquareBottomBar"),
                    )
                }
            }
        }
    }) {
        MusicSquareNavHost(
            navController = appState.navController
        )
    }
}

@Composable
fun MusicSquareBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    MusicSquareNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            MusicSquareNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    val icon = if (selected) {
                        destination.selectedIcon
                    } else {
                        destination.unselectedIcon
                    }
                    Icon(imageVector = icon, contentDescription = null)
                },
                label = { Text(destination.title) },
            )
        }
    }
}

@Composable
fun RowScope.MusicSquareNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    selectedIcon: @Composable () -> Unit = icon,
    enabled: Boolean = true,
    label: @Composable (() -> Unit)? = null,
    alwaysShowLabel: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = if (selected) selectedIcon else icon,
        modifier = modifier,
        enabled = enabled,
        label = label,
        alwaysShowLabel = alwaysShowLabel,
    )
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

@Composable
fun MusicSquareNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        content = content,
    )
}

@MultiThemePreviews
@Composable
fun MusicSquareNavigationBarPreview() {
    MusicSquareNavigationBar {
        MusicSquareNavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
            label = { Text("Label") },
        )
    }
}
