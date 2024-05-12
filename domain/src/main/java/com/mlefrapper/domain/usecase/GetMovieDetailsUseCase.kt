package com.mlefrapper.domain.usecase

import com.mlefrapper.domain.entities.MovieEntity
import com.mlefrapper.domain.repository.MovieRepository
import com.mlefrapper.domain.util.Result

class GetMovieDetailsUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<MovieEntity> = movieRepository.getMovie(movieId)
}
