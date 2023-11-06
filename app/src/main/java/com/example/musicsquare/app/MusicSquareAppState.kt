package com.example.musicsquare.app

import TopLevelDestination
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.musicsquare.feature.home.HomeDestination
import com.example.musicsquare.feature.my_library.MyLibraryDestination
import com.example.musicsquare.feature.search.SearchDestination

@Composable
fun rememberMusicSquareAppState(
    navController: NavHostController = rememberNavController(),
): MusicSquareAppState {
    return remember(navController) {
        MusicSquareAppState(navController)
    }
}

@Stable
class MusicSquareAppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            HomeDestination.route -> TopLevelDestination.HOME
            SearchDestination.route -> TopLevelDestination.SEARCH
            MyLibraryDestination.route -> TopLevelDestination.MY_LIBRARY
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigate(HomeDestination.route)
            TopLevelDestination.SEARCH -> navController.navigate(SearchDestination.route)
            TopLevelDestination.MY_LIBRARY -> navController.navigate(MyLibraryDestination.route)
        }
    }
}
