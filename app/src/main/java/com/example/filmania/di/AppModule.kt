package com.example.filmania.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.filmania.data.local.MoviesDatabase
import com.example.filmania.data.local.MoviesEntity
import com.example.filmania.data.remote.MoviesAPI
import com.example.filmania.data.remote.MoviesRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providesMoviesDatabase(@ApplicationContext context: Context): MoviesDatabase {
        return Room.databaseBuilder(context, MoviesDatabase::class.java, "movies.db").fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor { chain ->
            val originalRequest = chain.request()
            val modifiedRequest = originalRequest.newBuilder()
                .header(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJlNzZjMGE3NWU3NzI1YWFiN2FjYTljODUwZjdjYWNkMSIsInN1YiI6IjY0ODA3NGM5ZTM3NWMwMDBmZjQ2ODc0MCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.aSv-0siMmDIeUrxSOBg67BmDmnh9v2jout6aj_0RGRU"
                )
                .build()
            chain.proceed(modifiedRequest)
        }
        return httpClient.build()
    }

    @Provides
    @Singleton
    fun providesMoviesAPI(client: OkHttpClient): MoviesAPI {
        return Retrofit.Builder()
            .baseUrl(MoviesAPI.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun providesMoviesPager(moviesDb: MoviesDatabase, moviesAPI: MoviesAPI): Pager<Int, MoviesEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MoviesRemoteMediator(
                moviesApi = moviesAPI,
                moviesDb = moviesDb
            ),
            pagingSourceFactory = { moviesDb.dao().getAllMovies() }
        )
    }
}