package com.echrisantus.buzz.network.model

import com.google.gson.annotations.SerializedName

data class GuestStar (
    val id: Int,
    val name: String,
    val character: String,

    @SerializedName("profile_path")
    val profilePath: String
)