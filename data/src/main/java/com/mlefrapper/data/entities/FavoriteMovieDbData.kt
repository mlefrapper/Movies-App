package com.mlefrapper.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteMovieDbData(
    @PrimaryKey val movieId: Int
)