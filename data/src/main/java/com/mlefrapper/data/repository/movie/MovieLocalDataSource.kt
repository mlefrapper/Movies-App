package com.mlefrapper.data.repository.movie

import androidx.paging.PagingSource
import com.mlefrapper.data.db.movies.MovieDao
import com.mlefrapper.data.db.movies.MovieRemoteKeyDao
import com.mlefrapper.data.entities.MovieDbData
import com.mlefrapper.data.entities.MovieRemoteKeyDbData
import com.mlefrapper.data.entities.toDomain
import com.mlefrapper.data.exception.DataNotAvailableException
import com.mlefrapper.data.mapper.toDbData
import com.mlefrapper.data.util.DiskExecutor
import com.mlefrapper.domain.entities.MovieEntity
import com.mlefrapper.domain.util.Result
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

class MovieLocalDataSource(
    private val executor: DiskExecutor,
    private val movieDao: MovieDao,
    private val remoteKeyDao: MovieRemoteKeyDao,
) : MovieDataSource.Local {

    override fun movies(): PagingSource<Int, MovieDbData> = movieDao.movies()

    override suspend fun getMovies(): Result<List<MovieEntity>> = withContext(executor.asCoroutineDispatcher()) {
        val movies = movieDao.getMovies()
        return@withContext if (movies.isNotEmpty()) {
            Result.Success(movies.map { it.toDomain() })
        } else {
            Result.Error(DataNotAvailableException())
        }
    }

    override suspend fun getMovie(movieId: Int): Result<MovieEntity> = withContext(executor.asCoroutineDispatcher()) {
        return@withContext movieDao.getMovie(movieId)?.let {
            Result.Success(it.toDomain())
        } ?: Result.Error(DataNotAvailableException())
    }

    override suspend fun saveMovies(movieEntities: List<MovieEntity>) = withContext(executor.asCoroutineDispatcher()) {
        movieDao.saveMovies(movieEntities.map { it.toDbData() })
    }

    override suspend fun getLastRemoteKey(): MovieRemoteKeyDbData? = withContext(executor.asCoroutineDispatcher()) {
        remoteKeyDao.getLastRemoteKey()
    }

    override suspend fun saveRemoteKey(key: MovieRemoteKeyDbData) = withContext(executor.asCoroutineDispatcher()) {
        remoteKeyDao.saveRemoteKey(key)
    }

    override suspend fun clearMovies() = withContext(executor.asCoroutineDispatcher()) {
        movieDao.clearMoviesExceptFavorites()
    }

    override suspend fun clearRemoteKeys() = withContext(executor.asCoroutineDispatcher()) {
        remoteKeyDao.clearRemoteKeys()
    }
}