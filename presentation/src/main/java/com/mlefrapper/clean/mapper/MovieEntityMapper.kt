package com.mlefrapper.clean.mapper

import com.mlefrapper.clean.entities.MovieListItem
import com.mlefrapper.domain.entities.MovieEntity

fun MovieEntity.toPresentation() = MovieListItem.Movie(
    id = id,
    imageUrl = image,
    category = category
)