package com.mlefrapper.clean.ui.main

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mlefrapper.clean.navigation.Graphs
import com.mlefrapper.clean.ui.moviedetails.MovieDetailsPage
import com.mlefrapper.clean.ui.moviedetails.MovieDetailsViewModel
import com.mlefrapper.clean.navigation.Page
import com.mlefrapper.clean.ui.navigationbar.NavigationBarNestedGraph
import com.mlefrapper.clean.ui.navigationbar.NavigationBarScreen
import com.mlefrapper.clean.ui.search.SearchPage
import com.mlefrapper.clean.ui.search.SearchViewModel
import com.mlefrapper.clean.util.composableHorizontalSlide
import com.mlefrapper.clean.util.sharedViewModel

@Composable
fun MainGraph(
    mainNavController: NavHostController,
    darkMode: Boolean,
    onThemeUpdated: () -> Unit
) {
    NavHost(
        navController = mainNavController,
        startDestination = Page.NavigationBar.route,
        route = Graphs.GRAPH_ROUTE_MAIN.name
    ) {
        composableHorizontalSlide(route = Page.NavigationBar.route) { backStack ->
            val nestedNavController = rememberNavController()
            NavigationBarScreen(
                sharedViewModel = backStack.sharedViewModel(navController = mainNavController),
                mainRouter = MainRouter(mainNavController),
                darkMode = darkMode,
                onThemeUpdated = onThemeUpdated,
                nestedNavController = nestedNavController
            ) {
                NavigationBarNestedGraph(
                    navController = nestedNavController,
                    mainNavController = mainNavController,
                    parentRoute = Graphs.GRAPH_ROUTE_MAIN.name
                )
            }
        }

        composableHorizontalSlide(route = Page.Search.route) {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchPage(
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }

        composableHorizontalSlide(
            route = "${Page.MovieDetails.route}/{${Page.MovieDetails.MOVIE_ID}}",
            arguments = listOf(
                navArgument(Page.MovieDetails.MOVIE_ID) {
                    type = NavType.IntType
                    defaultValue = 0
                    nullable = false
                }
            )
        ) {
            val viewModel = hiltViewModel<MovieDetailsViewModel>()
            MovieDetailsPage(
                mainNavController = mainNavController,
                viewModel = viewModel,
            )
        }
    }
}