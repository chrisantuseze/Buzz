package com.echrisantus.buzz.network.model

import com.google.gson.annotations.SerializedName

data class TVData(
    val id: Int,

    @SerializedName("original_name")
    val title: String,

    val overview: String,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("first_air_date")
    val firstAirDate: String,

    @SerializedName("last_air_date")
    val lastAirDate: String,

    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int,

    @SerializedName("poster_path")
    val posterPath: String,

    @SerializedName("backdrop_path")
    val backdropPath: String,

    @SerializedName("last_episode_to_air")
    val lastEpisodeToAir: LastEpisodeToAir,

    val genres: List<GenreData>

)