package com.echrisantus.buzz.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.echrisantus.buzz.BuzzApplication
import com.echrisantus.buzz.database.AppSharedPref
import com.echrisantus.buzz.database.model.Genre
import com.echrisantus.buzz.network.model.*
import com.echrisantus.buzz.repository.MovieRepository
import com.echrisantus.buzz.repository.TVRepository
import com.echrisantus.buzz.util.Resource
import kotlinx.coroutines.*
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

class HomeViewModel  @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TVRepository): ViewModel() {

    private val viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val _mediaType = MutableLiveData<String>().apply { value = MediaType.MOVIE.value }
    val mediaType: LiveData<String>
    get() = _mediaType

    private val _category = MutableLiveData<String>().apply { value = Category.POPULAR.value }
    val category: LiveData<String>
        get() = _category

    private val _movieList = MutableLiveData<List<MovieList>>()
    val movieList: LiveData<List<MovieList>>
    get() = _movieList

    private val _tvList = MutableLiveData<List<TVList>>()
    val tvList: LiveData<List<TVList>>
    get() = _tvList

    init {
        getMovies(1)
    }

    fun switchMediaTypes() {
        _category.value = Category.POPULAR.value
        _mediaType.value = when(_mediaType.value) {
            MediaType.TV.value -> MediaType.MOVIE.value
            else -> MediaType.TV.value
        }
        if (StringUtils.equalsIgnoreCase(_mediaType.value, MediaType.MOVIE.value)) {
            getMovies(1)
        } else {
            getTvSeries(1)
        }
    }

    fun setCategory(category: Category) {
        _category.value = category.value
        if (StringUtils.equalsIgnoreCase(_mediaType.value, MediaType.MOVIE.value)) {
            getMov(1)
        } else {
            getTv(1)
        }
    }

    fun getMovies(page: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = when(_category.value) {
                Category.POPULAR.value -> movieRepository.getPopularMovies(page)
                Category.TOP_RATED.value -> movieRepository.getTopRatedMovies(page)
                Category.UPCOMING.value -> movieRepository.getUpComingMovies(page)
                else -> movieRepository.getPopularMovies(page)
            }
            if (response.isSuccessful) {
                val resp  = response.body() as MovieResult
                emit(Resource.success(data = resp))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getTvSeries(page: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val response = when(_category.value) {
                Category.POPULAR.value -> tvRepository.getPopularTV(page)
                Category.TOP_RATED.value -> tvRepository.getTopRatedTV(page)
                else -> tvRepository.getPopularTV(page)
            }
            if (response.isSuccessful) {
                val resp = response.body() as TVResult
                emit(Resource.success(data = resp))
            }
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getMov(page: Int) {
        uiCoroutineScope.launch {
            withContext(Dispatchers.Default) {
                val response = when(_category.value) {
                    Category.POPULAR.value -> movieRepository.getPopularMovies(page)
                    Category.TOP_RATED.value -> movieRepository.getTopRatedMovies(page)
                    Category.UPCOMING.value -> movieRepository.getUpComingMovies(page)
                    else -> movieRepository.getPopularMovies(page)
                }
                if (response.isSuccessful) {
                    val resp  = response.body() as MovieResult
                    _movieList.postValue(resp.results)
                }
            }
        }
    }

    fun getTv(page: Int) {
        uiCoroutineScope.launch {
            withContext(Dispatchers.Default) {
                val response = when(_category.value) {
                    Category.POPULAR.value -> tvRepository.getPopularTV(page)
                    Category.TOP_RATED.value -> tvRepository.getTopRatedTV(page)
                    else -> tvRepository.getPopularTV(page)
                }
                if (response.isSuccessful) {
                    val resp = response.body() as TVResult
                    _tvList.postValue(resp.results)
                }
            }
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