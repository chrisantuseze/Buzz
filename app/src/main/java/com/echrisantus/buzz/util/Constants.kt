package com.echrisantus.buzz.util

import com.echrisantus.buzz.database.model.Movie
import com.echrisantus.buzz.database.model.TV

object Constants {
    const val API_KEY = "6fd7a2c9b2d8d3ea66d04b98e312c656"
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val POSTER_BASE_URL = "${IMAGE_BASE_URL}w300"
    const val BACKDROP_BASE_URL = "${IMAGE_BASE_URL}w780"
    const val PROFILE_BASE_URL = "${IMAGE_BASE_URL}w185"

    const val DATABASE_NAME = "buzz"
}

typealias MovieFlowResult = Result<Movie>
typealias TVFlowResult = Result<TV>