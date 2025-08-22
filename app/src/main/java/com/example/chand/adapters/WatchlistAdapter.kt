package com.example.chand.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.chand.DataBase.WatchlistItemEntity
import com.example.chand.databinding.ItemWatchlistBinding

class WatchlistAdapter(
    private val onDeleteClick: (WatchlistItemEntity) -> Unit
) : RecyclerView.Adapter<WatchlistAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemWatchlistBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<WatchlistItemEntity>() {
        override fun areItemsTheSame(oldItem: WatchlistItemEntity, newItem: WatchlistItemEntity): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: WatchlistItemEntity, newItem: WatchlistItemEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWatchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.binding.apply {
            nameTextView.text = item.name ?: item.nameEn
            priceTextView.text = item.price
            unitTextView.text = item.unit
            changePercentTextView.text = "${item.changePercent}%"?.toString() ?: "N/A"

            // تنظیم کلیک برای دکمه حذف
            deleteButton.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }

    override fun getItemCount(): Int = differ.currentList.size
}