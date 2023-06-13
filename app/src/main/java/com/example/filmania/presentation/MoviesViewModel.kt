package com.example.filmania.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.filmania.data.local.MoviesEntity
import com.example.filmania.data.mappers.toMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    pager: Pager<Int, MoviesEntity>
): ViewModel() {
    val moviesPageFlow = pager.flow.map { pagingData ->
        pagingData.map { it.toMovies() }
    }.cachedIn(viewModelScope)
}