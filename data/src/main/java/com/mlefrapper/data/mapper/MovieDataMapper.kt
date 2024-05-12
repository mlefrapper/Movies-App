package com.mlefrapper.data.mapper

import com.mlefrapper.data.entities.MovieDbData
import com.mlefrapper.domain.entities.MovieEntity

fun MovieEntity.toDbData() = MovieDbData(
    id = id,
    image = image,
    description = description,
    title = title,
    category = category,
    backgroundUrl = backgroundUrl
)
