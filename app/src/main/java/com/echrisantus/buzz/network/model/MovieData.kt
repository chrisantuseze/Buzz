package com.echrisantus.buzz.network.model

import com.google.gson.annotations.SerializedName

data class MovieData(
    val id: Int,

    @SerializedName("original_title")
    val title: String,

    val tagline: String,
    val overview: String,
    var runtime: Int,

    @SerializedName("release_date")
    val releaseDate: String,

    val status: String,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    val genres: List<GenreData>
)