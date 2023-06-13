package com.example.filmania.data.remote

data class MoviesDto(
    val id: Int,
    val title: String,
    val release_date: String,
    val vote_average: String,
    val poster_path: String,
    var page: Int
)