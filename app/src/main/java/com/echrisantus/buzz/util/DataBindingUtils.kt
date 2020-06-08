package com.echrisantus.buzz.util

import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.bumptech.glide.Glide
import com.echrisantus.buzz.R
import com.echrisantus.buzz.network.model.GenreData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.apache.commons.lang3.StringUtils

@BindingAdapter(value = ["bind:pmtOpt", "bind:pmtOptAttrChanged"], requireAll = false)
fun setPmtOpt(spinner: Spinner, selectedPmtOpt: String, inverseBindingListener: InverseBindingListener) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            inverseBindingListener.onChange()
        }
        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
    if (selectedPmtOpt != null) {
        val pos = (spinner.adapter as ArrayAdapter<String>).getPosition(selectedPmtOpt)
        spinner.setSelection(pos, true)
    }
}

@InverseBindingAdapter(attribute = "bind:pmtOpt", event = "bind:pmtOptAttrChanged")
fun getPmtOpt(spinner: Spinner): String {
    return spinner.selectedItem as String
}

@BindingAdapter("active")
fun TextView.makeActive(active: Boolean?) {
    if (active!!) {
        setTextColor(resources.getColor(R.color.appDarkBg))
    } else {
        setTextColor(Color.WHITE)
    }
}

@BindingAdapter("active")
fun RelativeLayout.makeActive(active: Boolean?) {
    if (active!!) {
        setBackgroundResource(R.drawable.category_rect_white)
    } else {
        setBackgroundResource(R.drawable.category_rect_gray)
    }
}

@BindingAdapter("loadPoster")
fun ImageView.loadPoster(url: String) {
    Glide.with(this.context)
        .load(Constants.POSTER_BASE_URL + url)
        .into(this)
}

@BindingAdapter("loadBackdrop")
fun ImageView.loadBackdrop(backdropPath: String?) {
    backdropPath?.let {
        Glide.with(this.context)
            .load(Constants.BACKDROP_BASE_URL + backdropPath)
            .into(this)
    }
}

@BindingAdapter("loadProfile")
fun ImageView.loadProfile(profile: String?) {
    profile?.let {
        Glide.with(this.context)
            .load(Constants.PROFILE_BASE_URL + profile)
            .into(this)
    }
}

@BindingAdapter("title")
fun TextView.setTitle(title: String?) {
    title?.let {
        text = title
    }
}

@BindingAdapter("genres")
fun TextView.setGenres(genres: String?) {
    genres?.let {
        text = genres
    }
}

@BindingAdapter("genres")
fun TextView.setGenres(genres: List<GenreData>?) {
    genres?.let {
        text = Utils.getGenres(genres)
    }
}

@BindingAdapter("runtime")
fun TextView.setRuntime(runtime: Int?) {
    runtime?.let {
        text = "${runtime} mins"
    }
}

@BindingAdapter("voteAverage")
fun TextView.setVoteAverage(voteAverage: Double?) {
    voteAverage?.let {
        text = String.format("%.1f", voteAverage)
    }
}

@BindingAdapter("overview")
fun TextView.setOverview(overview: String?) {
    overview?.let {
        text = overview
    }
}

@BindingAdapter("releaseDate")
fun TextView.setReleaseDate(releaseDate: String?) {
    releaseDate?.let {
        text = Utils.getFormattedDate(releaseDate)
    }
}

@BindingAdapter("status")
fun ImageView.setStatus(status: String?) {
    status?.let {
        setImageDrawable(resources.getDrawable(if (StringUtils.equalsIgnoreCase(status, "Released")) R.drawable.ic_correct else R.drawable.ic_wrong))
    }
}

//TV Series
@BindingAdapter("lastAirDate")
fun TextView.setLastAirDate(lastAirDate: String?) {
    lastAirDate?.let {
        text = Utils.getFormattedDate(lastAirDate)
    }
}

@BindingAdapter("firstAirDate")
fun TextView.setFirstAirDate(firstAirDate: String?) {
    firstAirDate?.let {
        text = Utils.getFormattedDate(firstAirDate)
    }
}

@BindingAdapter("lastEpisodeToAir")
fun TextView.setLastEpisodeToAir(episodeNumber: Int?) {
    episodeNumber?.let {
        text = episodeNumber.toString()
    }
}

@BindingAdapter("numberOfSeasons")
fun TextView.setNumberOfSeasons(numberOfSeasons: Int?) {
    numberOfSeasons?.let {
        text = numberOfSeasons.toString()
    }
}

@BindingAdapter("airDate")
fun TextView.setAirDate(airDate: String?) {
    airDate?.let {
        text = Utils.getFormattedDate(airDate)
    }
}

@BindingAdapter("genreIds")
fun TextView.setGenreIds(genreIds: List<Int>?) {
    genreIds?.let {
        text = Utils.getGenresFromIds(it)
    }
}
