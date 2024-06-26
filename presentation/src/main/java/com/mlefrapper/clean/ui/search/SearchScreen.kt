package com.mlefrapper.clean.ui.search

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mlefrapper.clean.R
import com.mlefrapper.clean.entities.MovieListItem
import com.mlefrapper.clean.navigation.Page
import com.mlefrapper.clean.ui.search.SearchViewModel.NavigationState.MovieDetails
import com.mlefrapper.clean.ui.widget.EmptyStateView
import com.mlefrapper.clean.ui.widget.LoaderFullScreen
import com.mlefrapper.clean.ui.widget.MovieList
import com.mlefrapper.clean.ui.widget.SearchView
import com.mlefrapper.clean.util.preview.PreviewContainer
import kotlinx.coroutines.flow.flowOf

@Composable
fun SearchPage(
    mainNavController: NavHostController,
    viewModel: SearchViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigationState by viewModel.navigationState.collectAsState(null)
    val movies = viewModel.movies.collectAsLazyPagingItems()
    viewModel.onLoadStateUpdate(movies.loadState, movies.itemCount)

    LaunchedEffect(key1 = navigationState) {
        when (val navState = navigationState) {
            is MovieDetails -> {
                mainNavController.navigate(Page.MovieDetails.route + "/${navState.movieId}")
            }

            else -> Unit
        }
    }

    SearchScreen(
        searchUiState = uiState,
        movies = movies,
        onMovieClick = viewModel::onMovieClicked,
        onQueryChange = viewModel::onSearch,
        onBackClick = { mainNavController.popBackStack() }
    )
}

@Composable
fun SearchScreen(
    searchUiState: SearchUiState,
    movies: LazyPagingItems<MovieListItem>,
    onMovieClick: (movieId: Int) -> Unit,
    onQueryChange: (query: String) -> Unit,
    onBackClick: () -> Unit
) {
    Surface {
        val context = LocalContext.current
        val showDefaultState = searchUiState.showDefaultState
        val showNoMoviesFound = searchUiState.showNoMoviesFound
        val isLoading = searchUiState.showLoading
        val errorMessage = searchUiState.errorMessage

        if (errorMessage != null) Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()

        Scaffold(topBar = {
            SearchView(onQueryChange, onBackClick)
        }) {
            Box(modifier = Modifier.padding(it)) {
                if (!showDefaultState) {
                    if (isLoading) {
                        LoaderFullScreen(
                            alignment = Alignment.TopCenter,
                            modifier = Modifier.padding(top = 150.dp)
                        )
                    } else {
                        if (showNoMoviesFound) {
                            EmptyStateView(
                                titleRes = R.string.no_movies_found,
                                alignment = Alignment.TopCenter,
                                modifier = Modifier.padding(top = 150.dp)
                            )
                        } else {
                            MovieList(movies = movies, onMovieClick = onMovieClick)
                        }
                    }
                }
            }

        }
    }
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SearchScreenPreview() {
    val movieItems: List<MovieListItem> = listOf(
        MovieListItem.Separator("Action"),
        MovieListItem.Movie(1, "image1.jpg", "Action"),
        MovieListItem.Movie(2, "image2.jpg", "Comedy"),
        MovieListItem.Separator("Drama"),
        MovieListItem.Movie(3, "image3.jpg", "Drama")
    )

    PreviewContainer {
        val movies = flowOf(PagingData.from(movieItems)).collectAsLazyPagingItems()
//            SearchScreen(SearchUiState(), movies, {}, {}, {})
        SearchScreen(SearchUiState(showDefaultState = false, showNoMoviesFound = true), movies, {}, {}, {})
    }
}