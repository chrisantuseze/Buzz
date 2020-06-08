package com.echrisantus.buzz.ui.favourite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.echrisantus.buzz.database.model.Movie
import com.echrisantus.buzz.database.model.TV
import com.echrisantus.buzz.network.model.MediaType
import com.echrisantus.buzz.network.model.MovieData
import com.echrisantus.buzz.network.model.TVData
import com.echrisantus.buzz.repository.MovieRepository
import com.echrisantus.buzz.repository.TVRepository
import com.echrisantus.buzz.util.Resource
import kotlinx.coroutines.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

class FavouriteViewModel  @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TVRepository): ViewModel() {

    private val viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val _mediaType = MutableLiveData<String>().apply { value = MediaType.MOVIE.value }
    val mediaType: LiveData<String>
    get() = _mediaType

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
    get() = _movieList

    private val _tvList = MutableLiveData<List<TV>>()
    val tvList: LiveData<List<TV>>
    get() = _tvList

    private val _deleted = MutableLiveData<Boolean>().apply { value = false }
    val deleted: LiveData<Boolean>
    get() = _deleted

    val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
    get() = _movie

    val _tv = MutableLiveData<TV>()
    val tv: LiveData<TV>
    get() = _tv

    private val _isMovie = MutableLiveData<Boolean>()
    val isMovie: LiveData<Boolean>
    get() = _isMovie

    init {
        getFavouriteMovies()
        getFavouriteTvSeries()
    }

    fun switchMediaTypes() {
        _mediaType.value = when(_mediaType.value) {
            MediaType.TV.value -> MediaType.MOVIE.value
            else -> MediaType.TV.value
        }
    }

    fun getFavouriteMovies() {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                val movieList = movieRepository.getAllMovies()
                _movieList.postValue(movieList)
            }
        }
    }

    fun getFavouriteTvSeries() {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                val tvList = tvRepository.getAllTvSeries()
                _tvList.postValue(tvList)
            }
        }
    }

    fun clearFavourites() {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                tvRepository.deleteAllFavTvSeries()
                movieRepository.deleteAllFavMovies()
                _deleted.postValue(true)

                getFavouriteTvSeries()
                getFavouriteMovies()
            }
        }
    }

    fun deleteFavMovie(id: Int) {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                movieRepository.deleteFavMovieById(id)
                getFavouriteMovies()
            }
        }
    }

    fun deleteFavSeries(id: Int) {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                tvRepository.deleteFavTvSeriesById(id)
                getFavouriteTvSeries()
            }
        }
    }

    fun getMovie(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = movieRepository.getMovieByIdDB(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getTvSeries(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = tvRepository.getTvSeriesById(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun setMediaType(mediaType: String) {
        _isMovie.value = when(mediaType) {
            MediaType.MOVIE.value -> true
            MediaType.TV.value -> false
            else -> true
        }
    }
}