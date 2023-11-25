import android.os.Build
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MusicSquareNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier, navController = navController, startDestination = HomeDestination.ROUTE
    ) {
        composable(HomeDestination.ROUTE) {
            Home(
                navigateToListening = { navController.navigate(ListeningDestination.ROUTE) },
            )
        }
        composable(SearchDestination.ROUTE) {
            Search(
                navigateToListening = { navController.navigate(ListeningDestination.ROUTE) },
            )
        }
        composable(MyLibraryDestination.ROUTE) {
            MyLibrary(
                navigateToListening = { navController.navigate(ListeningDestination.ROUTE) },
            )
        }
        composable(ListeningDestination.ROUTE) {
            Listening(
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}
