package com.echrisantus.buzz.network.service

import com.echrisantus.buzz.network.model.GenreData
import com.echrisantus.buzz.network.model.GenreList
import com.echrisantus.buzz.network.model.MovieData
import com.echrisantus.buzz.network.model.MovieResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/{movie_id}")
    suspend fun getMovieById(@Path("movie_id")id: Int, @Query("api_key")apiKey: String): Response<MovieData>

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("api_key")apiKey: String, @Query("page")page: Int): Response<MovieResult>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(@Query("api_key")apiKey: String, @Query("page")page: Int): Response<MovieResult>

    @GET("movie/upcoming")
    suspend fun getUpComingMovies(@Query("api_key")apiKey: String, @Query("page")page: Int): Response<MovieResult>

    @GET("trending/movie/{time_window}")
    suspend fun getTrendingMoviesByWindow(@Path("time_window")timeWindow: String, @Query("api_key")apiKey: String): Response<MovieResult>

    @GET("genre/movie/list")
    suspend fun getGenreList(@Query("api_key")apiKey: String): Response<GenreList>
}