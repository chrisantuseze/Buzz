package com.echrisantus.buzz.ui.moviedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.echrisantus.buzz.network.model.MovieData
import com.echrisantus.buzz.repository.MovieRepository
import com.echrisantus.buzz.util.Resource
import kotlinx.coroutines.*
import javax.inject.Inject


class MovieDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository): ViewModel() {

    private val viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val _movie = MutableLiveData<MovieData>()
    val movie: LiveData<MovieData>
    get() = _movie

    fun getMovies(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = movieRepository.getMovieById(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun saveFavouriteMovie() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val movieData = _movie.value
            movieData?.let {
                val response = movieRepository.getMovieById(movieData.id)
                movieRepository.addFavMovieToDatabase(response.body()!!)
            }
            emit(Resource.success(data = null, message = "Added to favourites"))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }
}