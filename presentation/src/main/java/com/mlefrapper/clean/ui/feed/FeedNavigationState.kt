package com.mlefrapper.clean.ui.feed

sealed class FeedNavigationState {
    data class MovieDetails(val movieId: Int) : FeedNavigationState()
}