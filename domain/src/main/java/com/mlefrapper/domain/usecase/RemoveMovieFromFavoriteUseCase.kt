package com.mlefrapper.domain.usecase

import com.mlefrapper.domain.repository.MovieRepository

class RemoveMovieFromFavoriteUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = movieRepository.removeMovieFromFavorite(movieId)
}