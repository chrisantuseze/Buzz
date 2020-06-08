package com.echrisantus.buzz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echrisantus.buzz.databinding.TvseriesCardBinding
import com.echrisantus.buzz.network.model.TVList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TVAdapter(val clickListener: TvListener): ListAdapter<DataItem, TVAdapter.TVViewHolder>(DiffCallBack()) {
    val adapterCoroutineScope = CoroutineScope(Dispatchers.Default)
    private lateinit var onBottomReachListener: OnBottomReachListener

    fun setOnBottomReachListener(onBottomReachListener: OnBottomReachListener) {
        this.onBottomReachListener = onBottomReachListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVViewHolder {
        return TVViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TVViewHolder, position: Int) {
        if (position == itemCount - 1) {
            onBottomReachListener.onBottomReached(position)
        }

        val tvItem = getItem(position) as DataItem.TVItem
        holder.bindToHome(tvItem.tv, clickListener)
    }

    fun addItemAndSubmit(list: List<TVList>) {
        adapterCoroutineScope.launch {
            var items = list.let {
                list.map { DataItem.TVItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    class TVViewHolder(val binding: TvseriesCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindToHome(tv: TVList, clickListener: TvListener) {
            binding.tv = tv
            binding.clicklistener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TVViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TvseriesCardBinding.inflate(layoutInflater, parent, false)
                return TVViewHolder(binding)
            }
        }
    }
}