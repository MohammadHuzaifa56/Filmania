package com.example.filmania.data.remote

import org.intellij.lang.annotations.Language
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesAPI {
    @GET("movie/{category}")
    suspend fun getMoviesList(
        @Path("category") category: String,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ) : MoviesAPIResponse

    companion object {
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }
}