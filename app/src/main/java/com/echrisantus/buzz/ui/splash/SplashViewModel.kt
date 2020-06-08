package com.echrisantus.buzz.ui.splash

import android.content.Context
import androidx.lifecycle.ViewModel
import com.echrisantus.buzz.BuzzApplication
import com.echrisantus.buzz.database.AppSharedPref
import com.echrisantus.buzz.database.model.Genre
import com.echrisantus.buzz.repository.MovieRepository
import com.echrisantus.buzz.repository.TVRepository
import kotlinx.coroutines.*
import javax.inject.Inject

class SplashViewModel  @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tvRepository: TVRepository
): ViewModel() {

    private val viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var appSharedPref: AppSharedPref

    init {
        appSharedPref = AppSharedPref(BuzzApplication.applicationContext())
        saveMovieGenres()
        saveTvSeriesGenres()
//        saveInSharedPrefs()
    }

    private fun saveTvSeriesGenres() {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                val response = tvRepository.getGenreList()
                tvRepository.addTvSeriesGenreListToDatabase(response.body()!!)
            }
        }
    }

    private fun saveMovieGenres() {
        uiCoroutineScope.launch {
            withContext(Dispatchers.IO) {
                val response = movieRepository.getGenreList()
                movieRepository.addMovieGenreListToDatabase(response.body()!!)
            }
        }
    }
//
//    fun saveInSharedPrefs() {
//        uiCoroutineScope.launch {
//            withContext(Dispatchers.IO) {
//                val movieGenreList = movieRepository.getMovieGenreListFromDatabase()
//                val tvGenreList = tvRepository.getTvGenreListFromDatabase()
//
//                val genreList = mutableListOf<Genre>()
//                genreList.addAll(movieGenreList)
//                genreList.addAll(tvGenreList)
//
//                genreList.forEach {
//                    appSharedPref.saveData(it.id.toString(), it.name)
//                }
//            }
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}