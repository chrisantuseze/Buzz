package com.echrisantus.buzz.network.model

import com.google.gson.annotations.SerializedName

data class TVEpisode (
    val id: Int,
    val name: String,
    val overview: String,

    @SerializedName("vote_average")
    val voteAverage: Double,

    @SerializedName("air_date")
    val airDate: String,

    @SerializedName("episode_number")
    val episodeNumber: Int,

    @SerializedName("season_number")
    val seasonNumber: Int,

    @SerializedName("still_path")
    val stillPath: String,

    @SerializedName("guest_stars")
    val guestStar: List<GuestStar>
)