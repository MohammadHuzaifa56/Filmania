package com.example.filmania.data.mappers

import com.example.filmania.data.local.MoviesEntity
import com.example.filmania.data.remote.MoviesDto
import com.example.filmania.domain.Movies

fun MoviesDto.toMoviesEntity(): MoviesEntity {
    return MoviesEntity(
        id = id,
        title = title,
        releaseData = release_date,
        voteAverage = vote_average,
        posterImage = poster_path,
        page = page
    )
}

fun MoviesEntity.toMovies(): Movies {
    return Movies(
        id = id,
        title = title,
        releaseData = releaseData,
        voteAverage = voteAverage,
        posterImage = posterImage,
        page = page
    )
}