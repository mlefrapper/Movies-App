package com.mlefrapper.clean.ui.favorites

sealed class NavigationState {
    data class MovieDetails(val movieId: Int) : NavigationState()
}