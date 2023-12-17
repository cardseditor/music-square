import androidx.lifecycle.ViewModel
import com.example.musicsquare.core.data.music.Music

class SearchViewModel constructor(
//    private val musicRepository: com.example.musicsquare.core.data.music.MusicRepository
) : ViewModel() {

    val musicList = listOf(
        Music.mock,
    )
}
