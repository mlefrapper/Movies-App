package com.mlefrapper.data.repository.movie

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mlefrapper.data.entities.toDomain
import com.mlefrapper.data.exception.DataNotAvailableException
import com.mlefrapper.data.repository.movie.favorite.FavoriteMoviesDataSource
import com.mlefrapper.domain.entities.MovieEntity
import com.mlefrapper.domain.repository.MovieRepository
import com.mlefrapper.domain.util.Result
import com.mlefrapper.domain.util.getResult
import com.mlefrapper.domain.util.onError
import com.mlefrapper.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl(
    private val remote: MovieDataSource.Remote,
    private val local: MovieDataSource.Local,
    private val remoteMediator: MovieRemoteMediator,
    private val localFavorite: FavoriteMoviesDataSource.Local
) : MovieRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun movies(pageSize: Int): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        remoteMediator = remoteMediator,
        pagingSourceFactory = { local.movies() }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }

    override fun favoriteMovies(pageSize: Int): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { localFavorite.favoriteMovies() }
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }

    override fun search(query: String, pageSize: Int): Flow<PagingData<MovieEntity>> = Pager(
        config = PagingConfig(
            pageSize = pageSize,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { SearchMoviePagingSource(query, remote) }
    ).flow

    override suspend fun getMovie(movieId: Int): Result<MovieEntity> = local.getMovie(movieId).getResult({
        it
    }, {
        remote.getMovie(movieId)
    })

    override suspend fun checkFavoriteStatus(movieId: Int): Result<Boolean> = localFavorite.checkFavoriteStatus(movieId)

    override suspend fun addMovieToFavorite(movieId: Int) {
        local.getMovie(movieId)
            .onSuccess {
                localFavorite.addMovieToFavorite(movieId)
            }
            .onError {
                remote.getMovie(movieId).onSuccess {
                    local.saveMovies(listOf(it))
                    localFavorite.addMovieToFavorite(movieId)
                }
            }
    }

    override suspend fun removeMovieFromFavorite(movieId: Int) = localFavorite.removeMovieFromFavorite(movieId)

    override suspend fun sync(): Boolean = local.getMovies().getResult({ movieIdsResult ->
        remote.getMovies(movieIdsResult.data.map { it.id }).getResult({
            local.saveMovies(it.data)
            true
        }, {
            it.error is DataNotAvailableException
        })
    }, {
        it.error is DataNotAvailableException
    })
}
