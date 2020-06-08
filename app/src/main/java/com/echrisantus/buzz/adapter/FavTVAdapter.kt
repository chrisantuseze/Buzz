package com.echrisantus.buzz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echrisantus.buzz.database.model.TV
import com.echrisantus.buzz.databinding.FavTvCardBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavTVAdapter(val clickListener: TvListener): ListAdapter<DataItem, FavTVAdapter.FavTVViewHolder>(DiffCallBack()) {
    val adapterCoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavTVViewHolder {
        return FavTVViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavTVViewHolder, position: Int) {
        val tvItem = getItem(position) as DataItem.FavTVItem
        holder.bindToHome(tvItem.tv, clickListener)
    }

    fun addItemAndSubmit(list: List<TV>) {
        adapterCoroutineScope.launch {
            var items = list.let {
                list.map { DataItem.FavTVItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    class FavTVViewHolder(val binding: FavTvCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindToHome(tv: TV, clickListener: TvListener) {
            binding.tv = tv
            binding.clicklistener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FavTVViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavTvCardBinding.inflate(layoutInflater, parent, false)
                return FavTVViewHolder(binding)
            }
        }
    }
}