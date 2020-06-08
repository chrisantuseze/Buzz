package com.echrisantus.buzz.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.echrisantus.buzz.database.model.Movie
import com.echrisantus.buzz.database.model.TV
import com.echrisantus.buzz.network.model.GuestStar
import com.echrisantus.buzz.network.model.MovieList
import com.echrisantus.buzz.network.model.TVList

class ItemListener(var clickListener: (id: Int) -> Unit) {
    fun onClick(movie: MovieList) = clickListener(movie.id)
    fun onClick(tv: TVList) = clickListener(tv.id)
    fun onClick(star: GuestStar) = clickListener(star.id)
}

interface MovieListener {
    fun onClick(movie: MovieList)
    fun onLiked(movie: MovieList)

    fun onClickFav(movie: Movie)
    fun onDelIconClick(movie: Movie)
}

interface TvListener {
    fun onClick(tvList: TVList)
    fun onLiked(tvList: TVList)

    fun onClickFav(tv: TV)
    fun onDelIconClick(tv: TV)
}

interface OnBottomReachListener {
    fun onBottomReached(position: Int)
}

sealed class DataItem{
    abstract val id: Int

    data class MovieItem(val movie: MovieList): DataItem() {
        override val id: Int
            get() = movie.id
    }

    data class TVItem(val tv: TVList): DataItem() {
        override val id: Int
            get() = tv.id
    }

    data class FavMovieItem(val movie: Movie): DataItem() {
        override val id: Int
            get() = movie.id
    }

    data class FavTVItem(val tv: TV): DataItem() {
        override val id: Int
            get() = tv.id
    }

    data class GuestStarItem(val star: GuestStar): DataItem() {
        override val id: Int
            get() = star.id
    }
}

class DiffCallBack: DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}