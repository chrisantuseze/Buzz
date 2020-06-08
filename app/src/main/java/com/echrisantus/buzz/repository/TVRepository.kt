package com.echrisantus.buzz.repository

import com.echrisantus.buzz.BuzzApplication
import com.echrisantus.buzz.database.AppSharedPref
import com.echrisantus.buzz.database.dao.GenreDao
import com.echrisantus.buzz.database.model.TV
import com.echrisantus.buzz.database.dao.TVDao
import com.echrisantus.buzz.database.model.Genre
import com.echrisantus.buzz.network.model.GenreData
import com.echrisantus.buzz.network.model.GenreList
import com.echrisantus.buzz.network.model.TVData
import com.echrisantus.buzz.util.Utils
import javax.inject.Inject

class TVRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val tvDao: TVDao,
    private val genreDao: GenreDao
) {

    private val appSharedPref = AppSharedPref(BuzzApplication.applicationContext())
//    fun getTVById(id: Int) = flow<TVFlowResult> {
//        val tvById = tvDao.getTVById(id)//TODO Ensure this check works
//        if (tvById == null) {
//            apiHelper.getTVById(id).run {
//                if (isSuccessful && body() != null) {
//                    tvDao.insert(getTVTable(body()!!))
//                }
//            }
//        } else {
//            tvById
//        }
//    }

    suspend fun getTVById(id: Int) = apiHelper.getTVById(id)
    suspend fun getPopularTV(page: Int) = apiHelper.getPopularTV(page)
    suspend fun getTopRatedTV(page: Int) = apiHelper.getTopRatedTV(page)
    suspend fun getTVByIdSeason(id: Int, seasonNumber: Int) = apiHelper.getTVByIdSeason(id, seasonNumber)
    suspend fun getTVByIdSeasonEpisode(id: Int, seasonNumber: Int, episodeNumber: Int) = apiHelper.getTVByIdSeasonEpisode(id, seasonNumber, episodeNumber)
    suspend fun searchTrendingTvSeries(timeWindow: String) = apiHelper.searchTrendingTvSeries(timeWindow)
    suspend fun getGenreList() = apiHelper.getTvGenreList()


    private fun getTVTable(tvSeries: TVData): TV {
        return TV().apply {
            title = Utils.getNoneNullValue(tvSeries.title)
            backdropPath = Utils.getNoneNullValue(tvSeries.backdropPath)
            firstAirDate = Utils.getNoneNullValue(tvSeries.firstAirDate)
            id = tvSeries.id
            lastAirDate = Utils.getNoneNullValue(tvSeries.lastAirDate)
            numberOfSeasons = tvSeries.numberOfSeasons
            overview = Utils.getNoneNullValue(tvSeries.overview)
            voteAverage = tvSeries.voteAverage
            posterPath = Utils.getNoneNullValue(tvSeries.posterPath)
            episodeNumber = tvSeries.lastEpisodeToAir.episodeNumber
            genres = Utils.getGenres(tvSeries.genres)
        }
    }

    suspend fun addFavTvSeriesToDatabase(tvSeries: TVData) {
        val tv = getTVTable(tvSeries)
        tv.isFavourite = true
        tvDao.insert(tv)
    }

    fun addTvSeriesGenreListToDatabase(genreList: GenreList) {
        val listOfGenre = mutableListOf<Genre>()
        genreList.genres.forEach {genreData ->
//            listOfGenre.add(getGenre(genreData))
            appSharedPref.saveData(genreData.id.toString(), genreData.name)
        }
//        genreDao.insertList(listOfGenre)
    }

    suspend fun getTvGenreListFromDatabase(): List<Genre> {
        return genreDao.getAllGenres()
    }

    private fun getGenre(genreData: GenreData): Genre {
        return Genre().apply {
            id = genreData.id
            name = genreData.name
        }
    }

    suspend fun addTvSeriesToDatabase(tvSeries: TVData) {
        tvDao.insert(getTVTable(tvSeries))
    }


    suspend fun getTvSeriesByCategory(category: String) = tvDao.getTvByCategory(category, 1)
    suspend fun getTvSeriesById(id: Int) = tvDao.getTVById(id)
    suspend fun getAllTvSeries() = tvDao.getAllTv()
    suspend fun deleteAllFavTvSeries() = tvDao.deleteAllFav(true)
    suspend fun deleteFavTvSeriesById(id: Int) = tvDao.deleteFavById(id)
    suspend fun getGenreById(id: Int) = genreDao.getGenreById(id)


}
