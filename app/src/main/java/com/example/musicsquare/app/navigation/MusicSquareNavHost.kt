import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicsquare.feature.home.Home
import com.example.musicsquare.feature.home.HomeDestination
import com.example.musicsquare.feature.listening.Listening
import com.example.musicsquare.feature.listening.ListeningDestination
import com.example.musicsquare.feature.my_library.MyLibrary
import com.example.musicsquare.feature.my_library.MyLibraryDestination
import com.example.musicsquare.feature.search.Search
import com.example.musicsquare.feature.search.SearchDestination

@Composable
fun MusicSquareNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = HomeDestination.route
    ) {
        composable(HomeDestination.route) {
            Home(
                navigateToListening = { navController.navigate(ListeningDestination.route) },
            )
        }
        composable(SearchDestination.route) {
            Search(
                navigateToListening = { navController.navigate(ListeningDestination.route) },
            )
        }
        composable(MyLibraryDestination.route) {
            MyLibrary(
                navigateToListening = { navController.navigate(ListeningDestination.route) },
            )
        }
        composable(ListeningDestination.route) {
            Listening(
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}
