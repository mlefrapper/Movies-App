package com.mlefrapper.data.repository.movie.favorite

import androidx.paging.PagingSource
import com.mlefrapper.data.entities.MovieDbData
import com.mlefrapper.domain.util.Result

interface FavoriteMoviesDataSource {

    interface Local {
        fun favoriteMovies(): PagingSource<Int, MovieDbData>
        suspend fun getFavoriteMovieIds(): Result<List<Int>>
        suspend fun addMovieToFavorite(movieId: Int)
        suspend fun removeMovieFromFavorite(movieId: Int)
        suspend fun checkFavoriteStatus(movieId: Int): Result<Boolean>
    }
}
