package com.mlefrapper.clean.ui.favorites

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.mlefrapper.clean.R
import com.mlefrapper.clean.entities.MovieListItem
import com.mlefrapper.clean.ui.main.MainRouter
import com.mlefrapper.clean.ui.widget.EmptyStateView
import com.mlefrapper.clean.ui.widget.LoaderFullScreen
import com.mlefrapper.clean.ui.widget.MovieList
import com.mlefrapper.clean.util.preview.PreviewContainer
import kotlinx.coroutines.flow.flowOf

@Composable
fun FavoritesPage(
    mainRouter: MainRouter,
    viewModel: FavoritesViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()
    val movies = viewModel.movies.collectAsLazyPagingItems()
    val navigationState by viewModel.navigationState.collectAsState(null)
    viewModel.onLoadStateUpdate(movies.loadState, movies.itemCount)

    LaunchedEffect(key1 = navigationState) {
        when (val navState = navigationState) {
            is NavigationState.MovieDetails -> mainRouter.navigateToMovieDetails(navState.movieId)
            else -> Unit
        }
    }

    FavoritesScreen(
        favoriteUiState = uiState,
        movies = movies,
        onMovieClick = viewModel::onMovieClicked
    )
}

@Composable
fun FavoritesScreen(
    favoriteUiState: FavoriteUiState,
    movies: LazyPagingItems<MovieListItem>,
    onMovieClick: (movieId: Int) -> Unit
) {
    Surface {
        val isLoading = favoriteUiState.isLoading
        val noDataAvailable = favoriteUiState.noDataAvailable

        MovieList(movies = movies, onMovieClick = onMovieClick)

        if (isLoading) {
            LoaderFullScreen()
        } else {
            if (noDataAvailable) {
                EmptyStateView(titleRes = R.string.no_favorite_movies_at_the_moment)
            }
        }
    }
}

@Preview(showSystemUi = true, name = "Light")
@Preview(showSystemUi = true, name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun FavoritesScreenPreview() {
    val movieItems: List<MovieListItem> = listOf(
        MovieListItem.Separator("Action"),
        MovieListItem.Movie(1, "image1.jpg", "Action"),
        MovieListItem.Movie(2, "image2.jpg", "Comedy"),
        MovieListItem.Separator("Drama"),
        MovieListItem.Movie(3, "image3.jpg", "Drama")
    )

    PreviewContainer {
        val movies = flowOf(PagingData.from(movieItems)).collectAsLazyPagingItems()
        FavoritesScreen(FavoriteUiState(isLoading = false, noDataAvailable = true), movies) {}
    }
}