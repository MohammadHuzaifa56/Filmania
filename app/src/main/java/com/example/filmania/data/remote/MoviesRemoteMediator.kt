package com.example.filmania.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.filmania.data.local.MoviesDatabase
import com.example.filmania.data.local.MoviesEntity
import com.example.filmania.data.local.RemoteKeys
import com.example.filmania.data.mappers.toMoviesEntity
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val moviesDb: MoviesDatabase,
    private val moviesApi: MoviesAPI
) : RemoteMediator<Int, MoviesEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (moviesDb.remoteKeysDao().getCreationTime() ?: 0) < cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MoviesEntity>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }
            val movies = moviesApi.getMoviesList(category = "popular", page = loadKey)
            val endOfPaginationReached = movies.results.isEmpty()
            val prevKey = if (loadKey == 1) null else loadKey.minus(1)
            val nextKey = if (endOfPaginationReached) null else loadKey.plus(1)
            moviesDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    moviesDb.dao().clearAll()
                    moviesDb.remoteKeysDao().clearRemoteKeys()
                }
                val moviesEntities = movies.results.map { it.toMoviesEntity() }
                val remoteKeys = movies.results.map {
                    RemoteKeys(
                        id = it.id,
                        prevKey = prevKey,
                        currentPage = loadKey,
                        nextKey = nextKey
                    )
                }
                moviesDb.remoteKeysDao().insertAll(remoteKeys)
                moviesDb.dao().upsertAll(moviesEntities.onEachIndexed { _, movie -> movie.page = loadKey })
            }

            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MoviesEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                moviesDb.remoteKeysDao().remoteKeyById(movie.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MoviesEntity>): RemoteKeys? {
        return state.pages.firstOrNull {it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movies ->
                moviesDb.remoteKeysDao().remoteKeyById(movies.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MoviesEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                moviesDb.remoteKeysDao().remoteKeyById(movieId)
            }
        }
    }
}