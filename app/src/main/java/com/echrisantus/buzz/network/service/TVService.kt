package com.echrisantus.buzz.network.service

import com.echrisantus.buzz.network.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVService {
    @GET("tv/{tv_id}")
    suspend fun getTVById(@Path("tv_id")id: Int, @Query("api_key")apiKey: String): Response<TVData>

    @GET("tv/popular")
    suspend fun getPopularTV(@Query("api_key")apiKey: String, @Query("page")page: Int): Response<TVResult>

    @GET("tv/top_rated")
    suspend fun getTopRatedTV(@Query("api_key")apiKey: String, @Query("page")page: Int): Response<TVResult>

    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getTVByIdSeason(@Path("tv_id")id: Int, @Path("season_number")seasonNumber: Int, @Query("api_key")apiKey: String): Response<TVSeason>

    @GET("tv/{tv_id}/season/{season_number}/episode/{episode_number}")
    suspend fun getTVByIdSeasonEpisode(@Path("tv_id")id: Int, @Path("season_number")seasonNumber: Int,
                                       @Path("episode_number")episodeNumber: Int, @Query("api_key")apiKey: String): Response<TVEpisode>

    @GET("genre/tv/list")
    suspend fun getGenreList(@Query("api_key")apiKey: String): Response<GenreList>

    @GET("trending/tv/{time_window}")
    suspend fun getTrendingTVByWindow(@Path("time_window")timeWindow: String, @Query("api_key")apiKey: String): Response<TVResult>
}