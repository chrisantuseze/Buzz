package com.echrisantus.buzz.repository

import com.echrisantus.buzz.network.service.MovieService
import com.echrisantus.buzz.network.service.TVService
import com.echrisantus.buzz.util.Constants
import javax.inject.Inject

class ApiHelper @Inject constructor(private val movieService: MovieService, private val tvService: TVService) {
    suspend fun getMovieById(id: Int) = movieService.getMovieById(id, Constants.API_KEY)
    suspend fun getPopularMovies(page: Int) = movieService.getPopularMovies(Constants.API_KEY, page)
    suspend fun getTopRatedMovies(page: Int) = movieService.getTopRatedMovies(Constants.API_KEY, page)
    suspend fun getUpComingMovies(page: Int) = movieService.getUpComingMovies(Constants.API_KEY, page)
    suspend fun searchTrendingMovies(timeWindow: String) = movieService.getTrendingMoviesByWindow(timeWindow, Constants.API_KEY)
    suspend fun getMovieGenreList() = movieService.getGenreList(Constants.API_KEY)


    suspend fun getTVById(id: Int) = tvService.getTVById(id, Constants.API_KEY)
    suspend fun getPopularTV(page: Int) = tvService.getPopularTV(Constants.API_KEY, page)
    suspend fun getTopRatedTV(page: Int) = tvService.getTopRatedTV(Constants.API_KEY, page)
    suspend fun getTVByIdSeason(id: Int, seasonNumber: Int) = tvService.getTVByIdSeason(id, seasonNumber, Constants.API_KEY)
    suspend fun getTVByIdSeasonEpisode(id: Int, seasonNumber: Int, episodeNumber: Int) = tvService.getTVByIdSeasonEpisode(id, seasonNumber, episodeNumber, Constants.API_KEY)
    suspend fun searchTrendingTvSeries(timeWindow: String) = tvService.getTrendingTVByWindow(timeWindow, Constants.API_KEY)
    suspend fun getTvGenreList() = tvService.getGenreList(Constants.API_KEY)

}