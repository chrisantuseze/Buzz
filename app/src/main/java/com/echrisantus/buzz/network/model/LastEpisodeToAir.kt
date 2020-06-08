package com.echrisantus.buzz.network.model

import com.google.gson.annotations.SerializedName

data class LastEpisodeToAir(
    val id: Int,

    @SerializedName("episode_number")
    val episodeNumber: Int,

    @SerializedName("air_date")
    val airDate: String,

    val name: String,
    val overview: String,
    val season: Int,

    @SerializedName("still_path")
    val stillPath: String
)