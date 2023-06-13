package com.example.filmania

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.filmania.presentation.MoviesListScreen
import com.example.filmania.presentation.MoviesViewModel
import com.example.filmania.ui.theme.FilmaniaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FilmaniaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val moviesViewModel: MoviesViewModel = hiltViewModel()
                    MoviesListScreen(movies = moviesViewModel.moviesPageFlow.collectAsLazyPagingItems())
                }
            }
        }
    }
}