package com.mlefrapper.domain.usecase

import androidx.paging.PagingData
import com.mlefrapper.domain.entities.MovieEntity
import com.mlefrapper.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetFavoriteMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(pageSize: Int): Flow<PagingData<MovieEntity>> = movieRepository.favoriteMovies(pageSize)
}