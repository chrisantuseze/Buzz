package com.echrisantus.buzz.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.echrisantus.buzz.databinding.MovieCardBinding
import com.echrisantus.buzz.network.model.MovieList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieAdapter(val clickListener: MovieListener): ListAdapter<DataItem, MovieAdapter.MovieViewHolder>(DiffCallBack()) {
    private val adapterCorountineScope = CoroutineScope(Dispatchers.Default)
    private lateinit var onBottomReachListener: OnBottomReachListener

    fun setOnBottomReachListener(onBottomReachListener: OnBottomReachListener) {
        this.onBottomReachListener = onBottomReachListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if (position == itemCount - 1) {
            onBottomReachListener.onBottomReached(position)
        }

        val movieItem = getItem(position) as DataItem.MovieItem
        holder.bind(movieItem.movie, clickListener)
    }

    fun addItemAndSubmit(list: List<MovieList>) {
        adapterCorountineScope.launch {
            val items = list.let {
                list.map { DataItem.MovieItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    class MovieViewHolder(val binding: MovieCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieList, clickListener: MovieListener) {
            binding.movie = movie
            binding.clicklistener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MovieViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieCardBinding.inflate(layoutInflater, parent, false)
                return MovieViewHolder(binding)
            }
        }
    }
}