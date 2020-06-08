package com.echrisantus.buzz.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tv")
data class TV(
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0,

    @ColumnInfo(name = "original_name")
    var title: String = "",

    @ColumnInfo(name = "first_air_date")
    var firstAirDate: String = "",

    @ColumnInfo(name = "last_air_date")
    var lastAirDate: String = "",

    @ColumnInfo(name = "number_of_seasons")
    var numberOfSeasons: Int = 0,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Double = 0.0,

    @ColumnInfo(name = "overview")
    var overview: String = "",

    @ColumnInfo(name = "poster_path")
    var posterPath: String = "",

    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String = "",

    @ColumnInfo(name = "category")
    var category: String = "",

    @ColumnInfo(name = "genres")
    var genres: String = "",

    @ColumnInfo(name = "episode_number")
    var episodeNumber: Int = 0,

    @ColumnInfo(name = "favourite")
    var isFavourite: Boolean = false
)