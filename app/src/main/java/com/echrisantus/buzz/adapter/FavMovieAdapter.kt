package com.echrisantus.buzz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echrisantus.buzz.database.model.Movie
import com.echrisantus.buzz.databinding.FavMovieCardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavMovieAdapter(val clickListener: MovieListener): ListAdapter<DataItem, FavMovieAdapter.FavMovieViewHolder>(DiffCallBack()) {
    private val adapterCorountineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavMovieViewHolder {
        return FavMovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavMovieViewHolder, position: Int) {
        val movieItem = getItem(position) as DataItem.FavMovieItem
        holder.bind(movieItem.movie, clickListener)
    }

    fun addItemAndSubmit(list: List<Movie>) {
        adapterCorountineScope.launch {
            val items = list.let {
                list.map { DataItem.FavMovieItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    class FavMovieViewHolder(val binding: FavMovieCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie, clickListener: MovieListener) {
            binding.movie = movie
            binding.clicklistener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FavMovieViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavMovieCardBinding.inflate(layoutInflater, parent, false)
                return FavMovieViewHolder(binding)
            }
        }
    }
}