import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.LibraryMusic
import androidx.compose.material.icons.rounded.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val title: String,
    val route: String,
) {
    HOME(
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        title = "Home",
        route = "home",
    ),
    SEARCH(
        selectedIcon = Icons.Rounded.Search,
        unselectedIcon = Icons.Outlined.Search,
        title = "Search",
        route = "search",
    ),
    MY_LIBRARY(
        selectedIcon = Icons.Rounded.LibraryMusic,
        unselectedIcon = Icons.Outlined.LibraryMusic,
        title = "My Library",
        route = "my_library",
    );

    companion object {
        val routes = entries.map { it.route }
    }
}
