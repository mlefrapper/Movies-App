package com.mlefrapper.clean.ui.moviedetails

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import com.mlefrapper.clean.ui.base.BaseViewModel
import com.mlefrapper.clean.navigation.Page
import com.mlefrapper.data.util.DispatchersProvider
import com.mlefrapper.domain.entities.MovieEntity
import com.mlefrapper.domain.usecase.AddMovieToFavoriteUseCase
import com.mlefrapper.domain.usecase.CheckFavoriteStatusUseCase
import com.mlefrapper.domain.usecase.GetMovieDetailsUseCase
import com.mlefrapper.domain.usecase.RemoveMovieFromFavoriteUseCase
import com.mlefrapper.domain.util.Result
import com.mlefrapper.domain.util.getResult
import com.mlefrapper.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val checkFavoriteStatusUseCase: CheckFavoriteStatusUseCase,
    private val addMovieToFavoriteUseCase: AddMovieToFavoriteUseCase,
    private val removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase,
    savedStateHandle: SavedStateHandle,
    dispatchers: DispatchersProvider
) : BaseViewModel(dispatchers) {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    private val _uiState: MutableStateFlow<MovieDetailsUiState> = MutableStateFlow(MovieDetailsUiState())
    val uiState = _uiState.asStateFlow()

    private var movieId: Int = savedStateHandle[Page.MovieDetails.MOVIE_ID] ?: 0

    ///////////////////////////////////////////////////////////////////////////
    // LIFECYCLE
    ///////////////////////////////////////////////////////////////////////////

    init {
        onInitialState()
    }

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun onInitialState() = launchOnMainImmediate {
        val isFavorite = async { checkFavoriteStatus(movieId).getResult({ favoriteResult -> favoriteResult.data }, { false }) }
        getMovieById(movieId).onSuccess {
            _uiState.value = MovieDetailsUiState(
                title = it.title,
                description = it.description,
                imageUrl = it.backgroundUrl,
                isFavorite = isFavorite.await()
            )
        }
    }

    fun onFavoriteClicked() = launchOnMainImmediate {
        checkFavoriteStatus(movieId).onSuccess { isFavorite ->
            if (isFavorite) removeMovieFromFavoriteUseCase(movieId) else addMovieToFavoriteUseCase(movieId)
            _uiState.update { it.copy(isFavorite = !isFavorite) }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // HELPER
    ///////////////////////////////////////////////////////////////////////////

    private suspend fun getMovieById(movieId: Int): Result<MovieEntity> = getMovieDetailsUseCase(movieId)

    private suspend fun checkFavoriteStatus(movieId: Int): Result<Boolean> = checkFavoriteStatusUseCase.invoke(movieId)
}