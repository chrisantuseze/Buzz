package com.echrisantus.buzz.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.echrisantus.buzz.network.model.*
import com.echrisantus.buzz.repository.MovieRepository
import com.echrisantus.buzz.repository.TVRepository
import com.echrisantus.buzz.util.Resource
import kotlinx.coroutines.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

class SearchViewModel  @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TVRepository): ViewModel() {

    private val viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val prefix = "Trending "
    val _trendingMediaType = MutableLiveData<String>().apply { value = prefix + MediaType.MOVIE.value }

    var mediaType = MediaType.MOVIE.value
    var timeWindow = TimeWindow.DAY.value

    fun getTrending() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = when(mediaType) {
                MediaType.MOVIE.value -> movieRepository.searchTrendingMovies(timeWindow)
                MediaType.TV.value -> tvRepository.searchTrendingTvSeries(timeWindow)
                else -> movieRepository.searchTrendingMovies(timeWindow)
            }
            if (response.isSuccessful) {
                if(StringUtils.equalsIgnoreCase(mediaType, MediaType.MOVIE.value)) {
                    val resp = response.body() as MovieResult
                    emit(Resource.success(data = resp.results))
                } else {
                    val resp = response.body() as TVResult
                    emit(Resource.success(data = resp.results))
                }

            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun saveFavouriteMovie(id: Int) {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                val response = movieRepository.getMovieById(id)
                movieRepository.addFavMovieToDatabase(response.body()!!)
            }
        }
    }

    fun saveFavouriteTvSeries(id: Int) {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                val response = tvRepository.getTVById(id)
                tvRepository.addFavTvSeriesToDatabase(response.body()!!)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}