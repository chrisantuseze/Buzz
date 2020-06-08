package com.echrisantus.buzz.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,

    @ColumnInfo(name = "original_title")
    var title: String = "",

    @ColumnInfo(name = "tagline")
    var tagline: String = "",

    @ColumnInfo(name = "vote_average")
    var voteAverage: Double = 0.0,

    @ColumnInfo(name = "overview")
    var overview: String = "",

    @ColumnInfo(name = "runtime")
    var runtime: Int = 0,

    @ColumnInfo(name = "release_date")
    var releaseDate: String = "",

    @ColumnInfo(name = "status")
    var status: String = "",

    @ColumnInfo(name = "poster_path")
    var posterPath: String = "",

    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String = "",

    @ColumnInfo(name = "category")
    var category: String = "",

    @ColumnInfo(name = "genres")
    var genres: String = "",

    @ColumnInfo(name = "favourite")
    var isFavourite: Boolean = false
)