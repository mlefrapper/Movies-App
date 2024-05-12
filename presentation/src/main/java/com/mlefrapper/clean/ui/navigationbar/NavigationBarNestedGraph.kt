package com.mlefrapper.clean.ui.navigationbar

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.mlefrapper.clean.ui.favorites.FavoritesPage
import com.mlefrapper.clean.ui.favorites.FavoritesViewModel
import com.mlefrapper.clean.ui.feed.FeedPage
import com.mlefrapper.clean.ui.feed.FeedViewModel
import com.mlefrapper.clean.navigation.Page
import com.mlefrapper.clean.ui.main.MainRouter
import com.mlefrapper.clean.util.composableHorizontalSlide
import com.mlefrapper.clean.util.sharedViewModel

@Composable
fun NavigationBarNestedGraph(
    navController: NavHostController,
    mainNavController: NavHostController,
    parentRoute: String
) {
    NavHost(
        navController = navController,
        startDestination = Page.Feed.route,
        route = parentRoute
    ) {
        composableHorizontalSlide(route = Page.Feed.route) { backStack ->
            val viewModel = hiltViewModel<FeedViewModel>()
            FeedPage(
                mainRouter = MainRouter(mainNavController),
                viewModel = viewModel,
                sharedViewModel = backStack.sharedViewModel(navController = mainNavController)
            )
        }
        composableHorizontalSlide(route = Page.Favorites.route) {
            val viewModel = hiltViewModel<FavoritesViewModel>()
            FavoritesPage(
                mainRouter = MainRouter(mainNavController),
                viewModel = viewModel,
            )
        }
    }
}