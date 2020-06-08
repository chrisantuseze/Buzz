package com.echrisantus.buzz.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echrisantus.buzz.databinding.GuestStarCardBinding
import com.echrisantus.buzz.network.model.GuestStar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GuestStarAdapter(val clicklistener: ItemListener): ListAdapter<DataItem, GuestStarAdapter.GuestStarViewHolder>(DiffCallBack()) {
    val adapterCoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestStarViewHolder {
        return GuestStarViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: GuestStarViewHolder, position: Int) {
        val guestStarItem = getItem(position) as DataItem.GuestStarItem
        holder.bind(guestStarItem.star, clicklistener)
    }

    fun addItemAndSubmit(list: List<GuestStar>) {
        adapterCoroutineScope.launch {
            val items = list.let {
                list.map { DataItem.GuestStarItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    class GuestStarViewHolder(val binding: GuestStarCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(star: GuestStar, clicklistener: ItemListener) {
            binding.guestStar = star
            binding.clicklistener = clicklistener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): GuestStarViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GuestStarCardBinding.inflate(layoutInflater, parent, false)
                return GuestStarViewHolder(binding)
            }
        }
    }
}