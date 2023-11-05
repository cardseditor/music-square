import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicsquare.core.data.music.Music
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MusicViewModel constructor(
//    private val musicRepository: MusicRepository
) : ViewModel() {

    val musicList = listOf(
        Music.mock,
        Music.mock,
    )
}
