package com.echrisantus.buzz.repository

import com.bumptech.glide.util.Util
import com.echrisantus.buzz.BuzzApplication
import com.echrisantus.buzz.database.AppSharedPref
import com.echrisantus.buzz.database.dao.GenreDao
import com.echrisantus.buzz.database.model.Movie
import com.echrisantus.buzz.database.dao.MovieDao
import com.echrisantus.buzz.database.model.Genre
import com.echrisantus.buzz.network.model.GenreData
import com.echrisantus.buzz.network.model.GenreList
import com.echrisantus.buzz.network.model.MovieData
import com.echrisantus.buzz.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val movieDao: MovieDao,
    private val genreDao: GenreDao
) {

    private val appSharedPref = AppSharedPref(BuzzApplication.applicationContext())
//    suspend fun getMovieById(id: Int) = flow<MovieFlowResult> {
//        val movie= movieDao.getMovieById(id)//TODO Ensure this check works
//        if (movie == null) {
//            apiHelper.getMovieById(id).run {
//                if (isSuccessful && body() != null) {
//                    movieDao.insert(getMovieTable(body()!!))
//                }
//            }
//        } else {
//            movie
//        }
//    }
    suspend fun getMovieById(id: Int) = apiHelper.getMovieById(id)
    suspend fun getPopularMovies(page: Int) = apiHelper.getPopularMovies(page)
    suspend fun getTopRatedMovies(page: Int) = apiHelper.getTopRatedMovies(page)
    suspend fun getUpComingMovies(page: Int) = apiHelper.getUpComingMovies(page)
    suspend fun searchTrendingMovies(timeWindow: String) = apiHelper.searchTrendingMovies(timeWindow)
    suspend fun getGenreList() = apiHelper.getMovieGenreList()

    suspend fun addMovieToDatabase(movies: MovieData) {
        movieDao.insert(getMovieTable(movies))
    }

    suspend fun addFavMovieToDatabase(movies: MovieData) {
        val movie = getMovieTable(movies)
        movie.isFavourite = true
        movieDao.insert(movie)
    }

    fun addMovieGenreListToDatabase(genreList: GenreList) {
//        val listOfGenre = mutableListOf<Genre>()
        genreList.genres.forEach {genreData ->
//            listOfGenre.add(getGenre(genreData))
            appSharedPref.saveData(genreData.id.toString(), genreData.name)
        }
//        genreDao.insertList(listOfGenre)
    }

    suspend fun getMovieGenreListFromDatabase(): List<Genre> {
        return genreDao.getAllGenres()
    }

    private fun getGenre(genreData: GenreData): Genre {
        return Genre().apply {
            id = genreData.id
            name = genreData.name
        }
    }

    suspend fun getMovieByIdDB(id: Int) = movieDao.getMovieById(id)
    suspend fun getAllMovies() = movieDao.getAllMovies()
    suspend fun deleteAllFavMovies() = movieDao.deleteAllFav(true)
    suspend fun deleteFavMovieById(id: Int) = movieDao.deleteFavById(id)
    suspend fun getGenreById(id: Int) = genreDao.getGenreById(id)


    fun getMovieTable(movies: MovieData): Movie {
        return Movie().apply {
            title = Utils.getNoneNullValue(movies.title)
            backdropPath = Utils.getNoneNullValue(movies.backdropPath)
            overview = Utils.getNoneNullValue(movies.overview)
            id = movies.id
            voteAverage = movies.voteAverage
            posterPath = Utils.getNoneNullValue(movies.posterPath)
            releaseDate = Utils.getNoneNullValue(movies.releaseDate)
            status = Utils.getNoneNullValue(movies.status)
            tagline = Utils.getNoneNullValue(movies.tagline)
            runtime = movies.runtime
            genres = Utils.getGenres(movies.genres)
        }
    }


}