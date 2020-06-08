package com.echrisantus.buzz.ui.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.echrisantus.buzz.network.model.GuestStar
import com.echrisantus.buzz.network.model.TVData
import com.echrisantus.buzz.network.model.TVEpisode
import com.echrisantus.buzz.network.model.TVSeason
import com.echrisantus.buzz.repository.TVRepository
import com.echrisantus.buzz.util.Resource
import kotlinx.coroutines.*
import javax.inject.Inject

class TVSeriesDetailViewModel @Inject constructor(
    private val tvRepository: TVRepository): ViewModel() {

    private val viewModelJob = Job()
    private val uiCoroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val _tv = MutableLiveData<TVData>()
    val tv: LiveData<TVData>
    get() = _tv

    val _tvEpisode = MutableLiveData<TVEpisode>()
    val tvEpisode: LiveData<TVEpisode>
    get() = _tvEpisode

    val _guestStars = MutableLiveData<List<GuestStar>>()
    val guestStars: LiveData<List<GuestStar>>
    get() = _guestStars

    private val _isShowStarBottomSheet = MutableLiveData<Boolean>()
    val showStarBottomSheet: LiveData<Boolean>
    get() = _isShowStarBottomSheet

    val _season = MutableLiveData<String>().apply { value = "1" }
    val season: LiveData<String>
    get() = _season

    val _episode = MutableLiveData<String>().apply { value = "1" }
    val episode: LiveData<String>
    get() = _episode

    val numberOfEpisodes = MutableLiveData<Int>()
    val _episodeArray = MutableLiveData<Array<String>>().apply {
        value = listOf("1").toTypedArray()
    }
    val episodeArray: LiveData<Array<String>>
        get() = _episodeArray

    fun getTvSeries(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = tvRepository.getTVById(id)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun saveFavouriteTvSeries() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            val tvData = _tv.value
            tvData?.let {
                val response = tvRepository.getTVById(tvData.id)
                tvRepository.addFavTvSeriesToDatabase(response.body()!!)
            }
            emit(Resource.success(data = null, message = "Added to favourites"))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getTvMoreDetails(id: Int, seasonNumber: Int, episodeNumber: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = tvRepository.getTVByIdSeasonEpisode(id, seasonNumber, episodeNumber)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun getNumberOfSeasonsArray(numberOfSeasons: Int): Array<String> {
        val list = mutableListOf<String>()
        for (i in 1..numberOfSeasons) list.add("$i")
        return list.toTypedArray()
    }

    fun getNumberOfEpisodesArray(numberOfEpisodes: Int): Array<String> {
        val list = mutableListOf<String>()
        for (i in 1..numberOfEpisodes) list.add("$i")
        return list.toTypedArray()
    }

    fun showStarBottomSheet(id: Int) {
        val guestStarList = _tvEpisode.value!!.guestStar

    }

    fun getNumberOfEpisodes(id: Int, seasonNumber: Int) {
        uiCoroutineScope.launch {
            withContext(Dispatchers.Default) {
                val response = tvRepository.getTVByIdSeason(id, seasonNumber)
                if (response.isSuccessful) {
                    val resp = response.body() as TVSeason
                    _episodeArray.postValue(getNumberOfEpisodesArray(resp.episodes.size))
                }
            }
        }
    }

}