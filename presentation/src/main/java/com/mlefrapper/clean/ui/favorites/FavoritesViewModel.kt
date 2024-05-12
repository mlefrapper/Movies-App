package com.mlefrapper.clean.ui.favorites

import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mlefrapper.clean.entities.MovieListItem
import com.mlefrapper.clean.mapper.toPresentation
import com.mlefrapper.clean.ui.base.BaseViewModel
import com.mlefrapper.clean.util.singleSharedFlow
import com.mlefrapper.data.util.DispatchersProvider
import com.mlefrapper.domain.usecase.GetFavoriteMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    dispatchers: DispatchersProvider
) : BaseViewModel(dispatchers) {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    val movies: Flow<PagingData<MovieListItem>> = getFavoriteMoviesUseCase(30)
        .map { pagingData ->
            pagingData.map { movieEntity ->
                movieEntity.toPresentation() as MovieListItem
            }
        }.cachedIn(viewModelScope)

    private val _uiState: MutableStateFlow<FavoriteUiState> = MutableStateFlow(FavoriteUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<NavigationState> = singleSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun onMovieClicked(movieId: Int) =
        _navigationState.tryEmit(NavigationState.MovieDetails(movieId))

    fun onLoadStateUpdate(loadState: CombinedLoadStates, itemCount: Int) {
        val showLoading = loadState.refresh is LoadState.Loading
        val showNoData = loadState.append.endOfPaginationReached && itemCount < 1

        _uiState.update {
            it.copy(
                isLoading = showLoading,
                noDataAvailable = showNoData
            )
        }
    }
}
