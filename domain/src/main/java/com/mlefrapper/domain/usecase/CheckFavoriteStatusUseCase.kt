package com.mlefrapper.domain.usecase

import com.mlefrapper.domain.repository.MovieRepository
import com.mlefrapper.domain.util.Result

class CheckFavoriteStatusUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Result<Boolean> = movieRepository.checkFavoriteStatus(movieId)
}