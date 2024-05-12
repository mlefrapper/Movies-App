package com.mlefrapper.clean.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mlefrapper.clean.entities.MovieListItem
import com.mlefrapper.clean.mapper.toPresentation
import com.mlefrapper.clean.ui.base.BaseViewModel
import com.mlefrapper.clean.util.singleSharedFlow
import com.mlefrapper.data.util.DispatchersProvider
import com.mlefrapper.domain.usecase.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase,
    private val savedStateHandle: SavedStateHandle,
    dispatchers: DispatchersProvider
) : BaseViewModel(dispatchers) {

    sealed class NavigationState {
        data class MovieDetails(val movieId: Int) : NavigationState()
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    var movies: Flow<PagingData<MovieListItem>> = savedStateHandle.getStateFlow(KEY_SEARCH_QUERY, "")
        .onEach { query ->
            _uiState.value = if (query.isNotEmpty()) SearchUiState(showDefaultState = false, showLoading = true) else SearchUiState()
        }
        .debounce(500)
        .filter { it.isNotEmpty() }
        .flatMapLatest { query ->
            searchMoviesUseCase(query, 30).map { pagingData ->
                pagingData.map { movieEntity -> movieEntity.toPresentation() as MovieListItem }
            }
        }.cachedIn(viewModelScope)

    private val _uiState: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationState: MutableSharedFlow<NavigationState> = singleSharedFlow()
    val navigationState = _navigationState.asSharedFlow()

    fun onSearch(query: String) {
        savedStateHandle[KEY_SEARCH_QUERY] = query
    }

    fun onMovieClicked(movieId: Int) =
        _navigationState.tryEmit(NavigationState.MovieDetails(movieId))

    fun onLoadStateUpdate(loadState: CombinedLoadStates, itemCount: Int) {
        val showLoading = loadState.refresh is LoadState.Loading
        val showNoData = loadState.append.endOfPaginationReached && itemCount < 1

        val error = when (val refresh = loadState.refresh) {
            is LoadState.Error -> refresh.error.message
            else -> null
        }

        _uiState.update {
            it.copy(
                showLoading = showLoading,
                showNoMoviesFound = showNoData,
                errorMessage = error
            )
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // CONST
    ///////////////////////////////////////////////////////////////////////////

    companion object {
        const val KEY_SEARCH_QUERY = "search_query"
    }
}