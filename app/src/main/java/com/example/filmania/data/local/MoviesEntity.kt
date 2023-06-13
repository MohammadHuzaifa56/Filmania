package com.example.filmania.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoviesEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val releaseData: String,
    val voteAverage: String,
    val posterImage: String,
    var page: Int,
)
