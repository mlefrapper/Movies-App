package com.mlefrapper.data.db.favorites

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mlefrapper.data.entities.FavoriteMovieDbData
import com.mlefrapper.data.entities.MovieDbData

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorites")
    fun getAll(): List<FavoriteMovieDbData>

    @Query("SELECT * FROM movies where id in (SELECT movieId FROM favorites)")
    fun favoriteMovies(): PagingSource<Int, MovieDbData>

    @Query("SELECT * FROM favorites where movieId=:movieId")
    fun get(movieId: Int): FavoriteMovieDbData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(favoriteMovieDbData: FavoriteMovieDbData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(favoriteMovieDbData: List<FavoriteMovieDbData>)

    @Query("DELETE FROM favorites WHERE movieId=:movieId")
    fun remove(movieId: Int)
}
