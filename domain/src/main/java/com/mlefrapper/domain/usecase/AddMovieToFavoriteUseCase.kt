package com.mlefrapper.domain.usecase

import com.mlefrapper.domain.repository.MovieRepository

class AddMovieToFavoriteUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int) = movieRepository.addMovieToFavorite(movieId)
}