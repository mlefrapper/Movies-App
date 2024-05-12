package com.mlefrapper.clean.ui.navigationbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DynamicFeed
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.graphics.vector.ImageVector
import com.mlefrapper.clean.navigation.Page

data class NavigationBarUiState(
    val bottomItems: List<BottomNavigationBarItem> = listOf(
        BottomNavigationBarItem.Feed,
        BottomNavigationBarItem.MyFavorites
    )
)

sealed class BottomNavigationBarItem(
    val tabName: String,
    val imageVector: ImageVector,
    val route: String,
) {
    data object Feed : BottomNavigationBarItem("Feed", imageVector = Icons.Default.DynamicFeed, Page.Feed.route)
    data object MyFavorites : BottomNavigationBarItem("My Favorites", imageVector = Icons.Default.FavoriteBorder, Page.Favorites.route)
}
