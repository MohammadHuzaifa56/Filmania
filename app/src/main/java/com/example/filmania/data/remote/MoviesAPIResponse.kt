package com.example.filmania.data.remote

data class MoviesAPIResponse(
    val page: Int,
    val results: List<MoviesDto>
)
