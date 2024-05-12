package com.mlefrapper.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies_remote_keys")
data class MovieRemoteKeyDbData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val prevPage: Int?,
    val nextPage: Int?
)