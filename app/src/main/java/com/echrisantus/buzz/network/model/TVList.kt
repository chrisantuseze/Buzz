package com.echrisantus.buzz.network.model

import com.google.gson.annotations.SerializedName

data class TVList(
    val id: Int,

    @SerializedName("original_name")
    val name: String,

    val overview: String,

    @SerializedName("first_air_date")
    val firstAirDate: String,

    @SerializedName("genre_ids")
    val genreIds: List<Int>,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("poster_path")
    val posterPath: String
)