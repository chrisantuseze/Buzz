package com.echrisantus.buzz.network.model

import com.google.gson.annotations.SerializedName

data class TVSearchResult(
    val id: Int,
    val name: String,
    val overview: String,

    @SerializedName("air_date")
    val airDate: String,

    @SerializedName("episode_number")
    val episodeNumber: Int,

    @SerializedName("still_path")
    val stillPath: String
)