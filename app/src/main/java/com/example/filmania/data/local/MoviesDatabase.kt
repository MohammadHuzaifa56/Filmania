package com.example.filmania.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [MoviesEntity::class, RemoteKeys::class],
    version = 3
)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun dao(): MoviesDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}