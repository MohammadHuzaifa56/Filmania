package com.example.filmania.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(movies: List<MoviesEntity>)

    @Query("SELECT * FROM MoviesEntity Order By page")
    fun getAllMovies(): PagingSource<Int, MoviesEntity>

    @Query("DELETE FROM MoviesEntity")
    suspend fun clearAll()
}