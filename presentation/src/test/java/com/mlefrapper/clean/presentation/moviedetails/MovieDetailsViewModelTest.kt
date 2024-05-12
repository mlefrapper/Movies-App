package com.mlefrapper.clean.presentation.moviedetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mlefrapper.clean.navigation.Page
import com.mlefrapper.clean.presentation.base.BaseViewModelTest
import com.mlefrapper.clean.presentation.util.mock
import com.mlefrapper.clean.ui.moviedetails.MovieDetailsViewModel
import com.mlefrapper.domain.entities.MovieEntity
import com.mlefrapper.domain.usecase.AddMovieToFavoriteUseCase
import com.mlefrapper.domain.usecase.CheckFavoriteStatusUseCase
import com.mlefrapper.domain.usecase.GetMovieDetailsUseCase
import com.mlefrapper.domain.usecase.RemoveMovieFromFavoriteUseCase
import com.mlefrapper.domain.util.Result
import com.google.common.truth.Truth.assertThat
import com.mlefrapper.clean.ui.moviedetails.MovieDetailsUiState
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieDetailsViewModelTest : BaseViewModelTest() {

    private var movieId: Int = 1413

    private val movie = MovieEntity(movieId, "title", "desc", "image", "category", "")

    @Mock
    lateinit var getMovieDetailsUseCase: GetMovieDetailsUseCase

    @Mock
    lateinit var checkFavoriteStatusUseCase: CheckFavoriteStatusUseCase

    @Mock
    lateinit var addMovieToFavoriteUseCase: AddMovieToFavoriteUseCase

    @Mock
    lateinit var removeMovieFromFavoriteUseCase: RemoveMovieFromFavoriteUseCase

    @Mock
    lateinit var savedStateHandle: SavedStateHandle

    private lateinit var viewModel: MovieDetailsViewModel

    @BeforeEach
    fun setUp() {
        `when`(savedStateHandle.get<Int>(Page.MovieDetails.MOVIE_ID)).thenReturn(movieId)

        viewModel = MovieDetailsViewModel(
            getMovieDetailsUseCase = getMovieDetailsUseCase,
            checkFavoriteStatusUseCase = checkFavoriteStatusUseCase,
            removeMovieFromFavoriteUseCase = removeMovieFromFavoriteUseCase,
            addMovieToFavoriteUseCase = addMovieToFavoriteUseCase,
            savedStateHandle = savedStateHandle,
            dispatchers = coroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun onInitialState_movieAvailable_showMovieDetails() = runTest {
        `when`(getMovieDetailsUseCase(movieId)).thenReturn(Result.Success(movie))

        viewModel.onInitialState()

        viewModel.uiState.test {
            val emission = awaitItem()
            assertThat(emission.description).isEqualTo(movie.description)
            assertThat(emission.imageUrl).isEqualTo(movie.image)
            assertThat(emission.title).isEqualTo(movie.title)
            assertThat(emission.isFavorite).isFalse()
        }
    }

    @Test
    fun onInitialState_movieNotAvailable_doNothing() = runTest {
        `when`(getMovieDetailsUseCase(movieId)).thenReturn(Result.Error(mock()))

        viewModel.onInitialState()

        viewModel.uiState.test {
            val emission = awaitItem()
            assertThat(emission).isEqualTo(MovieDetailsUiState())
        }
    }
}
