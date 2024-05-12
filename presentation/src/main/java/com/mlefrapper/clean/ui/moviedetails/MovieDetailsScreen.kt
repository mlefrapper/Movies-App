package com.mlefrapper.clean.ui.moviedetails

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.SubcomposeAsyncImage
import com.mlefrapper.clean.R
import com.mlefrapper.clean.R.drawable
import com.mlefrapper.clean.util.preview.PreviewContainer

@Composable
fun MovieDetailsPage(
    mainNavController: NavHostController,
    viewModel: MovieDetailsViewModel,
) {
    val state by viewModel.uiState.collectAsState()
    MovieDetailsScreen(state, viewModel::onFavoriteClicked, mainNavController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    state: MovieDetailsUiState,
    onFavoriteClick: () -> Unit,
    appNavController: NavHostController
) {
    val favoriteIcon =
        if (state.isFavorite) drawable.ic_favorite_fill_white_48 else drawable.ic_favorite_border_white_48

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { onFavoriteClick() }) {
                Image(
                    painter = painterResource(id = favoriteIcon),
                    contentDescription = null,
                    Modifier.size(24.dp)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = LocalContext.current.getString(R.string.overview_title)) },
                navigationIcon = {
                    IconButton(onClick = { appNavController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                SubcomposeAsyncImage(
                    model = state.imageUrl,
                    loading = { MovieItemPlaceholder() },
                    error = { MovieItemPlaceholder() },
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                        .blur(30.dp)
                        .alpha(0.2f)
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    // Contenu de la page
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top
                    ) {
                        SubcomposeAsyncImage(
                            model = state.imageUrl,
                            loading = { MovieItemPlaceholder() },
                            error = { MovieItemPlaceholder() },
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(280.dp)
                                .fillMaxWidth(1f)
                        )
                        Text(
                            text = state.title,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                        Text(
                            text = state.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun MovieItemPlaceholder() {
    Image(
        painter = painterResource(id = drawable.bg_image),
        contentDescription = "",
        contentScale = ContentScale.Crop,
    )
}

@Preview(name = "Light")
@Preview(name = "Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MovieDetailsScreenPreview() {
    PreviewContainer {
        MovieDetailsScreen(
            MovieDetailsUiState(
                imageUrl = "https://i.stack.imgur.com/lDFzt.jpg",
                title = "Avatar",
                description = "Lorem ipsum dolor sit amet",
                isFavorite = false,
            ),
            onFavoriteClick = {},
            rememberNavController()
        )
    }
}
