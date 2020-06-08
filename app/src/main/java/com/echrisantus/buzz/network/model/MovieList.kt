package com.echrisantus.buzz.network.model

import com.google.gson.annotations.SerializedName

data class MovieList(
    val id: Int,

    @SerializedName("original_title")
    val title: String,

    val overview: String,

    @SerializedName("release_date")
    val releaseDate: String,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("genre_ids")
    val genreIds: List<Int>,

    @SerializedName("poster_path")
    val posterPath: String
)