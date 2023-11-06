import androidx.lifecycle.ViewModel
import com.example.musicsquare.core.data.music.Music

class MyLibraryViewModel constructor(
//    private val musicRepository: MusicRepository
) : ViewModel() {

    val musicList = listOf(
        Music.mock,
    )
}
