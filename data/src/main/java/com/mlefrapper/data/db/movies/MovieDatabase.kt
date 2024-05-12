package com.mlefrapper.data.db.movies

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mlefrapper.data.db.favorites.FavoriteMovieDao
import com.mlefrapper.data.entities.FavoriteMovieDbData
import com.mlefrapper.data.entities.MovieDbData
import com.mlefrapper.data.entities.MovieRemoteKeyDbData

@Database(
    entities = [MovieDbData::class, FavoriteMovieDbData::class, MovieRemoteKeyDbData::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeyDao
    abstract fun favoriteMovieDao(): FavoriteMovieDao
}