package com.example.musicsquare.feature.listening

import MultiThemePreviews
import MusicItem
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.musicsquare.core.data.music.Music
import com.example.musicsquare.core.designsystem.theme.MusicSquareTheme

object ListeningDestination {
    const val route = "listening"
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Listening(
    navController: NavController = NavController(LocalContext.current),
    navigateBack: () -> Unit = { navController.popBackStack() },
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Music Square") },
            navigationIcon = {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
        )
    }) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            item { MusicItem(Music.mock) }
        }
    }
}

@MultiThemePreviews
@Composable
fun ListeningPreview() {
    MusicSquareTheme {
        Listening()
    }
}
