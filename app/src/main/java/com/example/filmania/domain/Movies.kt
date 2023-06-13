package com.example.filmania.domain

data class Movies(
    val id: Int,
    val title: String,
    val releaseData: String,
    val voteAverage: String,
    val posterImage: String,
    val page: Int
)
