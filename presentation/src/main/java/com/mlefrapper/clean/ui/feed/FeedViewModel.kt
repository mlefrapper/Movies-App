package com.mlefrapper.clean.ui.feed

import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mlefrapper.clean.entities.MovieListItem
import com.mlefrapper.clean.ui.base.BaseViewModel
import com.mlefrapper.clean.ui.feed.usecase.GetMoviesWithSeparators
import com.mlefrapper.clean.util.NetworkMonitor
import com.mlefrapper.clean.util.singleSharedFlow
import com.mlefrapper.data.util.DispatchersProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getMoviesWithSeparators: GetMoviesWithSeparators,
    networkMonitor: NetworkMonitor,
    dispatchers: DispatchersProvider
) : BaseViewModel(dispatchers) {

    ///////////////////////////////////////////////////////////////////////////
    // DATA
    ///////////////////////////////////////////////////////////////////////////

    val movies: Flow<PagingData<MovieListItem>> = getMoviesWithSeparators.movies(
        pageSize = 30
    ).cachedIn(viewModelScope)

    private val _uiState: MutableStateFlow<FeedUiState> = MutableStateFlow(FeedUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<FeedNavigationState> = singleSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    val networkState = networkMonitor.networkState

    ///////////////////////////////////////////////////////////////////////////
    // PUBLIC API
    ///////////////////////////////////////////////////////////////////////////

    fun onMovieClicked(movieId: Int) =
        _navigationState.tryEmit(FeedNavigationState.MovieDetails(movieId))

    fun onLoadStateUpdate(loadState: CombinedLoadStates) {
        val showLoading = loadState.refresh is LoadState.Loading

        val error = when (val refresh = loadState.refresh) {
            is LoadState.Error -> refresh.error.message
            else -> null
        }

        _uiState.update { it.copy(showLoading = showLoading, errorMessage = error) }
    }
}
